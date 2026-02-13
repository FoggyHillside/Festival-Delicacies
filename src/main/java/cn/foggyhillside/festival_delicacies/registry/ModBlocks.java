package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.*;
import cn.foggyhillside.festival_delicacies.blocks.growers.FruitTreeGrower;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

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
            () -> new FruitingCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.RED_BEAN.get(), () -> ModItems.RED_BEAN.get(), CropShapes.RED_BEAN_SHAPES));
    public static final RegistryObject<Block> GREEN_ONION = BLOCKS.register("greenonion",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.GREEN_ONION_SEEDS.get()));
    public static final RegistryObject<Block> SESAME = BLOCKS.register("sesame",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.SESAME.get(), CropShapes.SESAME_SHAPES));
    public static final RegistryObject<Block> PEANUT = BLOCKS.register("peanut",
            () -> new SeedCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), () -> ModItems.PEANUT.get(), CropShapes.PEANUT_SHAPES));
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
            () -> new SaplingBlock(new FruitTreeGrower("patch_jujube"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> JUJUBE = BLOCKS.register("jujube",
            () -> new FruitBlock(() -> ModItems.JUJUBE.get()));
    public static final RegistryObject<Block> JUJUBE_LEAVES = BLOCKS.register("jujube_leaves",
            () -> new FruitLeavesBlock(JUJUBE, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> JUJUBE_LOG = BLOCKS.register("jujube_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_JUJUBE_SAPLING = BLOCKS.register("potted_jujube_sapling",
            () -> flowerPot(JUJUBE_SAPLING));
    public static final RegistryObject<Block> OLIVE_SAPLING = BLOCKS.register("olive_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_olive"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> OLIVE = BLOCKS.register("olive",
            () -> new FruitBlock(() -> ModItems.OLIVE.get()));
    public static final RegistryObject<Block> OLIVE_LEAVES = BLOCKS.register("olive_leaves",
            () -> new FruitLeavesBlock(OLIVE, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> OLIVE_LOG = BLOCKS.register("olive_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_OLIVE_SAPLING = BLOCKS.register("potted_olive_sapling",
            () -> flowerPot(OLIVE_SAPLING));
    public static final RegistryObject<Block> WALNUT_SAPLING = BLOCKS.register("walnut_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_walnut"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> WALNUT = BLOCKS.register("walnut",
            () -> new FruitBlock(() -> ModItems.WALNUT.get()));
    public static final RegistryObject<Block> WALNUT_LEAVES = BLOCKS.register("walnut_leaves",
            () -> new FruitLeavesBlock(WALNUT, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> WALNUT_LOG = BLOCKS.register("walnut_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_WALNUT_SAPLING = BLOCKS.register("potted_walnut_sapling",
            () -> flowerPot(WALNUT_SAPLING));
    public static final RegistryObject<Block> APRICOT_SAPLING = BLOCKS.register("apricot_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_apricot"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> APRICOT = BLOCKS.register("apricot",
            () -> new FruitBlock(() -> ModItems.APRICOT.get()));
    public static final RegistryObject<Block> APRICOT_LEAVES = BLOCKS.register("apricot_leaves",
            () -> new FruitLeavesBlock(APRICOT, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> APRICOT_LOG = BLOCKS.register("apricot_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_APRICOT_SAPLING = BLOCKS.register("potted_apricot_sapling",
            () -> flowerPot(APRICOT_SAPLING));
    public static final RegistryObject<Block> TANGERINE_SAPLING = BLOCKS.register("tangerine_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_tangerine"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> TANGERINE = BLOCKS.register("tangerine",
            () -> new FruitBlock(() -> ModItems.TANGERINE.get()));
    public static final RegistryObject<Block> TANGERINE_LEAVES = BLOCKS.register("tangerine_leaves",
            () -> new FruitLeavesBlock(TANGERINE, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> TANGERINE_LOG = BLOCKS.register("tangerine_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_TANGERINE_SAPLING = BLOCKS.register("potted_tangerine_sapling",
            () -> flowerPot(TANGERINE_SAPLING));
    public static final RegistryObject<Block> PERSIMMON_SAPLING = BLOCKS.register("persimmon_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_persimmon"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> PERSIMMON = BLOCKS.register("persimmon",
            () -> new FruitBlock(() -> ModItems.PERSIMMON.get()));
    public static final RegistryObject<Block> PERSIMMON_LEAVES = BLOCKS.register("persimmon_leaves",
            () -> new FruitLeavesBlock(PERSIMMON, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> PERSIMMON_LOG = BLOCKS.register("persimmon_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_PERSIMMON_SAPLING = BLOCKS.register("potted_persimmon_sapling",
            () -> flowerPot(PERSIMMON_SAPLING));
    public static final RegistryObject<Block> HAWTHORN_SAPLING = BLOCKS.register("hawthorn_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_hawthorn"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> HAWTHORN = BLOCKS.register("hawthorn",
            () -> new FruitBlock(() -> ModItems.HAWTHORN.get()));
    public static final RegistryObject<Block> HAWTHORN_LEAVES = BLOCKS.register("hawthorn_leaves",
            () -> new FruitLeavesBlock(HAWTHORN, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> HAWTHORN_LOG = BLOCKS.register("hawthorn_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_HAWTHORN_SAPLING = BLOCKS.register("potted_hawthorn_sapling",
            () -> flowerPot(HAWTHORN_SAPLING));
    public static final RegistryObject<Block> PEACH_SAPLING = BLOCKS.register("peach_sapling",
            () -> new SaplingBlock(new FruitTreeGrower("patch_peach"), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> PEACH = BLOCKS.register("peach",
            () -> new FruitBlock(() -> ModItems.PEACH.get()));
    public static final RegistryObject<Block> PEACH_LEAVES = BLOCKS.register("peach_leaves",
            () -> new FruitLeavesBlock(PEACH, BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> PEACH_LOG = BLOCKS.register("peach_log",
            FruitLogBlock::new);
    public static final RegistryObject<Block> POTTED_PEACH_SAPLING = BLOCKS.register("potted_peach_sapling",
            () -> flowerPot(PEACH_SAPLING));

    //Decorations
    public static final RegistryObject<Block> LOTUS_FLOWER = BLOCKS.register("lotus_flower",
            () -> new LotusFlowerBlock(BlockBehaviour.Properties.copy(Blocks.LILY_PAD).noCollission()));

    private static FlowerPotBlock flowerPot(Supplier<? extends Block> blockSupplier) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(Blocks.POTTED_OAK_SAPLING);
        return new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, blockSupplier, properties);
    }
}
