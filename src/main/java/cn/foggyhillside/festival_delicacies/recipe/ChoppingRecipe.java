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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class ChoppingRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack input;
    private final NonNullList<ItemStack> results;

    public ChoppingRecipe(ResourceLocation id, ItemStack input, NonNullList<ItemStack> results) {
        this.id = id;
        this.input = input;
        this.results = results;
    }

    public ItemStack getInput() {
        return input;
    }

    public NonNullList<ItemStack> getResultsList() {
        NonNullList<ItemStack> list = NonNullList.create();
        results.forEach(itemStack -> {
            list.add(itemStack.copy());
        });
        return list;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        return pContainer.getContainerSize() == 1 && pContainer.getItem(0).is(input.getItem());
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return results.get(0);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return results.get(0);
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

    public static class Type implements RecipeType<ChoppingRecipe> {
        private Type() {
        }

        public static final ChoppingRecipe.Type INSTANCE = new ChoppingRecipe.Type();
    }

    public static class Serializer implements RecipeSerializer<ChoppingRecipe> {
        public static final ChoppingRecipe.Serializer INSTANCE = new ChoppingRecipe.Serializer();

        @Override
        public ChoppingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack input = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(pSerializedRecipe, "ingredient"), true);
            if (input.isEmpty()) {
                throw new JsonParseException("No ingredient for chopping recipe");
            } else {
                NonNullList<ItemStack> results = Utils.readResults(GsonHelper.getAsJsonArray(pSerializedRecipe, "results"));
                if (results.size() > 3) {
                    throw new JsonParseException("Too many results for chopping recipe! The maximum is 3");
                } else if (results.isEmpty()) {
                    throw new JsonParseException("There is no result for chopping recipe!");
                } else {
                    return new ChoppingRecipe(pRecipeId, input, results);
                }
            }
        }

        @Override
        public @Nullable ChoppingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            ItemStack input = pBuffer.readItem();
            int i = pBuffer.readInt();
            NonNullList<ItemStack> results = NonNullList.create();
            for (int j = 0; j < i; j++) {
                ItemStack itemStack = pBuffer.readItem();
                if (!itemStack.isEmpty()) {
                    results.add(itemStack);
                }
            }
            return new ChoppingRecipe(pRecipeId, input, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ChoppingRecipe pRecipe) {
            pBuffer.writeItem(pRecipe.input);
            int size = pRecipe.results.size();
            pBuffer.writeInt(size);
            for (ItemStack stack : pRecipe.results) {
                pBuffer.writeItem(stack);
            }
        }
    }
}