package com.blamejared.slimyboyos.network;

import com.blamejared.slimyboyos.reference.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(Reference.MODID);
    public static int ID = 0;
    
    public static void preInit() {
        INSTANCE.registerMessage(MessageEntitySync.class, MessageEntitySync.class, ID++, Side.CLIENT);
    }
}
