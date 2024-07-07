package com.blamejared.slimyboyos;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class Constants {
    
    public static final String MOD_ID = "slimyboyos";
    public static final String MOD_NAME = "SlimyBoyos";
    public static final Logger LOG = LogManager.getLogger(MOD_NAME);
    
    public static final Supplier<TagKey<Item>> SLIMES_CANNOT_ABSORB = Suppliers.memoize(() -> TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "slimes_cannot_absorb")));
    
    
}