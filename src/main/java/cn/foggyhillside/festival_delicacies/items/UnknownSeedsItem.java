package cn.foggyhillside.festival_delicacies.items;

import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class UnknownSeedsItem extends Item {
    public UnknownSeedsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        List<Item> list = new ArrayList<>(Utils.getItemsByTag(pLevel.registryAccess(), ModTags.MOD_SEEDS));

        if(pLevel.isClientSide()){
            return InteractionResultHolder.pass(stack);
        }

        if (list.isEmpty()) {
            return InteractionResultHolder.pass(stack);
        } else {
            int randomIndex = pLevel.getRandom().nextInt(0, list.size());
            ItemStack seedItem = new ItemStack(list.get(randomIndex));
            if (!pPlayer.addItem(seedItem)) {//添加物品时会产生错乱？？？！！！
                Containers.dropItemStack(pLevel, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), seedItem);
            }
            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (!pPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
            pPlayer.getCooldowns().addCooldown(this, 8);
            return InteractionResultHolder.success(stack);
        }

    }


}
