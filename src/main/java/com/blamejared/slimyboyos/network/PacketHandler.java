package com.blamejared.slimyboyos.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("slimyboyos:main"),
            () -> "3.1.0", "3.1.0"::equals, "3.1.0"::equals);

    private static int ID = 0;

    public static void init() {
        CHANNEL.registerMessage(
                ID++,
                MessageItemPickup.class,
                (msg, pb) -> {
                    pb.writeVarInt(msg.collectedItemEntityId);
                    pb.writeVarInt(msg.collectorEntityId);
                    pb.writeItemStack(msg.absorbedStack);
                },
                pb -> new MessageItemPickup(pb.readVarInt(), pb.readVarInt(), pb.readItemStack()),
                (msg, ctx) -> ClientPacketHandler.handleItemPickup(msg, ctx)
        );
        CHANNEL.registerMessage(
                ID++,
                MessageItemSync.class,
                (msg, pb) -> {
                    pb.writeVarInt(msg.entityId);
                    pb.writeItemStack(msg.absorbedStack);
                },
                pb -> new MessageItemSync(pb.readVarInt(), pb.readItemStack()),
                (msg, ctx) -> ClientPacketHandler.handleItemSync(msg, ctx)
        );
    }
}
