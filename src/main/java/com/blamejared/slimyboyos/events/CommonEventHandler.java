package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.capability.SlimeAbsorption;
import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import com.blamejared.slimyboyos.network.MessageItemPickup;
import com.blamejared.slimyboyos.network.PacketHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;


public class CommonEventHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living.world.isRemote || !living.isAlive() || !living.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            return;
        }

        living.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
            if (!slimeAbsorption.getAbsorbedStack().isEmpty()) {
                return;
            }
            AxisAlignedBB bb = living.getBoundingBox();
            List<ItemEntity> list = living.world.getEntitiesWithinAABB(ItemEntity.class, bb,
                    item -> item.isAlive() && !item.cannotPickup() && !item.getItem().isEmpty());
            if (!list.isEmpty()) {
                ItemEntity item = list.get(0);
                ItemStack stack = item.getItem();

                ItemStack absorbedStack = stack.split(1);
                slimeAbsorption.setAbsorbedStack(absorbedStack);
                PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(event::getEntity),
                        new MessageItemPickup(item.getEntityId(), living.getEntityId(), absorbedStack.copy()));

                if (stack.isEmpty()) {
                    item.remove();
                }
            }
        });
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living.world.isRemote) {
            return;
        }

        living.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
            ItemStack stack = slimeAbsorption.getAbsorbedStack();
            if (!stack.isEmpty()) {
                World world = living.world;
                ItemEntity item = new ItemEntity(world, living.getPosX(), living.getPosY(), living.getPosZ(),
                        stack.copy());
                item.setDefaultPickupDelay();
                event.getDrops().add(item);
            }
        });
    }

    @SubscribeEvent
    public void onCheckDespawn(LivingSpawnEvent.AllowDespawn event) {
        event.getEntity().getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION)
                .lazyMap(SlimeAbsorption::getAbsorbedStack)
                .filter(stack -> !stack.isEmpty())
                .ifPresent(stack -> event.setResult(Event.Result.DENY));
    }
}
