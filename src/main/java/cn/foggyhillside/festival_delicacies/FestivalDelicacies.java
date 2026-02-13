package cn.foggyhillside.festival_delicacies;

import cn.foggyhillside.festival_delicacies.blocks.screen.FermentationJarScreen;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveScreen;
import cn.foggyhillside.festival_delicacies.config.CommonConfigs;
import cn.foggyhillside.festival_delicacies.events.loot.CompostableAddition;
import cn.foggyhillside.festival_delicacies.network.ModNetwork;
import cn.foggyhillside.festival_delicacies.registry.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FestivalDelicacies.MODID)
public class FestivalDelicacies {
    public static final String MODID = "festival_delicacies";

    public FestivalDelicacies() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC);
        ModSounds.SOUNDS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);
        ModTreeDecorators.DECORATOR_TYPES.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModCreativeModeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvent {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenuTypes.STOVE_MENU.get(), StoveScreen::new);
            MenuScreens.register(ModMenuTypes.FERMENTATION_JAR_MENU.get(), FermentationJarScreen::new);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.JUJUBE_SAPLING.getId(), ModBlocks.POTTED_JUJUBE_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.OLIVE_SAPLING.getId(), ModBlocks.POTTED_OLIVE_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.WALNUT_SAPLING.getId(), ModBlocks.POTTED_WALNUT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.APRICOT_SAPLING.getId(), ModBlocks.POTTED_APRICOT_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.TANGERINE_SAPLING.getId(), ModBlocks.POTTED_TANGERINE_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PERSIMMON_SAPLING.getId(), ModBlocks.POTTED_PERSIMMON_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.HAWTHORN_SAPLING.getId(), ModBlocks.POTTED_HAWTHORN_SAPLING);
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.PEACH_SAPLING.getId(), ModBlocks.POTTED_PEACH_SAPLING);
        });

        CompostableAddition.addCompostable();
        ModNetwork.register();
    }
}
