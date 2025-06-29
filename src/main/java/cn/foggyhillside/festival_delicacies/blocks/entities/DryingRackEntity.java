package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.DryingRackItemHandler;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DryingRackEntity extends SyncedBlockEntity {
    private final int TOTAL_PROGRESS = 600;
    private final int SLOTS = 12;

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> itemHandler;

    private final int[] progress = new int[SLOTS];

    public DryingRackEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DRYING_RACK.get(), pPos, pBlockState);
        this.inventory = this.createHandler();
        this.itemHandler = LazyOptional.of(() -> new DryingRackItemHandler(inventory));
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(SLOTS) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Inventory")) {
            this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        } else {
            this.inventory.deserializeNBT(pTag);
        }
        if (pTag.contains("progress", 11)) {
            int[] arrayProgress = pTag.getIntArray("progress");
            System.arraycopy(arrayProgress, 0, this.progress, 0, Math.min(this.progress.length, arrayProgress.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.putIntArray("progress", progress);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap.equals(ForgeCapabilities.ITEM_HANDLER) ? this.itemHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.itemHandler.invalidate();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackEntity stove) {
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public SimpleContainer getInventoryContainer() {
        SimpleContainer container;
        int slot = 0;
        container = new SimpleContainer(this.inventory.getSlots());
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                container.setItem(slot, this.inventory.getStackInSlot(i));
                slot++;
            }
        }

        return container;
    }

    public NonNullList<ItemStack> getInventoryList() {
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }

        NonNullList<ItemStack> nonNullList = NonNullList.withSize(list.size(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); i++) {
            nonNullList.set(i, list.get(0).copy());
        }

        return nonNullList;
    }

    public int getNextEmptySlot() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    public void addItem(ItemStack itemStack, Player player, int slot) {

    }

    public boolean isEmpty() {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

}
