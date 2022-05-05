package com.blamejared.slimyboyos.platform;

import com.blamejared.slimyboyos.platform.services.IPlatformHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
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
        
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("forge", "slimes"));
    }
}
