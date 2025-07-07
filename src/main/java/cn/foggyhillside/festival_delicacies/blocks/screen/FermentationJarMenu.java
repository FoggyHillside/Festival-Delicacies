package cn.foggyhillside.festival_delicacies.blocks.screen;

import cn.foggyhillside.festival_delicacies.blocks.entities.FermentationJarEntity;
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

import java.util.Objects;

public class FermentationJarMenu extends AbstractContainerMenu {
    public final FermentationJarEntity entity;

    private final Level level;

    private final ContainerData data;

    public FermentationJarMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, getBlockEntity(inventory, extraData), new SimpleContainerData(2));
    }

    public FermentationJarMenu(int id, Inventory inventory, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.FERMENTATION_JAR_MENU.get(), id);
        checkContainerSize(inventory, FermentationJarEntity.SLOTS);
        this.entity = (FermentationJarEntity) entity;
        this.level = inventory.player.level();
        this.data = data;

        addPlayerHotbar(inventory);
        addPlayerInventory(inventory);

        this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 48, 22));
            this.addSlot(new SlotItemHandler(handler, 1, 66, 22));
            this.addSlot(new SlotItemHandler(handler, 2, 48, 40));
            this.addSlot(new SlotItemHandler(handler, 3, 66, 40));
            this.addSlot(new SlotItemHandler(handler, 4, 48, 58));
            this.addSlot(new SlotItemHandler(handler, 5, 66, 58));
            this.addSlot(new SlotItemHandler(handler, 6, 112, 49));
            this.addSlot(new SlotItemHandler(handler, 7, 93, 19));
        });

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        int containerInput = 42;
        int output = 43;
        int startPlayerInv = 0;
        int endPlayerInv = 35;
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex == output) {
                if (!this.moveItemStackTo(itemstack1, startPlayerInv, endPlayerInv + 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex <= endPlayerInv) {
                if ((itemstack1.is(Items.BOWL) || itemstack1.is(Items.GLASS_BOTTLE)) && !this.moveItemStackTo(itemstack1, containerInput, containerInput + 1, false)) {
                    return ItemStack.EMPTY;
                }
                if (!this.moveItemStackTo(itemstack1, containerInput - (FermentationJarEntity.SLOTS - 2), containerInput, false)) {
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

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), pPlayer, ModBlocks.FERMENTATION_JAR.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 91 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 149));
        }
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxprogress = this.data.get(1);
        int progressArrowSize = 36;

        return maxprogress != 0 && progress != 0 ? progress * progressArrowSize / maxprogress : 0;
    }

    private static FermentationJarEntity getBlockEntity(Inventory inventory, FriendlyByteBuf extraData) {
        Objects.requireNonNull(inventory, "inventory cannot be null");
        Objects.requireNonNull(extraData, "extraData cannot be null");
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(extraData.readBlockPos());
        if (blockEntity instanceof FermentationJarEntity) {
            return (FermentationJarEntity) blockEntity;
        } else {
            throw new IllegalStateException("Block entity is not correct! " + blockEntity);
        }
    }
}
