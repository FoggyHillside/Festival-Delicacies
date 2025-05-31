package cn.foggyhillside.festival_delicacies.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BowlItem extends Item {
    public BowlItem(Item.Properties properties) {
        super(properties);
    }

    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack containerStack = itemStack.getCraftingRemainingItem();
        Player player;
        if (itemStack.isEdible()) {
            super.finishUsingItem(itemStack, level, entity);
        } else {
            player = entity instanceof Player ? (Player)entity : null;
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, itemStack);
            }

            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
            }
        }

        if (itemStack.isEmpty()) {
            return containerStack;
        } else {
            if (entity instanceof Player) {
                player = (Player)entity;
                if (!((Player)entity).getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                    player.drop(containerStack, false);
                }
            }

            return itemStack;
        }
    }
}
