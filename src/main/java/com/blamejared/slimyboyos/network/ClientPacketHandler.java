package com.blamejared.slimyboyos.network;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    static void handleItemPickup(MessageItemPickup msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            World w = mc.world;
            if (w == null) {
                return;
            }

            Entity collected = w.getEntityByID(msg.collectedItemEntityId);
            Entity collector = w.getEntityByID(msg.collectorEntityId);

            if (collector != null && collected instanceof ItemEntity) {
                ItemEntity item = (ItemEntity) collected;

                w.playSound(item.getPosX(), item.getPosY(), item.getPosZ(),
                        SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS,
                        0.2F, (w.rand.nextFloat() - w.rand.nextFloat()) * 1.4F + 2.0F, false);
                mc.particles.addEffect(new ItemPickupParticle(mc.getRenderManager(),
                        mc.getRenderTypeBuffers(), mc.world, item, collector));

                collector.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION)
                        .ifPresent(slimeAbsorption -> slimeAbsorption.setAbsorbedStack(msg.absorbedStack));
            }
        });
    }

    static void handleItemSync(MessageItemSync msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
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
        });
    }
}
