package cn.foggyhillside.festival_delicacies.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class SeedCropBlock extends CropBlock {
    protected final Supplier<Item> seed;
    private final VoxelShape[] shapesByAge;

    public SeedCropBlock(Properties pProperties, Supplier<Item> seed, VoxelShape[] shapesByAge) {
        super(pProperties);
        this.seed = seed;
        this.shapesByAge = shapesByAge;
    }

    public SeedCropBlock(Properties pProperties, Supplier<Item> seed) {
        super(pProperties);
        this.seed = seed;
        this.shapesByAge = null;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seed.get();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shapesByAge == null ? super.getShape(pState, pLevel, pPos, pContext) : shapesByAge[this.getAge(pState)];
    }
}
