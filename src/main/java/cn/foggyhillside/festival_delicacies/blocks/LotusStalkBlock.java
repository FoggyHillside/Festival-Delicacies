package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.registry.ModBlockStateProperties;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LotusStalkBlock extends BushBlock implements BonemealableBlock, LiquidBlockContainer {
    public static final BooleanProperty HAS_STALK = ModBlockStateProperties.HAS_STALK;
    private static final VoxelShape SHAPE = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);

    public LotusStalkBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_STALK, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HAS_STALK);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(HAS_STALK);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) {
            return;
        }

        float f = 10.0F;
        if (pLevel.getRawBrightness(pPos, 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
            BlockState aboveState = pLevel.getBlockState(pPos.above());
            FluidState fluidState = pLevel.getFluidState(pPos.above());
            LotusAerialPartBlock aerialPart = (LotusAerialPartBlock) ModBlocks.LOTUS_AERIAL_PART.get();
            if (!isMaxHeight(pLevel, pPos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && this.canSurvive(aboveState, pLevel, pPos.above())) {
                pLevel.setBlock(pPos.above(), this.defaultBlockState(), 2);
            } else if (aboveState.isAir() && aerialPart.canSurvive(aboveState, pLevel, pPos.above())) {
                pLevel.setBlock(pPos.above(), aerialPart.defaultBlockState(), 2);
            }
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
        }

    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return new ItemStack(ModItems.LOTUS_SEEDS.get());
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluid = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return fluid.is(FluidTags.WATER) && fluid.getAmount() == 8 ? super.getStateForPlacement(pContext) : null;
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(ModBlocks.LOTUS.get()) || pState.is(ModBlocks.LOTUS_STALK.get());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        FluidState fluid = pLevel.getFluidState(pPos);
        return super.canSurvive(pState, pLevel, pPos) && fluid.is(FluidTags.WATER) && fluid.getAmount() == 8;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState newState = super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        if (!newState.isAir()) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
            if (pFacing.equals(Direction.UP)) {
                return newState.setValue(HAS_STALK, pFacingState.is(ModBlocks.LOTUS_AERIAL_PART.get()) || pFacingState.is(ModBlocks.LOTUS_STALK.get()));
            }
        }

        return newState;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        if (aboveState.is(ModBlocks.LOTUS_AERIAL_PART.get()) || aboveState.is(ModBlocks.LOTUS_STALK.get())) {
            BonemealableBlock aboveBlock = (BonemealableBlock) aboveState.getBlock();
            return aboveBlock.isValidBonemealTarget(pLevel, pPos.above(), aboveState, pIsClient);
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
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        FluidState fluidState = pLevel.getFluidState(pPos.above());
        if (!pState.getValue(HAS_STALK)) {
            LotusAerialPartBlock aerialPart = (LotusAerialPartBlock) ModBlocks.LOTUS_AERIAL_PART.get();
            if (!isMaxHeight(pLevel, pPos) && fluidState.is(FluidTags.WATER) && fluidState.getAmount() == 8 && this.canSurvive(aboveState, pLevel, pPos.above())) {
                pLevel.setBlock(pPos.above(), this.defaultBlockState(), 2);
            } else if (aboveState.isAir() && aerialPart.canSurvive(aboveState, pLevel, pPos.above())) {
                pLevel.setBlock(pPos.above(), aerialPart.defaultBlockState().setValue(LotusAerialPartBlock.LOTUS_AGE, getBonemealAgeIncrease(pLevel) - 1), 2);
            }
        } else if (aboveState.getBlock().equals(ModBlocks.LOTUS_STALK.get()) || aboveState.getBlock().equals(ModBlocks.LOTUS_AERIAL_PART.get())) {
            BonemealableBlock growable = (BonemealableBlock) pLevel.getBlockState(pPos.above()).getBlock();
            if (growable.isValidBonemealTarget(pLevel, pPos.above(), aboveState, false)) {
                growable.performBonemeal(pLevel, pLevel.random, pPos.above(), aboveState);
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

    private static boolean isMaxHeight(ServerLevel pLevel, BlockPos pPos) {
        int h = 0;
        for (int i = -1; i < 3; i++) {
            if (i == 0 || pLevel.getBlockState(pPos.below(i)).is(ModBlocks.LOTUS_STALK.get()) || pLevel.getBlockState(pPos.below(i)).is(ModBlocks.LOTUS.get())) {
                h++;
            }
        }

        return h >= 3;
    }

}
