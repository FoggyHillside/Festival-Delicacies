package cn.foggyhillside.festival_delicacies.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DumplingItemHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<ListTag>, Iterable<ItemStack> {

    protected final List<ItemStack> items;
    protected FoodProperties foodProperties;

    public DumplingItemHandler() {
        this.items = new ArrayList<>();
        this.foodProperties = new FoodProperties.Builder().build();
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void add(ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        items.add(stack);
    }

    public void setItems(List<ItemStack> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    @Override
    public int getSlots() {
        return items.size();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            throw new IllegalArgumentException();
        }
        validateSlotIndex(slot);
        items.set(slot, stack);
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return items.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public ListTag serializeNBT() {
        return serializeItems(items);
    }

    public static ListTag serializeItems(List<ItemStack> items) {
        ListTag result = new ListTag();
        for (ItemStack stack : items) {
            result.add(stack.save(new CompoundTag()));
        }
        return result;
    }

    @Override
    public void deserializeNBT(ListTag listTag) {
        items.clear();
        for (int i = 0; i < listTag.size(); i++) {
            items.add(ItemStack.of(listTag.getCompound(i)));
        }
    }

    public static List<ItemStack> deserializeNBTS(ListTag listTag) {
        List<ItemStack> stackList = new ArrayList<>();
        for (int i = 0; i < listTag.size(); i++) {
            stackList.add(ItemStack.of(listTag.getCompound(i)));
        }
        return stackList;
    }

    @Override
    public Iterator<ItemStack> iterator() {
        return items.iterator();
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= items.size()) {
            throw new IndexOutOfBoundsException("Slot " + slot + " not in valid range - [0," + items.size() + ")");
        }
    }
}
