package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.FoodList;
import cn.foggyhillside.festival_delicacies.blocks.BowlItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FestivalDelicacies.MOD_ID);

    //BlockItem
    public static final RegistryObject<Item> STOVE = ITEMS.register("stove",
            () -> new BlockItem(ModBlocks.STOVE.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> POT = ITEMS.register("pot",
            () -> new BlockItem(ModBlocks.POT.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));

    //Item
    public static final RegistryObject<Item> UNCOOKED_SWEET_ZONGZI = ITEMS.register("uncooked_sweet_zongzi",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> SWEET_ZONGZI = ITEMS.register("sweet_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.SWEET_ZONGZI).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> UNCOOKED_MEAT_ZONGZI = ITEMS.register("uncooked_meat_zongzi",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> MEAT_ZONGZI = ITEMS.register("meat_zongzi",
            () -> new Item(new Item.Properties().food(FoodList.MEAT_ZONGZI).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> QINGTUAN = ITEMS.register("qingtuan",
            () -> new Item(new Item.Properties().food(FoodList.QINGTUAN).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> RED_BEAN_PASTE = ITEMS.register("red_bean_paste",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.RED_BEAN_PASTE).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> RED_BEAN = ITEMS.register("red_bean",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> RICE = ITEMS.register("rice",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> JUJUBE = ITEMS.register("jujube",
            () -> new Item(new Item.Properties().food(FoodList.JUJUBE).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PRESERVED_MEAT = ITEMS.register("preserved_meat",
            () -> new Item(new Item.Properties().food(FoodList.PRESERVED_MEAT).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> BAMBOO_LEAF = ITEMS.register("bamboo_leaf",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> ARTEMISIA_ARGYI = ITEMS.register("artemisia_argyi",
            () -> new Item(new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    //Crop
    public static final RegistryObject<Item> CHINESE_CABBAGE_SEEDS = ITEMS.register("chinese_cabbage_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CHINESE_CABBAGE.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> CHINESE_CABBAGE = ITEMS.register("chinese_cabbage",
            ()-> new Item(new Item.Properties().food(FoodList.CHINESE_CABBAGE).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> CHINESE_CABBAGE_LEAF = ITEMS.register("chinese_cabbage_leaf",
            ()-> new Item(new Item.Properties().food(FoodList.CHINESE_CABBAGE_LEAF).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic",
            ()-> new ItemNameBlockItem(ModBlocks.GARLIC.get(), new Item.Properties().food(FoodList.GARLIC).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> GREENONION = ITEMS.register("greenonion",
            ()-> new ItemNameBlockItem(ModBlocks.GREEN_ONION.get(), new Item.Properties().food(FoodList.GREEN_ONION).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant",
            ()-> new Item(new Item.Properties().food(FoodList.EGGPLANT).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.EGGPLANT.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> GARLIC_CHIVE = ITEMS.register("garlic_chive",
            ()-> new Item(new Item.Properties().food(FoodList.GARLIC_CHIVE).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> GARLIC_CHIVE_SEEDS = ITEMS.register("garlic_chive_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.GARLIC_CHIVE.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> FENNEL = ITEMS.register("fennel",
            ()-> new Item(new Item.Properties().food(FoodList.FENNEL).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> FENNEL_SEEDS = ITEMS.register("fennel_seeds",
            ()-> new ItemNameBlockItem(ModBlocks.FENNEL.get(), new Item.Properties().tab(FestivalDelicacies.FestivalDelicaciesTab)));
    //Dumplings
    public static final RegistryObject<Item> PORK_CABBAGE_BOILED_DUMPLING = ITEMS.register("pork_cabbage_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.PORK_CABBAGE_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PORK_KELP_BOILED_DUMPLING = ITEMS.register("pork_kelp_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.PORK_KELP_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PORK_POTATO_BOILED_DUMPLING = ITEMS.register("pork_potato_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.PORK_POTATO_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PORK_FENNEL_BOILED_DUMPLING = ITEMS.register("pork_fennel_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.PORK_FENNEL_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> MUTTON_BOILED_DUMPLING = ITEMS.register("mutton_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.MUTTON_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> CHICKEN_MUSHROOM_BOILED_DUMPLING = ITEMS.register("chicken_mushroom_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.CHICKEN_MUSHROOM_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> COD_BOILED_DUMPLING = ITEMS.register("cod_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.COD_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> SALMON_BOILED_DUMPLING = ITEMS.register("salmon_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.SALMON_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> EGGPLANT_EGG_BOILED_DUMPLING = ITEMS.register("eggplant_egg_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.EGGPLANT_EGG_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> MUSHROOM_BOILED_DUMPLING = ITEMS.register("mushroom_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.MUSHROOM_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> FUNGUS_BOILED_DUMPLING = ITEMS.register("fungus_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.FUNGUS_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> GARLIC_CHIVE_EGG_BOILED_DUMPLING = ITEMS.register("garlic_chive_egg_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.GARLIC_CHIVE_EGG_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> DANDELION_LEAF_BOILED_DUMPLING = ITEMS.register("dandelion_leaf_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.DANDELION_LEAF_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PUFFERFISH_BOILED_DUMPLING = ITEMS.register("pufferfish_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.PUFFERFISH_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> RABBIT_MEAT_BOILED_DUMPLING = ITEMS.register("rabbit_meat_boiled_dumpling",
            () -> new Item(new Item.Properties().food(FoodList.RABBIT_MEAT_BOILED_DUMPLING).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    //Wonton
    public static final RegistryObject<Item> PORK_CARROT_WONTON = ITEMS.register("pork_carrot_wonton",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.PORK_CARROT_WONTON).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PORK_MUSHROOM_WONTON = ITEMS.register("pork_mushroom_wonton",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.PORK_MUSHROOM_WONTON).tab(FestivalDelicacies.FestivalDelicaciesTab)));
    public static final RegistryObject<Item> PORK_CABBAGE_WONTON = ITEMS.register("pork_cabbage_wonton",
            () -> new BowlItem(new Item.Properties().craftRemainder(Items.BOWL).stacksTo(16).food(FoodList.PORK_CABBAGE_WONTON).tab(FestivalDelicacies.FestivalDelicaciesTab)));
}
