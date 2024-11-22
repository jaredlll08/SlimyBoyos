package com.blamejared.slimyboyos.client;

import com.blamejared.slimyboyos.api.IAbsorberRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class SlimeItemLayer extends RenderLayer<LivingEntityRenderState, SlimeModel> {
    
    private final ItemRenderer itemRenderer;
    
    public SlimeItemLayer(RenderLayerParent<LivingEntityRenderState, SlimeModel> renderer, ItemRenderer itemRenderer) {
        
        super(renderer);
        this.itemRenderer = itemRenderer;
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, LivingEntityRenderState state, float yRot, float xRot) {
        
        if(!state.isInvisible && state instanceof IAbsorberRenderState abs) {
            ItemStack stack = abs.slimyboyos$getAbsorbedItem();
            BakedModel bakedModel = abs.slimyboyos$getAbsorbedItemModel();
            if(bakedModel == null || stack.isEmpty()) {
                return;
            }
            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf().rotateX(Mth.PI));
            poseStack.translate(0, -1, 0);
            poseStack.mulPose(new Quaternionf().rotateX(Mth.PI / 2f).rotateY(abs.slimyboyos$getId() % 360));
            poseStack.translate(0, -(4 * 0.0626), 0);
            poseStack.translate(0, 0, -0.0626 / 4);
            poseStack.mulPose(new Quaternionf().rotateY(Mth.PI / 2f));
            
            this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY, bakedModel);
            poseStack.popPose();
            
        }
    }
    
    
}
