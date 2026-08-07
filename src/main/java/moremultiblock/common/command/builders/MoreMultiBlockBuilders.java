package moremultiblock.common.command.builders;

import mekanism.common.command.builders.StructureBuilder;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MoreMultiBlockBuilders {
    protected MoreMultiBlockBuilders() {
    }

    public static class RadioactiveWasteVaultBuilder extends StructureBuilder {

        public RadioactiveWasteVaultBuilder() {
            super(18, 18, 18);
        }

        @Override
        public void build(Level world, BlockPos start, boolean empty) {
            buildFrame(world, start);
            buildWalls(world, start);
            buildInteriorLayers(world, start, 1, 16, Blocks.AIR);
        }

        @Override
        protected Block getCasing() {
            return MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock();
        }

        @Override
        protected  Block getWallBlock(BlockPos pos) {
            return MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock();
        }
    }

}
