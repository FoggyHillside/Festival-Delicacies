package cn.foggyhillside.festival_delicacies.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RecipeUtils {
    public RecipeUtils() {
    }

    @OnlyIn(Dist.CLIENT)
    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        } else {
            RegistryAccess registryAccess = level.registryAccess();
            return recipe.getResultItem(registryAccess);
        }
    }
}
