package cn.foggyhillside.festival_delicacies.blocks.entities.inventory;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class StoveItemHandler implements IItemHandlerModifiable {
    private static final int CONTAINER_INPUT = 9;
    private static final int RESULT_OUTPUT = 10;
    private final IItemHandlerModifiable itemHandler;
    private final Direction side;

    public StoveItemHandler(IItemHandlerModifiable itemHandler, @Nullable Direction side) {
        this.itemHandler = itemHandler;
        this.side = side;
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
        if (side == null) {
            return slot <= CONTAINER_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        } else if (side.equals(Direction.UP)) {
            return slot < CONTAINER_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        } else {
            return slot == CONTAINER_INPUT ? itemHandler.insertItem(slot, stack, simulate) : stack;
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (side == null) {
            return slot <= RESULT_OUTPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else if (side.equals(Direction.UP)) {
            return slot < CONTAINER_INPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        } else {
            return slot == RESULT_OUTPUT ? itemHandler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot >= 0 && slot <= 9;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.itemHandler.setStackInSlot(slot, stack);
    }
}
