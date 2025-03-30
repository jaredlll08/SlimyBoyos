package com.blamejared.slimyboyos.api;

import net.minecraft.client.renderer.item.ItemStackRenderState;

public interface IAbsorberRenderState {
    
    ItemStackRenderState slimyboyos$getAbsorbedItemState();
    
    int slimyboyos$getId();
    
    void slimyboyos$setId(int id);
    
}
