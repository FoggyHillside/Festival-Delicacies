package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.entities.ChoppingBlockEntity;
import cn.foggyhillside.festival_delicacies.recipe.ChoppingRecipe;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModBlockStateProperties;
import cn.foggyhillside.festival_delicacies.registry.ModSounds;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ChoppingBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty KNIFE_INSERTED = ModBlockStateProperties.KNIFE_INSERTED;
    public static final BooleanProperty WITH_KNIFE = ModBlockStateProperties.WITH_KNIFE;
    protected static final VoxelShape SHAPE = Block.box(2.0F, 0.0F, 2.0F, 14.0F, 3.0F, 14.0F);

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(KNIFE_INSERTED, false).setValue(WITH_KNIFE, false);
    }

    public ChoppingBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH)
                .setValue(KNIFE_INSERTED, false)
                .setValue(WITH_KNIFE, false));
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(KNIFE_INSERTED);
        builder.add(WITH_KNIFE);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ChoppingBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ChoppingBlockEntity choppingBlock) {
                Containers.dropItemStack(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), choppingBlock.getItemStack());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof ChoppingBlockEntity choppingBlockEntity && !choppingBlockEntity.getItemStack().isEmpty()) {
            return 15;
        } else {
            return 0;
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        Direction direction = pState.getValue(ChoppingBlock.FACING).getCounterClockWise();
        if (blockEntity instanceof ChoppingBlockEntity choppingBlockEntity) {
            if (choppingBlockEntity.getItemStack().isEmpty()) {
                if (isKnife(heldStack) && choppingBlockEntity.addItem(heldStack, pPlayer)) {
                    pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            } else if (heldStack.isEmpty()) {
                if (pPlayer.isShiftKeyDown()) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(KNIFE_INSERTED, !pState.getValue(KNIFE_INSERTED)));
                    return InteractionResult.SUCCESS;
                } else if (pHand.equals(InteractionHand.MAIN_HAND) && choppingBlockEntity.extractItem(pPlayer, pHand)) {
                    pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (!pPlayer.isShiftKeyDown()) {
                    SimpleContainer container = new SimpleContainer(heldStack.copy());
                    Optional<ChoppingRecipe> recipe = pLevel.getRecipeManager().getRecipeFor(ChoppingRecipe.Type.INSTANCE, container, pLevel);

                    if (recipe.isPresent()) {
                        NonNullList<ItemStack> list = recipe.get().getResultsList();
                        pLevel.playSound(null, pPos.getX(), pPos.getY(), pPos.getZ(), ModSounds.CHOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        spawnChoppingParticles(pLevel, pPos, heldStack);
                        if (!pPlayer.isCreative()) {
                            heldStack.shrink(1);
                            choppingBlockEntity.itemHurt(pLevel, pPos, pState);
                        }
                        for (ItemStack stack : list) {
                            Utils.dropItem(pLevel, stack.split(stack.getCount()), pPos.getX() + 0.5F, pPos.getY() + 0.3F, pPos.getZ() + 0.5F, direction.getStepX() * 0.2F, 0F, direction.getStepZ() * 0.2F);
                        }
                        return InteractionResult.SUCCESS;
                    }

                    if (!pLevel.isClientSide()) {
                        pPlayer.displayClientMessage(Component.translatable("%s.%s".formatted(FestivalDelicacies.MODID, "client_message.cannot_chop")), true);
                    }
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static void spawnChoppingParticles(Level level, BlockPos pos, ItemStack stack) {
        Vec3 vec3d = new Vec3(((double) level.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double) level.random.nextFloat() - 0.5D) * 0.1D);
        if (level instanceof ServerLevel) {
            ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.4F, pos.getZ() + 0.5F, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
        } else {
            level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), pos.getX() + 0.5F, pos.getY() + 0.4F, pos.getZ() + 0.5F, vec3d.x, vec3d.y + 0.05D, vec3d.z);
        }
    }

    public static boolean isKnife(ItemStack stack) {
        return !stack.isEmpty() && stack.isDamageableItem() && stack.is(ModTags.KNIFE);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 100;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlockEntities.CHOPPING_BLOCK.get(), ChoppingBlockEntity::tick);
    }
}
