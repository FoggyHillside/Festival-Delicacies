package cn.foggyhillside.festival_delicacies.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class FruitTreeLogBlock extends Block {
    public static final BooleanProperty NORTH_BRANCH = BooleanProperty.create("north_branch");
    public static final BooleanProperty EAST_BRANCH = BooleanProperty.create("east_branch");
    public static final BooleanProperty SOUTH_BRANCH = BooleanProperty.create("south_branch");
    public static final BooleanProperty WEST_BRANCH = BooleanProperty.create("west_branch");
    private final Map<Direction, BooleanProperty> DIRECTION_MAP = Map.of(
            Direction.NORTH, NORTH_BRANCH,
            Direction.EAST, EAST_BRANCH,
            Direction.SOUTH, SOUTH_BRANCH,
            Direction.WEST, WEST_BRANCH
    );

    protected static final VoxelShape SHAPE = Block.box(4.0F, 0.0F, 4.0F, 12.0F, 16.0F, 12.0F);

    public FruitTreeLogBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava());
        this.registerDefaultState(this.getStateDefinition().any().setValue(NORTH_BRANCH, false).setValue(EAST_BRANCH, false).setValue(SOUTH_BRANCH, false).setValue(WEST_BRANCH, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        VoxelShape voxelShape = SHAPE;
        if (pState.getValue(NORTH_BRANCH)) {
            voxelShape = Shapes.join(voxelShape, Block.box(4.0F, 4.0F, 0.0F, 12.0F, 12.0F, 4.0F), BooleanOp.OR);
        }
        if (pState.getValue(EAST_BRANCH)) {
            voxelShape = Shapes.join(voxelShape, Block.box(12.0F, 4.0F, 4.0F, 16.0F, 12.0F, 12.0F), BooleanOp.OR);
        }
        if (pState.getValue(SOUTH_BRANCH)) {
            voxelShape = Shapes.join(voxelShape, Block.box(4.0F, 4.0F, 12.0F, 12.0F, 12.0F, 16.0F), BooleanOp.OR);
        }
        if (pState.getValue(WEST_BRANCH)) {
            voxelShape = Shapes.join(voxelShape, Block.box(0.0F, 4.0F, 4.0F, 4.0F, 12.0F, 12.0F), BooleanOp.OR);
        }

        return voxelShape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(NORTH_BRANCH);
        pBuilder.add(EAST_BRANCH);
        pBuilder.add(SOUTH_BRANCH);
        pBuilder.add(WEST_BRANCH);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos) {
        BlockState newState = super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
        if (pDirection.equals(Direction.UP) || pDirection.equals(Direction.DOWN)) {
            return newState;
        } else {
            return newState.setValue(getPropertyFromDirection(pDirection), pNeighborState.is(BlockTags.LEAVES));
        }
    }

    private BooleanProperty getPropertyFromDirection(Direction direction) {
        return DIRECTION_MAP.get(direction);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }
}
