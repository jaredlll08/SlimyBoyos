package com.blamejared.slimyboyos.mixin.client;

import com.blamejared.slimyboyos.api.IAbsorber;
import com.blamejared.slimyboyos.api.IAbsorberRenderState;
import com.blamejared.slimyboyos.client.SlimeItemLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {
    
    @Shadow
    @Final
    protected ItemModelResolver itemModelResolver;
    
    protected MixinLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        
        super($$0);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    public void slimyboyos$init(EntityRendererProvider.Context $$0, EntityModel $$1, float $$2, CallbackInfo ci) {
        
        ((AccessLivingEntityRenderer) this).slimyboyos$callAddLayer(new SlimeItemLayer((LivingEntityRenderer) (Object) this));
    }
    
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    public void slimyboyos$extractRenderState(T entity, S state, float $$2, CallbackInfo ci) {
        
        if(entity instanceof IAbsorber entAbs && state instanceof IAbsorberRenderState stateAbs) {
            this.itemModelResolver.updateForLiving(stateAbs.slimyboyos$getAbsorbedItemState(), entAbs.slimyboyos$getAbsorbedItem(), ItemDisplayContext.GROUND, entity);
            stateAbs.slimyboyos$setId(entity.getId());
        }
    }
    
}
