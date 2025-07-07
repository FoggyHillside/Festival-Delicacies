package cn.foggyhillside.festival_delicacies.blocks.entities;

import cn.foggyhillside.festival_delicacies.blocks.FermentationJarBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.inventory.MenuBlockItemHandler;
import cn.foggyhillside.festival_delicacies.blocks.screen.FermentationJarMenu;
import cn.foggyhillside.festival_delicacies.recipe.FermentingRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class FermentationJarEntity extends SyncedBlockEntity implements MenuProvider, RecipeHolder {
    private static final int CONTAINER_INPUT = 6;
    private static final int RESULT_OUTPUT = 7;
    public static final int SLOTS = 8;
    private int progress = 0;
    private int maxProgress;
    private boolean hasLastSignal;
    private final Object2IntOpenHashMap<ResourceLocation> usedRecipeTracker;
    protected final ContainerData data;
    private final ItemStackHandler inventory;
    private final LazyOptional<IItemHandler> playerItemHandler;
    private final LazyOptional<IItemHandler> inputItemHandler;
    private final LazyOptional<IItemHandler> outputItemHandler;

    public FermentationJarEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FERMENTATION_JAR.get(), pPos, pBlockState);
        this.inventory = createHandler();
        this.playerItemHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, null, CONTAINER_INPUT, RESULT_OUTPUT));
        this.inputItemHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, Direction.UP, CONTAINER_INPUT, RESULT_OUTPUT));
        this.outputItemHandler = LazyOptional.of(() -> new MenuBlockItemHandler(inventory, Direction.DOWN, CONTAINER_INPUT, RESULT_OUTPUT));
        this.usedRecipeTracker = new Object2IntOpenHashMap();
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FermentationJarEntity.this.progress;
                    case 1 -> FermentationJarEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FermentationJarEntity.this.progress = value;
                    case 1 -> FermentationJarEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(SLOTS) {
            @Override
            protected void onContentsChanged(int slot) {
                inventoryChanged();
            }
        };
    }

    @Override
    public void load(CompoundTag nbt) {
        inventory.deserializeNBT(nbt.getCompound("inventory"));
        this.progress = nbt.getInt("progress");
        this.maxProgress = nbt.getInt("maxprogress");
        CompoundTag compoundRecipes = nbt.getCompound("RecipesUsed");
        for (String key : compoundRecipes.getAllKeys()) {
            this.usedRecipeTracker.put(new ResourceLocation(key), compoundRecipes.getInt(key));
        }
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", inventory.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putInt("maxprogress", this.maxProgress);
        CompoundTag compoundRecipes = new CompoundTag();
        this.usedRecipeTracker.forEach((recipeId, craftedAmount) -> compoundRecipes.putInt(recipeId.toString(), craftedAmount));
        nbt.put("RecipesUsed", compoundRecipes);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return playerItemHandler.cast();
            } else if (side.equals(Direction.UP)) {
                return inputItemHandler.cast();
            } else {
                return outputItemHandler.cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inputItemHandler.invalidate();
        outputItemHandler.invalidate();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FermentationJarEntity jarEntity) {
        if (level.isClientSide) {
            return;
        }
        int redstone = state.getValue(FermentationJarBlock.RED_STONE);
        if (isStartButtonDown(level, pos, state, jarEntity, redstone)) {
            Optional<FermentingRecipe> recipe = level.getRecipeManager().getRecipeFor(FermentingRecipe.Type.INSTANCE, getOrderedInventoryContainer(jarEntity), level);
            if (recipe.isPresent() && canOutput(level, jarEntity, recipe.get())) {
                jarEntity.progress++;
                jarEntity.maxProgress = recipe.get().getCookTime();
                setChanged(level, pos, state);
                if (jarEntity.progress >= recipe.get().getCookTime()) {
                    craftItem(level, jarEntity, recipe.get());
                }
            } else {
                jarEntity.resetProgress();
                jarEntity.resetStartButton(level, pos);
                setChanged(level, pos, state);
            }
        } else {
            jarEntity.resetProgress();
            setChanged(level, pos, state);
        }

        jarEntity.hasLastSignal = level.hasNeighborSignal(pos);
    }

    private static boolean isStartButtonDown(Level level, BlockPos pos, BlockState state, FermentationJarEntity entity, int redstone) {
        return switch (state.getValue(FermentationJarBlock.START_BUTTON)) {
            case 0 -> {
                if (redstone == 1 && !entity.hasLastSignal && level.hasNeighborSignal(pos)) {
                    level.setBlockAndUpdate(pos, state.setValue(FermentationJarBlock.START_BUTTON, 1));
                    yield true;
                }
                yield false;
            }
            case 1 -> {
                if (redstone == 1 && !entity.hasLastSignal && level.hasNeighborSignal(pos)) {
                    level.setBlockAndUpdate(pos, state.setValue(FermentationJarBlock.START_BUTTON, 0));
                    yield false;
                }
                yield true;
            }
            default ->
                    throw new IllegalStateException("Unexpected value: " + state.getValue(FermentationJarBlock.START_BUTTON));
        };
    }

    private static SimpleContainer getOrderedInventoryContainer(FermentationJarEntity entity) {
        SimpleContainer container = new SimpleContainer(entity.inventory.getSlots());

        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = entity.inventory.getStackInSlot(i);

            if (i < SLOTS - 2) {
                if (stack != ItemStack.EMPTY) {
                    container.addItem(stack);
                }
            } else {
                container.setItem(i, stack);
            }
        }

        return container;
    }

    private static void craftItem(Level level, FermentationJarEntity entity, FermentingRecipe recipe) {
        for (int i = 0; i < SLOTS - 1; i++) {
            if (entity.inventory.getStackInSlot(i) != ItemStack.EMPTY) {
                if (entity.inventory.getStackInSlot(i).hasCraftingRemainingItem()) {
                    spawnItem(entity, level, entity.inventory.getStackInSlot(i).getCraftingRemainingItem());
                }
                entity.inventory.extractItem(i, 1, false);
            }
        }
        entity.inventory.setStackInSlot(SLOTS - 1, new ItemStack(recipe.getResultItem(level.registryAccess()).getItem(),
                entity.inventory.getStackInSlot(7).getCount()
                        + recipe.getResultItem(level.registryAccess()).getCount()));
        entity.setRecipeUsed(recipe);
        entity.resetProgress();

    }

    private static void spawnItem(FermentationJarEntity entity, Level level, ItemStack itemStack) {
        Direction direction = (entity.getBlockState().getValue(FermentationJarBlock.FACING)).getCounterClockWise();
        double x = entity.worldPosition.getX() + 0.5F + direction.getStepX() * 0.25F;
        double y = entity.worldPosition.getY() + 0.7F;
        double z = entity.worldPosition.getZ() + 0.5F + direction.getStepZ() * 0.25F;
        Utils.dropItem(level, itemStack, x, y, z, direction.getStepX() * 0.06F, 0.2F, direction.getStepZ() * 0.06F);
    }

    private static boolean canOutput(Level level, FermentationJarEntity entity, FermentingRecipe recipe) {
        var registryAccess = level.registryAccess();
        return entity.inventory.getStackInSlot(SLOTS - 1) == ItemStack.EMPTY
                || (entity.inventory.getStackInSlot(SLOTS - 1).getItem() == recipe.getResultItem(registryAccess).getItem()
                && entity.inventory.getStackInSlot(SLOTS - 1).getCount() < recipe.getResultItem(registryAccess).getMaxStackSize());
    }

    private void resetProgress() {
        this.progress = 0;
        inventoryChanged();
    }

    private void resetStartButton(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, this.getBlockState().setValue(FermentationJarBlock.START_BUTTON, 0));
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public SimpleContainer getInventoryContainer() {
        SimpleContainer container;
        container = new SimpleContainer(this.inventory.getSlots());
        for (int i = 0; i < SLOTS; i++) {
            ItemStack stack = this.inventory.getStackInSlot(i);
            container.addItem(stack);
        }

        return container;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.festival_delicacies.fermentation_jar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new FermentationJarMenu(pContainerId, pPlayerInventory, this, this.data);
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
                Utils.splitAndSpawnExperience((ServerLevel) level, pos, entry.getIntValue(), ((FermentingRecipe) recipe).getExperience());
            });
        }

        return list;
    }
}
