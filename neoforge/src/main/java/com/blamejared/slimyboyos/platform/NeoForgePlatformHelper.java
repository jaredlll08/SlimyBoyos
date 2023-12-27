package com.blamejared.slimyboyos.platform;

import com.blamejared.slimyboyos.platform.services.IPlatformHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
    
    @Override
    public TagKey<EntityType<?>> getSlimeTag() {
        
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("neoforge", "slimes"));
    }
}
