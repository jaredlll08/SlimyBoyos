package com.blamejared.slimyboyos;

import com.blamejared.slimyboyos.network.PacketHandler;
import com.blamejared.slimyboyos.proxy.CommonProxy;
import com.blamejared.slimyboyos.reference.Reference;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class SlimyBoyos {
    
    @Mod.Instance(Reference.MODID)
    public static SlimyBoyos INSTANCE;
    
    
    @SidedProxy(clientSide = "com.blamejared.slimyboyos.proxy.ClientProxy", serverSide = "com.blamejared.slimyboyos.proxy.CommonProxy")
    public static CommonProxy PROXY;
    
    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        PROXY.registerEvents();
        PacketHandler.preInit();
    }
    
    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        PROXY.registerRenderers();
    }
    
    @Mod.EventHandler
    public void onFMLPostInitialization(FMLPostInitializationEvent event) {
    
    }
}
