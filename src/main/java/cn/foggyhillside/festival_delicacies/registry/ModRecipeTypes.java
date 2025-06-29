package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.recipe.ChoppingRecipe;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, FestivalDelicacies.MODID);

    public static final RegistryObject<RecipeType<StoveRecipe>> STOVE_RECIPE = RECIPE_TYPES.register("stove", () -> registerRecipeType("stove"));
    public static final RegistryObject<RecipeType<ChoppingRecipe>> CHOPPING_RECIPE = RECIPE_TYPES.register("chopping", () -> registerRecipeType("chopping"));

    public static <T extends Recipe<?>> RecipeType<T> registerRecipeType(final String identifier) {
        return new RecipeType<>()
        {
            public String toString() {
                return FestivalDelicacies.MODID + ":" + identifier;
            }
        };
    }
}
