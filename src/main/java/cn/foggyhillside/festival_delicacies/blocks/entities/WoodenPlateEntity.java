package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.WoodenPlateBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.WoodenPlateItemHandler;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WoodenPlateEntity extends SyncedBlockEntity {
    private final Map<Item, Integer> ITEM_MAP = Map.of(
            ModItems.BOILED_DUMPLING.get(), 1,
            ModItems.STEAMED_DUMPLING.get(), 2,
            ModItems.BAOZI.get(), 3,
            ModItems.QINGTUAN.get(), 4,
            ModItems.FIVE_NUTS_MOONCAKE.get(), 5,
            ModItems.HAWTHORN_WITH_JUJUBE_PASTE_MOONCAKE.get(), 6,
            ModItems.LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE.get(), 7,
            ModItems.PRESERVED_MEAT_MOONCAKE.get(), 8);
    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;

    public WoodenPlateEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.WOODEN_PLATE.get(), pPos, pBlockState);
        this.inventory = this.createHandler();
        this.inputHandler = LazyOptional.of(() -> new WoodenPlateItemHandler(inventory));
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(6) {
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

    public int getMaxAmount() {
        if (inventory.getStackInSlot(0).is(ModTags.DUMPLING)) {
            return 6;
        }
        return 5;
    }

    public int getItemAmount() {
        int amount = 0;
        for (int i = 0; i < getMaxAmount(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                amount++;
            }
        }
        return amount;
    }

    public NonNullList<ItemStack> getInventoryList() {
        int maxAmount = getMaxAmount();
        NonNullList<ItemStack> list = NonNullList.withSize(maxAmount, ItemStack.EMPTY);
        for (int i = 0; i < maxAmount; i++) {
            list.set(i, this.inventory.getStackInSlot(i));
        }
        return list;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return cap.equals(ForgeCapabilities.ITEM_HANDLER) ? this.inputHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.inputHandler.invalidate();
    }

    private BlockState updateItemTypeState(BlockState pState, ItemStack stack) {
        int i = ITEM_MAP.get(stack.getItem()) == null ? 0 : ITEM_MAP.get(stack.getItem());
        if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(i)) {
            return pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, i);
        }

        return pState;
    }

    public BlockState getLastPlateState(BlockState pState) {
        BlockState newState = updateItemTypeState(pState, inventory.getStackInSlot(0));
        int amount = getItemAmount();
        if (!newState.getValue(WoodenPlateBlock.AMOUNT).equals(amount)) {
            return newState.setValue(WoodenPlateBlock.AMOUNT, amount);
        }

        return newState;
    }

    public boolean isEmpty() {
        for (int i = 0; i < 6; i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < 6; i++) {
            if (this.inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean addItem(ItemStack itemStack, Player player) {
        if (!this.isFull() && !itemStack.isEmpty() && (this.inventory.getStackInSlot(0).isEmpty() || itemStack.is(this.inventory.getStackInSlot(0).getItem()))) {
            for (int i = 0; i < this.getMaxAmount(); i++) {
                if (this.inventory.getStackInSlot(i).isEmpty()) {
                    if (!player.getAbilities().instabuild) {
                        this.inventory.setStackInSlot(i, itemStack.split(1));
                    } else {
                        this.inventory.setStackInSlot(i, itemStack.copyWithCount(1));
                    }
                    return true;
                }
            }

        }

        return false;
    }

    public boolean extractItem(Player player, InteractionHand hand) {
        if (!this.isEmpty()) {
            for (int i = getMaxAmount() - 1; i >= 0; i--) {
                if (!this.getInventory().getStackInSlot(i).isEmpty()) {
                    player.setItemInHand(hand, this.getInventory().getStackInSlot(i).split(1));
                    return true;
                }
            }
        }
        return false;
    }

}
