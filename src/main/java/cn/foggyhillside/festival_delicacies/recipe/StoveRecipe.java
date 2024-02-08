package cn.foggyhillside.festival_delicacies.recipe;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import cn.foggyhillside.festival_delicacies.tag.ModTags;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StoveRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;

    private final ItemStack output;

    private final ItemStack container;

    private final NonNullList<Ingredient> ingredients;

    private final Float experience;

    private final Integer cookTime;

    private final boolean needPot;

    public StoveRecipe(ResourceLocation id, ItemStack output, ItemStack container, NonNullList<Ingredient> ingredients, Float experience, Integer cookTime, Boolean needPot) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.container = container;
        this.experience = experience;
        this.cookTime = cookTime;
        this.needPot = needPot;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getContainer() {
        return container;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Boolean getNeedPot() {
        return needPot;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(SimpleContainer p_44004_) {
        return Recipe.super.getRemainingItems(p_44004_);
    }

    @Override
    public boolean matches(SimpleContainer inventory, Level level) {
        if (level.isClientSide) {
            return false;
        }
        ItemStack inputContainer = inventory.getItem(9);
        List<ItemStack> inputs = new ArrayList();
        int i = 0;

        for (int j = 0; j < 9; j++) {
            ItemStack itemstack = inventory.getItem(j);
            if (!itemstack.isEmpty()) {
                i++;
                inputs.add(itemstack);
            }
        }
        return i == this.ingredients.size() && RecipeMatcher.findMatches(inputs, this.ingredients) != null && this.container.getItem() == inputContainer.getItem();
    }

    @Override
    public ItemStack assemble(SimpleContainer container) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<StoveRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();

        public static final String ID = "stove";
    }

    public static class Serializer implements RecipeSerializer<StoveRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        public static final ResourceLocation ID =
                new ResourceLocation(FestivalDelicacies.MOD_ID, "stove");

        @Override
        public StoveRecipe fromJson(ResourceLocation location, JsonObject json) {
            NonNullList<Ingredient> ingredients = readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for cooking recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for stove recipe! The max is 9");
            } else {
                ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
                ItemStack container = GsonHelper.isValidNode(json, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true) : ItemStack.EMPTY;
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookTime = GsonHelper.getAsInt(json, "cookingtime", 200);
                boolean needPot = GsonHelper.getAsBoolean(json, "needpot", false);
                return new StoveRecipe(location, output, container, ingredients, experience, cookTime, needPot);
            }
        }

        private static NonNullList<Ingredient> readIngredients(JsonArray ingredientArray) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for (int i = 0; i < ingredientArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(ingredientArray.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }
            return nonnulllist;
        }

        @Nullable
        public StoveRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            for (int j = 0; j < inputItemsIn.size(); ++j) {
                inputItemsIn.set(j, Ingredient.fromNetwork(buffer));
            }

            ItemStack output = buffer.readItem();
            ItemStack container = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            boolean needPot = buffer.readBoolean();
            return new StoveRecipe(id, container, output, inputItemsIn, experience, cookTime, needPot);
        }

        public void toNetwork(FriendlyByteBuf buffer, StoveRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());
            Iterator var3 = recipe.ingredients.iterator();

            while (var3.hasNext()) {
                Ingredient ingredient = (Ingredient) var3.next();
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeItem(recipe.container);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
            buffer.writeBoolean(recipe.needPot);
        }
    }

}
