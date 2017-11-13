package com.blamejared.slimyboyos.proxy;

import com.blamejared.slimyboyos.client.render.RenderSlime;
import com.blamejared.slimyboyos.events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.*;

public class ClientProxy extends CommonProxy {
    
    
    @Override
    public void registerRenderers() {
        super.registerRenderers();
        
        Map<Class<? extends Entity>, Render<? extends Entity>> render = new HashMap<>();
        RenderingRegistry.registerEntityRenderingHandler(EntitySlime.class, new RenderSlime(Minecraft.getMinecraft().getRenderManager()));
        RenderingRegistry.loadEntityRenderers(render);
    }
    
    @Override
    public void registerEvents() {
        super.registerEvents();
        new ClientEventHandler();
    }
}
