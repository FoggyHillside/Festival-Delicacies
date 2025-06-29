package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FestivalDelicacies.MODID);

    public static final RegistryObject<Block> STOVE = BLOCKS.register("stove",
            () -> new StoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0F).requiresCorrectToolForDrops().noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
    public static final RegistryObject<Block> CHOPPING_BLOCK = BLOCKS.register("chopping_block",
            () -> new ChoppingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> WOODEN_PLATE = BLOCKS.register("wooden_plate",
            () -> new WoodenPlateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> DRYING_RACK = BLOCKS.register("drying_rack",
            () -> new DryingRack(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD)));

    //Crops
    public static final RegistryObject<Block> CHINESE_CABBAGE = BLOCKS.register("chinese_cabbage",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.CHINESE_CABBAGE_SEEDS.get()));
    public static final RegistryObject<Block> GARLIC = BLOCKS.register("garlic",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.GARLIC.get()));
    public static final RegistryObject<Block> FENNEL = BLOCKS.register("fennel",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.FENNEL_SEEDS.get()));
    public static final RegistryObject<Block> GARLIC_CHIVE = BLOCKS.register("garlic_chive",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.GARLIC_CHIVE_SEEDS.get()));
    public static final RegistryObject<Block> EGGPLANT = BLOCKS.register("eggplant",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.EGGPLANT.get()));
    public static final RegistryObject<Block> GREEN_ONION = BLOCKS.register("greenonion",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.GREENONION.get()));
}
