package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final TagKey<Item> DOUGH = ItemTags.create(new ResourceLocation(FestivalDelicacies.MODID, "dough"));
    public static final TagKey<Item> DUMPLING = ItemTags.create(new ResourceLocation(FestivalDelicacies.MODID, "dumpling"));
    public static final TagKey<Item> MOONCAKE = ItemTags.create(new ResourceLocation(FestivalDelicacies.MODID, "mooncake"));
    public static final TagKey<Item> KNIFE = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "knife")));
    public static final TagKey<Item> MOD_SEEDS = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "mod_seeds")));
    public static final TagKey<Item> BANNED_DUMPLING_INGREDIENTS_CRAFTING = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "banned_dumpling_ingredients_crafting")));
    public static final TagKey<Item> BANNED_DUMPLING_INGREDIENTS_CONSUMPTION = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "banned_dumpling_ingredients_consumption")));
    public static final TagKey<Item> ALLOWED_INEDIBLE_CRAFTING = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "allowed_inedible_crafting")));
    public static final TagKey<Item> ALLOWED_INEDIBLE_CONSUMPTION = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "allowed_inedible_consumption")));
    public static final TagKey<Item> DRYING_RACK_ALLOWED = ItemTags.create((new ResourceLocation(FestivalDelicacies.MODID, "drying_rack_allowed")));
}
