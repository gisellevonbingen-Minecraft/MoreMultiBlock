package moremultiblock.common.tile.multiblock;

import mekanism.common.tile.prefab.TileEntityInternalMultiblock;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityLaserCore extends TileEntityInternalMultiblock {
    public TileEntityLaserCore(BlockPos pos, BlockState state) {
        super(MMBBlocks.LASER_CORE, pos, state);
    }
}
