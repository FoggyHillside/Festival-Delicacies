package cn.foggyhillside.festival_delicacies.jei;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import cn.foggyhillside.festival_delicacies.util.Utils;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StoveRecipeCategory implements IRecipeCategory<StoveRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(FestivalDelicacies.MODID,
            "stove");

    public static final ResourceLocation TEXTURE = new ResourceLocation(FestivalDelicacies.MODID,
            "textures/gui/stove_gui_jei.png");

    private final IDrawable background;

    private final IDrawable icon;

    private final IDrawable type_default;

    private final IDrawable type_steam;

    private final IDrawable type_fry;

    private final IDrawable type_bake;

    private final IDrawable arrow;

    private final IDrawable containerSlot;

    public StoveRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 29, 16, 114, 54);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.STOVE.get()));
        this.type_default = helper.createDrawable(TEXTURE, 176, 17, 19, 17);
        this.type_steam = helper.createDrawable(TEXTURE, 176, 34, 19, 17);
        this.type_fry = helper.createDrawable(TEXTURE, 176, 51, 19, 17);
        this.type_bake = helper.createDrawable(TEXTURE, 176, 68, 19, 17);
        this.arrow = helper.drawableBuilder(TEXTURE, 176, 0, 24, 17).buildAnimated(100, IDrawableAnimated.StartDirection.LEFT, false);
        this.containerSlot = helper.createDrawable(TEXTURE, 199, 0, 18, 18);
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
    public void draw(StoveRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, 60, 19);
        if (recipe.getContainer() == ItemStack.EMPTY) {
            this.containerSlot.draw(guiGraphics, 62, 0);
        }
        if (recipe.getCookingType() == 1) {
            this.type_default.draw(guiGraphics, 62, 36);
        } else if (recipe.getCookingType() == 2) {
            this.type_steam.draw(guiGraphics, 62, 36);
        } else if (recipe.getCookingType() == 3) {
            this.type_fry.draw(guiGraphics, 62, 36);
        } else if (recipe.getCookingType() == 4) {
            this.type_bake.draw(guiGraphics, 62, 36);
        }
    }

    @Override
    public List<Component> getTooltipStrings(StoveRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (62 <= mouseX && mouseX < 81 && 36 <= mouseY && mouseY < 53) {
            List<Component> tooltip = new ArrayList<>();
            int type = recipe.getCookingType();
            if (type == 1) {
                tooltip.add(Component.translatable("gui.festival_delicacies.stove.button.default"));
            } else if (type == 2) {
                tooltip.add(Component.translatable("gui.festival_delicacies.stove.button.steam"));
            } else if (type == 3) {
                tooltip.add(Component.translatable("gui.festival_delicacies.stove.button.fry"));
            } else if (type == 4) {
                tooltip.add(Component.translatable("gui.festival_delicacies.stove.button.bake"));
            }
            return tooltip;
        }
        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, StoveRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(Arrays.asList((recipe.getIngredients().get(0)).getItems()));
        if (recipe.getIngredients().size() > 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStacks(Arrays.asList((recipe.getIngredients().get(1)).getItems()));
        }
        if (recipe.getIngredients().size() > 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 37, 1).addItemStacks(Arrays.asList((recipe.getIngredients().get(2)).getItems()));
        }
        if (recipe.getIngredients().size() > 3) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 19).addItemStacks(Arrays.asList((recipe.getIngredients().get(3)).getItems()));
        }
        if (recipe.getIngredients().size() > 4) {
            builder.addSlot(RecipeIngredientRole.INPUT, 19, 19).addItemStacks(Arrays.asList((recipe.getIngredients().get(4)).getItems()));
        }
        if (recipe.getIngredients().size() > 5) {
            builder.addSlot(RecipeIngredientRole.INPUT, 37, 19).addItemStacks(Arrays.asList((recipe.getIngredients().get(5)).getItems()));
        }
        if (recipe.getIngredients().size() > 6) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addItemStacks(Arrays.asList((recipe.getIngredients().get(6)).getItems()));
        }
        if (recipe.getIngredients().size() > 7) {
            builder.addSlot(RecipeIngredientRole.INPUT, 19, 37).addItemStacks(Arrays.asList((recipe.getIngredients().get(7)).getItems()));
        }
        if (recipe.getIngredients().size() > 8) {
            builder.addSlot(RecipeIngredientRole.INPUT, 37, 37).addItemStacks(Arrays.asList((recipe.getIngredients().get(8)).getItems()));
        }
        if (recipe.getContainer() != ItemStack.EMPTY) {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 64, 1).addItemStack(recipe.getContainer());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 20).addItemStack(Utils.getResultItem(recipe));
    }
}
