package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FestivalDelicacies.MOD_ID);

    public static final RegistryObject<Block> STOVE = BLOCKS.register("stove",
            () -> new StoveBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops().noOcclusion().lightLevel((state) -> {
                return state.getValue(BlockStateProperties.LIT) ? 13 : 0;
            })));
    public static final RegistryObject<Block> POT = BLOCKS.register("pot",
            () -> new PotBlock(BlockBehaviour.Properties.of(Material.METAL).strength(1.0F).noOcclusion()));

    public static final RegistryObject<Block> CHINESE_CABBAGE = BLOCKS.register("chinese_cabbage",
            () -> new ChineseCabbageBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> GARLIC = BLOCKS.register("garlic",
            () -> new GarlicBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> FENNEL = BLOCKS.register("fennel",
            () -> new FennelBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> GARLIC_CHIVE = BLOCKS.register("garlic_chive",
            () -> new GarlicChiveBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> EGGPLANT = BLOCKS.register("eggplant",
            () -> new EggplantBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> GREEN_ONION = BLOCKS.register("greenonion",
            () -> new GreenOnionBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
}
