package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class RiceBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final BooleanProperty HAS_EAR = BooleanProperty.create("has_ear");
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 6.0F, 12.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F),
            Block.box(4.0F, 0.0F, 4.0F, 12.0F, 15.0F, 12.0F),
            Block.box(2.0F, 0.0F, 2.0F, 14.0F, 16.0F, 14.0F)
    };

    public RiceBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(AGE, 0).setValue(HAS_EAR, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[pState.getValue(AGE)];
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        FluidState fluid = pLevel.getFluidState(pPos);
        return super.canSurvive(pState, pLevel, pPos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(ModItems.RICE.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE);
        pBuilder.add(HAS_EAR);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluid = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(pContext) : null;
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(HAS_EAR);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState newState = super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        if (!newState.isAir()) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
            if (pFacing.equals(Direction.UP)) {
                return newState.setValue(HAS_EAR, pFacingState.is(ModBlocks.RICE_EAR.get()));
            }
        }

        return newState;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = pState.getValue(AGE);
            float f = 10.0F;
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
                if (i < MAX_AGE) {
                    pLevel.setBlock(pPos, pState.setValue(AGE, i + 1), 2);
                } else {
                    RiceEarBlock riceEar = (RiceEarBlock) ModBlocks.RICE_EAR.get();
                    BlockState aboveState = pLevel.getBlockState(pPos.above());
                    if (aboveState.isAir() && riceEar.canSurvive(aboveState, pLevel, pPos.above())) {
                        pLevel.setBlock(pPos.above(), riceEar.defaultBlockState(), 2);
                    }
                }

                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
            }

        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        if (aboveState.getBlock() instanceof RiceEarBlock riceEarBlock) {
            return !riceEarBlock.isMaxAge(aboveState);
        } else {
            return true;
        }
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return true;
    }

    protected int getBonemealAgeIncrease(Level pLevel) {
        return Mth.nextInt(pLevel.random, 1, 4);
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        int growAge = Math.min(pState.getValue(AGE) + getBonemealAgeIncrease(pLevel), 7);
        if (growAge <= MAX_AGE) {
            pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, growAge));
        } else {
            BlockState aboveState = pLevel.getBlockState(pPos.above());
            if (!pState.getValue(HAS_EAR)) {
                RiceEarBlock riceEar = (RiceEarBlock) ModBlocks.RICE_EAR.get();
                pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, MAX_AGE));
                if (aboveState.isAir() && riceEar.canSurvive(aboveState, pLevel, pPos.above())) {
                    int ageLeft = growAge - MAX_AGE - 1;
                    pLevel.setBlock(pPos.above(), riceEar.defaultBlockState().setValue(RiceEarBlock.RICE_AGE, ageLeft), 2);
                }

            } else {
                if (aboveState.getBlock().equals(ModBlocks.RICE_EAR.get())) {
                    BonemealableBlock growable = (BonemealableBlock) pLevel.getBlockState(pPos.above()).getBlock();
                    if (growable.isValidBonemealTarget(pLevel, pPos.above(), aboveState, false)) {
                        growable.performBonemeal(pLevel, pLevel.random, pPos.above(), aboveState);
                    }
                }
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        return false;
    }
}
