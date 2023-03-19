package com.blamejared.slimyboyos.api;

import net.minecraft.world.item.ItemStack;

public interface IAbsorber {
    
    ItemStack slimyboyos$getAbsorbedItem();
    
    void slimyboyos$setAbsorbedItem(ItemStack stack);
    
}
