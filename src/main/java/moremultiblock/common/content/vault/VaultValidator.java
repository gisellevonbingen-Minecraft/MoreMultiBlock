package moremultiblock.common.content.vault;

import mekanism.common.MekanismLang;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import moremultiblock.common.registries.MMBBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VaultValidator extends CuboidStructureValidator<VaultMultiblockData> {
    @Override
    protected FormationProtocol.CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, MMBBlockTypes.RADIOACTIVE_WASTE_VAULT)) {
            return CasingType.FRAME;
        } else if (BlockType.is(block, MMBBlockTypes.RADIOACTIVE_WASTE_VALVE)) {
            return CasingType.VALVE;
        }
        return CasingType.INVALID;
    }

    @Override
    protected FormationResult validateFrame(FormationProtocol<VaultMultiblockData> ctx, BlockPos pos, BlockState state, CasingType type, boolean needsFrame) {
        if (type == CasingType.INVALID) {
            return FormationResult.fail(MekanismLang.MULTIBLOCK_INVALID_FRAME, pos);
        }
        return super.validateFrame(ctx, pos, state, type, needsFrame);
    }
}
