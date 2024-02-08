package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.entities.StoveEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, FestivalDelicacies.MOD_ID);

    public static final RegistryObject<BlockEntityType<StoveEntity>> STOVE = BLOCK_ENTITIES.register("stove",
            ()-> BlockEntityType.Builder.of(StoveEntity::new, ModBlocks.STOVE.get()).build(null));

}
