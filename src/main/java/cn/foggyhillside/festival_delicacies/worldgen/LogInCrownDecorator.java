package cn.foggyhillside.festival_delicacies.worldgen;

import cn.foggyhillside.festival_delicacies.blocks.FruitTreeLogBlock;
import cn.foggyhillside.festival_delicacies.registry.ModTreeDecorators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class LogInCrownDecorator extends TreeDecorator {
    protected final BlockStateProvider blockProvider;

    public static final Codec<LogInCrownDecorator> CODEC = RecordCodecBuilder.create((p_225996_) -> p_225996_.group(BlockStateProvider.CODEC.fieldOf("block_provider").forGetter((p_226008_) -> p_226008_.blockProvider)).apply(p_225996_, LogInCrownDecorator::new));

    public LogInCrownDecorator(BlockStateProvider blockProvider) {
        this.blockProvider = blockProvider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.LOG_IN_CROWN_DECORATOR.get();
    }

    @Override
    public void place(Context pContext) {
        for (BlockPos logPos : pContext.logs()) {
            boolean[] branch = new boolean[4];
            branch[0] = pContext.leaves().contains(logPos.north());
            branch[1] = pContext.leaves().contains(logPos.east());
            branch[2] = pContext.leaves().contains(logPos.south());
            branch[3] = pContext.leaves().contains(logPos.west());
            if (branch[0] || branch[1] || branch[2] || branch[3]) {
                pContext.setBlock(logPos, blockProvider.getState(pContext.random(), logPos)
                        .setValue(FruitTreeLogBlock.NORTH_BRANCH, branch[0])
                        .setValue(FruitTreeLogBlock.EAST_BRANCH, branch[1])
                        .setValue(FruitTreeLogBlock.SOUTH_BRANCH, branch[2])
                        .setValue(FruitTreeLogBlock.WEST_BRANCH, branch[3]));
            }
        }
    }
}
