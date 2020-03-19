package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.client.render.SlimeItemLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientEventHandler {
    
    @SubscribeEvent
    public void recipeUpdated(RecipesUpdatedEvent e) {
        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        ForgeRegistries.ENTITIES.getValues().stream().filter(entityType -> entityType.getTags().contains(new ResourceLocation("forge:slimes"))).map(manager.renderers::get).filter(entityRenderer -> entityRenderer instanceof MobRenderer).map(entityRenderer -> (MobRenderer) entityRenderer).forEach(mobRenderer -> {
            mobRenderer.addLayer(new SlimeItemLayer(mobRenderer));
        });
    }
}
