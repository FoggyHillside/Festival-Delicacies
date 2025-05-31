package cn.foggyhillside.festival_delicacies.blocks.screen;

import cn.foggyhillside.festival_delicacies.blocks.container.ResultSlot;
import cn.foggyhillside.festival_delicacies.blocks.container.StoveSlot;
import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import cn.foggyhillside.festival_delicacies.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class StoveMenu extends AbstractContainerMenu {

    public final StoveEntity entity;

    private final Level level;

    private final ContainerData data;

    public StoveMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, inventory.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public StoveMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.STOVE_MENU.get(), id);
        checkContainerSize(inventory, 11);
        this.entity = (StoveEntity) entity;
        this.level = inventory.player.level;
        this.data = data;

        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 30, 17));
            this.addSlot(new SlotItemHandler(handler, 1, 48, 17));
            this.addSlot(new StoveSlot(inventory.player.level, inventory.player, this.entity, handler, 2, 66, 17));
            this.addSlot(new SlotItemHandler(handler, 3, 30, 35));
            this.addSlot(new SlotItemHandler(handler, 4, 48, 35));
            this.addSlot(new StoveSlot(inventory.player.level, inventory.player, this.entity, handler, 5, 66, 35));
            this.addSlot(new StoveSlot(inventory.player.level, inventory.player, this.entity, handler, 6, 30, 53));
            this.addSlot(new StoveSlot(inventory.player.level, inventory.player, this.entity, handler, 7, 48, 53));
            this.addSlot(new StoveSlot(inventory.player.level, inventory.player, this.entity, handler, 8, 66, 53));
            this.addSlot(new SlotItemHandler(handler, 9, 93, 17));
            this.addSlot(new ResultSlot(inventory.player, this.entity, handler, 10, 124, 36));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxprogress = this.data.get(1);
        int progressArrowSize = 24;

        return maxprogress != 0 && progress != 0 ? progress * progressArrowSize / maxprogress : 0;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int id) {
        int containerInput = 45;
        int output = 46;
        int startPlayerInv = 0;
        int endPlayerInv = 35;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(id);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (id == output) {
                if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (id <= endPlayerInv) {
                if ((itemstack1.getItem() == Items.BOWL || itemstack1.getItem() == Items.GLASS_BOTTLE) && !this.moveItemStackTo(itemstack1, containerInput, containerInput + 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, containerInput - 9, containerInput, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, ModBlocks.STOVE.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}
