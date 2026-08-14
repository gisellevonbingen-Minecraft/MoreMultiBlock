package moremultiblock.common.content.lasercore;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.AutomationType;
import mekanism.api.NBTConstants;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.chemical.multiblock.MultiblockChemicalTankBuilder;
import mekanism.common.capabilities.energy.VariableCapacityEnergyContainer;
import mekanism.common.config.MekanismConfig;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.registries.MekanismGases;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.NBTUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LaserCoreMultiblockData extends MultiblockData implements IValveHandler {

    @ContainerSync
    @WrappingComputerMethod(wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class, methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"}, docPlaceholder = "input tank")
    public IGasTank gasTank;

    @ContainerSync
    public IEnergyContainer energyContainer;

    public final SyncableLaserAmplifierData laserAmplifierData = new SyncableLaserAmplifierData();

    @ContainerSync(getter = "getEnergy")
    private FloatingLong clientEnergy = FloatingLong.ZERO;

    @ContainerSync(getter = "getEnergyCapacity")
    private FloatingLong clientMaxEnergy = FloatingLong.ZERO;

    private FloatingLong energyCapacity = FloatingLong.create(1_000_000);

    @ContainerSync
    public int inputProcessed = 0;

    public LaserCoreMultiblockData(BlockEntity tile) {
        super(tile);
        gasTanks.add(gasTank = MultiblockChemicalTankBuilder.GAS.input(this, this::getMaxGas, gas -> gas == MekanismGases.POLONIUM.get(),
                ChemicalAttributeValidator.ALWAYS_ALLOW, createSaveAndComparator()));
        energyContainers.add(energyContainer = VariableCapacityEnergyContainer.create(
                this::getEnergyCapacity,
                automationType -> automationType == AutomationType.INTERNAL && isFormed(),
                automationType -> isFormed(),
                this
        ));
    }

    @Override
    public void onCreated(Level world) {
        super.onCreated(world);
    }

    @Override
    public boolean tick(Level world) {
        boolean needsPacket = super.tick(world);
        super.onCreated(world);
        return needsPacket;
    }

    private long getMaxGas() { // TODO
        return MekanismConfig.general.spsInputPerAntimatter.get() * 2L;
    }

    @NotNull
    public FloatingLong getEnergyCapacity() {
        return energyCapacity;
    }

    @NotNull
    public IEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    public FloatingLong getEnergy() {
        return isRemote() ? clientEnergy : energyContainer.getEnergy();
    }

    @Override
    public void setVolume(int volume) { // TODO
        if (getVolume() != volume) {
            super.setVolume(volume);
            energyCapacity = MekanismGeneratorsConfig.generators.turbineEnergyCapacityPerVolume.get().multiply(volume).copyAsConst();
        }
    }

    @Override
    public void readUpdateTag(CompoundTag tag) {
        super.readUpdateTag(tag);
        laserAmplifierData.read(tag);
        NBTUtils.setGasStackIfPresent(tag, NBTConstants.GAS_STORED, value -> gasTank.setStack(value));
        NBTUtils.setFloatIfPresent(tag, NBTConstants.ENERGY_STORED, t -> energyContainer.setEnergy(FloatingLong.create(t)));
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        super.writeUpdateTag(tag);
        laserAmplifierData.write(tag);
        tag.put(NBTConstants.GAS_STORED, gasTank.getStack().write(new CompoundTag()));
        tag.putFloat(NBTConstants.ENERGY_STORED, energyContainer.getEnergy().floatValue());
    }

    @Override
    protected int getMultiblockRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(gasTank.getStored(), gasTank.getCapacity());
    }

    public void addLaser(BlockPos laserAmplifierPos) {
        laserAmplifierData.laserMap.put(laserAmplifierPos, new LaserAmplifierData(laserAmplifierPos));
    }

    public static class SyncableLaserAmplifierData {
        public final Map<BlockPos, LaserAmplifierData> laserMap = new Object2ObjectOpenHashMap<>();
        public int prevHash;

        private boolean tick() {
            int newHash = laserMap.hashCode();
            boolean ret = newHash != prevHash;
            prevHash = newHash;
            return ret;
        }

        public void write(CompoundTag tags) {
            ListTag list = new ListTag();
            for (LaserAmplifierData data : laserMap.values()) {
                CompoundTag tag = new CompoundTag();
                tag.put(NBTConstants.POSITION, NbtUtils.writeBlockPos(data.amplifierPos));
                list.add(tag);
            }
            tags.put("lasers", list); // TODO
        }

        public void read(CompoundTag tags) {
            laserMap.clear();
            ListTag list = tags.getList("lasers", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); ++i) {
                CompoundTag tag = list.getCompound(i);
                BlockPos pos = NbtUtils.readBlockPos(tag.getCompound(NBTConstants.POSITION));
                LaserAmplifierData data = new LaserAmplifierData(pos);
                laserMap.put(pos, data);
            }
        }
    }

    public static class LaserAmplifierData {
        public final BlockPos amplifierPos;

        private LaserAmplifierData(BlockPos laserPos) {
            this.amplifierPos = laserPos;
        }

        @Override
        public int hashCode() {
            int result = 1;
            result = 31 * result + amplifierPos.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return o instanceof LaserAmplifierData other && amplifierPos.equals(other.amplifierPos);
        }
    }
}
