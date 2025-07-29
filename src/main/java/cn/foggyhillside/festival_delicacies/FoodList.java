package cn.foggyhillside.festival_delicacies;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FoodList {
    public static final FoodProperties SWEET_ZONGZI = new FoodProperties.Builder().nutrition(7).saturationMod(0.6F).build();
    public static final FoodProperties MEAT_ZONGZI = new FoodProperties.Builder().nutrition(9).saturationMod(0.6F).build();
    public static final FoodProperties QINGTUAN = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).build();
    public static final FoodProperties JUJUBE = new FoodProperties.Builder().nutrition(2).saturationMod(0.7F).build();
    public static final FoodProperties PRESERVED_MEAT = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties RED_BEAN_PASTE = new FoodProperties.Builder().nutrition(4).saturationMod(0.7F).build();
    public static final FoodProperties DUMPLING = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build();
    public static final FoodProperties CHINESE_CABBAGE = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties CHINESE_CABBAGE_LEAF = new FoodProperties.Builder().nutrition(1).saturationMod(0.4F).build();
    public static final FoodProperties GARLIC = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties GREEN_ONION = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties EGGPLANT = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties EGGPLANT_SLICE = new FoodProperties.Builder().nutrition(1).saturationMod(0.4F).build();
    public static final FoodProperties GARLIC_CHIVE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties FENNEL = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties LOTUS_ROOT = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties LOTUS_SEEDS = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();

    //Mooncake Food Properties | Update Needed!!!
    public static final FoodProperties FIVE_NUTS_MOONCAKE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties HAWTHORN_AND_JUJUBE_PASTE_MOONCAKE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();
    public static final FoodProperties PRESERVED_MEAT_MOONCAKE = new FoodProperties.Builder().nutrition(1).saturationMod(0.2F).build();

    public static FoodProperties getWontonFood() {
        if (ModCompat.isFarmersDelightLoaded()) {
            return new FoodProperties.Builder().nutrition(17).saturationMod(0.5F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 6000, 0), 1.0F).build();
        } else {
            return new FoodProperties.Builder().nutrition(17).saturationMod(0.5F).build();
        }
    }
}