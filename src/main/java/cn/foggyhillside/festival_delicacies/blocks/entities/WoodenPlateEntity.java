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

public class WoodenPlateEntity extends SyncedBlockEntity {
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
        for (int i = 0; i < 6; i++) {
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

    public void emptyInventory() {
        for (int i = 0; i < 6; i++) {
            this.inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.inventory.deserializeNBT(pTag.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.inventory.serializeNBT());
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

    public static void tick(Level level, BlockPos pos, BlockState state, WoodenPlateEntity entity) {
        updatePlateItemType(level, pos, state, entity.inventory.getStackInSlot(0));
        if (!state.getValue(WoodenPlateBlock.AMOUNT).equals(entity.getItemAmount())) {
            level.setBlockAndUpdate(pos, state.setValue(WoodenPlateBlock.AMOUNT, entity.getItemAmount()));
        }
        setChanged(level, pos, state);
    }
    
    private static void updatePlateItemType(Level pLevel, BlockPos pPos, BlockState pState, ItemStack stack) {
        if (stack.is(ModItems.BOILED_DUMPLING.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(2)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 1));
            }
        } else if (stack.is(ModItems.STEAMED_DUMPLING.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(2)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 2));
            }
        } else if (stack.is(ModItems.BAOZI.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(3)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 3));
            }
        } else if (stack.is(ModItems.QINGTUAN.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(4)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 4));
            }
        } else if (stack.is(ModItems.FIVE_NUTS_MOONCAKE.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(5)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 5));
            }
        } else if (stack.is(ModItems.HAWTHORN_AND_JUJUBE_PASTE_MOONCAKE.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(6)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 6));
            }
        } else if (stack.is(ModItems.LOTUS_SEED_PASTE_WITH_SALTED_YOLK_MOONCAKE.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(7)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 7));
            }
        } else if (stack.is(ModItems.PRESERVED_MEAT_MOONCAKE.get())) {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(8)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 8));
            }
        } else {
            if (!pState.getValue(WoodenPlateBlock.PLATE_ITEM_TYPE).equals(0)) {
                pLevel.setBlockAndUpdate(pPos, pState.setValue(WoodenPlateBlock.PLATE_ITEM_TYPE, 0));
            }
        }
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
                    if (!player.isCreative()) {
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
                if(!this.getInventory().getStackInSlot(i).isEmpty()){
                    player.setItemInHand(hand, this.getInventory().getStackInSlot(i).split(1));
                    return true;
                }
            }
        }
        return false;
    }

}
