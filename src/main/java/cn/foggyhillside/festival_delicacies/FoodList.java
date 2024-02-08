package cn.foggyhillside.festival_delicacies;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class FoodList {

    public static final FoodProperties SWEET_ZONGZI = new FoodProperties.Builder().nutrition(7).saturationMod(0.6F).build();
    public static final FoodProperties MEAT_ZONGZI = new FoodProperties.Builder().nutrition(9).saturationMod(0.6F).build();
    public static final FoodProperties QINGTUAN = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
    public static final FoodProperties JUJUBE = new FoodProperties.Builder().nutrition(2).saturationMod(0.7F).build();
    public static final FoodProperties PRESERVED_MEAT = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties RED_BEAN_PASTE = new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).build();
    public static final FoodProperties PORK_CABBAGE_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(8).saturationMod(0.9F).build();
    public static final FoodProperties PORK_CARROT_WONTON = new FoodProperties.Builder().nutrition(17).saturationMod(0.5F).build();
    public static final FoodProperties PORK_MUSHROOM_WONTON = new FoodProperties.Builder().nutrition(16).saturationMod(0.6F).build();
    public static final FoodProperties PORK_CABBAGE_WONTON = new FoodProperties.Builder().nutrition(12).saturationMod(0.8F).build();
    public static final FoodProperties FUNGUS_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties PORK_KELP_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(7).saturationMod(0.8F).build();
    public static final FoodProperties DANDELION_LEAF_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION,3*20,0), 1).build();
    public static final FoodProperties PUFFERFISH_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).effect(() -> new MobEffectInstance(MobEffects.POISON,3*20,0),0.01F).effect(() -> new MobEffectInstance(MobEffects.WATER_BREATHING,3*20,0), 1).build();
    public static final FoodProperties RABBIT_MEAT_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).build();
    public static final FoodProperties MUTTON_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties CHICKEN_MUSHROOM_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(7).saturationMod(0.8F).build();
    public static final FoodProperties MUSHROOM_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties COD_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties PORK_POTATO_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(10).saturationMod(0.6F).build();
    public static final FoodProperties SALMON_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).build();
    public static final FoodProperties EGGPLANT_EGG_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
    public static final FoodProperties CHINESE_CABBAGE = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties CHINESE_CABBAGE_LEAF = new FoodProperties.Builder().nutrition(1).saturationMod(0.4F).build();
    public static final FoodProperties GARLIC = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties GREEN_ONION = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties EGGPLANT = new FoodProperties.Builder().nutrition(2).saturationMod(0.2F).build();
    public static final FoodProperties GARLIC_CHIVE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties FENNEL = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties GARLIC_CHIVE_EGG_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(5).saturationMod(0.7F).build();
    public static final FoodProperties PORK_FENNEL_BOILED_DUMPLING = new FoodProperties.Builder().nutrition(6).saturationMod(0.7F).build();
}