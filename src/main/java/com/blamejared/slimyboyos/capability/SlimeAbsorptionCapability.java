package com.blamejared.slimyboyos.capability;

import com.blamejared.slimyboyos.SlimyBoyos;
import com.blamejared.slimyboyos.network.MessageItemSync;
import com.blamejared.slimyboyos.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.network.PacketDistributor;


public class SlimeAbsorptionCapability {

    @CapabilityInject(SlimeAbsorption.class)
    public static Capability<SlimeAbsorption> SLIME_ABSORPTION;

    public static void init() {
        CapabilityManager.INSTANCE.register(SlimeAbsorption.class, new Capability.IStorage<SlimeAbsorption>() {
                    @Override
                    public INBT writeNBT(Capability<SlimeAbsorption> capability, SlimeAbsorption instance,
                                         Direction side) {
                        return instance.serializeNBT();
                    }

                    @Override
                    public void readNBT(Capability<SlimeAbsorption> capability, SlimeAbsorption instance,
                                        Direction side, INBT nbt) {
                        if (nbt instanceof CompoundNBT) {
                            instance.deserializeNBT((CompoundNBT) nbt);
                        }
                    }
                },
                SlimeAbsorption.Impl::new);

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, SlimeAbsorptionCapability::attachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(SlimeAbsorptionCapability::onStartTracking);
    }

    private static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity e = event.getObject();
        if (e.getType().getTags().contains(SlimyBoyos.SLIMES)) {
            event.addCapability(SlimeAbsorption.Provider.NAME, new SlimeAbsorption.Provider());
        }
    }

    private static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity) || !event.getTarget().isAlive()) {
            return;
        }

        event.getTarget().getCapability(SLIME_ABSORPTION).ifPresent(
                slimeAbsorption -> PacketHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                        new MessageItemSync(event.getTarget().getEntityId(), slimeAbsorption.getAbsorbedStack())
                )
        );
    }
}
