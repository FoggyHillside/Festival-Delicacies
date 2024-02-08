package cn.foggyhillside.festival_delicacies.blocks.container;

import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import cn.foggyhillside.festival_delicacies.registry.ModBlocks;
import cn.foggyhillside.festival_delicacies.tag.ModTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class StoveSlot extends SlotItemHandler {
    public final StoveEntity tileEntity;
    private final Player player;

    private final Level level;

    public StoveSlot(Level level, Player player, StoveEntity entity, IItemHandler handler, int i, int xPosition, int yPosition) {
        super(handler, i, xPosition, yPosition);
        this.tileEntity = entity;
        this.player = player;
        this.level = level;
    }

    public boolean mayPlace(ItemStack stack) {
        return level.getBlockState(tileEntity.getBlockPos().above()).is(ModTags.IS_POT);
    }
}