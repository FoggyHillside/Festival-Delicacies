package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FestivalDelicacies.MOD_ID);

    public static final RegistryObject<RecipeSerializer<StoveRecipe>> STOVE_SERIALIZER =
            RECIPE_SERIALIZERS.register("stove", () -> StoveRecipe.Serializer.INSTANCE);
}
