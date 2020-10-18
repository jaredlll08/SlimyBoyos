package com.blamejared.slimyboyos.network;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
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
                (msg, ctx) -> ctx.get().enqueueWork(() -> {
                    Minecraft mc = Minecraft.getInstance();
                    ClientWorld w = mc.world;
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
                }));
        CHANNEL.registerMessage(
                ID++,
                MessageItemSync.class,
                (msg, pb) -> {
                    pb.writeVarInt(msg.entityId);
                    pb.writeItemStack(msg.absorbedStack);
                },
                pb -> new MessageItemSync(pb.readVarInt(), pb.readItemStack()),
                (msg, ctx) -> ctx.get().enqueueWork(() -> {
                    Entity e = Minecraft.getInstance().world.getEntityByID(msg.entityId);

                    if (e != null) {
                        e.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION)
                                .ifPresent(slimeAbsorption -> slimeAbsorption.setAbsorbedStack(msg.absorbedStack));
                    }
                }));
    }


}
