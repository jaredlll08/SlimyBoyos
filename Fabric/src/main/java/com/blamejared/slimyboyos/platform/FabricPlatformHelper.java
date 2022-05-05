package com.blamejared.slimyboyos.platform;

import com.blamejared.slimyboyos.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
    
    @Override
    public TagKey<EntityType<?>> getSlimeTag() {
        
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("c", "slimes"));
    }
    
}
