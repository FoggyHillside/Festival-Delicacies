package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.StoveBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.MenuBlockItemHandler;
import cn.foggyhillside.festival_delicacies.blocks.screen.StoveMenu;
import cn.foggyhillside.festival_delicacies.config.CommonConfigs;
import cn.foggyhillside.festival_delicacies.items.DumplingItem;
import cn.foggyhillside.festival_delicacies.recipe.StoveRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoveEntity extends SyncedBlockEntity implements MenuProvider, RecipeHolder {
    private static final int CONTAINER_INPUT = 9;
    private static final int RESULT_OUTPUT = 10;

    public final ItemStackHandler inventory;
    private final LazyOptional<IItemHandlerModifiable> playerInputHandler;
    private final LazyOptional<IItemHandlerModifiable> inputHandler;
    private final LazyOptional<IItemHandlerModifiable> outputHandler;

    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;

    protected final ContainerData data;

    private int progress = 0;
    private int maxProgress;

    private boolean hasLastSignal;

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
        this.playerInputHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, null, CONTAINER_INPUT, RESULT_OUTPUT));
        this.inputHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, Direction.UP, CONTAINER_INPUT, RESULT_OUTPUT));
        this.outputHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, Direction.DOWN, CONTAINER_INPUT, RESULT_OUTPUT));
        this.usedRecipeTracker = new Object2IntOpenHashMap();
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
        nbt.put("Inventory", inventory.serializeNBT());
        nbt.putInt("Progress", this.progress);
        nbt.putInt("MaxProgress", this.maxProgress);
        CompoundTag compoundRecipes = new CompoundTag();
        this.usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        nbt.put("RecipesUsed", compoundRecipes);
    }

    @Override
    public void load(CompoundTag nbt) {
        inventory.deserializeNBT(nbt.getCompound("Inventory"));
        this.progress = nbt.getInt("Progress");
        this.maxProgress = nbt.getInt("MaxProgress");
        CompoundTag compoundRecipes = nbt.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            this.usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
        }
        super.load(nbt);
    }

    public SimpleContainer getInventoryContainer() {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            container.addItem(inventory.getStackInSlot(i));
        }

        return container;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, StoveEntity entity) {
        if (level.isClientSide) {
            return;
        }
        int type = state.getValue(StoveBlock.COOKING_TYPE);
        int start = state.getValue(StoveBlock.START_BUTTON);
        int redstone = state.getValue(StoveBlock.RED_STONE);
        if (type >= 1 && type <= 4) {
            if (start == 1) {
                level.setBlockAndUpdate(pos, state.setValue(StoveBlock.START_BUTTON, 0));
            }
            Optional<StoveRecipe> recipe = level.getRecipeManager().getRecipeFor(StoveRecipe.Type.INSTANCE, getOrderedInventoryContainer(entity), level);
            int cookingType = state.getValue(StoveBlock.COOKING_TYPE);
            if (recipe.isPresent() && canCook(level, cookingType, entity, state, recipe.get())) {
                entity.progress++;
                entity.maxProgress = recipe.get().getCookTime();
                setChanged(level, pos, state);
                if (entity.progress >= recipe.get().getCookTime()) {
                    craftItem(level, entity, recipe.get());
                }
            } else {
                entity.resetProgress();
                setChanged(level, pos, state);
            }
        } else {
            if (entity.inventory.getStackInSlot(9).is(ModTags.DOUGH)) {
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
                    entity.resetStartButton(level, pos);
                    setChanged(level, pos, state);
                }
            } else {
                entity.resetProgress();
                entity.resetStartButton(level, pos);
                setChanged(level, pos, state);
            }

        }

        entity.hasLastSignal = level.hasNeighborSignal(pos);
    }

    private static boolean canCook(Level level, int cookingType, StoveEntity entity, BlockState state, StoveRecipe recipe) {
        boolean isSameType = cookingType == recipe.getCookingType();
        return isSameType && isLit(state) && canOutput(level, entity, recipe);
    }

    private static boolean canCookDumpling(Level level, BlockPos pos, StoveEntity entity, BlockState state, ItemStack result, int redstone) {
        return isLit(state) && canOutputDumpling(entity, result) && isStartButtonDown(level, pos, state, entity, redstone);
    }

    private static boolean canOutput(Level level, StoveEntity entity, StoveRecipe recipe) {
        var registryAccess = level.registryAccess();
        ItemStack resultSlot = entity.inventory.getStackInSlot(10);
        ItemStack recipeResult = recipe.getResultItem(registryAccess);
        return resultSlot == ItemStack.EMPTY
                || (resultSlot.getItem() == recipeResult.getItem()
                && recipeResult.getMaxStackSize() - resultSlot.getCount() >= recipeResult.getCount());
    }

    private static boolean canOutputDumpling(StoveEntity entity, ItemStack result) {
        ItemStack resultSlot = entity.inventory.getStackInSlot(10);
        return resultSlot == ItemStack.EMPTY
                || (ItemStack.isSameItemSameTags(resultSlot, result)
                && result.getMaxStackSize() - resultSlot.getCount() >= result.getCount());
    }

    private static boolean isLit(BlockState state) {
        return state.getValue(StoveBlock.LIT);
    }

    private static boolean isStartButtonDown(Level level, BlockPos pos, BlockState state, StoveEntity entity, int redstone) {
        return switch (state.getValue(StoveBlock.START_BUTTON)) {
            case 0 -> {
                if (redstone == 1 && !entity.hasLastSignal && level.hasNeighborSignal(pos)) {
                    level.setBlockAndUpdate(pos, state.setValue(StoveBlock.START_BUTTON, 1));
                    yield true;
                }
                yield false;
            }
            case 1 -> {
                if (redstone == 1 && !entity.hasLastSignal && level.hasNeighborSignal(pos)) {
                    level.setBlockAndUpdate(pos, state.setValue(StoveBlock.START_BUTTON, 0));
                    yield false;
                }
                yield true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + state.getValue(StoveBlock.START_BUTTON));
        };
    }

    private void resetProgress() {
        this.progress = 0;
        inventoryChanged();
    }

    private void resetStartButton(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, this.getBlockState().setValue(StoveBlock.START_BUTTON, 0));
    }

    private static SimpleContainer getOrderedInventoryContainer(StoveEntity entity) {
        SimpleContainer container = new SimpleContainer(entity.inventory.getSlots());

        for (int i = 0; i < 11; i++) {
            ItemStack stack = entity.inventory.getStackInSlot(i);

            if (!stack.isEmpty()) {
                container.setItem(i, stack);
            }
        }

        return container;
    }

    private static void craftItem(Level level, StoveEntity entity, StoveRecipe recipe) {
        for (int i = 0; i < 10; i++) {
            if (entity.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                if (entity.inventory.getStackInSlot(i).hasCraftingRemainingItem()) {
                    spawnItem(entity, level, entity.inventory.getStackInSlot(i).getCraftingRemainingItem());
                }
                entity.inventory.extractItem(i, 1, false);
            }
        }
        entity.inventory.setStackInSlot(10, new ItemStack(recipe.getResultItem(level.registryAccess()).getItem(),
                entity.inventory.getStackInSlot(10).getCount()
                        + recipe.getResultItem(level.registryAccess()).getCount()));
        entity.setRecipeUsed(recipe);
        entity.resetProgress();

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
        double x = entity.worldPosition.getX() + 0.5F + direction.getStepX() * 0.25F;
        double y = entity.worldPosition.getY() + 1.0F;
        double z = entity.worldPosition.getZ() + 0.5F + direction.getStepZ() * 0.25F;
        Utils.dropItem(level, itemStack, x, y, z, direction.getStepX() * 0.06F, 0.2F, direction.getStepZ() * 0.06F);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new StoveMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation recipeID = pRecipe.getId();
            this.usedRecipeTracker.addTo(recipeID, 1);
        }
    }

    @Override
    public @Nullable Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player pPlayer, List<ItemStack> pItems) {
        List<Recipe<?>> usedRecipes = this.getUsedRecipesAndPopExperience(pPlayer.level(), pPlayer.position());
        pPlayer.awardRecipes(usedRecipes);
        this.usedRecipeTracker.clear();
    }

    public List<Recipe<?>> getUsedRecipesAndPopExperience(Level level, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();
        ObjectIterator var4 = this.usedRecipeTracker.object2IntEntrySet().iterator();

        while (var4.hasNext()) {
            Object2IntMap.Entry<ResourceLocation> entry = (Object2IntMap.Entry) var4.next();
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                Utils.splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((StoveRecipe) recipe).getExperience());
            });
        }

        return list;
    }

}
