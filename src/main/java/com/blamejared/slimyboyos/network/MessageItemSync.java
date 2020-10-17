package com.blamejared.slimyboyos.network;

public class MessageItemSync {

    public final int collectedItemEntityId;
    public final int collectorEntityId;
    public final int collectedAmount;

    public MessageItemSync(int collectedItemEntityId, int collectorEntityId, int collectedAmount) {
        this.collectedItemEntityId = collectedItemEntityId;
        this.collectorEntityId = collectorEntityId;
        this.collectedAmount = collectedAmount;
    }
}