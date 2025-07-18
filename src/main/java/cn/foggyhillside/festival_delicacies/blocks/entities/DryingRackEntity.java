package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.DryingRackBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.DryingRackItemHandler;
import cn.foggyhillside.festival_delicacies.recipe.DryingRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
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
import java.util.Optional;

public class DryingRackEntity extends SyncedBlockEntity {
    private final int MAX_PROGRESS = 600;
    private final int SLOTS = 12;

    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> inputItemHandler;
    private final LazyOptional<IItemHandler> outputItemHandler;

    private final int[] progressArray = new int[SLOTS];

    public DryingRackEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DRYING_RACK.get(), pPos, pBlockState);
        this.inventory = this.createHandler();
        this.inputItemHandler = LazyOptional.of(() -> new DryingRackItemHandler(inventory, Direction.UP));
        this.outputItemHandler = LazyOptional.of(() -> new DryingRackItemHandler(inventory, Direction.DOWN));
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
        }
        if (pTag.contains("Progress", 11)) {
            int[] arrayProgress = pTag.getIntArray("Progress");
            System.arraycopy(arrayProgress, 0, this.progressArray, 0, Math.min(this.progressArray.length, arrayProgress.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("inventory", this.inventory.serializeNBT());
        pTag.putIntArray("progress", progressArray);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return inputItemHandler.cast();
            } else if (side.equals(Direction.DOWN)) {
                return outputItemHandler.cast();
            } else {
                return inputItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.inputItemHandler.invalidate();
        this.outputItemHandler.invalidate();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackEntity rackEntity) {
        if (level.isClientSide) {
            return;
        }
        ItemStackHandler rackInventory = rackEntity.inventory;
        Direction direction = state.getValue(DryingRackBlock.FACING).getCounterClockWise();
        for (int i = 0; i < rackInventory.getSlots(); i++) {
            ItemStack stack = rackInventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                SimpleContainer container = new SimpleContainer(stack);
                Optional<DryingRecipe> recipe = level.getRecipeManager().getRecipeFor(DryingRecipe.Type.INSTANCE, container, level);

                if (recipe.isPresent()) {
                    if (rackEntity.progressArray[i] < rackEntity.MAX_PROGRESS) {
                        rackEntity.progressArray[i]++;
                        setChanged(level, pos, state);
                    } else {
                        rackInventory.setStackInSlot(i, recipe.get().getResultItem(level.registryAccess()).copy());
                        Utils.dropItem(level, rackInventory.getStackInSlot(i).split(rackInventory.getStackInSlot(i).getCount()), pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, direction.getStepX() * 0.2F, 0F, direction.getStepZ() * 0.2F);
                        rackEntity.progressArray[i] = 0;
                        setChanged(level, pos, state);
                    }
                } else {
                    Utils.dropItem(level, stack.split(stack.getCount()), pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, direction.getStepX() * 0.2F, 0F, direction.getStepZ() * 0.2F);
                    setChanged(level, pos, state);
                }
            }
        }
    }

    public int[] getProgressArray() {
        return this.progressArray;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
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
            nonNullList.set(i, list.get(i));
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

    public boolean addItem(ItemStack heldStack, Player player, int nextSlot) {
        if (!heldStack.isEmpty() && heldStack.is(ModTags.DRYING_RACK_ALLOWED)) {
            if (!player.getAbilities().instabuild) {
                this.inventory.setStackInSlot(nextSlot, heldStack.split(1));
            } else {
                this.inventory.setStackInSlot(nextSlot, heldStack.copyWithCount(1));
            }
            setChanged();
            return true;
        }
        return false;
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
