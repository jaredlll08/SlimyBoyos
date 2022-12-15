package com.blamejared.slimyboyos.client;

import com.blamejared.slimyboyos.api.IAbsorber;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class SlimeItemLayer<T extends Entity> extends RenderLayer<T, SlimeModel<T>> {
    
    public SlimeItemLayer(RenderLayerParent<T, SlimeModel<T>> renderer) {
        
        super(renderer);
    }
    
    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        
        if(entity.isAlive() && !entity.isInvisible()) {
            
            if(entity instanceof IAbsorber abs) {
                ItemStack stack = abs.slimyboyos$getAbsorbedItem();
                if(stack.isEmpty()) {
                    return;
                }
                if(!stack.isEmpty()) {
                    poseStack.pushPose();
                    poseStack.mulPose(new Quaternionf().rotateX(Mth.PI));
                    poseStack.translate(0, -1, 0);
                    poseStack.mulPose(new Quaternionf().rotateX(Mth.PI / 2f));
                    poseStack.translate(0, -(4 * 0.0626), 0);
                    poseStack.translate(0, 0, -0.0626 / 4);
                    poseStack.mulPose(new Quaternionf().rotateY(Mth.PI / 2f));
                    Minecraft.getInstance()
                            .getItemRenderer()
                            .renderStatic(stack, ItemTransforms.TransformType.GROUND, i, OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, (int) entity.blockPosition()
                                    .asLong());
                    poseStack.popPose();
                }
                
            }
        }
    }
    
}
