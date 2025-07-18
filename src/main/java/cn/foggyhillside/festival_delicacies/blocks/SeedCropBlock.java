package cn.foggyhillside.festival_delicacies.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

import java.util.function.Supplier;

public class SeedCropBlock extends CropBlock {
    protected final Supplier<Item> seed;

    public SeedCropBlock(Properties pProperties, Supplier<Item> seed) {
        super(pProperties);
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return this.seed.get();
    }
}
