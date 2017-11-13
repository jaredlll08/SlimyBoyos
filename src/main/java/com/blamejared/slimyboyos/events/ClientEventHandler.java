package com.blamejared.slimyboyos.events;

import net.minecraftforge.common.MinecraftForge;

public class ClientEventHandler {
    
    public ClientEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
}
