package moremultiblock.client.data;

import moremultiblock.MoreMultiblock;
import moremultiblock.common.registries.MMBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MMBBlockStateProvider extends BlockStateProvider {
    public MMBBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MoreMultiblock.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(MMBBlocks.RADIOACTIVE_WASTE_VAULT.getBlock());
        simpleBlockWithItem(MMBBlocks.RADIOACTIVE_WASTE_VALVE.getBlock());
    }

    private void simpleBlockWithItem(Block block) {
        simpleBlock(block);
        simpleBlockItem(block, cubeAll(block));
    }
}
