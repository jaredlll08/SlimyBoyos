package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.capability.*;
import com.blamejared.slimyboyos.network.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;


public class CommonEventHandler {
    
    public static final ResourceLocation SLIMES = new ResourceLocation("forge:slimes");
    
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        
        LivingEntity living = event.getEntityLiving();
        if(living.world.isRemote || !living.isAlive() || !living.world.getGameRules()
                .getBoolean(GameRules.MOB_GRIEFING)) {
            return;
        }
        
        living.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
            if(!slimeAbsorption.getAbsorbedStack().isEmpty()) {
                return;
            }
            AxisAlignedBB bb = living.getBoundingBox();
            List<ItemEntity> list = living.world.getEntitiesWithinAABB(ItemEntity.class, bb,
                    item -> item.isAlive() && !item.cannotPickup() && !item.getItem().isEmpty());
            if(!list.isEmpty()) {
                ItemEntity item = list.get(0);
                ItemStack stack = item.getItem();
                
                ItemStack absorbedStack = stack.split(1);
                slimeAbsorption.setAbsorbedStack(absorbedStack);
                PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(event::getEntity),
                        new MessageItemPickup(item.getEntityId(), living.getEntityId(), absorbedStack.copy()));
                
                if(stack.isEmpty()) {
                    item.remove();
                }
            }
        });
    }
    
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        
        LivingEntity living = event.getEntityLiving();
        if(living.world.isRemote || !living.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
            return;
        }
        
        living.getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(slimeAbsorption -> {
            ItemStack stack = slimeAbsorption.getAbsorbedStack();
            if(!stack.isEmpty()) {
                World world = living.world;
                ItemEntity item = new ItemEntity(world, living.getPosX(), living.getPosY(), living.getPosZ(),
                        stack.copy());
                item.setDefaultPickupDelay();
                event.getDrops().add(item);
            }
        });
    }
    
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        
        Entity e = event.getObject();
        if(e.getType().getTags().contains(SLIMES)) {
            event.addCapability(SlimeAbsorption.Provider.NAME, new SlimeAbsorption.Provider());
        }
    }
    
    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking event) {
        
        if(!(event.getPlayer() instanceof ServerPlayerEntity) || !event.getTarget().isAlive()) {
            return;
        }
        
        event.getTarget().getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION).ifPresent(
                slimeAbsorption -> PacketHandler.CHANNEL.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getPlayer()),
                        new MessageItemSync(event.getTarget().getEntityId(), slimeAbsorption.getAbsorbedStack())
                )
        );
    }
    
    @SubscribeEvent
    public void onCheckDespawn(LivingSpawnEvent.AllowDespawn event) {
        
        event.getEntity().getCapability(SlimeAbsorptionCapability.SLIME_ABSORPTION)
                .lazyMap(SlimeAbsorption::getAbsorbedStack)
                .filter(stack -> !stack.isEmpty())
                .ifPresent(stack -> event.setResult(Event.Result.DENY));
    }
    
}
