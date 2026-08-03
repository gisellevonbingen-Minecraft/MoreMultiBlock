package moremultiblock.common.block;

import mekanism.common.block.prefab.BlockBasicMultiblock;
import moremultiblock.common.block.states.PartPosition;
import moremultiblock.common.registries.MMBBlockTypes;
import moremultiblock.common.tile.multiblock.TileEntityRadioactiveWasteVault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;

public class BlockRadioactiveWasteVault extends BlockBasicMultiblock<TileEntityRadioactiveWasteVault> {
    public BlockRadioactiveWasteVault() {
        super(MMBBlockTypes.RADIOACTIVE_WASTE_VAULT, properties -> properties.mapColor(MapColor.COLOR_YELLOW));
        this.registerDefaultState(this.stateDefinition.any().setValue(PartPosition.PART_POSITION, PartPosition.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PartPosition.PART_POSITION);
    }
}
