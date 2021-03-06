package com.blamejared.slimyboyos.client.render;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.SlimeModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class SlimeItemLayer<T extends Entity> extends LayerRenderer<T, SlimeModel<T>> {
    
    public SlimeItemLayer(IEntityRenderer<T, SlimeModel<T>> renderer) {
        
        super(renderer);
    }
    
    public void render(MatrixStack stack, IRenderTypeBuffer type, int p_225628_3_, T entity, float p_225628_5_,
                       float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        
        if(entity.isAlive() && !entity.isInvisible()) {
            entity.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
                ItemStack itemStack = slimeAbsorption.getAbsorbedStack();
                if(!itemStack.isEmpty()) {
                    stack.push();
                    stack.rotate(Vector3f.XP.rotationDegrees(180));
                    stack.translate(0, -1, 0);
                    stack.rotate(Vector3f.XP.rotationDegrees(90));
                    stack.translate(0, -(4 * 0.0626), 0);
                    stack.translate(0, 0, -0.0626 / 4);
                    stack.rotate(Vector3f.YP.rotationDegrees(90));
                    Minecraft.getInstance().getItemRenderer().renderItem(itemStack,
                            ItemCameraTransforms.TransformType.GROUND, p_225628_3_, OverlayTexture.NO_OVERLAY, stack,
                            type);
                    stack.pop();
                }
            });
        }
    }
    
}
