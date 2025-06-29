package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.ChoppingBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.ChoppingBlockItemHandler;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class ChoppingBlockEntity extends SyncedBlockEntity {
    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputHandler;

    public ChoppingBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CHOPPING_BLOCK.get(), pPos, pBlockState);
        this.inventory = this.createHandler();
        this.inputHandler = LazyOptional.of(() -> new ChoppingBlockItemHandler(inventory));
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(1) {
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

    public ItemStack getItemStack() {
        return inventory.getStackInSlot(0).copy();
    }

    public void itemHurt(Level pLevel, BlockPos pPos, BlockState pState) {
        if (inventory.getStackInSlot(0).hurt(1, pLevel.random, null)) {
            inventory.getStackInSlot(0).setCount(0);
            pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
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

    public static void tick(Level level, BlockPos pos, BlockState state, ChoppingBlockEntity entity) {
        if(!entity.getItemStack().isEmpty() && !state.getValue(ChoppingBlock.WITH_KNIFE)){
            level.setBlockAndUpdate(pos, state.setValue(ChoppingBlock.WITH_KNIFE, true));
            setChanged(level, pos, state);
        } else if (entity.getItemStack().isEmpty() && state.getValue(ChoppingBlock.WITH_KNIFE)){
            level.setBlockAndUpdate(pos, state.setValue(ChoppingBlock.WITH_KNIFE, false));
            setChanged(level, pos, state);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.inputHandler.invalidate();
    }

    public boolean addItem(ItemStack itemStack, Player player) {
        if (inventory.getStackInSlot(0).isEmpty() && !itemStack.isEmpty()) {
            if (!player.isCreative()) {
                this.inventory.setStackInSlot(0, itemStack.split(1));
            } else {
                this.inventory.setStackInSlot(0, itemStack.copyWithCount(1));
            }
            return true;
        }
        return false;
    }

    public boolean extractItem(Player player, InteractionHand hand) {
        if (!inventory.getStackInSlot(0).isEmpty() && player.getItemInHand(hand).isEmpty()) {
            ItemStack itemStack = inventory.getStackInSlot(0);
            player.setItemInHand(hand, itemStack.split(1));
            return true;
        }
        return false;
    }

}
