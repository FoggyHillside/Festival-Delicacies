package cn.foggyhillside.festival_delicacies.recipe;

import cn.foggyhillside.festival_delicacies.blocks.entities.FermentationJarEntity;
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

public class FermentingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;

    private final ItemStack output;

    private final ItemStack container;

    private final NonNullList<Ingredient> ingredients;

    private final float experience;

    private final int cookTime;

    public FermentingRecipe(ResourceLocation id, ItemStack output, ItemStack container, NonNullList<Ingredient> ingredients, float experience, int cookTime) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.container = container;
        this.experience = experience;
        this.cookTime = cookTime;
    }

    public ItemStack getContainer() {
        return container;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookTime() {
        return cookTime;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide) {
            return false;
        }
        ItemStack inputContainer = pContainer.getItem(FermentationJarEntity.SLOTS - 2);
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;

        for (int j = 0; j < FermentationJarEntity.SLOTS - 2; j++) {
            ItemStack itemstack = pContainer.getItem(j);
            if (!itemstack.isEmpty()) {
                i++;
                inputs.add(itemstack);
            }
        }
        return i == this.ingredients.size() && RecipeMatcher.findMatches(inputs, this.ingredients) != null && this.container.getItem() == inputContainer.getItem();
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
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

    public static class Type implements RecipeType<FermentingRecipe> {
        public static final FermentingRecipe.Type INSTANCE = new FermentingRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<FermentingRecipe> {
        public static final FermentingRecipe.Serializer INSTANCE = new FermentingRecipe.Serializer();

        @Override
        public FermentingRecipe fromJson(ResourceLocation location, JsonObject json) {
            NonNullList<Ingredient> ingredients = Utils.readIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for fermentation recipe");
            } else if (ingredients.size() > FermentationJarEntity.SLOTS - 2) {
                throw new JsonParseException("Too many ingredients for fermentation recipe! The max is " + (FermentationJarEntity.SLOTS - 2));
            } else {
                ItemStack output = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
                ItemStack container = GsonHelper.isValidNode(json, "container") ? CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "container"), true) : ItemStack.EMPTY;
                float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
                int cookTime = GsonHelper.getAsInt(json, "cookingtime", 200);
                return new FermentingRecipe(location, output, container, ingredients, experience, cookTime);
            }
        }

        @Nullable
        public FermentingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<Ingredient> inputItemsIn = NonNullList.withSize(i, Ingredient.EMPTY);

            inputItemsIn.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            ItemStack output = buffer.readItem();
            ItemStack container = buffer.readItem();
            float experience = buffer.readFloat();
            int cookTime = buffer.readVarInt();
            return new FermentingRecipe(id, container, output, inputItemsIn, experience, cookTime);
        }

        public void toNetwork(FriendlyByteBuf buffer, FermentingRecipe recipe) {
            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);
            buffer.writeItem(recipe.container);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookTime);
        }
    }
}
