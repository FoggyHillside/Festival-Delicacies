package cn.foggyhillside.festival_delicacies.blocks.screen;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.FermentationJarBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.FermentationJarEntity;
import cn.foggyhillside.festival_delicacies.network.ModNetwork;
import cn.foggyhillside.festival_delicacies.network.packet.ServerRedstoneButtonPacket;
import cn.foggyhillside.festival_delicacies.network.packet.ServerStartButtonPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class FermentationJarScreen extends AbstractContainerScreen<FermentationJarMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FestivalDelicacies.MODID, "textures/gui/fermentation_jar_gui.png");

    public ModeButton startButton;
    public ModeButton redstoneButton;

    public FermentationJarScreen(FermentationJarMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.titleLabelX = 28;
        this.imageHeight = 173;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        this.startButton = new ModeButton(new Button.Builder(Component.translatable("gui." + FestivalDelicacies.MODID + ".start_button"), pButton -> {
            ModNetwork.sendToServer(new ServerStartButtonPacket(this.getMenu().entity.getBlockPos()));
        }).pos(((width - imageWidth) / 2) + 123, ((height - imageHeight) / 2) + 18).size(19, 17), 176, 18, 256, 256, TEXTURE, () ->
                switch (startButton.mode) {
                    case 0 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.not_started"));
                    case 1 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.started"));
                    default -> throw new IllegalStateException("Unexpected value: " + startButton.mode);
                });
        this.redstoneButton = new ModeButton(new Button.Builder(Component.translatable("gui." + FestivalDelicacies.MODID + ".redstone_button"), pButton -> {
            ModNetwork.sendToServer(new ServerRedstoneButtonPacket(this.getMenu().entity.getBlockPos()));
        }).pos(((width - imageWidth) / 2) + 27, ((height - imageHeight) / 2) + 40).size(15, 14), 176, 52, 256, 256, TEXTURE, () ->
                switch (redstoneButton.mode) {
                    case 0 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.no_redstone"));
                    case 1 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.redstone"));
                    default -> throw new IllegalStateException("Unexpected value: " + redstoneButton.mode);
                });
        this.addWidget(this.startButton);
        this.addWidget(this.redstoneButton);
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        pGuiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(pGuiGraphics, x, y);
        renderContainerSlot(pGuiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int scaledProgress = menu.getScaledProgress();
            guiGraphics.blit(TEXTURE, x + 97, y + 40 + (36 - scaledProgress), 176, 116 - scaledProgress, 5, scaledProgress);
        }
    }

    private void renderContainerSlot(GuiGraphics guiGraphics, int x, int y) {
        if (menu.entity.getInventory().getStackInSlot(FermentationJarEntity.SLOTS - 2) == ItemStack.EMPTY) {
            guiGraphics.blit(TEXTURE, x + 111, y + 48, 176, 0, 18, 18);
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        startButton.setMode(menu.entity.getBlockState().getValue(FermentationJarBlock.START_BUTTON));
        redstoneButton.setMode(menu.entity.getBlockState().getValue(FermentationJarBlock.RED_STONE));
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
        this.startButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.redstoneButton.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
