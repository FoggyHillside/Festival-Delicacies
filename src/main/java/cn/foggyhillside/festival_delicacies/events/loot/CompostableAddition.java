package cn.foggyhillside.festival_delicacies.events.loot;

import cn.foggyhillside.festival_delicacies.registry.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostableAddition {

    public static void addCompostable(){
        add(0.3F, ModItems.APRICOT_KERNEL.get());
        add(0.3F, ModItems.BAMBOO_LEAVES.get());
        add(0.3F, ModItems.SUNFLOWER_SEEDS.get());
        add(0.3F, ModItems.SHELLED_MELON_SEEDS.get());
        add(0.3F, ModItems.SHELLED_PUMPKIN_SEEDS.get());
        add(0.3F, ModItems.UNKNOWN_SEEDS.get());
        add(0.3F, ModItems.RED_BEAN.get());
        add(0.3F, ModItems.SESAME.get());
        add(0.3F, ModItems.PEANUT.get());
        add(0.3F, ModItems.RICE.get());
        add(0.3F, ModItems.CHINESE_CABBAGE_SEEDS.get());
        add(0.3F, ModItems.GREEN_ONION_SEEDS.get());
        add(0.3F, ModItems.EGGPLANT_SEEDS.get());
        add(0.3F, ModItems.GARLIC_CHIVE_SEEDS.get());
        add(0.3F, ModItems.FENNEL_SEEDS.get());
        add(0.3F, ModItems.LOTUS_SEEDS.get());
        add(0.3F, ModItems.LOTUS_FLOWER.get());
        add(0.3F, ModItems.JUJUBE_SAPLING.get());
        add(0.3F, ModItems.JUJUBE_LEAVES.get());
        add(0.3F, ModItems.OLIVE_SAPLING.get());
        add(0.3F, ModItems.OLIVE_LEAVES.get());
        add(0.3F, ModItems.WALNUT_SAPLING.get());
        add(0.3F, ModItems.WALNUT_LEAVES.get());
        add(0.3F, ModItems.APRICOT_SAPLING.get());
        add(0.3F, ModItems.APRICOT_LEAVES.get());
        add(0.3F, ModItems.TANGERINE_SAPLING.get());
        add(0.3F, ModItems.TANGERINE_LEAVES.get());
        add(0.3F, ModItems.PERSIMMON_SAPLING.get());
        add(0.3F, ModItems.PERSIMMON_LEAVES.get());
        add(0.3F, ModItems.HAWTHORN_SAPLING.get());
        add(0.3F, ModItems.HAWTHORN_LEAVES.get());
        add(0.3F, ModItems.PEACH_SAPLING.get());
        add(0.3F, ModItems.PEACH_LEAVES.get());
        add(0.5F, ModItems.APRICOT_PULP.get());
        add(0.5F, ModItems.CHINESE_CABBAGE_LEAF.get());
        add(0.5F, ModItems.EGGPLANT_SLICE.get());
        add(0.65F, ModItems.CHINESE_CABBAGE.get());
        add(0.65F, ModItems.GARLIC.get());
        add(0.65F, ModItems.GREEN_ONION.get());
        add(0.65F, ModItems.EGGPLANT.get());
        add(0.65F, ModItems.GARLIC_CHIVE.get());
        add(0.65F, ModItems.FENNEL.get());
        add(0.65F, ModItems.LOTUS_ROOT.get());
        add(0.65F, ModItems.WILD_GARLIC.get());
        add(0.65F, ModItems.MUGWORT.get());
        add(0.65F, ModItems.JUJUBE.get());
        add(0.65F, ModItems.OLIVE.get());
        add(0.65F, ModItems.WALNUT.get());
        add(0.65F, ModItems.APRICOT.get());
        add(0.65F, ModItems.TANGERINE.get());
        add(0.65F, ModItems.PERSIMMON.get());
        add(0.65F, ModItems.HAWTHORN.get());
        add(0.65F, ModItems.PEACH.get());
        add(0.85F, ModItems.UNCOOKED_SWEET_ZONGZI.get());
        add(0.85F, ModItems.UNCOOKED_MEAT_ZONGZI.get());
        add(0.85F, ModItems.SWEET_ZONGZI.get());
        add(0.85F, ModItems.MEAT_ZONGZI.get());
        add(0.85F, ModItems.QINGTUAN.get());
        add(0.85F, ModItems.LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE.get());
        add(0.85F, ModItems.PRESERVED_MEAT_MOONCAKE.get());
        add(0.85F, ModItems.MANTOU.get());
        add(0.85F, ModItems.BOILED_DUMPLING.get());
        add(0.85F, ModItems.STEAMED_DUMPLING.get());
        add(0.85F, ModItems.BAOZI.get());
        add(0.85F, ModItems.CANDIED_TANGERINE.get());
        add(0.85F, ModItems.DRIED_PERSIMMON.get());
        add(1.0F, ModItems.FIVE_NUTS_MOONCAKE.get());
        add(1.0F, ModItems.HAWTHORN_WITH_JUJUBE_PASTE_MOONCAKE.get());

    }

    private static void add(float f, ItemLike itemLike) {
        ComposterBlock.COMPOSTABLES.put(itemLike.asItem(), f);
    }

}