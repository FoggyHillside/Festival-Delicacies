package cn.foggyhillside.festival_delicacies.blocks.entities.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class DryingRackItemHandler implements IItemHandlerModifiable {
    private final IItemHandlerModifiable itemHandler;
    private Direction direction;

    public DryingRackItemHandler(IItemHandlerModifiable itemHandler, Direction direction) {
        this.itemHandler = itemHandler;
        this.direction = direction;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return itemHandler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return direction.equals(Direction.DOWN) ? stack : itemHandler.insertItem(slot, stack, simulate);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }
}
