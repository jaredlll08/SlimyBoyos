package com.blamejared.slimyboyos;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import com.blamejared.slimyboyos.client.render.SlimeItemLayer;
import com.blamejared.slimyboyos.events.CommonEventHandler;
import com.blamejared.slimyboyos.network.MessageItemPickup;
import com.blamejared.slimyboyos.network.MessageItemSync;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onTagsUpdated(TagsUpdatedEvent event) {
        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        ForgeRegistries.ENTITIES.getValues().stream()
                .filter(entityType -> entityType.getTags().contains(CommonEventHandler.SLIMES))
                .map(manager.renderers::get)
                .filter(entityRenderer -> entityRenderer instanceof MobRenderer)
                .map(entityRenderer -> (MobRenderer<?, ?>) entityRenderer)
                .forEach(mobRenderer -> mobRenderer.addLayer(new SlimeItemLayer(mobRenderer)));
    }

    public void handleItemPickup(MessageItemPickup msg) {
        Minecraft mc = Minecraft.getInstance();
        ClientWorld w = mc.world;
        if (w == null) {
            return;
        }

        Entity collected = w.getEntityByID(msg.collectedItemEntityId);
        Entity collector = w.getEntityByID(msg.collectorEntityId);

        if (collector != null && collected instanceof ItemEntity) {
            ItemEntity item = (ItemEntity) collected;

            collector.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
                w.playSound(item.getPosX(), item.getPosY(), item.getPosZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2F, (w.rand.nextFloat() - w.rand.nextFloat()) * 1.4F + 2.0F, false);
                mc.particles.addEffect(new ItemPickupParticle(mc.getRenderManager(),
                        mc.getRenderTypeBuffers(), w, item, collector));
                slimeAbsorption.setAbsorbedStack(msg.absorbedStack);
            });
        }
    }

    public void handleItemSync(MessageItemSync msg) {
        World w = Minecraft.getInstance().world;
        if (w == null) {
            return;
        }

        Entity e = w.getEntityByID(msg.entityId);
        if (e == null) {
            return;
        }

        e.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION)
                .ifPresent(slimeAbsorption -> slimeAbsorption.setAbsorbedStack(msg.absorbedStack));
    }
}
