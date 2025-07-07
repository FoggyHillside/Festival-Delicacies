package cn.foggyhillside.festival_delicacies.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class Utils {
    //JEI
    @OnlyIn(Dist.CLIENT)
    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        } else {
            RegistryAccess registryAccess = level.registryAccess();
            return recipe.getResultItem(registryAccess);
        }
    }

    //Recipe
    public static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for (int i = 0; i < ingredientArray.size(); i++) {
            Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
            if (!ingredient.isEmpty()) {
                nonnulllist.add(ingredient);
            }
        }
        return nonnulllist;
    }

    public static NonNullList<ItemStack> readResults(JsonArray resultArray) {
        NonNullList<ItemStack> results = NonNullList.create();

        for (JsonElement element : resultArray) {
            ItemStack result = CraftingHelper.getItemStack(element.getAsJsonObject(), true);
            if (!result.isEmpty()) {
                results.add(result);
            }
        }
        return results;
    }

    //BlockEntity
    public static void splitAndSpawnExperience(ServerLevel level, Vec3 pos, int craftedAmount, float experience) {
        int expTotal = Mth.floor(craftedAmount * experience);
        float expFraction = Mth.frac(craftedAmount * experience);
        if (expFraction != 0.0F && Math.random() < expFraction) {
            ++expTotal;
        }

        ExperienceOrb.award(level, pos, expTotal);
    }

    //Config
    public static boolean checkItem(ForgeConfigSpec.ConfigValue<List<? extends String>> config, ItemStack itemStack) {
        for (String str : config.get()) {
            ResourceLocation id = ResourceLocation.tryParse(str);
            if (id != null) {
                Item item = ForgeRegistries.ITEMS.getValue(id);
                return item == itemStack.getItem();
            }
        }
        return false;
    }

    public static boolean checkTag(ForgeConfigSpec.ConfigValue<List<? extends String>> config, ItemStack itemStack) {
        for (String str : config.get()) {
            ResourceLocation id = ResourceLocation.tryParse(str);
            if (id != null) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
                return itemStack.is(tag);
            }
        }
        return false;
    }

    public static boolean checkItemAndTag(TagKey<Item> itemTag, ForgeConfigSpec.ConfigValue<List<? extends String>> configItem, ForgeConfigSpec.ConfigValue<List<? extends String>> configTag, ItemStack itemStack) {
        ResourceLocation locationItem = null;
        ResourceLocation locationTag = null;
        if (itemStack.is(itemTag)) {
            return true;
        }
        for (String str : configItem.get()) {
            locationItem = ResourceLocation.tryParse(str);
        }
        for (String str : configTag.get()) {
            locationTag = ResourceLocation.tryParse(str);
        }
        if (locationItem != null) {
            Item item = ForgeRegistries.ITEMS.getValue(locationItem);
            if (item == itemStack.getItem()) {
                return true;
            } else if (locationTag != null) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, locationTag);
                return itemStack.is(tag);
            }
        }
        return false;
    }

    //Item
    public static void dropItem(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.setDeltaMovement(xMotion, yMotion, zMotion);
        level.addFreshEntity(entity);
    }
}
