package cn.foggyhillside.festival_delicacies.items;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.FoodList;
import cn.foggyhillside.festival_delicacies.ModCompat;
import cn.foggyhillside.festival_delicacies.config.CommonConfigs;
import cn.foggyhillside.festival_delicacies.registry.ModItems;
import cn.foggyhillside.festival_delicacies.registry.ModTags;
import cn.foggyhillside.festival_delicacies.util.Utils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vectorwing.farmersdelight.common.FoodValues;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DumplingItem extends TooltipItem {

    public DumplingItem(Properties pProperties) {
        super(pProperties);
        this.hasTooltip = false;
    }

    public DumplingItem(Properties pProperties, boolean hasTooltip) {
        super(pProperties);
        this.hasTooltip = hasTooltip;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        List<ItemStack> list = DumplingItemHandler.deserializeNBTS(pStack.getOrCreateTagElement("BlockEntityTag").getList("Dumpling", 10));
        for (ItemStack itemStack : list) {
            if ((itemStack.getItem().isEdible() || Utils.checkItemAndTag(ModTags.ALLOWED_INEDIBLE_CONSUMPTION, CommonConfigs.ALLOWED_INEDIBLE_CONSUMPTION, CommonConfigs.ALLOWED_INEDIBLE_CONSUMPTION_TAGS, itemStack))
                    && !(Utils.checkItemAndTag(ModTags.BANNED_DUMPLING_INGREDIENTS_CONSUMPTION, CommonConfigs.BANNED_DUMPLING_INGREDIENTS_CONSUMPTION, CommonConfigs.BANNED_INGREDIENT_TAGS_CONSUMPTION, itemStack))) {
                if (CommonConfigs.NO_FURTHER_NESTING.get()) {
                    finishUsingItemNoTag(itemStack, pLevel, pLivingEntity);
                } else {
                    if (itemStack.is(ModTags.DUMPLING) && !CommonConfigs.NESTING_DUMPLING.get()) {
                        finishUsingItemNoTag(itemStack, pLevel, pLivingEntity);
                    } else {
                        itemStack.finishUsingItem(pLevel, pLivingEntity);
                    }
                }
            }
        }
        ItemStack stackNoTag = new ItemStack(pStack.getItem(), pStack.getCount());
        stackNoTag = super.finishUsingItem(stackNoTag, pLevel, pLivingEntity);
        pStack.shrink(pStack.getCount() - stackNoTag.getCount());
        return pStack;
    }

    private void finishUsingItemNoTag(ItemStack itemStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack stackNoTag = new ItemStack(itemStack.getItem(), 1);
        stackNoTag.finishUsingItem(pLevel, pLivingEntity);
    }

    @Override
    public Component getName(ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            return super.getName(pStack);
        }
        return getDisplayName(pStack);
    }

    public Component getDisplayName(ItemStack pStack) {
        List<ItemStack> stackList = DumplingItemHandler.deserializeNBTS(pStack.getOrCreateTagElement("BlockEntityTag").getList("Dumpling", 10));
        List<Component> componentList = new ArrayList<>();
        String type;
        if (pStack.getItem() == ModItems.BOILED_DUMPLING.get()) {
            type = "boiled_dumpling";
        } else if (pStack.getItem() == ModItems.STEAMED_DUMPLING.get()) {
            type = "steamed_dumpling";
        } else {
            type = "baozi";
        }
        stackList.stream().collect(Collectors.groupingBy(item -> getFullName(item).plainCopy())).forEach((item, list) -> {
            componentList.add(item);
        });
        if (componentList.size() > 3) {
            return translate(type);
        } else {
            type = "ingredients." + type;
            return translate(type,
                    Component.translatable("item.festival_delicacies.ingredients.%s".formatted(componentList.size()),
                            componentList.toArray()));
        }
    }

    public Component translate(String type, Object... args) {
        return Component.translatable("item.%s.%s".formatted(FestivalDelicacies.MODID, type), args);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        List<ItemStack> list = DumplingItemHandler.deserializeNBTS(stack.getOrCreateTagElement("BlockEntityTag").getList("Dumpling", 10));
        list.stream().collect(Collectors.groupingBy(item -> getFullName(item).plainCopy(), LinkedHashMap::new, Collectors.counting())).forEach((item, count) -> {
            if (count > 1) {
                tooltip.add(Component.translatable("%s.%s".formatted(FestivalDelicacies.MODID, "tooltip.ingredient_count"), item, count).withStyle(ChatFormatting.GRAY));
            } else {
                tooltip.add(item.withStyle(ChatFormatting.GRAY));
            }
        });
    }

    private int getTotalNutrition(List<ItemStack> list) {
        int nutrition = FoodList.DUMPLING.getNutrition();
        for (ItemStack item : list) {
            nutrition += getFood(item).getNutrition();
        }
        return nutrition;
    }

    private float getSaturationMod(List<ItemStack> list) {
        float t = FoodList.DUMPLING.getNutrition() * FoodList.DUMPLING.getSaturationModifier();
        for (ItemStack item : list) {
            t += (getFood(item).getSaturationModifier() * getFood(item).getNutrition());
        }
        return t / getTotalNutrition(list);
    }

    private FoodProperties getFood(ItemStack stack) {
        Item item = stack.getItem();
        FoodProperties.Builder builder = new FoodProperties.Builder();
        if (item == Items.POTION) {
            for (MobEffectInstance effectInstance : PotionUtils.getMobEffects(stack)) {
                builder.effect(effectInstance, 1.0F).build();
            }
            return builder.build();
        } else if (stack.isEdible()) {
            if (ModCompat.isFarmersDelightLoaded()
                    && (item == Items.MUSHROOM_STEW || item == Items.BEETROOT_SOUP || item == Items.RABBIT_STEW)) {
                FoodProperties.Builder foodBuilder = new FoodProperties.Builder().nutrition(item.getFoodProperties().getNutrition()).saturationMod(item.getFoodProperties().getSaturationModifier());
                for (Pair<MobEffectInstance, Float> pair : FoodValues.VANILLA_SOUP_EFFECTS.get(item).getEffects()) {
                    foodBuilder.effect(pair.getFirst(), pair.getSecond());
                }
                return foodBuilder.build();
            } else {
                return stack.getFoodProperties(null);
            }
        } else {
            return builder.build();
        }
    }


    private void addEffect(List<Pair<MobEffectInstance, Float>> effectList, Pair<MobEffectInstance, Float> pair) {
        if (effectList.isEmpty()) {
            effectList.add(pair);
        } else {
            int id = 0;
            boolean hasSameEffect = false;
            for (int i = 0; i < effectList.size(); i++) {
                Pair<MobEffectInstance, Float> listPair = effectList.get(i);
                if (listPair.getSecond().equals(1F) && pair.getSecond().equals(1F) && listPair.getFirst().getEffect() == pair.getFirst().getEffect()) {
                    hasSameEffect = true;
                    id = i;
                    break;
                }
            }
            if (!hasSameEffect) {
                effectList.add(pair);
            } else if (effectList.get(id).getFirst().getAmplifier() < pair.getFirst().getAmplifier()) {
                effectList.set(id, pair);
            } else if (effectList.get(id).getFirst().getAmplifier() == pair.getFirst().getAmplifier()) {
                if (effectList.get(id).getFirst().getDuration() < pair.getFirst().getDuration()) {
                    effectList.set(id, pair);
                }
            }
        }
    }

    @Override
    public @Nullable FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        List<ItemStack> list = DumplingItemHandler.deserializeNBTS(stack.getOrCreateTagElement("BlockEntityTag").getList("Dumpling", 10));
        FoodProperties.Builder builder = new FoodProperties.Builder()
                .nutrition(getTotalNutrition(list))
                .saturationMod(getSaturationMod(list));
        List<Pair<MobEffectInstance, Float>> effectList = new ArrayList<>();
        for (ItemStack item : list) {
            for (Pair<MobEffectInstance, Float> pair : getFood(item).getEffects()) {
                addEffect(effectList, pair);
            }
        }
        for (Pair<MobEffectInstance, Float> pair : effectList) {
            if (pair.getFirst() != null) {
                builder.effect(pair.getFirst(), pair.getSecond());
            }
        }
        return builder.build();
    }

    public static Component getFullName(ItemStack itemStack) {
        return itemStack.getHoverName();
    }

    public static ItemStack makeDumpling(int type, ItemLike... items) {
        return makeDumpling(type, Arrays.stream(items)
                .map(ItemStack::new)
                .toArray(ItemStack[]::new));
    }

    public static ItemStack makeDumpling(int type, ItemStack... items) {
        ArrayList<ItemStack> list = new ArrayList<>();
        list.addAll(Arrays.asList(items));
        return of(type, list);
    }

    public static ItemStack of(int type, List<ItemStack> items) {
        List<ItemStack> flattenedItems = new ArrayList<>();
        Item itemType;
        for (ItemStack item : items) {
            if (item.getCount() != 1) {
                throw new IllegalArgumentException();
            }
            if (CommonConfigs.NESTING_DUMPLING.get() || !item.is(ModTags.DUMPLING)) {
                flattenedItems.add(item);
            }
        }
        itemType = switch (type) {
            case 6 -> ModItems.STEAMED_DUMPLING.get();
            case 7 -> ModItems.BAOZI.get();
            default -> ModItems.BOILED_DUMPLING.get();
        };
        ItemStack result = new ItemStack(itemType, 1);
        result.getOrCreateTagElement("BlockEntityTag").put("Dumpling", DumplingItemHandler.serializeItems(flattenedItems));
        return result;
    }
}
