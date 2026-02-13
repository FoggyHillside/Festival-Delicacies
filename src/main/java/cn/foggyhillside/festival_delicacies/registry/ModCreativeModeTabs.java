package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.items.DumplingItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FestivalDelicacies.MODID);

    public static final RegistryObject<CreativeModeTab> FESTIVAL_DELICACIES_TAB = CREATIVE_MODE_TABS.register(FestivalDelicacies.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.festival_delicacies"))
            .icon(() -> ModItems.SWEET_ZONGZI.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.STOVE.get());
                output.accept(ModItems.CHOPPING_BLOCK.get());
                output.accept(ModItems.WOODEN_PLATE.get());
                output.accept(ModItems.DRYING_RACK.get());
                output.accept(ModItems.FERMENTATION_JAR.get());
                output.accept(ModItems.KITCHEN_KNIFE.get());
                output.accept(ModItems.UNCOOKED_SWEET_ZONGZI.get());
                output.accept(ModItems.UNCOOKED_MEAT_ZONGZI.get());
                output.accept(ModItems.SWEET_ZONGZI.get());
                output.accept(ModItems.MEAT_ZONGZI.get());
                output.accept(ModItems.QINGTUAN.get());
                output.accept(ModItems.FIVE_NUTS_MOONCAKE.get());
                output.accept(ModItems.HAWTHORN_WITH_JUJUBE_PASTE_MOONCAKE.get());
                output.accept(ModItems.LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE.get());
                output.accept(ModItems.PRESERVED_MEAT_MOONCAKE.get());
                output.accept(ModItems.MANTOU.get());
                output.accept(ModItems.SWEETENED_LOTUS_SEED_PASTE.get());
                output.accept(ModItems.JUJUBE_PASTE.get());
                output.accept(ModItems.RED_BEAN_PASTE.get());
                output.accept(ModItems.PRESERVED_MEAT.get());
                output.accept(ModItems.SALTED_EGG_YOLK.get());
                output.accept(ModItems.CANDIED_TANGERINE.get());
                output.accept(ModItems.DRIED_PERSIMMON.get());
                output.accept(ModItems.APRICOT_KERNEL.get());
                output.accept(ModItems.APRICOT_PULP.get());
                output.accept(ModItems.BAMBOO_LEAVES.get());
                output.accept(ModItems.SUNFLOWER_SEEDS.get());
                output.accept(ModItems.SHELLED_MELON_SEEDS.get());
                output.accept(ModItems.SHELLED_PUMPKIN_SEEDS.get());
                output.accept(ModItems.UNKNOWN_SEEDS.get());
                output.accept(ModItems.RED_BEAN.get());
                output.accept(ModItems.SESAME.get());
                output.accept(ModItems.PEANUT.get());
                output.accept(ModItems.RICE.get());
                output.accept(ModItems.CHINESE_CABBAGE_SEEDS.get());
                output.accept(ModItems.CHINESE_CABBAGE.get());
                output.accept(ModItems.CHINESE_CABBAGE_LEAF.get());
                output.accept(ModItems.GARLIC.get());
                output.accept(ModItems.GREEN_ONION_SEEDS.get());
                output.accept(ModItems.GREEN_ONION.get());
                output.accept(ModItems.EGGPLANT_SEEDS.get());
                output.accept(ModItems.EGGPLANT.get());
                output.accept(ModItems.EGGPLANT_SLICE.get());
                output.accept(ModItems.GARLIC_CHIVE_SEEDS.get());
                output.accept(ModItems.GARLIC_CHIVE.get());
                output.accept(ModItems.FENNEL_SEEDS.get());
                output.accept(ModItems.FENNEL.get());
                output.accept(ModItems.LOTUS_ROOT.get());
                output.accept(ModItems.LOTUS_SEEDS.get());
                output.accept(ModItems.LOTUS_FLOWER.get());
                output.accept(DumplingItem.makeDumpling(5,
                        ModItems.GARLIC_CHIVE.get(),
                        Items.EGG));
                output.accept(DumplingItem.makeDumpling(6,
                        ModItems.CHINESE_CABBAGE_LEAF.get(),
                        Items.COOKED_PORKCHOP));
                output.accept(DumplingItem.makeDumpling(7,
                        Items.COOKED_PORKCHOP,
                        ModItems.GREEN_ONION.get()));
                output.accept(ModItems.WONTON.get());
                output.accept(ModItems.WILD_GARLIC.get());
                output.accept(ModItems.MUGWORT.get());
                output.accept(ModItems.JUJUBE.get());
                output.accept(ModItems.JUJUBE_SAPLING.get());
                output.accept(ModItems.JUJUBE_LEAVES.get());
                output.accept(ModItems.JUJUBE_LOG.get());
                output.accept(ModItems.OLIVE.get());
                output.accept(ModItems.OLIVE_SAPLING.get());
                output.accept(ModItems.OLIVE_LEAVES.get());
                output.accept(ModItems.OLIVE_LOG.get());
                output.accept(ModItems.WALNUT.get());
                output.accept(ModItems.WALNUT_SAPLING.get());
                output.accept(ModItems.WALNUT_LEAVES.get());
                output.accept(ModItems.WALNUT_LOG.get());
                output.accept(ModItems.APRICOT.get());
                output.accept(ModItems.APRICOT_SAPLING.get());
                output.accept(ModItems.APRICOT_LEAVES.get());
                output.accept(ModItems.APRICOT_LOG.get());
                output.accept(ModItems.TANGERINE.get());
                output.accept(ModItems.TANGERINE_SAPLING.get());
                output.accept(ModItems.TANGERINE_LEAVES.get());
                output.accept(ModItems.TANGERINE_LOG.get());
                output.accept(ModItems.PERSIMMON.get());
                output.accept(ModItems.PERSIMMON_SAPLING.get());
                output.accept(ModItems.PERSIMMON_LEAVES.get());
                output.accept(ModItems.PERSIMMON_LOG.get());
                output.accept(ModItems.HAWTHORN.get());
                output.accept(ModItems.HAWTHORN_SAPLING.get());
                output.accept(ModItems.HAWTHORN_LEAVES.get());
                output.accept(ModItems.HAWTHORN_LOG.get());
                output.accept(ModItems.PEACH.get());
                output.accept(ModItems.PEACH_SAPLING.get());
                output.accept(ModItems.PEACH_LEAVES.get());
                output.accept(ModItems.PEACH_LOG.get());
            }).build());
}
