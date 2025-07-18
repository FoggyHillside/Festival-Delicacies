package cn.foggyhillside.festival_delicacies.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Supplier;

public class FruitLeavesBlock extends LeavesBlock implements BonemealableBlock {
    protected final Supplier<Block> fruitBlock;
    private static final int GROWTH_CHANCE = 10;
    public static final BooleanProperty FRUITED = BooleanProperty.create("fruited");

    public FruitLeavesBlock(Supplier<Block> pBlock, Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(DISTANCE, 7).setValue(PERSISTENT, false).setValue(WATERLOGGED, false).setValue(FRUITED, false));
        this.fruitBlock = pBlock;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState newState = super.getStateForPlacement(pContext);
        return newState.setValue(FRUITED, false);
    }

    private boolean isFruited(Level pLevel, BlockPos pPos) {
        return pLevel.getBlockState(pPos.below()).is(fruitBlock.get());
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return (pState.getValue(DISTANCE) == 7 || !pState.getValue(FRUITED)) && !pState.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if (pState.getValue(DISTANCE) == 7) {
            return;
        }
        if (!isFruited(pLevel, pPos) && pRandom.nextInt(GROWTH_CHANCE) < 1
                && pLevel.getBlockState(pPos.below()).isAir() && ((FruitBlock) fruitBlock.get()).canSurvive(pState, pLevel, pPos.above())) {
            pLevel.setBlockAndUpdate(pPos, pState.setValue(FRUITED, true));
            pLevel.setBlock(pPos.below(), fruitBlock.get().defaultBlockState(), 2);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        BlockState belowState = pLevel.getBlockState(pPos.below());
        boolean isFruited = belowState.is(fruitBlock.get());
        if (pState.getValue(FRUITED) != isFruited) {
            pLevel.setBlock(pPos, pState.setValue(FRUITED, isFruited), 2);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState newState = super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        BlockState belowState = pLevel.getBlockState(pCurrentPos.below());
        boolean isFruited = belowState.is(fruitBlock.get());
        if (pState.getValue(FRUITED) != isFruited) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
        }

        return newState;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FRUITED);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        if (isFruited(pLevel, pPos)) {
            BlockState fruitState = pLevel.getBlockState(pPos.below());
            BonemealableBlock growable = (BonemealableBlock) pLevel.getBlockState(pPos.above()).getBlock();
            if (growable.isValidBonemealTarget(pLevel, pPos.above(), fruitState, false)) {
                growable.performBonemeal(pLevel, pLevel.random, pPos.above(), fruitState);
            }
        } else {
            if (pRandom.nextInt(GROWTH_CHANCE) < 1) {
                pLevel.setBlock(pPos.below(), fruitBlock.get().defaultBlockState(), 2);
            }
        }
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }
}
