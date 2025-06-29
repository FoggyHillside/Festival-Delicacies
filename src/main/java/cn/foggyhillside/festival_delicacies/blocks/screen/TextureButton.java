package cn.foggyhillside.festival_delicacies.blocks.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TextureButton extends Button {
    protected int textureX;
    protected int textureY;
    protected final int textureWidth;
    protected final int textureHeight;
    protected ResourceLocation texture;
    protected Supplier<Tooltip> tooltipSupplier;

    protected TextureButton(Builder builder, int pX, int pY, int pW, int pH, ResourceLocation pTexture, Supplier<Tooltip> pSupplier) {
        super(builder);
        this.textureX = pX;
        this.textureY = pY;
        this.textureWidth = pW;
        this.textureHeight = pH;
        this.texture = pTexture;
        this.tooltipSupplier = pSupplier;
    }

    protected int getRenderTextureX() {
        if (isHovered) {
            return textureX + width;
        } else {
            return textureX;
        }
    }

    @Override
    public void onPress() {
        super.onPress();
    }

    protected int getRenderTextureY() {
        return textureY;
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.setTooltip(tooltipSupplier.get());
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableDepthTest();
        pGuiGraphics.blit(texture, this.getX(), this.getY(), getRenderTextureX(), getRenderTextureY(), this.width, this.height, this.textureWidth, this.textureHeight);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}

