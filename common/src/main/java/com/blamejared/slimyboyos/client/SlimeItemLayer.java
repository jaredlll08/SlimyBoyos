package com.blamejared.slimyboyos.client;

import com.blamejared.slimyboyos.api.IAbsorberRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;

public class SlimeItemLayer extends RenderLayer<LivingEntityRenderState, SlimeModel> {
    
    public SlimeItemLayer(RenderLayerParent<LivingEntityRenderState, SlimeModel> renderer) {
        
        super(renderer);
    }
    
    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int i, LivingEntityRenderState state, float yRot, float xRot) {
        
        if(!state.isInvisible && state instanceof IAbsorberRenderState abs) {
            ItemStackRenderState itemState = abs.slimyboyos$getAbsorbedItemState();
            if(itemState.isEmpty()) {
                return;
            }
            poseStack.pushPose();
            poseStack.mulPose(new Quaternionf().rotateX(Mth.PI));
            poseStack.translate(0, -1, 0);
            poseStack.mulPose(new Quaternionf().rotateX(Mth.PI / 2f).rotateY(abs.slimyboyos$getId() % 360));
            poseStack.translate(0, -(4 * 0.0626), 0);
            poseStack.translate(0, 0, -0.0626 / 4);
            poseStack.mulPose(new Quaternionf().rotateY(Mth.PI / 2f));
            itemState.submit(poseStack, collector, i, OverlayTexture.NO_OVERLAY, state.outlineColor);
            poseStack.popPose();
            
        }
    }
    
}
