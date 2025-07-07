package cn.foggyhillside.festival_delicacies.blocks.screen;

import cn.foggyhillside.festival_delicacies.FestivalDelicacies;
import cn.foggyhillside.festival_delicacies.blocks.StoveBlock;
import cn.foggyhillside.festival_delicacies.network.ModNetwork;
import cn.foggyhillside.festival_delicacies.network.packet.ServerCookingTypeButtonPacket;
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

public class StoveScreen extends AbstractContainerScreen<StoveMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(FestivalDelicacies.MODID, "textures/gui/stove_gui.png");

    public ModeButton cookingTypeButton;
    public ModeButton startButton;
    public ModeButton redstoneButton;

    public StoveScreen(StoveMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        this.cookingTypeButton = new ModeButton(new Button.Builder(Component.translatable("gui." + FestivalDelicacies.MODID + ".cooking_type_button"), pButton -> {
            if (menu.entity.getBlockState().getValue(StoveBlock.LIT)) {
                ModNetwork.sendToServer(new ServerCookingTypeButtonPacket(this.getMenu().entity.getBlockPos()));
            }
        }).pos(((width - imageWidth) / 2) + 91, ((height - imageHeight) / 2) + 55).size(19, 17), 176, 35, 256, 256, TEXTURE, () ->
                switch (cookingTypeButton.mode) {
                    case 0 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.not_lit"));
                    case 1 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.default"));
                    case 2 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.steam"));
                    case 3 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.fry"));
                    case 4 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.bake"));
                    case 5 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.default_dumpling"));
                    case 6 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.steamed_dumpling"));
                    case 7 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".stove.button.baozi"));
                    default -> throw new IllegalStateException("Unexpected value: " + cookingTypeButton.mode);
                });
        this.startButton = new ModeButton(new Button.Builder(Component.translatable("gui." + FestivalDelicacies.MODID + ".start_button"), pButton -> {
            ModNetwork.sendToServer(new ServerStartButtonPacket(this.getMenu().entity.getBlockPos()));
        }).pos(((width - imageWidth) / 2) + 6, ((height - imageHeight) / 2) + 34).size(19, 17), 0, 166, 256, 256, TEXTURE, () ->
                switch (startButton.mode) {
                    case 0 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.not_started"));
                    case 1 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.started"));
                    default -> throw new IllegalStateException("Unexpected value: " + startButton.mode);
                });
        this.redstoneButton = new ModeButton(new Button.Builder(Component.translatable("gui." + FestivalDelicacies.MODID + ".redstone_button"), pButton -> {
            ModNetwork.sendToServer(new ServerRedstoneButtonPacket(this.getMenu().entity.getBlockPos()));
        }).pos(((width - imageWidth) / 2) + 8, ((height - imageHeight) / 2) + 55).size(15, 14), 0, 200, 256, 256, TEXTURE, () ->
                switch (redstoneButton.mode) {
                    case 0 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.no_redstone"));
                    case 1 ->
                            Tooltip.create(Component.translatable("gui." + FestivalDelicacies.MODID + ".button.redstone"));
                    default -> throw new IllegalStateException("Unexpected value: " + redstoneButton.mode);
                });
        this.addWidget(this.cookingTypeButton);
        this.addWidget(this.startButton);
        this.addWidget(this.redstoneButton);
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i1, int i2) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(guiGraphics, x, y);
        renderContainerSlot(guiGraphics, x, y);
    }

    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 90, y + 35, 176, 0, menu.getScaledProgress(), 17);
        }
    }

    private void renderContainerSlot(GuiGraphics guiGraphics, int x, int y) {
        if (menu.entity.inventory.getStackInSlot(9) == ItemStack.EMPTY) {
            int type = menu.entity.getBlockState().getValue(StoveBlock.COOKING_TYPE);
            if (type > 4 && type < 8) {
                guiGraphics.blit(TEXTURE, x + 92, y + 16, 176, 17, 18, 18);
            } else {
                guiGraphics.blit(TEXTURE, x + 92, y + 16, 194, 17, 18, 18);
            }
        }
    }

    @Override
    public void render(GuiGraphics poseStack, int mouseX, int mouseY, float f) {
        if (menu.entity.getBlockState().getValue(StoveBlock.LIT)) {
            cookingTypeButton.setMode(menu.entity.getBlockState().getValue(StoveBlock.COOKING_TYPE));
        } else {
            cookingTypeButton.setMode(0);
        }
        if (menu.entity.getBlockState().getValue(StoveBlock.LIT) && cookingTypeButton.mode >= 5 && cookingTypeButton.mode <= 7) {
            if (!startButton.visible) {
                startButton.visible = true;
                redstoneButton.visible = true;
            }
        } else {
            if (startButton.visible) {
                startButton.visible = false;
                redstoneButton.visible = false;
            }
        }
        startButton.setMode(menu.entity.getBlockState().getValue(StoveBlock.START_BUTTON));
        redstoneButton.setMode(menu.entity.getBlockState().getValue(StoveBlock.RED_STONE));
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, f);
        renderTooltip(poseStack, mouseX, mouseY);
        this.cookingTypeButton.render(poseStack, mouseX, mouseY, f);
        this.startButton.render(poseStack, mouseX, mouseY, f);
        this.redstoneButton.render(poseStack, mouseX, mouseY, f);
    }

}
