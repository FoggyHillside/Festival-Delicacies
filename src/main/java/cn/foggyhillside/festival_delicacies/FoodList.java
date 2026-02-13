package cn.foggyhillside.festival_delicacies;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class FoodList {
    public static final FoodProperties FOOD_9_0$6 = new FoodProperties.Builder().nutrition(9).saturationMod(0.6F).build();
    public static final FoodProperties FOOD_8_0$6 = new FoodProperties.Builder().nutrition(8).saturationMod(0.6F).build();
    public static final FoodProperties FOOD_7_0$6 = new FoodProperties.Builder().nutrition(7).saturationMod(0.6F).build();
    public static final FoodProperties FOOD_6_0$5 = new FoodProperties.Builder().nutrition(6).saturationMod(0.5F).build();
    public static final FoodProperties FOOD_5_0$6 = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties FOOD_4_0$5 = new FoodProperties.Builder().nutrition(4).saturationMod(0.2F).build();
    public static final FoodProperties FOOD_3_0$5 = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).build();
    public static final FoodProperties FOOD_2_0$4 = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).build();
    public static final FoodProperties FOOD_1_0$4 = new FoodProperties.Builder().nutrition(1).saturationMod(0.4F).build();

    public static FoodProperties getWontonFood() {
        if (ModCompat.isFarmersDelightLoaded()) {
            return new FoodProperties.Builder().nutrition(17).saturationMod(0.5F).effect(() -> new MobEffectInstance(ModEffects.COMFORT.get(), 6000, 0), 1.0F).build();
        } else {
            return new FoodProperties.Builder().nutrition(17).saturationMod(0.5F).build();
        }
    }
}