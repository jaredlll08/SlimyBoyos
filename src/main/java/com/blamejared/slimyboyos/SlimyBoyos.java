package com.blamejared.slimyboyos;

import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


@Mod("slimyboyos")
public class SlimyBoyos {
    
    public static CommonProxy PROXY;
    
    public SlimyBoyos() {
        
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    }
    
}
