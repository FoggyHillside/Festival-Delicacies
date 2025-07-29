package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FestivalDelicacies.MODID);

    public static final RegistryObject<SoundEvent> CHOP = register("block.chop");
    public static final RegistryObject<SoundEvent> FRUIT_PICK = register("block.fruit_pick");
    public static final RegistryObject<SoundEvent> FERMENTATION_JAR_OPEN = register("block.fermentation_jar.open");
    public static final RegistryObject<SoundEvent> FERMENTATION_JAR_CLOSE = register("block.fermentation_jar.close");

    private static RegistryObject<SoundEvent> register(String path) {
        return SOUNDS.register(path, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FestivalDelicacies.MODID, path)));
    }
}
