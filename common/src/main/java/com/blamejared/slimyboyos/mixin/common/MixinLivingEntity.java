package com.blamejared.slimyboyos.mixin.common;

import com.blamejared.slimyboyos.Constants;
import com.blamejared.slimyboyos.api.IAbsorber;
import com.blamejared.slimyboyos.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IAbsorber {
    
    @Shadow
    public abstract boolean isAlive();
    
    @Unique
    public boolean slimyboyos$canAbsorb;
    
    public MixinLivingEntity(EntityType<?> $$0, Level $$1) {
        
        super($$0, $$1);
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    public void slimyboyos$tick(CallbackInfo ci) {
        
        if(!(this.level() instanceof ServerLevel sl) || !this.isAlive() || !sl.getGameRules()
                .get(GameRules.MOB_GRIEFING)) {
            return;
        }
        if(this.tickCount % 20 == 0) {
            this.slimyboyos$canAbsorb = this.getType().is(Services.PLATFORM.getSlimeTag());
        }
        
        if(slimyboyos$canAbsorb) {
            if(!slimyboyos$getAbsorbedItem().isEmpty()) {
                return;
            }
            
            AABB boundingBox = this.getBoundingBox();
            List<ItemEntity> entities = this.level()
                    .getEntities(EntityType.ITEM, boundingBox, item -> item.isAlive() && !item.isPickable() && !item.getItem()
                            .isEmpty());
            entities.stream()
                    .filter(item -> !item.getItem().is(Constants.SLIMES_CANNOT_ABSORB.get()))
                    .findFirst()
                    .ifPresent(item -> {
                        ItemStack stack = item.getItem();
                        ItemStack absorbedStack = stack.split(1);
                        slimyboyos$setAbsorbedItem(absorbedStack);
                        if(stack.isEmpty()) {
                            item.discard();
                        }
                    });
        }
    }
    
    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    public void slimyboyos$dropCustomDeathLoot(ServerLevel level, DamageSource $$1, CallbackInfo ci) {
        
        if(!(this.level() instanceof ServerLevel sl) || !level.getGameRules().get(GameRules.MOB_DROPS)) {
            return;
        }
        ItemStack stack = slimyboyos$getAbsorbedItem();
        if(!stack.isEmpty()) {
            spawnAtLocation(sl, stack);
        }
        
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void slimyboyos$load(ValueInput value, CallbackInfo ci) {
        
        value.read("slimyboyos:absorbed_item", ItemStack.CODEC).ifPresent(this::slimyboyos$setAbsorbedItem);
    }
    
}
