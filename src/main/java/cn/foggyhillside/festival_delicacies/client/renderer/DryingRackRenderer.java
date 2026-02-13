package cn.foggyhillside.festival_delicacies.client.renderer;

import cn.foggyhillside.festival_delicacies.blocks.DryingRackBlock;
import cn.foggyhillside.festival_delicacies.blocks.entities.DryingRackEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;

public class DryingRackRenderer implements BlockEntityRenderer<DryingRackEntity> {
    ItemRenderer itemRenderer;

    public DryingRackRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    @Override
    public void render(DryingRackEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Direction direction = (pBlockEntity.getBlockState().getValue(DryingRackBlock.FACING)).getOpposite();
        Vec2[] OFFSETS;
        switch (direction) {
            case NORTH ->
                    OFFSETS = new Vec2[]{new Vec2(0.25F, 0.3125F), new Vec2(0.5F, 0.3125F), new Vec2(0.75F, 0.3125F), new Vec2(0.25F, 0.6875F), new Vec2(0.5F, 0.6875F), new Vec2(0.75F, 0.6875F)};
            case EAST ->
                    OFFSETS = new Vec2[]{new Vec2(0.6875F, 0.25F), new Vec2(0.6875F, 0.5F), new Vec2(0.6875F, 0.75F), new Vec2(0.3125F, 0.25F), new Vec2(0.3125F, 0.5F), new Vec2(0.3125F, 0.75F)};
            case WEST ->
                    OFFSETS = new Vec2[]{new Vec2(0.3125F, 0.75F), new Vec2(0.3125F, 0.5F), new Vec2(0.3125F, 0.25F), new Vec2(0.6875F, 0.75F), new Vec2(0.6875F, 0.5F), new Vec2(0.6875F, 0.25F)};
            default ->
                    OFFSETS = new Vec2[]{new Vec2(0.75F, 0.6875F), new Vec2(0.5F, 0.6875F), new Vec2(0.25F, 0.6875F), new Vec2(0.75F, 0.3125F), new Vec2(0.5F, 0.3125F), new Vec2(0.25F, 0.3125F)};
        }

        ItemStackHandler inventory = pBlockEntity.getInventory();

        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack.isEmpty()) {
                return;
            }
            pPoseStack.pushPose();
            if (i < 6) {
                pPoseStack.translate(OFFSETS[i].x, 0.9F, OFFSETS[i].y);
            } else {
                pPoseStack.translate(OFFSETS[i - 6].x, 0.4F, OFFSETS[i - 6].y);
            }
            pPoseStack.scale(0.25F, 0.25F, 0.25F);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90F));
            BakedModel bakedModel = itemRenderer.getModel(stack, pBlockEntity.getLevel(), null, 0);
            itemRenderer.render(stack, ItemDisplayContext.FIXED, true, pPoseStack, pBuffer, pPackedLight, pPackedOverlay, bakedModel);
            pPoseStack.popPose();
        }
    }
}
