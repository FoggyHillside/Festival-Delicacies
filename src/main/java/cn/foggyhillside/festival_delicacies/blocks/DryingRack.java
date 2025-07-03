package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.blocks.entities.DryingRackEntity;
import cn.foggyhillside.festival_delicacies.registry.ModBlockEntities;
import cn.foggyhillside.festival_delicacies.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class DryingRack extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(0.0F, 0.0F, 0.0F, 2.0F, 15.99F, 2.0F),
            Block.box(0.0F, 0.0F, 14.0F, 2.0F, 15.99F, 16.0F),
            Block.box(14.0F, 0.0F, 0.0F, 16.0F, 15.99F, 2.0F),
            Block.box(14.0F, 0.0F, 14.0F, 16.0F, 15.99F, 16.0F),
            Block.box(0.0F, 4.0F, 0.0F, 16.0F, 6.0F, 16.0F),
            Block.box(0.0F, 12.0F, 0.0F, 16.0F, 14.0F, 16.0F));

    public DryingRack(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DryingRackEntity(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        BlockEntity entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof DryingRackEntity rackEntity) {
            int nextSlot = rackEntity.getNextEmptySlot();
            if (pPlayer.isShiftKeyDown()) {
                if (pHand.equals(InteractionHand.MAIN_HAND)) {
                    if (heldStack.isEmpty() && !rackEntity.isEmpty()) {
                        NonNullList<ItemStack> list = rackEntity.getInventoryList();
                        Direction direction = pState.getValue(FACING).getCounterClockWise();
                        for (int i = 0; i < list.size(); i++) {
                            Utils.dropItem(pLevel, list.get(i), pPos.getX() + 0.5F, pPos.getY() + 1F, pPos.getZ() + 0.5F, direction.getStepX() * 0.2F, 0F, direction.getStepZ() * 0.2F);
                        }
                        Arrays.fill(rackEntity.getProgressArray(), 0);
                        pLevel.updateNeighbourForOutputSignal(pPos, this);

                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                if (nextSlot < 0) {
                    return InteractionResult.PASS;
                }
                if (pHand.equals(InteractionHand.MAIN_HAND)) {
                    if (rackEntity.addItem(heldStack, pPlayer, nextSlot)) {
                        return InteractionResult.SUCCESS;
                    }

                } else {
                    ItemStack mainHandStack = pPlayer.getItemInHand(InteractionHand.MAIN_HAND);
                    if (mainHandStack.isEmpty()) {
                        if (rackEntity.addItem(heldStack, pPlayer, nextSlot)) {
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof DryingRackEntity rackEntity) {
                Containers.dropContents(pLevel, pPos, rackEntity.getInventoryList());
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
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
        return createTickerHelper(pBlockEntityType, ModBlockEntities.DRYING_RACK.get(), DryingRackEntity::tick);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof DryingRackEntity entity) {
            int size = entity.getInventoryList().size();
            if (size < 12) {
                return size;
            } else {
                return 15;
            }
        } else {
            return super.getAnalogOutputSignal(pState, pLevel, pPos);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

}
