package com.blamejared.slimyboyos.network;

import net.minecraft.item.ItemStack;

public class MessageItemPickup {

    public final int collectedItemEntityId;
    public final int collectorEntityId;
    public final ItemStack absorbedStack;

    public MessageItemPickup(int collectedItemEntityId, int collectorEntityId, ItemStack absorbedStack) {
        this.collectedItemEntityId = collectedItemEntityId;
        this.collectorEntityId = collectorEntityId;
        this.absorbedStack = absorbedStack;
    }
}