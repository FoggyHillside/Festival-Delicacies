package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LotusBlock extends LotusStalkBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SHAPE_1 = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 8.0F, 13.0F);
    private static final VoxelShape SHAPE_2 = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 12.0F, 14.0F);
    private static final VoxelShape SHAPE_3 = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);

    public LotusBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(this.getAgeProperty(), 0).setValue(HAS_STALK, false));
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    public int getAge(BlockState pState) {
        return pState.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int pAge) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), pAge);
    }

    public final boolean isMaxAge(BlockState pState) {
        return this.getAge(pState) >= this.getMaxAge();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (this.getAge(pState)) {
            case 0 -> SHAPE_1;
            case 3 -> SHAPE_3;
            default -> SHAPE_2;
        };
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.DIRT) || pState.is(Blocks.FARMLAND);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(this.getAgeProperty());
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }

        float f = 10.0F;
        if (pLevel.getRawBrightness(pPos, 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
            if (this.isMaxAge(pState)) {
                BlockState aboveState = pLevel.getBlockState(pPos.above());
                FluidState fluidState = pLevel.getFluidState(pPos.above());
                LotusStalkBlock stalk = (LotusStalkBlock) ModBlocks.LOTUS_STALK.get();
                LotusAerialPartBlock aerialPart = (LotusAerialPartBlock) ModBlocks.LOTUS_AERIAL_PART.get();
                if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && stalk.canSurvive(aboveState, pLevel, pPos.above())) {
                    pLevel.setBlock(pPos.above(), stalk.defaultBlockState(), 2);
                } else if (aboveState.isAir() && aerialPart.canSurvive(aboveState, pLevel, pPos.above())) {
                    pLevel.setBlock(pPos.above(), aerialPart.defaultBlockState(), 2);
                }
            } else {
                pLevel.setBlock(pPos, pState.setValue(this.getAgeProperty(), this.getAge(pState) + 1), 2);
            }

            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        int growAge = Math.min(this.getAge(pState) + getBonemealAgeIncrease(pLevel), 7);
        if (growAge <= this.getMaxAge()) {
            pLevel.setBlockAndUpdate(pPos, this.getStateForAge(growAge));
        } else {
            BlockState aboveState = pLevel.getBlockState(pPos.above());
            FluidState fluidState = pLevel.getFluidState(pPos.above());
            if (!pState.getValue(HAS_STALK)) {
                LotusStalkBlock stalk = (LotusStalkBlock) ModBlocks.LOTUS_STALK.get();
                LotusAerialPartBlock aerialPart = (LotusAerialPartBlock) ModBlocks.LOTUS_AERIAL_PART.get();
                pLevel.setBlockAndUpdate(pPos, pState.setValue(this.getAgeProperty(), this.getMaxAge()));
                if (fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && stalk.canSurvive(aboveState, pLevel, pPos.above())) {
                    pLevel.setBlock(pPos.above(), stalk.defaultBlockState(), 2);
                } else if (aboveState.isAir() && aerialPart.canSurvive(aboveState, pLevel, pPos.above())) {
                    int ageLeft = growAge - this.getMaxAge() - 1;
                    pLevel.setBlock(pPos.above(), aerialPart.defaultBlockState().setValue(LotusAerialPartBlock.LOTUS_AGE, ageLeft), 2);
                }
            } else if (aboveState.getBlock().equals(ModBlocks.LOTUS_STALK.get()) || aboveState.getBlock().equals(ModBlocks.LOTUS_AERIAL_PART.get())) {
                BonemealableBlock growable = (BonemealableBlock) pLevel.getBlockState(pPos.above()).getBlock();
                if (growable.isValidBonemealTarget(pLevel, pPos.above(), aboveState, false)) {
                    growable.performBonemeal(pLevel, pLevel.random, pPos.above(), aboveState);
                }
            }

        }
    }

}
