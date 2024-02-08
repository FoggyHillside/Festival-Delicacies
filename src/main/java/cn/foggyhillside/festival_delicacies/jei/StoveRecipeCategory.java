package cn.foggyhillside.festival_delicacies.jei;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoveRecipeCategory implements IRecipeCategory<StoveRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(FestivalDelicacies.MOD_ID,
            "stove");

    public static final ResourceLocation TEXTURE = new ResourceLocation(FestivalDelicacies.MOD_ID,
            "textures/gui/pot_stove_gui_jei.png");


    private final IDrawable background;

    private final IDrawable icon;

    private final IDrawable lit;

    private final IDrawable arrow;

    private final IDrawable containerSlot;

    private final IDrawable pot;

    public StoveRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STOVE.get()));
        this.lit = helper.createDrawable(TEXTURE, 176, 17, 14, 18);
        this.arrow = helper.drawableBuilder(TEXTURE, 176, 0, 24, 17).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.containerSlot = helper.createDrawable(TEXTURE, 30, 17, 17, 17);
        this.pot = helper.createDrawable(TEXTURE, 176, 35, 15, 15);
    }

    @Override
    public RecipeType<StoveRecipe> getRecipeType() {
        return JEIPlugin.STOVE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.festival_delicacies.stove");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void draw(StoveRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        this.arrow.draw(matrixStack, 89, 35);
        if (recipe.getContainer() != ItemStack.EMPTY) {
            this.containerSlot.draw(matrixStack, 93, 17);
        }
        if (recipe.getNeedPot()) {
            this.pot.draw(matrixStack, 94, 54);
        } else {
            this.lit.draw(matrixStack, 94, 52);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StoveRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 30, 17).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(0)).getItems()));
        if (recipe.getIngredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 17).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(1)).getItems()));
        }
        if (recipe.getIngredients().size() > 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 66, 17).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(2)).getItems()));
        }
        if (recipe.getIngredients().size() > 3) {
            builder.addSlot(RecipeIngredientRole.INPUT, 30, 35).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(3)).getItems()));
        }
        if (recipe.getIngredients().size() > 4) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 35).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(4)).getItems()));
        }
        if (recipe.getIngredients().size() > 5) {
            builder.addSlot(RecipeIngredientRole.INPUT, 66, 35).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(5)).getItems()));
        }
        if (recipe.getIngredients().size() > 6) {
            builder.addSlot(RecipeIngredientRole.INPUT, 30, 53).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(6)).getItems()));
        }
        if (recipe.getIngredients().size() > 7) {
            builder.addSlot(RecipeIngredientRole.INPUT, 48, 53).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(7)).getItems()));
        }
        if (recipe.getIngredients().size() > 8) {
            builder.addSlot(RecipeIngredientRole.INPUT, 66, 53).addItemStacks(Arrays.asList(((Ingredient) recipe.getIngredients().get(8)).getItems()));
        }
        if (recipe.getContainer() != ItemStack.EMPTY) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 93, 17).addItemStack(recipe.getContainer());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 36).addItemStack(recipe.getResultItem());
    }
}
