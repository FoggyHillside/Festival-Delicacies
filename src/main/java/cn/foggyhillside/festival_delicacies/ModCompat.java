package cn.foggyhillside.festival_delicacies;

import net.minecraftforge.fml.ModList;

public class ModCompat {
    public static boolean isFarmersDelightLoaded() {
        return ModList.get().isLoaded("farmersdelight");
    }

}
