package com.blamejared.slimyboyos.network;

import net.minecraft.item.ItemStack;

public class MessageItemSync {

    public final int entityId;
    public final ItemStack absorbedStack;

    public MessageItemSync(int entityId, ItemStack absorbedStack) {
        this.entityId = entityId;
        this.absorbedStack = absorbedStack;
    }
}
