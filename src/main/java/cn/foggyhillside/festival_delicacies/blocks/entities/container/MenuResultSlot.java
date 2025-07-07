package cn.foggyhillside.festival_delicacies.blocks.entities.container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MenuResultSlot extends SlotItemHandler {
    public final RecipeHolder recipeHolder;
    private final Player player;
    private int removeCount;

    public MenuResultSlot(Player player, RecipeHolder recipeHolder, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.recipeHolder = recipeHolder;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Override
    public @NotNull ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        super.onTake(pPlayer, pStack);
    }

    @Override
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack pStack) {
        pStack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        if (!this.player.level().isClientSide) {
            this.recipeHolder.awardUsedRecipes(this.player, NonNullList.create());
        }

        this.removeCount = 0;
    }
}
