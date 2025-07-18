package cn.foggyhillside.festival_delicacies.blocks.growers;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class JujubeGrower extends AbstractTreeGrower {

    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pHasFlowers) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(FestivalDelicacies.MODID, "patch_jujube"));
    }
}
