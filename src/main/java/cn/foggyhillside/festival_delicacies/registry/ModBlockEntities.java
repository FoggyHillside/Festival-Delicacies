package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.entities.ChoppingBlockEntity;
import cn.foggyhillside.festival_delicacies.blocks.entities.DryingRackEntity;
import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import cn.foggyhillside.festival_delicacies.blocks.entities.WoodenPlateEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FestivalDelicacies.MODID);

    public static final RegistryObject<BlockEntityType<StoveEntity>> STOVE = BLOCK_ENTITIES.register("stove",
            () -> BlockEntityType.Builder.of(StoveEntity::new, ModBlocks.STOVE.get()).build(null));
    public static final RegistryObject<BlockEntityType<WoodenPlateEntity>> WOODEN_PLATE = BLOCK_ENTITIES.register("wooden_plate",
            () -> BlockEntityType.Builder.of(WoodenPlateEntity::new, ModBlocks.WOODEN_PLATE.get()).build(null));
    public static final RegistryObject<BlockEntityType<ChoppingBlockEntity>> CHOPPING_BLOCK = BLOCK_ENTITIES.register("chopping_block",
            () -> BlockEntityType.Builder.of(ChoppingBlockEntity::new, ModBlocks.CHOPPING_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<DryingRackEntity>> DRYING_RACK = BLOCK_ENTITIES.register("drying_rack",
            () -> BlockEntityType.Builder.of(DryingRackEntity::new, ModBlocks.DRYING_RACK.get()).build(null));

}
