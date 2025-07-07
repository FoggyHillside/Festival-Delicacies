package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.screen.FermentationJarMenu;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, FestivalDelicacies.MODID);

    public static final RegistryObject<MenuType<StoveMenu>> STOVE_MENU = MENU_TYPES.register("stove_menu", () ->
            IForgeMenuType.create(StoveMenu::new));
    public static final RegistryObject<MenuType<FermentationJarMenu>> FERMENTATION_JAR_MENU = MENU_TYPES.register("fermentation_jar_menu", () ->
            IForgeMenuType.create(FermentationJarMenu::new));

}
