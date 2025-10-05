package com.blamejared.slimyboyos.mixin.common;

import com.blamejared.slimyboyos.SlimyBoyos;
import com.blamejared.slimyboyos.api.IAbsorber;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = LivingEntity.class)
public abstract class MixinLivingEntityNeoForge extends Entity implements IAbsorber {
    
    public MixinLivingEntityNeoForge(EntityType<?> p_19870_, Level p_19871_) {
        
        super(p_19870_, p_19871_);
    }
    
    @Override
    public ItemStack slimyboyos$getAbsorbedItem() {
        
        if(this.hasData(SlimyBoyos.ABSORBED)) {
            return this.getData(SlimyBoyos.ABSORBED);
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public void slimyboyos$setAbsorbedItem(ItemStack stack) {
        
        this.setData(SlimyBoyos.ABSORBED, stack);
    }
    
}
