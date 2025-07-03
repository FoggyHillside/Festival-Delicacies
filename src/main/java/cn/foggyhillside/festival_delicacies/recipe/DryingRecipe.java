package cn.foggyhillside.festival_delicacies.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class DryingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack input;
    private final ItemStack result;

    public DryingRecipe(ResourceLocation id, ItemStack input, ItemStack result) {
        this.id = id;
        this.input = input;
        this.result = result;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return pContainer.getContainerSize() == 1 && pContainer.getItem(0).is(input.getItem());
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return result;
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

    public static class Type implements RecipeType<DryingRecipe> {
        private Type() {
        }

        public static final DryingRecipe.Type INSTANCE = new DryingRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<DryingRecipe> {
        public static final DryingRecipe.Serializer INSTANCE = new DryingRecipe.Serializer();

        @Override
        public DryingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack input = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient"), true);
            if (input.isEmpty()) {
                throw new JsonParseException("No ingredient for drying recipe");
            } else {
                ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"), true);
                if (result.isEmpty()) {
                    throw new JsonParseException("There is no result for drying recipe!");
                } else {
                    return new DryingRecipe(pRecipeId, input, result);
                }
            }
        }

        @Override
        public @Nullable DryingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack input = pBuffer.readItem();
            ItemStack result = pBuffer.readItem();
            return new DryingRecipe(pRecipeId, input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, DryingRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.input);
            pBuffer.writeItem(pRecipe.result);
        }
    }
}
