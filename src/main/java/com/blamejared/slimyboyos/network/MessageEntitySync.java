package com.blamejared.slimyboyos.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageEntitySync implements IMessage, IMessageHandler<MessageEntitySync, IMessage> {
    
    private int id;
    private ItemStack headStack;
    
    public MessageEntitySync() {
    }
    
    public MessageEntitySync(EntitySlime slime) {
        this.id = slime.getEntityId();
        this.headStack = slime.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
    }
    
    @Override
    public void fromBytes(ByteBuf buf) {
        
        this.id = buf.readInt();
        this.headStack = ByteBufUtils.readItemStack(buf);
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        ByteBufUtils.writeItemStack(buf, headStack);
    }
    
    @Override
    public IMessage onMessage(MessageEntitySync message, MessageContext ctx) {
        
        Minecraft.getMinecraft().addScheduledTask(() -> handle(message, ctx));
        
        return null;
    }
    
    private void handle(MessageEntitySync message, MessageContext ctx) {
        if(FMLClientHandler.instance().getClient().world != null) {
            Entity entityByID = FMLClientHandler.instance().getClient().world.getEntityByID(message.id);
            entityByID.setItemStackToSlot(EntityEquipmentSlot.HEAD, message.headStack);
        }
    }
    
}
