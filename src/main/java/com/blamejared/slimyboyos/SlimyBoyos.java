package com.blamejared.slimyboyos;

import com.blamejared.slimyboyos.client.render.SlimeItemLayer;
import com.blamejared.slimyboyos.events.*;
import com.blamejared.slimyboyos.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;


@Mod("slimyboyos")
public class SlimyBoyos {
    
    public SlimyBoyos() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        PacketHandler.init();
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
        
    }
    
    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
    
    
    
}
