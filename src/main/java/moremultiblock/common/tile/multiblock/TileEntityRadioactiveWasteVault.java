package moremultiblock.common.tile.multiblock;

import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.radiation.IRadiationManager;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.interfaces.IFluidContainerManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.block.states.PartPosition;
import moremultiblock.common.content.vault.VaultMultiblockData;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TileEntityRadioactiveWasteVault extends TileEntityMultiblock<VaultMultiblockData> implements IFluidContainerManager {

    public TileEntityRadioactiveWasteVault(BlockPos pos, BlockState state) {
        this(MMBBlocks.RADIOACTIVE_WASTE_VAULT, pos, state);
        addDisabledCapabilities(ForgeCapabilities.ITEM_HANDLER);
    }

    public TileEntityRadioactiveWasteVault(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected boolean onUpdateServer(VaultMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        PartPosition type = PartPosition.NONE;
        BlockState state = getBlockState();

        if (state.hasProperty(PartPosition.PART_POSITION)) {
            if (multiblock.isFormed()) {
                type = getPosition(getBlockPos());
            }

            if (state.getValue(PartPosition.PART_POSITION) != type) {
                BlockState newState = state.setValue(PartPosition.PART_POSITION, type);
                level.setBlockAndUpdate(worldPosition, newState);
            }
        }

        setActive(multiblock.isFormed());
        return needsPacket;
    }

    private PartPosition getPosition(BlockPos pos) {
        PartPosition type = PartPosition.NONE;
        VaultMultiblockData multiblock = getMultiblock();

        if (pos.getY() == multiblock.getMaxPos().getY()) {
            return PartPosition.TOP;
        }

        if (pos.getY() == multiblock.getMinPos().getY()) {
            return PartPosition.BOTTOM;
        }

        if (isCenterOfSide(pos)) {
            return PartPosition.CENTER;
        }
        return type;
    }

    private boolean isCenterOfSide(BlockPos pos) {
        VaultMultiblockData multiblock = getMultiblock();

        int minX = multiblock.getMinPos().getX();
        int minY = multiblock.getMinPos().getY();
        int minZ = multiblock.getMinPos().getZ();
        int maxX = multiblock.getMaxPos().getX();
        int maxZ = multiblock.getMaxPos().getZ();

        int centerX = minX + (multiblock.length() - 1) / 2;
        int centerY = minY + (multiblock.height() - 1) / 2;
        int centerZ = minZ + (multiblock.width() - 1) / 2;

        return pos.equals(new BlockPos(centerX, centerY, minZ)) || // 前
                pos.equals(new BlockPos(centerX, centerY, maxZ)) || // 後
                pos.equals(new BlockPos(minX, centerY, centerZ)) || // 左
                pos.equals(new BlockPos(maxX, centerY, centerZ));   // 右
    }

    @Override
    public InteractionResult onActivate(Player player, InteractionHand hand, ItemStack stack) {
        if (!player.isShiftKeyDown()) {
            VaultMultiblockData multiblock = getMultiblock();
            if (multiblock.isFormed()) {
                return openGui(player);
            }
        }
        return InteractionResult.PASS;
    }

    @NotNull
    @Override
    public VaultMultiblockData createMultiblock() {
        return new VaultMultiblockData(this);
    }

    @Override
    public MultiblockManager<VaultMultiblockData> getManager() {
        return MoreMultiblock.vaultManager;
    }

    @Override
    public ContainerEditMode getContainerEditMode() {
        return getMultiblock().editMode;
    }

    @Override
    public void nextMode() {
        VaultMultiblockData multiblock = getMultiblock();
        multiblock.setContainerEditMode(multiblock.editMode.getNext());
    }

    @Override
    public void previousMode() {
        VaultMultiblockData multiblock = getMultiblock();
        multiblock.setContainerEditMode(multiblock.editMode.getPrevious());
    }

    // NOTE MultiBlockDataでは形成時のみタンク容量を渡しているため変更が必要
    @NotNull
    @Override
    public List<IGasTank> getGasTanks(@Nullable Direction side) {
        VaultMultiblockData multiblock = getMultiblock();
        return multiblock.getGasTanks(side);
    }

    @Override
    public void blockRemoved() {
        VaultMultiblockData multiblock = getMultiblock();

        if (!isRemote() && IRadiationManager.INSTANCE.isRadiationEnabled()) {
            IRadiationManager.INSTANCE.dumpRadiation(getTileCoord(), getGasTanks(null), false);
        }

        // タンクをキャッシュごと空にする
        multiblock.getWasteTank().setEmpty();

        if (this.getCacheID() != null) {
            var cache = getManager().getCache(this.getCacheID());
            if (cache != null) {
                cache.getGasTanks(null).clear();
            }
        }

        super.blockRemoved();
    }

    @Override
    public boolean shouldDumpRadiation() {
        return true;
    }
}