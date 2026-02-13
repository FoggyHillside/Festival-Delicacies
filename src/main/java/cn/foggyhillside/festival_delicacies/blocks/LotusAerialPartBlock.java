package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LotusAerialPartBlock extends CropBlock {
    public static final IntegerProperty LOTUS_AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0F, 0.0F, 6.0F, 10.0F, 5.0F, 10.0F),
            Block.box(6.0F, 0.0F, 6.0F, 10.0F, 5.0F, 10.0F),
            Block.box(2.0D, 0.0D, 2.0D, 14.0D, 9.0D, 14.0D),
            Block.box(5.0F, 0.0F, 5.0F, 11.0F, 14.0F, 11.0F)
    };

    public LotusAerialPartBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_AGE[this.getAge(pState)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.LOTUS_SEEDS.get();
    }

    @Override
    protected IntegerProperty getAgeProperty() {
        return LOTUS_AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(this.getAgeProperty());
    }

    @Override
    protected int getBonemealAgeIncrease(Level pLevel) {
        return Mth.nextInt(pLevel.random, 1, 2);
    }


    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(ModBlocks.LOTUS.get()) || pState.is(ModBlocks.LOTUS_STALK.get());
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return (pLevel.getRawBrightness(pPos, 0) >= 8 || pLevel.canSeeSky(pPos)) && this.mayPlaceOn(pLevel.getBlockState(pPos.below()), pLevel, pPos);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (!pLevel.isAreaLoaded(pPos, 1)) return;
        if (pLevel.getRawBrightness(pPos, 0) >= 9) {
            int i = this.getAge(pState);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, pLevel, pPos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, pRandom.nextInt((int) (25.0F / f) + 1) == 0)) {
                    pLevel.setBlock(pPos, this.getStateForAge(i + 1), 2);
                    if (i >= 1) {
                        growLilyPadRandomly(pLevel, pPos, pRandom);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(pLevel, pPos, pState);
                }
            }
        }
    }

    @Override
    public void growCrops(Level pLevel, BlockPos pPos, BlockState pState) {
        super.growCrops(pLevel, pPos, pState);
        if (getAge(pLevel.getBlockState(pPos)) >= 2) {
            RandomSource random = pLevel.getRandom();
            growLilyPadRandomly(pLevel, pPos, random);
        }
    }

    public void growLilyPadRandomly(Level pLevel, BlockPos pPos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            int[][] offsets = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            int i = random.nextInt(offsets.length);
            int[] offset = offsets[i];
            for (int j = 0; j < 4; j++) {
                BlockPos neighborPos = pPos.offset(offset[0], 0, offset[1]);
                WaterlilyBlock waterlilyBlock = (WaterlilyBlock) Blocks.LILY_PAD;
                BlockState lilyPadState = waterlilyBlock.defaultBlockState();
                if (pLevel.getBlockState(neighborPos).isAir() && lilyPadState.canSurvive(pLevel, neighborPos)) {
                    pLevel.setBlock(neighborPos, lilyPadState, 2);
                    break;
                } else {
                    offset = offsets[(i + 1) % offsets.length];
                }
            }
        }
    }
}
