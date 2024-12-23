package com.blamejared.slimyboyos.api;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IAbsorberRenderState {
    
    ItemStackRenderState slimyboyos$getAbsorbedItemState();
    
    int slimyboyos$getId();
    
    void slimyboyos$setId(int id);
    
}
