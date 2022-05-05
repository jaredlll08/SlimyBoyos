package com.blamejared.slimyboyos.mixin.client;

import com.blamejared.slimyboyos.client.SlimeItemLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends Entity> extends EntityRenderer<T> {
    
    protected MixinLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        
        super($$0);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    public void slimyboyos$init(EntityRendererProvider.Context $$0, EntityModel<? extends Entity> $$1, float $$2, CallbackInfo ci) {
        
        ((AccessLivingEntityRenderer) this).slimyboyos$callAddLayer(new SlimeItemLayer<>(((LivingEntityRenderer) (Object) this)));
    }
    
}
