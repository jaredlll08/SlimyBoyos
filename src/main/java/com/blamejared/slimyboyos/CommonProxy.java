package com.blamejared.slimyboyos;

import com.blamejared.slimyboyos.capability.SlimeAbsorptionCapability;
import com.blamejared.slimyboyos.events.CommonEventHandler;
import com.blamejared.slimyboyos.network.MessageItemPickup;
import com.blamejared.slimyboyos.network.MessageItemSync;
import com.blamejared.slimyboyos.network.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CommonProxy {

    public CommonProxy() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }

    private void setup(FMLCommonSetupEvent event) {
        PacketHandler.init();
        SlimeAbsorptionCapability.init();
    }

    public void handleItemPickup(MessageItemPickup msg) {
    }

    public void handleItemSync(MessageItemSync msg) {
    }
}
