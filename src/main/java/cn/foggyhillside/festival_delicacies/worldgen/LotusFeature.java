package cn.foggyhillside.festival_delicacies.worldgen;

import cn.foggyhillside.festival_delicacies.blocks.LotusAerialPartBlock;
import cn.foggyhillside.festival_delicacies.blocks.LotusBlock;
import cn.foggyhillside.festival_delicacies.blocks.LotusStalkBlock;
import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LotusFeature extends Feature<NoneFeatureConfiguration> {

    public LotusFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos originPos = pContext.origin();
        RandomSource randomsource = pContext.random();
        int i = worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, originPos.getX(), originPos.getZ());
        int j = worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, originPos.getX(), originPos.getZ());
        int waterHeight = j - i;
        BlockPos blockPos = new BlockPos(originPos.getX(), i, originPos.getZ());
        LotusBlock lotus = (LotusBlock) ModBlocks.LOTUS.get();
        LotusStalkBlock lotusStalk = (LotusStalkBlock) ModBlocks.LOTUS_STALK.get();
        LotusAerialPartBlock lotusAerialPart = (LotusAerialPartBlock) ModBlocks.LOTUS_AERIAL_PART.get();
        if (waterHeight > 0 && worldgenlevel.isEmptyBlock(blockPos.above(waterHeight))) {
            if (randomsource.nextBoolean() && lotus.defaultBlockState().canSurvive(worldgenlevel, blockPos)) {
                for (int k = 0; k < waterHeight + 1; k++) {
                    if (k == 0) {
                        worldgenlevel.setBlock(blockPos, lotus.defaultBlockState().setValue(LotusBlock.AGE, lotus.getMaxAge()).setValue(LotusBlock.HAS_STALK, true), 2);
                    } else if (k == waterHeight) {
                        worldgenlevel.setBlock(blockPos, lotusAerialPart.defaultBlockState().setValue(LotusAerialPartBlock.LOTUS_AGE, 1 + randomsource.nextInt(lotusAerialPart.getMaxAge())), 2);
                    } else {
                        worldgenlevel.setBlock(blockPos, lotusStalk.defaultBlockState().setValue(LotusStalkBlock.HAS_STALK, true), 2);
                    }
                    blockPos = blockPos.above();
                }
            } else {
                worldgenlevel.setBlock(blockPos.atY(j), Blocks.LILY_PAD.defaultBlockState(), 2);
            }

            return true;
        }

        return false;
    }

}
