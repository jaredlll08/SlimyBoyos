package com.blamejared.slimyboyos.mixin.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface AccessLivingEntityRenderer {
    
    @Invoker("addLayer")
    <T extends Entity, M extends EntityModel<T>> boolean slimyboyos$callAddLayer(RenderLayer<T, M> layer);
    
}
