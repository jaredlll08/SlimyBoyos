package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.SlimyBoyos;
import com.blamejared.slimyboyos.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.List;

public class CommonEventHandler {
    
    public CommonEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity().world.isRemote) {
            return;
        }
        if(event.getEntity() instanceof EntitySlime) {
            if(event.getEntityLiving().isDead){
                return;
            }
            if(!((EntitySlime) event.getEntity()).getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()){
                return;
            }
            
            AxisAlignedBB bb = event.getEntity().getEntityBoundingBox();
            List<EntityItem> list = event.getEntity().world.getEntitiesWithinAABB(EntityItem.class, bb);
            if(!list.isEmpty()) {
                if(list.get(0).cannotPickup()){
                   return;
                }
                event.getEntity().setItemStackToSlot(EntityEquipmentSlot.HEAD, list.get(0).getItem().copy());
                PacketHandler.INSTANCE.sendToAllAround(new MessageEntitySync((EntitySlime) event.getEntityLiving()), new NetworkRegistry.TargetPoint(event.getEntity().world.provider.getDimension(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, 128D));
                list.get(0).getItem().shrink(1);
                if(list.get(0).getItem().getCount() <= 0) {
                    list.get(0).setDead();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if(event.getEntity().world.isRemote) {
            return;
        }
        if(event.getEntity() instanceof EntitySlime) {
            EntityLivingBase base = event.getEntityLiving();
            ItemStack stack = base.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if(!stack.isEmpty()) {
                World world = base.world;
                EntityItem entityitem = new EntityItem(world, base.posX, base.posY+1, base.posZ, stack.copy());
                entityitem.setPickupDelay(20);
                world.spawnEntity(entityitem);
            }
        }
    }
}
