package cn.foggyhillside.festival_delicacies.blocks.screen;

import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModeButton extends TextureButton {
    protected int mode;

    protected ModeButton(Builder builder, int pX, int pY, int pW, int pH, ResourceLocation pTexture, Supplier<Tooltip> pSupplier) {
        super(builder, pX, pY, pW, pH, pTexture, pSupplier);
    }

    protected void setMode(int pMode) {
        this.mode = pMode;
    }

    @Override
    protected int getRenderTextureY() {
        return textureY + (mode * height);
    }
}
