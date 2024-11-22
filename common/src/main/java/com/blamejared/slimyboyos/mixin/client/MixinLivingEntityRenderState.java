package com.blamejared.slimyboyos.mixin.client;

import com.blamejared.slimyboyos.api.IAbsorberRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class MixinLivingEntityRenderState implements IAbsorberRenderState {
    
    @Unique
    public ItemStack slimyboyos$absorbed;
    @Unique
    public BakedModel slimyboyos$absorbedModel;
    
    @Unique
    public int slimyboyos$id;
    
    @Override
    public BakedModel slimyboyos$getAbsorbedItemModel() {
        
        return slimyboyos$absorbedModel;
    }
    
    @Override
    public void slimyboyos$setAbsorbedItemModel(BakedModel stack) {
        
        this.slimyboyos$absorbedModel = stack;
    }
    
    @Override
    public ItemStack slimyboyos$getAbsorbedItem() {
        
        return this.slimyboyos$absorbed;
    }
    
    @Override
    public void slimyboyos$setAbsorbedItem(ItemStack stack) {
        
        this.slimyboyos$absorbed = stack;
    }
    
    @Override
    public int slimyboyos$getId() {
        
        return this.slimyboyos$id;
    }
    
    @Override
    public void slimyboyos$setId(int id) {
        
        this.slimyboyos$id = id;
    }
    
}
