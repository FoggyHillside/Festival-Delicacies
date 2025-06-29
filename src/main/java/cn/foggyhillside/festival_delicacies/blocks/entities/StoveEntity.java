package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.StoveBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.StoveItemHandler;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveMenu;
import cn.foggyhillside.festival_delicacies.config.CommonConfigs;
import cn.foggyhillside.festival_delicacies.items.DumplingItem;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
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
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

import static cn.foggyhillside.festival_delicacies.blocks.StoveBlock.RED_STONE;
import static cn.foggyhillside.festival_delicacies.blocks.StoveBlock.START_BUTTON;

public class StoveEntity extends SyncedBlockEntity implements MenuProvider {

    public final ItemStackHandler inventory;
    private final LazyOptional<IItemHandlerModifiable> playerInputHandler;
    private final LazyOptional<IItemHandlerModifiable> inputHandler;
    private final LazyOptional<IItemHandlerModifiable> outputHandler;

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress;

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(11) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    public StoveEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.STOVE.get(), pos, state);
        this.inventory = createHandler();
        this.playerInputHandler = LazyOptional.of(() -> new StoveItemHandler(inventory, null));
        this.inputHandler = LazyOptional.of(() -> new StoveItemHandler(inventory, Direction.UP));
        this.outputHandler = LazyOptional.of(() -> new StoveItemHandler(inventory, Direction.DOWN));
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> StoveEntity.this.progress;
                    case 1 -> StoveEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> StoveEntity.this.progress = value;
                    case 1 -> StoveEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return playerInputHandler.cast();
            } else if (side.equals(Direction.UP)) {
                return inputHandler.cast();
            } else {
                return outputHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.festival_delicacies.stove");
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputHandler.invalidate();
        outputHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putInt("maxprogress", this.maxProgress);
    }

    @Override
    public void load(CompoundTag nbt) {
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("maxprogress");
        super.load(nbt);
    }

    public void drops() {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            container.setItem(i, inventory.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StoveEntity entity) {
        if (level.isClientSide) {
            return;
        }
        int type = state.getValue(StoveBlock.COOKING_TYPE);
        int start = state.getValue(START_BUTTON);
        int redstone = state.getValue(RED_STONE);
        if (type >= 1 && type <= 4) {
            if (start == 1) {
                level.setBlockAndUpdate(pos, state.setValue(START_BUTTON, 0));
            }
            Optional<StoveRecipe> recipe = level.getRecipeManager().getRecipeFor(StoveRecipe.Type.INSTANCE, getInventory(entity), level);
            int cookingType = state.getValue(StoveBlock.COOKING_TYPE);
            if (hasRecipe(recipe) && canCook(level, cookingType, entity, state, recipe)) {
                entity.progress++;
                entity.maxProgress = recipe.get().getCookTime();
                setChanged(level, pos, state);
                if (entity.progress >= recipe.get().getCookTime()) {
                    craftItem(level, entity, recipe);
                }
            } else {
                entity.resetProgress();
                setChanged(level, pos, state);
            }
        } else {
            if (!entity.inventory.getStackInSlot(9).isEmpty()
                    && entity.inventory.getStackInSlot(9).is(ModTags.DOUGH)) {
                ArrayList<ItemStack> list = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < 9; i++) {
                    ItemStack stack = entity.inventory.getStackInSlot(i).copy();
                    if ((CommonConfigs.NESTING_DUMPLING.get() || !stack.is(ModTags.DUMPLING))
                            && !(Utils.checkItemAndTag(ModTags.BANNED_DUMPLING_INGREDIENTS_CRAFTING, CommonConfigs.BANNED_DUMPLING_INGREDIENTS_CRAFTING, CommonConfigs.BANNED_INGREDIENT_TAGS_CRAFTING, stack))) {
                        stack.setCount(1);
                        if (!stack.isEmpty()) {
                            count++;
                            if (stack.getItem().isEdible() || stack.is(Items.POTION)
                                    || (CommonConfigs.ALLOW_INEDIBLE_INGREDIENTS.get()
                                    && (Utils.checkItemAndTag(ModTags.ALLOWED_INEDIBLE_CRAFTING, CommonConfigs.ALLOWED_INEDIBLE_CRAFTING, CommonConfigs.ALLOWED_INEDIBLE_CRAFTING_TAGS, stack)))) {
                                list.add(stack);
                            }
                        }
                    } else {
                        return;
                    }
                }
                ItemStack result = DumplingItem.of(entity.getBlockState().getValue(StoveBlock.COOKING_TYPE), list);
                if (!list.isEmpty() && list.size() == count
                        && canCookDumpling(level, pos, entity, state, result, redstone)) {
                    entity.progress++;
                    entity.maxProgress = CommonConfigs.DUMPLING_COOKING_TIME.get();
                    setChanged(level, pos, state);
                    if (entity.progress >= entity.maxProgress) {
                        craftDumpling(level, entity, result);
                    }
                } else {
                    entity.resetProgress();
                    entity.resetStartButton(level, pos, entity);
                    setChanged(level, pos, state);
                }
            } else {
                entity.resetProgress();
                entity.resetStartButton(level, pos, entity);
                setChanged(level, pos, state);
            }

        }
    }

    private static boolean canCook(Level level, int cookingType, StoveEntity entity, BlockState state, Optional<StoveRecipe> recipe) {
        boolean isSameType = recipe.filter(stoveRecipe -> (cookingType == stoveRecipe.getCookingType())).isPresent();
        return isSameType && isLit(state) && canOutput(level, entity, recipe);
    }

    private static boolean canCookDumpling(Level level, BlockPos pos, StoveEntity entity, BlockState state, ItemStack result, int redstone) {
        return isLit(state) && canOutputDumpling(entity, result) && isStartButtonDown(level, pos, state, redstone);
    }

    private static boolean canOutput(Level level, StoveEntity entity, Optional<StoveRecipe> recipe) {
        var registryAccess = level.registryAccess();
        return entity.inventory.getStackInSlot(10) == ItemStack.EMPTY
                || (entity.inventory.getStackInSlot(10).getItem() == recipe.get().getResultItem(registryAccess).getItem()
                && entity.inventory.getStackInSlot(10).getCount() < recipe.get().getResultItem(registryAccess).getMaxStackSize());
    }

    private static boolean canOutputDumpling(StoveEntity entity, ItemStack result) {
        return entity.inventory.getStackInSlot(10) == ItemStack.EMPTY
                || (ItemStack.isSameItemSameTags(entity.inventory.getStackInSlot(10), result)
                && entity.inventory.getStackInSlot(10).getCount() < result.getMaxStackSize());
    }

    private static boolean isLit(BlockState state) {
        return state.getValue(StoveBlock.LIT);
    }

    private static boolean isStartButtonDown(Level level, BlockPos pos, BlockState state, int redstone) {
        if (state.getValue(START_BUTTON) == 1) {
            return true;
        } else {
            return switch (state.getValue(START_BUTTON)) {
                case 0 -> {
                    if (redstone == 1 && level.hasNeighborSignal(pos)) {
                        level.setBlockAndUpdate(pos, state.setValue(START_BUTTON, 1));
                        yield true;
                    }
                    yield false;
                }
                case 1 -> true;
                default -> throw new IllegalStateException("Unexpected value: " + state.getValue(START_BUTTON));
            };
        }
    }

    private static boolean hasRecipe(Optional<StoveRecipe> recipe) {
        return recipe.isPresent();
    }

    private void resetProgress() {
        this.progress = 0;
        inventoryChanged();
    }

    private void resetStartButton(Level level, BlockPos pos, StoveEntity entity) {
        level.setBlockAndUpdate(pos, entity.getBlockState().setValue(START_BUTTON, 0));
    }

    private static SimpleContainer getInventory(StoveEntity entity) {
        int h = 0;

        SimpleContainer container = new SimpleContainer(entity.inventory.getSlots());
        for (int i = 0; i < entity.inventory.getSlots(); i++) {
            if (i < 9 && entity.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                container.setItem(h, entity.inventory.getStackInSlot(i));
                h++;
            } else if (i < 9) {
                container.setItem(8 - (i - h), entity.inventory.getStackInSlot(i));
            }
            if (i == 9 || i == 10) {
                container.setItem(i, entity.inventory.getStackInSlot(i));
            }
        }
        return container;
    }

    private static void craftItem(Level level, StoveEntity entity, Optional<StoveRecipe> recipe) {
        if (hasRecipe(recipe)) {
            for (int i = 0; i < 10; i++) {
                if (entity.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                    if (entity.inventory.getStackInSlot(i).hasCraftingRemainingItem()) {
                        spawnItem(entity, level, entity.inventory.getStackInSlot(i).getCraftingRemainingItem());
                    }
                    entity.inventory.extractItem(i, 1, false);
                }
            }
            entity.inventory.setStackInSlot(10, new ItemStack(recipe.get().getResultItem(level.registryAccess()).getItem(),
                    entity.inventory.getStackInSlot(10).getCount()
                            + recipe.get().getResultItem(level.registryAccess()).getCount()));
            entity.resetProgress();
        }
    }

    private static void craftDumpling(Level level, StoveEntity entity, ItemStack result) {
        for (int i = 0; i < 10; i++) {
            if (entity.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                if (entity.inventory.getStackInSlot(i).hasCraftingRemainingItem()) {
                    spawnItem(entity, level, entity.inventory.getStackInSlot(i).getCraftingRemainingItem());
                } else if (entity.inventory.getStackInSlot(i).is(Items.POTION)) {
                    spawnItem(entity, level, new ItemStack(Items.GLASS_BOTTLE, 1));
                } else if (entity.inventory.getStackInSlot(i).getItem() instanceof SuspiciousStewItem
                        || entity.inventory.getStackInSlot(i).getItem() instanceof BowlFoodItem) {
                    spawnItem(entity, level, new ItemStack(Items.BOWL, 1));
                }
                entity.inventory.extractItem(i, 1, false);
            }
        }
        ItemStack resultSlot = entity.inventory.getStackInSlot(10);
        if (resultSlot == ItemStack.EMPTY) {
            entity.inventory.setStackInSlot(10, result);
        } else {
            ItemStack stack = result.copy();
            stack.setCount(resultSlot.getCount() + result.getCount());
            entity.inventory.setStackInSlot(10, stack);
        }
        entity.resetProgress();
    }

    private static void spawnItem(StoveEntity entity, Level level, ItemStack itemStack) {
        Direction direction = (entity.getBlockState().getValue(StoveBlock.FACING)).getCounterClockWise();
        double x = entity.worldPosition.getX() + 0.5 + direction.getStepX() * 0.25;
        double y = entity.worldPosition.getY() + 1.0;
        double z = entity.worldPosition.getZ() + 0.5 + direction.getStepZ() * 0.25;
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
        itemEntity.setDeltaMovement(direction.getStepX() * 0.06F, 0.2, direction.getStepZ() * 0.06F);
        level.addFreshEntity(itemEntity);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new StoveMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
