package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.StoveBlock;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveMenu;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.tag.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class StoveEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;

    private int progress = 0;

    private int maxprogress;

    private LazyOptional<IItemHandler> handlerLazyOptional = LazyOptional.empty();

    public final ItemStackHandler itemStackHandler = new ItemStackHandler(11) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return handlerLazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.festival_delicacies.stove");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        handlerLazyOptional = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handlerLazyOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("stove.progress", this.progress);
        nbt.putInt("stove_maxprogress", this.maxprogress);
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("stove.progress");
        this.maxprogress = nbt.getInt("stove_maxprogress");
        super.load(nbt);
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StoveEntity entity) {
        if (level.isClientSide) {
            return;
        }
        boolean isPot = level.getBlockState(entity.getBlockPos().above()).is(ModTags.IS_POT);
        if (!isPot) {
            if (entity.itemStackHandler.getStackInSlot(2) != ItemStack.EMPTY) {
                spawnItem(entity, level, 2, entity.itemStackHandler.getStackInSlot(2));
                entity.itemStackHandler.setStackInSlot(2, ItemStack.EMPTY);
            }
            if (entity.itemStackHandler.getStackInSlot(5) != ItemStack.EMPTY) {
                spawnItem(entity, level, 5, entity.itemStackHandler.getStackInSlot(5));
                entity.itemStackHandler.setStackInSlot(5, ItemStack.EMPTY);
            }
            if (entity.itemStackHandler.getStackInSlot(6) != ItemStack.EMPTY) {
                spawnItem(entity, level, 6, entity.itemStackHandler.getStackInSlot(6));
                entity.itemStackHandler.setStackInSlot(6, ItemStack.EMPTY);
            }
            if (entity.itemStackHandler.getStackInSlot(7) != ItemStack.EMPTY) {
                spawnItem(entity, level, 7, entity.itemStackHandler.getStackInSlot(7));
                entity.itemStackHandler.setStackInSlot(7, ItemStack.EMPTY);
            }
            if (entity.itemStackHandler.getStackInSlot(8) != ItemStack.EMPTY) {
                spawnItem(entity, level, 8, entity.itemStackHandler.getStackInSlot(8));
                entity.itemStackHandler.setStackInSlot(8, ItemStack.EMPTY);
            }
        }

        Optional<StoveRecipe> recipe = level.getRecipeManager().getRecipeFor(StoveRecipe.Type.INSTANCE, getInventory(entity), level);
        if (hasRecipe(entity, recipe) && canCook(isPot, entity, state, recipe)) {
            entity.progress++;
            entity.maxprogress = recipe.get().getCookTime();
            setChanged(level, pos, state);
            if (entity.progress >= recipe.get().getCookTime()) {
                craftItem(level, entity, recipe);
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private static boolean canCook(Boolean isPot, StoveEntity entity, BlockState state, Optional<StoveRecipe> recipe) {
        boolean isLit = (Boolean) state.getValue(StoveBlock.LIT);
        if (recipe.get().getNeedPot()) {
            return isPot && isLit && canOutput(entity, recipe);
        }
        return isLit && canOutput(entity, recipe);
    }

    private static boolean canOutput(StoveEntity entity, Optional<StoveRecipe> recipe) {
        var registryAccess = entity.getLevel().registryAccess();
        return entity.itemStackHandler.getStackInSlot(10) == ItemStack.EMPTY
                || (entity.itemStackHandler.getStackInSlot(10).getItem() == recipe.get().getResultItem(registryAccess).getItem()
                && entity.itemStackHandler.getStackInSlot(10).getCount() < recipe.get().getResultItem(registryAccess).getMaxStackSize());
    }

    private static boolean hasRecipe(StoveEntity entity, Optional<StoveRecipe> recipe) {
        return recipe.isPresent();
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static SimpleContainer getInventory(StoveEntity entity) {
        int h = 0;

        SimpleContainer inventory = new SimpleContainer(entity.itemStackHandler.getSlots());
        for (int i = 0; i < entity.itemStackHandler.getSlots(); i++) {
            if (i < 9 && entity.itemStackHandler.getStackInSlot(i) != ItemStack.EMPTY) {
                inventory.setItem(h, entity.itemStackHandler.getStackInSlot(i));
                h++;
            } else if (i < 9) {
                inventory.setItem(8 - (i - h), entity.itemStackHandler.getStackInSlot(i));
            }
            if (i == 9 || i == 10) {
                inventory.setItem(i, entity.itemStackHandler.getStackInSlot(i));
            }
        }
        return inventory;
    }

    private static void craftItem(Level level, StoveEntity entity, Optional<StoveRecipe> recipe) {
        if (hasRecipe(entity, recipe)) {
            for (int i = 0; i < 10; i++) {
                if (entity.itemStackHandler.getStackInSlot(i) != ItemStack.EMPTY) {
                    if (entity.itemStackHandler.getStackInSlot(i).hasCraftingRemainingItem()) {
                        spawnItem(entity, level, i, entity.itemStackHandler.getStackInSlot(i).getCraftingRemainingItem());
                    }
                    entity.itemStackHandler.extractItem(i, 1, false);
                }
            }
            entity.itemStackHandler.setStackInSlot(10, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    entity.itemStackHandler.getStackInSlot(10).getCount()
                            + recipe.get().getResultItem(level.registryAccess()).getCount()));
            entity.resetProgress();
        }
    }

    private static void spawnItem(StoveEntity entity, Level level, int i, ItemStack itemStack) {
        Direction direction = ((Direction) entity.getBlockState().getValue(StoveBlock.FACING)).getCounterClockWise();
        double x = (double) entity.worldPosition.getX() + 0.5 + (double) direction.getStepX() * 0.25;
        double y = (double) entity.worldPosition.getY() + 0.7;
        double z = (double) entity.worldPosition.getZ() + 0.5 + (double) direction.getStepZ() * 0.25;
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
        itemEntity.setDeltaMovement((double) ((float) direction.getStepX() * 0.08F), 0.75, (double) ((float) direction.getStepZ() * 0.08F));
        level.addFreshEntity(itemEntity);
    }

    public StoveEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STOVE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> StoveEntity.this.progress;
                    case 1 -> StoveEntity.this.maxprogress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> StoveEntity.this.progress = value;
                    case 1 -> StoveEntity.this.maxprogress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new StoveMenu(id, inventory, this, this.data);
    }
}
