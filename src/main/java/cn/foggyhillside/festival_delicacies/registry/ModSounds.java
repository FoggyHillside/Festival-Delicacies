package cn.foggyhillside.festival_delicacies.registry;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, FestivalDelicacies.MODID);

    public static final RegistryObject<SoundEvent> CHOP = SOUNDS.register("block.chop", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(FestivalDelicacies.MODID, "block.chop")));

}
