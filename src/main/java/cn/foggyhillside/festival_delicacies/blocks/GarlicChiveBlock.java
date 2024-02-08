package cn.foggyhillside.festival_delicacies.blocks;

import cn.foggyhillside.festival_delicacies.registry.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public class GarlicChiveBlock extends CropBlock {
    public GarlicChiveBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.GARLIC_CHIVE_SEEDS.get();
    }
}
