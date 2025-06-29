package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.blocks.entities.WoodenPlateEntity;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.registry.ModBlockStateProperties;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WoodenPlateBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty PLATE_ITEM_TYPE = ModBlockStateProperties.PLATE_ITEM_TYPE;
    public static final IntegerProperty AMOUNT = ModBlockStateProperties.AMOUNT;
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 2.0F, 16.0F);

    public WoodenPlateBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(PLATE_ITEM_TYPE, 0).setValue(AMOUNT, 0));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        if (blockEntity instanceof WoodenPlateEntity plateEntity) {
            if (pHand.equals(InteractionHand.MAIN_HAND)) {
                if (!heldStack.isEmpty() && !plateEntity.isFull()
                        && (heldStack.is(ModTags.DUMPLING) || heldStack.is(ModItems.QINGTUAN.get()) || heldStack.is(ModTags.MOONCAKE))) {
                    if (plateEntity.addItem(heldStack, pPlayer)) {
                        return InteractionResult.SUCCESS;
                    }
                } else if (heldStack.isEmpty() && !plateEntity.isEmpty()) {
                    if (plateEntity.extractItem(pPlayer, pHand)) {
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof WoodenPlateEntity plate) {
                Containers.dropContents(pLevel, pPos, plate.getInventoryList());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState pState) {
        return true;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PLATE_ITEM_TYPE);
        builder.add(AMOUNT);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof WoodenPlateEntity plateEntity) {
            if (plateEntity.isFull()) {
                return 15;
            } else if (plateEntity.isEmpty()) {
                return 0;
            } else {
                return 15 / plateEntity.getMaxAmount() * plateEntity.getItemAmount();
            }
        } else {
            return 0;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WoodenPlateEntity(pPos, pState);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.WOODEN_PLATE.get(), WoodenPlateEntity::tick);
    }
}
