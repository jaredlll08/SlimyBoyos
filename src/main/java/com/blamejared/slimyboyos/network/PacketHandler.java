package com.blamejared.slimyboyos.network;

import net.minecraft.client.Minecraft;
import net.minecraft.util.*;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    
    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("slimyboyos:main"), () -> "3.0.0", "3.0.0"::equals, "3.0.0"::equals);
    
    private static int ID = 0;
    
    public static void init() {
        CHANNEL.registerMessage(ID++, MessageItemSync.class, (messageItemSync, packetBuffer) -> {
            packetBuffer.writeItemStack(messageItemSync.stack);
            packetBuffer.writeInt(messageItemSync.entityId);
        }, packetBuffer -> new MessageItemSync(packetBuffer.readItemStack(), packetBuffer.readInt()), (messageItemSync, contextSupplier) -> contextSupplier.get().enqueueWork(() -> {
            Minecraft.getInstance().world.getEntityByID(messageItemSync.entityId).getPersistentData().put("AbsorbedItem",messageItemSync.stack.serializeNBT());
        }));
    }
    
    
}
