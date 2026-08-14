package moremultiblock.common.content.lasercore;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.MekanismLang;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.math.voxel.VoxelCuboid.CuboidSide;
import mekanism.common.lib.math.voxel.VoxelPlane;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.FormationProtocol.CasingType;
import mekanism.common.lib.multiblock.FormationProtocol.FormationResult;
import mekanism.common.lib.multiblock.FormationProtocol.StructureRequirement;
import mekanism.common.lib.multiblock.Structure.Axis;
import mekanism.common.lib.multiblock.StructureHelper;
import mekanism.common.registries.MekanismBlockTypes;
import moremultiblock.common.registries.MMBBlockTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.EnumSet;
import java.util.NavigableMap;

@SuppressWarnings("ALL")
public class LaserCoreValidator extends CuboidStructureValidator<LaserCoreMultiblockData> {
    private static final StructureRequirement I = StructureRequirement.IGNORED;
    private static final StructureRequirement F = StructureRequirement.FRAME;
    private static final StructureRequirement O = StructureRequirement.OTHER;
    private static final StructureRequirement N = StructureRequirement.INNER;

    public static final VoxelCuboid BOUNDS = new VoxelCuboid(5, 5, 5);
    private static final StructureRequirement[][] TOP_BOTTOM_ALLOWED_GRID = {
            {I, I, F, I, I},
            {I, I, F, I, I},
            {F, F, O, F, F},
            {I, I, F, I, I},
            {I, I, F, I, I}
    };

    private static final StructureRequirement[][] INTERMEDIATE_LAYER_ALLOWED_GRID = {
            {I, I, F, I, I},
            {I, O, N, O, I},
            {F, N, O, N, F},
            {I, O, N, O, I},
            {I, I, F, I, I}
    };

    private static final StructureRequirement[][] CENTER_ALLOWED_GRID = {
            {F, F, O, F, F}, // ↑ North
            {F, O, N, O, F},
            {O, O, N, O, O},// ← West → East
            {F, O, N, O, F},
            {F, F, O, F, F}, // ↓ South
    };

    @Override
    protected StructureRequirement getStructureRequirement(BlockPos pos) {
        int z = pos.getZ() - cuboid.getMinPos().getZ(); // 列
        int x = pos.getX() - cuboid.getMinPos().getX(); // 行
        int y = pos.getY() - cuboid.getMinPos().getY();
        int maxY = cuboid.getMaxPos().getY() - cuboid.getMinPos().getY();

        int gridX = x;
        int gridZ = z;
        Direction dir = getLaserAmplifierDirection();

        // Laser Amplifierが西東側にある場合x、z座標を90度回転
        if (dir == Direction.WEST || dir == Direction.EAST) {
            gridX = z;
            gridZ = 4 - x;
        }

        StructureRequirement[][] grid = getAllowedGrid(y, maxY);
        return grid[gridZ][gridX];
    }

    private StructureRequirement[][] getAllowedGrid(int y, int maxY) {
        if (y == 0 || y == maxY) {
            return TOP_BOTTOM_ALLOWED_GRID;
        }
        if (y == 1 || y == maxY - 1) {
            return INTERMEDIATE_LAYER_ALLOWED_GRID;
        }
        return CENTER_ALLOWED_GRID;
    }

    @Override
    protected CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, MMBBlockTypes.LASER_CORE_CASING)) {
            return CasingType.FRAME;
        }
        if (BlockType.is(block, MMBBlockTypes.LASER_CORE_PORT)) {
            return CasingType.VALVE;
        }
        if (BlockType.is(block, MekanismBlockTypes.LASER, MekanismBlockTypes.LASER_AMPLIFIER)) {
            return CasingType.OTHER;
        }
        return CasingType.INVALID;
    }

    @Override
    protected boolean validateInner(BlockState state, Long2ObjectMap<ChunkAccess> chunkMap, BlockPos pos) {
        if (super.validateInner(state, chunkMap, pos)) {
            return true;
        }
        return BlockType.is(state.getBlock(), MMBBlockTypes.LASER_CORE);
    }

    @Override
    public FormationResult postcheck(LaserCoreMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {

        BlockPos centerPos = cuboid.getMinPos().offset(2, 2, 2);
        Direction ampDir = getLaserAmplifierDirection();

        // Laser Coreがあるか
        if (!BlockType.is(world.getBlockState(centerPos).getBlock(), MMBBlockTypes.LASER_CORE)) {
            return FormationResult.FAIL;
        }

        // Laser Amplifierがあるか
        if (ampDir == null) {
            return FormationResult.FAIL;
        }

        structure.addLaser(getLaserAmplifierPos());

        // 中央の上下にLaserがあるか
        if (!checkBlockFacing(centerPos.above(), Direction.DOWN, MekanismBlockTypes.LASER) ||
                !checkBlockFacing(centerPos.below(), Direction.UP, MekanismBlockTypes.LASER)) {
            return FormationResult.FAIL;
        }

        Direction rightDir = ampDir.getClockWise(); // 右方向
        Direction leftDir = ampDir.getCounterClockWise();   // 左方向

        // Laser Amplifierの向きによって中央の左右にLaserがあるか
        if (!checkBlockFacing(centerPos.relative(rightDir), rightDir.getOpposite(), MekanismBlockTypes.LASER) ||
                !checkBlockFacing(centerPos.relative(leftDir), leftDir.getOpposite(), MekanismBlockTypes.LASER)) {
            return FormationResult.FAIL;
        }

        return FormationResult.SUCCESS;
    }

    private BlockPos getLaserAmplifierPos() {

        BlockPos pos = cuboid.getMinPos();

        BlockPos northPos = pos.offset(2, 2, 0);
        if (checkBlockFacing(northPos, Direction.NORTH, MekanismBlockTypes.LASER_AMPLIFIER)) {
            return northPos;
        }

        BlockPos southPos = pos.offset(2, 2, 4);
        if (checkBlockFacing(southPos, Direction.SOUTH, MekanismBlockTypes.LASER_AMPLIFIER)) {
            return southPos;
        }

        BlockPos westPos = pos.offset(0, 2, 2);
        if (checkBlockFacing(westPos, Direction.WEST, MekanismBlockTypes.LASER_AMPLIFIER)) {
            return westPos;
        }

        BlockPos eastPos = pos.offset(4, 2, 2);
        if (checkBlockFacing(eastPos, Direction.EAST, MekanismBlockTypes.LASER_AMPLIFIER)) {
            return eastPos;
        }

        return null;
    }

    private Direction getLaserAmplifierDirection() {
        BlockPos pos = getLaserAmplifierPos();

        if (pos != null) {
            BlockState state = world.getBlockState(pos);
            if (state.hasProperty(BlockStateProperties.FACING)) {
                return state.getValue(BlockStateProperties.FACING);
            }
        }
//        if (checkBlockFacing(pos.offset(2, 2, 0), Direction.NORTH, MekanismBlockTypes.LASER_AMPLIFIER)) {
//            return Direction.NORTH;
//        }
//        if (checkBlockFacing(pos.offset(2, 2, 4), Direction.SOUTH, MekanismBlockTypes.LASER_AMPLIFIER)) {
//            return Direction.SOUTH;
//        }
//        if (checkBlockFacing(pos.offset(0, 2, 2), Direction.WEST, MekanismBlockTypes.LASER_AMPLIFIER)) {
//            return Direction.WEST;
//        }
//        if (checkBlockFacing(pos.offset(4, 2, 2), Direction.EAST, MekanismBlockTypes.LASER_AMPLIFIER)) {
//            return Direction.EAST;
//        }
        return null;
    }

    private boolean checkBlockFacing(BlockPos pos, Direction direction, BlockType type) {
        BlockState state = world.getBlockState(pos);

        return BlockType.is(state.getBlock(), type)
                && state.hasProperty(BlockStateProperties.FACING)
                && state.getValue(BlockStateProperties.FACING) == direction;
    }

    @Override
    public boolean precheck() {
        // length = X
        // width = Z
        // height = Y
        cuboid = StructureHelper.fetchCuboid(structure, BOUNDS, BOUNDS, EnumSet.of(CuboidSide.TOP, CuboidSide.BOTTOM), 32);
        if (cuboid != null) {
            BlockPos minPos = cuboid.getMinPos();
            BlockPos maxPos = cuboid.getMaxPos();

            Axis xAxis = CuboidSide.EAST.getAxis();
            NavigableMap<Integer, VoxelPlane> xMinorMap = structure.getMinorAxisMap(xAxis);
            if (xMinorMap != null && !xMinorMap.isEmpty()) {
                int firstX = xMinorMap.firstEntry().getKey();
                int lastX = xMinorMap.lastEntry().getKey();

                if (firstX < minPos.getX() || lastX > maxPos.getX()) {
                    return false;
                }
            }

            Axis zAxis = CuboidSide.NORTH.getAxis();
            NavigableMap<Integer, VoxelPlane> zMinorMap = structure.getMinorAxisMap(zAxis);
            if (zMinorMap != null && !zMinorMap.isEmpty()) {
                int firstZ = zMinorMap.firstEntry().getKey();
                int lastZ = zMinorMap.lastEntry().getKey();

                if (firstZ < minPos.getZ() || lastZ > maxPos.getZ()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected FormationProtocol.FormationResult validateFrame(FormationProtocol<LaserCoreMultiblockData> ctx, BlockPos pos, BlockState state, CasingType type, boolean needsFrame) {
        Block block = state.getBlock();
        if (BlockType.is(block, MekanismBlockTypes.LASER, MekanismBlockTypes.LASER_AMPLIFIER)) {
            if (needsFrame) {
                return FormationResult.fail(MekanismLang.MULTIBLOCK_INVALID_FRAME, pos);
            }
            return FormationResult.SUCCESS;
        }

        return super.validateFrame(ctx, pos, state, type, needsFrame);
    }
}
