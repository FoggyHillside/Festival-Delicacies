package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.worldgen.LogInCrownDecorator;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> DECORATOR_TYPES = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, FestivalDelicacies.MODID);

    public static final RegistryObject<TreeDecoratorType<LogInCrownDecorator>> LOG_IN_CROWN_DECORATOR = DECORATOR_TYPES.register("log_in_crown_decorator", () -> new TreeDecoratorType<>(LogInCrownDecorator.CODEC));

}
