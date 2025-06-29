package cn.foggyhillside.festival_delicacies.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.IntValue DUMPLING_COOKING_TIME;

    public static ForgeConfigSpec.BooleanValue NESTING_DUMPLING;

    public static ForgeConfigSpec.BooleanValue NO_FURTHER_NESTING;

    public static ForgeConfigSpec.BooleanValue ALLOW_INEDIBLE_INGREDIENTS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_DUMPLING_INGREDIENTS_CRAFTING;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_INGREDIENT_TAGS_CRAFTING;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_DUMPLING_INGREDIENTS_CONSUMPTION;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> BANNED_INGREDIENT_TAGS_CONSUMPTION;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_INEDIBLE_CRAFTING;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_INEDIBLE_CRAFTING_TAGS;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_INEDIBLE_CONSUMPTION;

    public static ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_INEDIBLE_CONSUMPTION_TAGS;

    static {
        BUILDER.push("Configs for Festival Delicacies");

        DUMPLING_COOKING_TIME = BUILDER.comment("Default: 200").defineInRange("dumpling_cooking_time", 200, 10, 1000);
        NESTING_DUMPLING = BUILDER.comment("Allow nesting of dumplings.\nDefault: false").define("nesting_dumpling", false);
        NO_FURTHER_NESTING = BUILDER.comment("Prevent further nesting when cooking dumplings.\nDefault: true").define("no_further_nesting", true);
        BANNED_DUMPLING_INGREDIENTS_CRAFTING = BUILDER.comment("These items are not allowed as ingredients for dumplings.").defineList("banned_dumpling_ingredients_crafting", ImmutableList.of(), (obj) -> true);
        BANNED_INGREDIENT_TAGS_CRAFTING = BUILDER.comment("Items with these tags are not allowed as ingredients for dumplings.").defineList("banned_ingredient_tags_crafting", ImmutableList.of(), (obj) -> true);
        BANNED_DUMPLING_INGREDIENTS_CONSUMPTION = BUILDER.comment("If these ingredients have already been added to the dumplings, they will be skipped when the dumplings are consumed by players.").defineList("banned_dumpling_ingredients_consumption", ImmutableList.of(), (obj) -> true);
        BANNED_INGREDIENT_TAGS_CONSUMPTION = BUILDER.comment("If ingredients with these tags have already been added to the dumplings, they will be skipped when the dumplings are consumed by players.").defineList("banned_ingredient_tags_consumption", ImmutableList.of(), (obj) -> true);
        ALLOW_INEDIBLE_INGREDIENTS = BUILDER.comment("Allow inedible items as ingredients for dumplings.\nDefault: false").define("allow_inedible_ingredients", false);
        ALLOWED_INEDIBLE_CRAFTING = BUILDER.comment("These inedible items are allowed as ingredients for dumplings.\n\"allow_inedible_ingredients = true\" is required.").defineList("allowed_inedible_crafting", ImmutableList.of(), (obj) -> true);
        ALLOWED_INEDIBLE_CRAFTING_TAGS = BUILDER.comment("Inedible items with these tags are allowed as ingredients for dumplings.\n\"allow_inedible_ingredients = true\" is required.").defineList("allowed_inedible_crafting_tags", ImmutableList.of(), (obj) -> true);
        ALLOWED_INEDIBLE_CONSUMPTION = BUILDER.comment("If these inedible ingredients have already been added to the dumplings, they will not be skipped when the dumplings are consumed by players.").defineList("allowed_inedible_consumption", ImmutableList.of(), (obj) -> true);
        ALLOWED_INEDIBLE_CONSUMPTION_TAGS = BUILDER.comment("If inedible ingredients with these tags have already been added to the dumplings, they will not be skipped when the dumplings are consumed by players.").defineList("allowed_inedible_consumption_tags", ImmutableList.of(), (obj) -> true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
