package com.blamejared.slimyboyos.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;


public class SlimeAbsorptionCapability {
    
    @CapabilityInject(SlimeAbsorption.class)
    public static Capability<SlimeAbsorption> SLIME_ABSORPTION;
    
    public static void init() {
        
        CapabilityManager.INSTANCE.register(SlimeAbsorption.class, new Capability.IStorage<SlimeAbsorption>() {
                    @Override
                    public INBT writeNBT(Capability<SlimeAbsorption> capability, SlimeAbsorption instance,
                                         Direction side) {
                        
                        return instance.serializeNBT();
                    }
                    
                    @Override
                    public void readNBT(Capability<SlimeAbsorption> capability, SlimeAbsorption instance,
                                        Direction side, INBT nbt) {
                        
                        if(nbt instanceof CompoundNBT) {
                            instance.deserializeNBT((CompoundNBT) nbt);
                        }
                    }
                },
                SlimeAbsorption.Impl::new);
    }
    
}
