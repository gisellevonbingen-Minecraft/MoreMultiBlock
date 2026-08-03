package moremultiblock.common.content.vault;

import com.mojang.datafixers.util.Either;
import mekanism.api.Action;
import mekanism.api.IContentsListener;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.capabilities.chemical.multiblock.MultiblockChemicalTankBuilder;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.SyntheticComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.registries.MekanismGases;
import mekanism.common.tags.MekanismTags;
import mekanism.common.tile.interfaces.IFluidContainerManager.ContainerEditMode;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class VaultMultiblockData extends MultiblockData implements IValveHandler {

    private long lastProcessTick;
    private int processTicks;

    @ContainerSync
    @SyntheticComputerMethod(getter = "getContainerEditMode")
    public ContainerEditMode editMode = ContainerEditMode.BOTH;

    @ContainerSync
    public final IGasTank wasteTank;

    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getInputItem", docPlaceholder = "input slot")
    GasInventorySlot inputSlot;
    @WrappingComputerMethod(wrapper = ComputerIInventorySlotWrapper.class, methodNames = "getOutputItem", docPlaceholder = "output slot")
    GasInventorySlot outputSlot;
    private long wasteTankCapacity;
    public float prevScale;

    public VaultMultiblockData(TileEntityRadioactiveWasteVault tile) {
        super(tile);
        IContentsListener saveAndComparator = createSaveAndComparator();
        wasteTank = MultiblockChemicalTankBuilder.GAS.create(
                this::getWasteTankCapacity,
                this.formedBiPred(),    // Formed状態か
                this.formedBiPred(),
                gas -> gas == MekanismGases.NUCLEAR_WASTE.getChemical() || gas == MekanismGases.SPENT_NUCLEAR_WASTE.getChemical(),
                ChemicalAttributeValidator.ALWAYS_ALLOW,
                saveAndComparator
        );
        gasTanks.add(wasteTank);
        inventorySlots.addAll(createBaseInventorySlots());
    }

    private List<IInventorySlot> createBaseInventorySlots() {
        List<IInventorySlot> inventorySlots = new ArrayList<>();
        inventorySlots.add(inputSlot = GasInventorySlot.drain(wasteTank, this, 146, 25));
        inventorySlots.add(outputSlot = GasInventorySlot.fill(wasteTank, this, 146, 55));
        inputSlot.setSlotType(ContainerSlotType.INPUT);
        outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        return inventorySlots;
    }

    @Override
    public boolean tick(Level world) {
        boolean needsPacket = super.tick(world);

        if (world.getGameTime() > lastProcessTick) {
            lastProcessTick = world.getGameTime();
            // TODO コンフィグ整備
            if (MekanismConfig.general.radioactiveWasteBarrelDecayAmount.get() > 0 && !wasteTank.isEmpty() &&
                    !MekanismTags.Gases.WASTE_BARREL_DECAY_LOOKUP.contains(wasteTank.getType()) &&
                    ++processTicks >= MekanismConfig.general.radioactiveWasteBarrelProcessTicks.get()) {
                processTicks = 0;
                wasteTank.shrinkStack(MekanismConfig.general.radioactiveWasteBarrelDecayAmount.get() * getVolume(), Action.EXECUTE);
            }
        }

        inputSlot.drainTank();
        outputSlot.fillTank();

        float scale = getScale();
        if (scale != prevScale) {
            prevScale = scale;
            needsPacket = true;
        }
        return needsPacket;
    }

    @Override
    public void readUpdateTag(CompoundTag tag) {
        super.readUpdateTag(tag);
        NBTUtils.setFloatIfPresent(tag, NBTConstants.SCALE, scale -> prevScale = scale);
        NBTUtils.setGasStackIfPresent(tag, NBTConstants.GAS_STORED, wasteTank::setStack);
        readValves(tag);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        super.writeUpdateTag(tag);
        tag.putFloat(NBTConstants.SCALE, prevScale);
        tag.put(NBTConstants.GAS_STORED, wasteTank.getStack().write(new CompoundTag()));
        writeValves(tag);
    }

    private float getScale() {
        return MekanismUtils.getScale(prevScale, wasteTank);
    }

    @ComputerMethod
    public long getWasteTankCapacity() {
        return wasteTankCapacity;
    }

    @Override
    public void setVolume(int volume) {
        if (getVolume() != volume) {
            super.setVolume(volume);
            wasteTankCapacity = volume * MekanismConfig.general.radioactiveWasteBarrelMaxGas.get();
        }
    }

    @Override
    protected int getMultiblockRedstoneLevel() {
        long capacity = getWasteTankCapacity();
        return MekanismUtils.redstoneLevelFromContents(getStoredAmount(), capacity);
    }

    private long getStoredAmount() {
        return wasteTank.getStored();
    }

    public IGasTank getWasteTank() {
        return wasteTank;
    }

    @ComputerMethod
    public void setContainerEditMode(ContainerEditMode mode) {
        if (editMode != mode) {
            editMode = mode;
            markDirty();
        }
    }

    public boolean isEmpty() {
        return wasteTank.isEmpty();
    }

    //Computer related methods
    @ComputerMethod
    void incrementContainerEditMode() {
        setContainerEditMode(editMode.getNext());
    }

    @ComputerMethod
    void decrementContainerEditMode() {
        setContainerEditMode(editMode.getPrevious());
    }

    @ComputerMethod
    Either<ChemicalStack<?>, FluidStack> getStored() {
        return Either.left(getWasteTank().getStack());
    }

    @ComputerMethod
    double getFilledPercentage() {
        long capacity = getWasteTankCapacity();
        return getStoredAmount() / (double) capacity;
    }
    //End computer related methods
}