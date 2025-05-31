package cn.foggyhillside.festival_delicacies.jei;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveMenu;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import cn.foggyhillside.festival_delicacies.registry.ModMenuTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@JeiPlugin
public class JEIPlugin implements IModPlugin {
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.STOVE.get()), new RecipeType[]{STOVE});
    }

    public static RecipeType<StoveRecipe> STOVE =
            new RecipeType<>(StoveRecipeCategory.UID, StoveRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(FestivalDelicacies.MOD_ID, "jei_plugin");

    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(StoveMenu.class, ModMenuTypes.STOVE_MENU.get(), STOVE, 36, 9, 0, 36);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new StoveRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<StoveRecipe> stoveRecipes = recipeManager.getAllRecipesFor(StoveRecipe.Type.INSTANCE);

        registration.addRecipes(STOVE, stoveRecipes);
    }
}

