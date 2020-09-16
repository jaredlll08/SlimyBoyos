package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.network.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;


public class CommonEventHandler {
    
    public static final ResourceLocation SLIMES = new ResourceLocation("forge:slimes");
    
    public CommonEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity().world.isRemote) {
            return;
        }
        
        if(event.getEntity().getType().getTags().contains(SLIMES)) {
            if(!event.getEntityLiving().isAlive()) {
                return;
            }
            CompoundNBT data = event.getEntityLiving().getPersistentData();
            if(data.contains("AbsorbedItem")) {
                return;
            }
            AxisAlignedBB bb = event.getEntity().getBoundingBox();
            List<ItemEntity> list = event.getEntity().world.getEntitiesWithinAABB(ItemEntity.class, bb);
            if(!list.isEmpty()) {
                if(list.get(0).cannotPickup()) {
                    return;
                }
                ItemStack newItem = list.get(0).getItem().copy();
                newItem.setCount(1);
                data.put("AbsorbedItem", newItem.serializeNBT());
                PacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY(), event.getEntityLiving().getPosZ(), 128, event.getEntityLiving().world.getDimensionKey())), new MessageItemSync(newItem, event.getEntity().getEntityId()));//sendToAllAround(new MessageEntitySync((EntitySlime) event.getEntityLiving()), new NetworkRegistry.TargetPoint(event.getEntity().world.provider.getDimension(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, 128D));
                list.get(0).getItem().shrink(1);
                if(list.get(0).getItem().getCount() <= 0) {
                    list.get(0).remove();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if(event.getEntity().world.isRemote) {
            return;
        }
        if(event.getEntity().getType().getTags().contains(SLIMES)) {
            LivingEntity base = event.getEntityLiving();
            CompoundNBT data = base.getPersistentData();
            ItemStack stack = ItemStack.read(data.getCompound("AbsorbedItem"));
            if(!stack.isEmpty()) {
                World world = base.world;
                ItemEntity entityitem = new ItemEntity(world, base.getPosX(), base.getPosY() + 1, base.getPosZ(), stack.copy());
                entityitem.setPickupDelay(20);
                world.addEntity(entityitem);
            }
        }
    }
}
