package com.blamejared.slimyboyos.events;

import com.blamejared.slimyboyos.network.MessageItemSync;
import com.blamejared.slimyboyos.network.PacketHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;


public class CommonEventHandler {

    public static final ResourceLocation SLIMES = new ResourceLocation("forge:slimes");

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living.world.isRemote || !living.isAlive() || !living.getType().getTags().contains(SlimyBoyos.SLIMES)) {
            return;
        }

        CompoundNBT data = living.getPersistentData();
        if (data.contains("AbsorbedItem")) {
            return;
        }

        AxisAlignedBB bb = event.getEntity().getBoundingBox();
        List<ItemEntity> list = event.getEntity().world.getEntitiesWithinAABB(ItemEntity.class, bb,
                item -> item.isAlive() && !item.cannotPickup() && !item.getItem().isEmpty());
        if (!list.isEmpty()) {
            ItemEntity item = list.get(0);
            ItemStack stack = item.getItem();

            int collectedAmount = 1;
            ItemStack absorbedStack = stack.split(collectedAmount);
            data.put("AbsorbedItem", absorbedStack.serializeNBT());
            PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(event::getEntity),
                    new MessageItemSync(item.getEntityId(), living.getEntityId(), collectedAmount));

            if (stack.isEmpty()) {
                item.remove();
            }
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living.world.isRemote || !living.getType().getTags().contains(SLIMES)) {
            return;
        }

        CompoundNBT data = living.getPersistentData();
        ItemStack stack = ItemStack.read(data.getCompound("AbsorbedItem"));
        data.remove("AbsorbedItem");

        if (!stack.isEmpty()) {
            World world = living.world;
            ItemEntity item = new ItemEntity(world, living.getPosX(), living.getPosY(), living.getPosZ(), stack.copy());
            item.setDefaultPickupDelay();
            event.getDrops().add(item);
        }
    }
}
