package com.blamejared.slimyboyos.api;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface IAbsorberRenderState {
    
    @Nullable
    BakedModel slimyboyos$getAbsorbedItemModel();
    
    void slimyboyos$setAbsorbedItemModel(BakedModel stack);
    
    ItemStack slimyboyos$getAbsorbedItem();
    
    void slimyboyos$setAbsorbedItem(ItemStack stack);
    
    int slimyboyos$getId();
    
    void slimyboyos$setId(int id);
    
}
