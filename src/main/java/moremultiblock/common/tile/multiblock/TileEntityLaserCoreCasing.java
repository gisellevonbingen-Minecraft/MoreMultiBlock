package moremultiblock.common.tile.multiblock;

import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import moremultiblock.MoreMultiblock;
import moremultiblock.common.content.lasercore.LaserCoreMultiblockData;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityLaserCoreCasing extends TileEntityMultiblock<LaserCoreMultiblockData> {
    public TileEntityLaserCoreCasing(BlockPos pos, BlockState state) {
        this(MMBBlocks.LASER_CORE_CASING, pos, state);
    }

    public TileEntityLaserCoreCasing(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected boolean onUpdateServer(LaserCoreMultiblockData multiblock) {
        boolean needsPacket = super.onUpdateServer(multiblock);
        setActive(multiblock.isFormed());
        return needsPacket;
    }

    @Override
    public LaserCoreMultiblockData createMultiblock() {
        return new LaserCoreMultiblockData(this);
    }

    @Override
    public LaserCoreMultiblockData getMultiblock() {
        return super.getMultiblock();
    }

    @Override
    public MultiblockManager<LaserCoreMultiblockData> getManager() {
        return MoreMultiblock.laserCoreManager;
    }
}
