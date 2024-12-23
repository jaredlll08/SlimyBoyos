package com.blamejared.slimyboyos.mixin.client;

import com.blamejared.slimyboyos.api.IAbsorberRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class MixinLivingEntityRenderState implements IAbsorberRenderState {
    
    @Unique
    public final ItemStackRenderState slimyboyos$absorbed = new ItemStackRenderState();
    
    @Unique
    public int slimyboyos$id;
    
    @Override
    public ItemStackRenderState slimyboyos$getAbsorbedItemState() {
        
        return slimyboyos$absorbed;
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
