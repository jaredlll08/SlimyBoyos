package com.blamejared.slimyboyos.network;

import net.minecraft.item.ItemStack;

public class MessageItemSync {
    
    public final ItemStack stack;
    public final int entityId;
    
    public MessageItemSync(ItemStack stack, int entityId) {
        this.stack = stack;
        this.entityId = entityId;
    }
}