package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.events.loot.*;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FestivalDelicacies.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ITEM_ADDITION_SERIALIZERS = LOOT_MODIFIERS.register("item_addition", ItemAdditionModifier.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> LOOT_TABLE_SERIALIZERS = LOOT_MODIFIERS.register("loot_table", LootTableAdditionModifier.CODEC);

    public static void register(IEventBus bus) {
        LOOT_MODIFIERS.register(bus);
    }
}
