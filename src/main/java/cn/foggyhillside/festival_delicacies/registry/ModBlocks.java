package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.*;
import cn.foggyhillside.festival_delicacies.blocks.growers.JujubeGrower;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FestivalDelicacies.MODID);

    public static final RegistryObject<Block> STOVE = BLOCKS.register("stove",
            () -> new StoveBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(2.0F).requiresCorrectToolForDrops().noOcclusion().lightLevel((state) -> state.getValue(BlockStateProperties.LIT) ? 13 : 0)));
    public static final RegistryObject<Block> CHOPPING_BLOCK = BLOCKS.register("chopping_block",
            () -> new ChoppingBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> WOODEN_PLATE = BLOCKS.register("wooden_plate",
            () -> new WoodenPlateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> DRYING_RACK = BLOCKS.register("drying_rack",
            () -> new DryingRackBlock(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> FERMENTATION_JAR = BLOCKS.register("fermentation_jar",
            () -> new FermentationJarBlock(BlockBehaviour.Properties.copy(Blocks.BROWN_GLAZED_TERRACOTTA).sound(SoundType.DECORATED_POT).pushReaction(PushReaction.IGNORE)));

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
            () -> new FruitingCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.EGGPLANT_SEEDS.get(), () -> ModItems.EGGPLANT.get()));
    public static final RegistryObject<Block> RED_BEAN = BLOCKS.register("red_bean",
            () -> new FruitingCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.RED_BEAN.get(), () -> ModItems.RED_BEAN.get()));
    public static final RegistryObject<Block> GREEN_ONION = BLOCKS.register("greenonion",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.GREEN_ONION_SEEDS.get()));
    public static final RegistryObject<Block> RICE = BLOCKS.register("rice",
            () -> new RiceBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> RICE_EAR = BLOCKS.register("rice_ear",
            () -> new RiceEarBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> LOTUS = BLOCKS.register("lotus",
            () -> new LotusBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> LOTUS_STALK = BLOCKS.register("lotus_stalk",
            () -> new LotusStalkBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> LOTUS_AERIAL_PART = BLOCKS.register("lotus_aerial_part",
            () -> new LotusAerialPartBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    //Wild Plant
    public static final RegistryObject<Block> WILD_GARLIC = BLOCKS.register("wild_garlic",
            () -> new WildCropBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final RegistryObject<Block> MUGWORT = BLOCKS.register("mugwort",
            () -> new MugwortBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    //Tree
    public static final RegistryObject<Block> JUJUBE_SAPLING = BLOCKS.register("jujube_sapling",
            () -> new SaplingBlock(new JujubeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> JUJUBE = BLOCKS.register("jujube",
            () -> new FruitBlock(() -> ModItems.JUJUBE.get()));
    public static final RegistryObject<Block> JUJUBE_LEAVES = BLOCKS.register("jujube_leaves",
            () -> new FruitLeavesBlock(JUJUBE, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> JUJUBE_LOG = BLOCKS.register("jujube_log",
            FruitTreeLogBlock::new);
    public static final RegistryObject<Block> POTTED_JUJUBE_SAPLING = BLOCKS.register("potted_jujube_sapling",
            () -> flowerPot(JUJUBE_SAPLING.get()));

    //Decorations
    public static final RegistryObject<Block> LOTUS_LEAF = BLOCKS.register("lotus_leaf",
            () -> new WaterlilyBlock(BlockBehaviour.Properties.copy(Blocks.LILY_PAD)));
    public static final RegistryObject<Block> LOTUS_FLOWER = BLOCKS.register("lotus_flower",
            () -> new WaterlilyBlock(BlockBehaviour.Properties.copy(Blocks.LILY_PAD)));

    private static FlowerPotBlock flowerPot(Block block) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY);
        return new FlowerPotBlock(block, properties);
    }
}
