package cn.foggyhillside.festival_delicacies.blocks.container;

import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ResultSlot extends SlotItemHandler {
    public final StoveEntity tileEntity;
    private final Player player;

    public ResultSlot(Player player, StoveEntity entity, IItemHandler handler, int i, int xPosition, int yPosition) {
        super(handler, i, xPosition, yPosition);
        this.tileEntity = entity;
        this.player = player;
    }

    public boolean mayPlace(ItemStack stack) {
        return false;
    }
}
