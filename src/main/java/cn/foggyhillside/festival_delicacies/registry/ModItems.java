package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.FoodList;
import cn.foggyhillside.festival_delicacies.items.BowlItem;
import cn.foggyhillside.festival_delicacies.items.DumplingItem;
import cn.foggyhillside.festival_delicacies.items.KitchenKnifeItem;
import cn.foggyhillside.festival_delicacies.items.UnknownSeedsItem;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FestivalDelicacies.MODID);

    //BlockItem
    public static final RegistryObject<Item> STOVE = ITEMS.register("stove",
            () -> new BlockItem(ModBlocks.STOVE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHOPPING_BLOCK = ITEMS.register("chopping_block",
            () -> new BlockItem(ModBlocks.CHOPPING_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> WOODEN_PLATE = ITEMS.register("wooden_plate",
            () -> new BlockItem(ModBlocks.WOODEN_PLATE.get(), new Item.Properties()));
    public static final RegistryObject<Item> DRYING_RACK = ITEMS.register("drying_rack",
            () -> new BlockItem(ModBlocks.DRYING_RACK.get(), new Item.Properties()));
    public static final RegistryObject<Item> FERMENTATION_JAR = ITEMS.register("fermentation_jar",
            () -> new BlockItem(ModBlocks.FERMENTATION_JAR.get(), new Item.Properties()));

    //Tool
    public static final RegistryObject<Item> KITCHEN_KNIFE = ITEMS.register("kitchen_knife",
            () -> new KitchenKnifeItem(2F, -2.2F, Tiers.IRON, BlockTags.SWORD_EFFICIENT, new Item.Properties()));

    public static final RegistryObject<Item> UNCOOKED_SWEET_ZONGZI = ITEMS.register("uncooked_sweet_zongzi",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SWEET_ZONGZI = ITEMS.register("sweet_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_7_0$6)));
    public static final RegistryObject<Item> UNCOOKED_MEAT_ZONGZI = ITEMS.register("uncooked_meat_zongzi",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEAT_ZONGZI = ITEMS.register("meat_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_9_0$6)));
    public static final RegistryObject<Item> QINGTUAN = ITEMS.register("qingtuan",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_5_0$6)));
    public static final RegistryObject<Item> FIVE_NUTS_MOONCAKE = ITEMS.register("five_nuts_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_8_0$6)));
    public static final RegistryObject<Item> HAWTHORN_WITH_JUJUBE_PASTE_MOONCAKE = ITEMS.register("hawthorn_with_jujube_paste_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_6_0$5)));
    public static final RegistryObject<Item> LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE = ITEMS.register("lotus_seed_paste_with_salted_yolk_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_8_0$6)));
    public static final RegistryObject<Item> PRESERVED_MEAT_MOONCAKE = ITEMS.register("preserved_meat_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_7_0$6)));
    public static final RegistryObject<Item> MANTOU = ITEMS.register("mantou",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_5_0$6)));

    public static final RegistryObject<Item> SWEETENED_LOTUS_SEED_PASTE = ITEMS.register("sweetened_lotus_seed_paste",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.FOOD_4_0$5)));
    public static final RegistryObject<Item> JUJUBE_PASTE = ITEMS.register("jujube_paste",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.FOOD_6_0$5)));
    public static final RegistryObject<Item> RED_BEAN_PASTE = ITEMS.register("red_bean_paste",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.FOOD_4_0$5)));
    public static final RegistryObject<Item> PRESERVED_MEAT = ITEMS.register("preserved_meat",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_5_0$6)));
    public static final RegistryObject<Item> SALTED_EGG_YOLK = ITEMS.register("salted_egg_yolk",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> CANDIED_TANGERINE = ITEMS.register("candied_tangerine",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_4_0$5)));
    public static final RegistryObject<Item> DRIED_PERSIMMON = ITEMS.register("dried_persimmon",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> APRICOT_KERNEL = ITEMS.register("apricot_kernel",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> APRICOT_PULP = ITEMS.register("apricot_pulp",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> BAMBOO_LEAVES = ITEMS.register("bamboo_leaves",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SUNFLOWER_SEEDS = ITEMS.register("sunflower_seeds",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> SHELLED_MELON_SEEDS = ITEMS.register("shelled_melon_seeds",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> SHELLED_PUMPKIN_SEEDS = ITEMS.register("shelled_pumpkin_seeds",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));

    //Crop
    public static final RegistryObject<Item> UNKNOWN_SEEDS = ITEMS.register("unknown_seeds",
            () -> new UnknownSeedsItem(new Item.Properties()));
    public static final RegistryObject<Item> CHINESE_CABBAGE_SEEDS = ITEMS.register("chinese_cabbage_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CHINESE_CABBAGE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHINESE_CABBAGE = ITEMS.register("chinese_cabbage",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_3_0$5)));
    public static final RegistryObject<Item> CHINESE_CABBAGE_LEAF = ITEMS.register("chinese_cabbage_leaf",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic",
            () -> new ItemNameBlockItem(ModBlocks.GARLIC.get(), new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> GREEN_ONION_SEEDS = ITEMS.register("greenonion_seeds",
            () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION.get(), new Item.Properties()));
    public static final RegistryObject<Item> GREEN_ONION = ITEMS.register("greenonion",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EGGPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_3_0$5)));
    public static final RegistryObject<Item> EGGPLANT_SLICE = ITEMS.register("eggplant_slice",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> GARLIC_CHIVE_SEEDS = ITEMS.register("garlic_chive_seeds",
            () -> new ItemNameBlockItem(ModBlocks.GARLIC_CHIVE.get(), new Item.Properties()));
    public static final RegistryObject<Item> GARLIC_CHIVE = ITEMS.register("garlic_chive",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> FENNEL_SEEDS = ITEMS.register("fennel_seeds",
            () -> new ItemNameBlockItem(ModBlocks.FENNEL.get(), new Item.Properties()));
    public static final RegistryObject<Item> FENNEL = ITEMS.register("fennel",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> RED_BEAN = ITEMS.register("red_bean",
            () -> new ItemNameBlockItem(ModBlocks.RED_BEAN.get(), new Item.Properties()));
    public static final RegistryObject<Item> SESAME = ITEMS.register("sesame",
            () -> new ItemNameBlockItem(ModBlocks.SESAME.get(), new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> PEANUT = ITEMS.register("peanut",
            () -> new ItemNameBlockItem(ModBlocks.PEANUT.get(), new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> RICE = ITEMS.register("rice",
            () -> new ItemNameBlockItem(ModBlocks.RICE.get(), new Item.Properties()));
    public static final RegistryObject<Item> LOTUS_ROOT = ITEMS.register("lotus_root",
            () -> new ItemNameBlockItem(ModBlocks.LOTUS.get(), new Item.Properties().food(FoodList.FOOD_3_0$5)));
    public static final RegistryObject<Item> LOTUS_SEEDS = ITEMS.register("lotus_seeds",
            () -> new ItemNameBlockItem(ModBlocks.LOTUS.get(), new Item.Properties().food(FoodList.FOOD_1_0$4)));
    public static final RegistryObject<Item> LOTUS_FLOWER = ITEMS.register("lotus_flower",
            () -> new PlaceOnWaterBlockItem(ModBlocks.LOTUS_FLOWER.get(), new Item.Properties()));

    //Dumplings
    public static final RegistryObject<Item> BOILED_DUMPLING = ITEMS.register("boiled_dumpling",
            () -> new DumplingItem(new Item.Properties().food(FoodList.FOOD_2_0$4), true));
    public static final RegistryObject<Item> STEAMED_DUMPLING = ITEMS.register("steamed_dumpling",
            () -> new DumplingItem(new Item.Properties().food(FoodList.FOOD_2_0$4), true));
    public static final RegistryObject<Item> BAOZI = ITEMS.register("baozi",
            () -> new DumplingItem(new Item.Properties().food(FoodList.FOOD_2_0$4), true));

    //Wonton
    public static final RegistryObject<Item> WONTON = ITEMS.register("wonton",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.getWontonFood()), true));

    //Wild Plant
    public static final RegistryObject<Item> WILD_GARLIC = ITEMS.register("wild_garlic",
            () -> new BlockItem(ModBlocks.WILD_GARLIC.get(), new Item.Properties()));
    public static final RegistryObject<Item> MUGWORT = ITEMS.register("mugwort",
            () -> new ItemNameBlockItem(ModBlocks.MUGWORT.get(), new Item.Properties()));

    //Fruit Tree
    public static final RegistryObject<Item> JUJUBE = ITEMS.register("jujube",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> JUJUBE_SAPLING = ITEMS.register("jujube_sapling",
            () -> new BlockItem(ModBlocks.JUJUBE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> JUJUBE_LEAVES = ITEMS.register("jujube_leaves",
            () -> new BlockItem(ModBlocks.JUJUBE_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> JUJUBE_LOG = ITEMS.register("jujube_log",
            () -> new BlockItem(ModBlocks.JUJUBE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> OLIVE = ITEMS.register("olive",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> OLIVE_SAPLING = ITEMS.register("olive_sapling",
            () -> new BlockItem(ModBlocks.OLIVE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> OLIVE_LEAVES = ITEMS.register("olive_leaves",
            () -> new BlockItem(ModBlocks.OLIVE_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> OLIVE_LOG = ITEMS.register("olive_log",
            () -> new BlockItem(ModBlocks.OLIVE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> WALNUT = ITEMS.register("walnut",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> WALNUT_SAPLING = ITEMS.register("walnut_sapling",
            () -> new BlockItem(ModBlocks.WALNUT_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> WALNUT_LEAVES = ITEMS.register("walnut_leaves",
            () -> new BlockItem(ModBlocks.WALNUT_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> WALNUT_LOG = ITEMS.register("walnut_log",
            () -> new BlockItem(ModBlocks.WALNUT_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> APRICOT = ITEMS.register("apricot",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> APRICOT_SAPLING = ITEMS.register("apricot_sapling",
            () -> new BlockItem(ModBlocks.APRICOT_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> APRICOT_LEAVES = ITEMS.register("apricot_leaves",
            () -> new BlockItem(ModBlocks.APRICOT_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> APRICOT_LOG = ITEMS.register("apricot_log",
            () -> new BlockItem(ModBlocks.APRICOT_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> TANGERINE = ITEMS.register("tangerine",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> TANGERINE_SAPLING = ITEMS.register("tangerine_sapling",
            () -> new BlockItem(ModBlocks.TANGERINE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> TANGERINE_LEAVES = ITEMS.register("tangerine_leaves",
            () -> new BlockItem(ModBlocks.TANGERINE_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> TANGERINE_LOG = ITEMS.register("tangerine_log",
            () -> new BlockItem(ModBlocks.TANGERINE_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERSIMMON = ITEMS.register("persimmon",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> PERSIMMON_SAPLING = ITEMS.register("persimmon_sapling",
            () -> new BlockItem(ModBlocks.PERSIMMON_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERSIMMON_LEAVES = ITEMS.register("persimmon_leaves",
            () -> new BlockItem(ModBlocks.PERSIMMON_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> PERSIMMON_LOG = ITEMS.register("persimmon_log",
            () -> new BlockItem(ModBlocks.PERSIMMON_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAWTHORN = ITEMS.register("hawthorn",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> HAWTHORN_SAPLING = ITEMS.register("hawthorn_sapling",
            () -> new BlockItem(ModBlocks.HAWTHORN_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAWTHORN_LEAVES = ITEMS.register("hawthorn_leaves",
            () -> new BlockItem(ModBlocks.HAWTHORN_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAWTHORN_LOG = ITEMS.register("hawthorn_log",
            () -> new BlockItem(ModBlocks.HAWTHORN_LOG.get(), new Item.Properties()));
    public static final RegistryObject<Item> PEACH = ITEMS.register("peach",
            () -> new Item(new Item.Properties().food(FoodList.FOOD_2_0$4)));
    public static final RegistryObject<Item> PEACH_SAPLING = ITEMS.register("peach_sapling",
            () -> new BlockItem(ModBlocks.PEACH_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> PEACH_LEAVES = ITEMS.register("peach_leaves",
            () -> new BlockItem(ModBlocks.PEACH_LEAVES.get(), new Item.Properties()));
    public static final RegistryObject<Item> PEACH_LOG = ITEMS.register("peach_log",
            () -> new BlockItem(ModBlocks.PEACH_LOG.get(), new Item.Properties()));
}
