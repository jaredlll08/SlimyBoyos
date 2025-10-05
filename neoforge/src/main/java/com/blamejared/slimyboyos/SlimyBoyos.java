package com.blamejared.slimyboyos;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class SlimyBoyos {
    
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);
    
    public static final Supplier<AttachmentType<ItemStack>> ABSORBED = ATTACHMENT_TYPES.register(
            "absorbed", () -> AttachmentType.builder(() -> ItemStack.EMPTY).serialize(ItemStack.MAP_CODEC).sync(ItemStack.STREAM_CODEC).build()
    );
    
    public SlimyBoyos(IEventBus modEventBus) {
        
        ATTACHMENT_TYPES.register(modEventBus);
    }
    
}