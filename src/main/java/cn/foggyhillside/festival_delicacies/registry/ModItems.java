package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.FoodList;
import cn.foggyhillside.festival_delicacies.items.BowlItem;
import cn.foggyhillside.festival_delicacies.items.DumplingItem;
import cn.foggyhillside.festival_delicacies.items.KitchenKnifeItem;
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

    //Tool
    public static final RegistryObject<Item> KITCHEN_KNIFE = ITEMS.register("kitchen_knife",
            () -> new KitchenKnifeItem(2F, -2.2F, Tiers.IRON, BlockTags.SWORD_EFFICIENT, new Item.Properties()));

    public static final RegistryObject<Item> UNCOOKED_SWEET_ZONGZI = ITEMS.register("uncooked_sweet_zongzi",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SWEET_ZONGZI = ITEMS.register("sweet_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.SWEET_ZONGZI)));
    public static final RegistryObject<Item> UNCOOKED_MEAT_ZONGZI = ITEMS.register("uncooked_meat_zongzi",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MEAT_ZONGZI = ITEMS.register("meat_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.MEAT_ZONGZI)));
    public static final RegistryObject<Item> QINGTUAN = ITEMS.register("qingtuan",
            () -> new Item(new Item.Properties().food(FoodList.QINGTUAN)));
    public static final RegistryObject<Item> FIVE_NUTS_MOONCAKE = ITEMS.register("five_nuts_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.FIVE_NUTS_MOONCAKE)));
    public static final RegistryObject<Item> HAWTHORN_AND_JUJUBE_PASTE_MOONCAKE = ITEMS.register("hawthorn_and_jujube_paste_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.HAWTHORN_AND_JUJUBE_PASTE_MOONCAKE)));
    public static final RegistryObject<Item> LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE = ITEMS.register("lotus_seed_paste_with_salted_yolk_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE)));
    public static final RegistryObject<Item> PRESERVED_MEAT_MOONCAKE = ITEMS.register("preserved_meat_mooncake",
            () -> new Item(new Item.Properties().food(FoodList.PRESERVED_MEAT_MOONCAKE)));
    //Ingredient
    public static final RegistryObject<Item> RED_BEAN_PASTE = ITEMS.register("red_bean_paste",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.RED_BEAN_PASTE)));
    public static final RegistryObject<Item> RED_BEAN = ITEMS.register("red_bean",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RICE = ITEMS.register("rice",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> JUJUBE = ITEMS.register("jujube",
            () -> new Item(new Item.Properties().food(FoodList.JUJUBE)));
    public static final RegistryObject<Item> PRESERVED_MEAT = ITEMS.register("preserved_meat",
            () -> new Item(new Item.Properties().food(FoodList.PRESERVED_MEAT)));
    public static final RegistryObject<Item> BAMBOO_LEAVES = ITEMS.register("bamboo_leaves",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MUGWORT_LEAVES = ITEMS.register("mugwort_leaves",
            () -> new Item(new Item.Properties()));
    //Crop
    public static final RegistryObject<Item> CHINESE_CABBAGE_SEEDS = ITEMS.register("chinese_cabbage_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CHINESE_CABBAGE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHINESE_CABBAGE = ITEMS.register("chinese_cabbage",
            () -> new Item(new Item.Properties().food(FoodList.CHINESE_CABBAGE)));
    public static final RegistryObject<Item> CHINESE_CABBAGE_LEAF = ITEMS.register("chinese_cabbage_leaf",
            () -> new Item(new Item.Properties().food(FoodList.CHINESE_CABBAGE_LEAF)));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic",
            () -> new ItemNameBlockItem(ModBlocks.GARLIC.get(), new Item.Properties().food(FoodList.GARLIC)));
    public static final RegistryObject<Item> GREENONION = ITEMS.register("greenonion",
            () -> new ItemNameBlockItem(ModBlocks.GREEN_ONION.get(), new Item.Properties().food(FoodList.GREEN_ONION)));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant",
            () -> new Item(new Item.Properties().food(FoodList.EGGPLANT)));
    public static final RegistryObject<Item> EGGPLANT_SLICE = ITEMS.register("eggplant_slice",
            () -> new Item(new Item.Properties().food(FoodList.EGGPLANT_SLICE)));
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds",
            () -> new ItemNameBlockItem(ModBlocks.EGGPLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> GARLIC_CHIVE = ITEMS.register("garlic_chive",
            () -> new Item(new Item.Properties().food(FoodList.GARLIC_CHIVE)));
    public static final RegistryObject<Item> GARLIC_CHIVE_SEEDS = ITEMS.register("garlic_chive_seeds",
            () -> new ItemNameBlockItem(ModBlocks.GARLIC_CHIVE.get(), new Item.Properties()));
    public static final RegistryObject<Item> FENNEL = ITEMS.register("fennel",
            () -> new Item(new Item.Properties().food(FoodList.FENNEL)));
    public static final RegistryObject<Item> FENNEL_SEEDS = ITEMS.register("fennel_seeds",
            () -> new ItemNameBlockItem(ModBlocks.FENNEL.get(), new Item.Properties()));
    //Dumplings
    public static final RegistryObject<Item> BOILED_DUMPLING = ITEMS.register("boiled_dumpling",
            () -> new DumplingItem(new Item.Properties().food(FoodList.DUMPLING), true));
    public static final RegistryObject<Item> STEAMED_DUMPLING = ITEMS.register("steamed_dumpling",
            () -> new DumplingItem(new Item.Properties().food(FoodList.DUMPLING), true));
    public static final RegistryObject<Item> BAOZI = ITEMS.register("baozi",
            () -> new DumplingItem(new Item.Properties().food(FoodList.DUMPLING), true));
    //Wonton
    public static final RegistryObject<Item> WONTON = ITEMS.register("wonton",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.getWontonFood()), true));
}
