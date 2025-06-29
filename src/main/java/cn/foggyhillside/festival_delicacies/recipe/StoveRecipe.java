package cn.foggyhillside.festival_delicacies.recipe;

import cn.foggyhillside.festival_delicacies.util.Utils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class StoveRecipe implements Recipe<SimpleContainer> {

    private final ResourceLocation id;

    private final ItemStack output;

    private final ItemStack container;

    private final NonNullList<Ingredient> ingredients;

    private final float experience;

    private final int cookTime;

    private final int cookingType;

    public StoveRecipe(ResourceLocation id, ItemStack output, ItemStack container, NonNullList<Ingredient> ingredients, float experience, int cookTime, int cookingType) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.container = container;
        this.experience = experience;
        this.cookTime = cookTime;
        this.cookingType = cookingType;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ItemStack getContainer() {
        return container;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCookingType() {
        return cookingType;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(SimpleContainer container) {
        return Recipe.super.getRemainingItems(container);
    }

    @Override
    public boolean matches(SimpleContainer inventory, Level level) {
        if (level.isClientSide) {
            return false;
        }
        ItemStack inputContainer = inventory.getItem(9);
        List<ItemStack> inputs = new ArrayList<>();
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
    public ItemStack assemble(SimpleContainer container, RegistryAccess access) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
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
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<StoveRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        private int getCookingTypeInt(String type) {
            return switch (type) {
                case "default" -> 1;
                case "steam" -> 2;
                case "fry" -> 3;
                case "bake" -> 4;
                default -> 0;
            };
        }

        @Override
        public StoveRecipe fromJson(ResourceLocation location, JsonObject json) {
            String cookingType = GsonHelper.getAsString(json, "cookingType", "default");
            int cookingTypeInt = getCookingTypeInt(cookingType);
            NonNullList<Ingredient> ingredients = Utils.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (cookingTypeInt < 1 || cookingTypeInt > 4) {
                throw new JsonParseException("Invalid cooking type");
            } else if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for cooking recipe");
            } else if (ingredients.size() > 9) {
                throw new JsonParseException("Too many ingredients for stove recipe! The max is 9");
            } else {
                ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
                ItemStack container = GsonHelper.isValidNode(json, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true) : ItemStack.EMPTY;
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookTime = GsonHelper.getAsInt(json, "cookingtime", 200);
                return new StoveRecipe(location, output, container, ingredients, experience, cookTime, cookingTypeInt);
            }
        }

        @Nullable
        public StoveRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            inputItemsIn.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack output = buffer.readItem();
            ItemStack container = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            int cookingType = buffer.readVarInt();
            return new StoveRecipe(id, container, output, inputItemsIn, experience, cookTime, cookingType);
        }

        public void toNetwork(FriendlyByteBuf buffer, StoveRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeItem(recipe.container);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
            buffer.writeVarInt(recipe.cookingType);
        }
    }

}
