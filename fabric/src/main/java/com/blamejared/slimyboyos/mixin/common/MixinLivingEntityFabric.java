package com.blamejared.slimyboyos.mixin.common;

import com.blamejared.slimyboyos.api.IAbsorber;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class)
public abstract class MixinLivingEntityFabric extends Entity implements IAbsorber {
    
    @Unique
    private static final EntityDataAccessor<ItemStack> DATA_ABSORBED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.ITEM_STACK);
    
    public MixinLivingEntityFabric(EntityType<?> entityType, Level level) {
        
        super(entityType, level);
    }
    
    
    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    private void slimyboyos$defineSyncedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        
        builder.define(DATA_ABSORBED, ItemStack.EMPTY);
    }
    
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void slimyboyos$save(ValueOutput value, CallbackInfo ci) {
        
        if(!slimyboyos$getAbsorbedItem().isEmpty()) {
            value.store("slimyboyos:absorbed_item", ItemStack.CODEC, slimyboyos$getAbsorbedItem());
        }
    }
    
    @Override
    public ItemStack slimyboyos$getAbsorbedItem() {
        
        return this.entityData.get(DATA_ABSORBED);
    }
    
    @Override
    public void slimyboyos$setAbsorbedItem(ItemStack stack) {
        
        this.entityData.set(DATA_ABSORBED, stack);
    }
    
}
