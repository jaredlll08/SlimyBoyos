package com.blamejared.slimyboyos.mixin.common;

import com.blamejared.slimyboyos.Constants;
import com.blamejared.slimyboyos.api.IAbsorber;
import com.blamejared.slimyboyos.platform.Services;
import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements IAbsorber {
    
    private static final EntityDataAccessor<ItemStack> DATA_ABSORBED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final Supplier<TagKey<Item>> SLIME_ABSORB_BLACKLIST = Suppliers.memoize(() -> TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MOD_ID, "slime_absorb_blacklist")));
    
    @Unique
    public boolean slimyboyos$canAbsorb;
    
    public MixinLivingEntity(EntityType<?> $$0, Level $$1) {
        
        super($$0, $$1);
    }
    
    @Inject(method = "tick", at = @At("HEAD"))
    public void slimyboyos$tick(CallbackInfo ci) {
        
        if(this.level.isClientSide || !this.isAlive() || !this.level.getGameRules()
                .getBoolean(GameRules.RULE_MOBGRIEFING)) {
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
            List<ItemEntity> entities = this.level.getEntities(EntityType.ITEM, boundingBox, item -> item.isAlive() && !item.isPickable() && !item.getItem()
                    .isEmpty());
            entities.stream()
                    .filter(item -> !item.getItem().is(SLIME_ABSORB_BLACKLIST.get()))
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
    public void slimyboyos$dropCustomDeathLoot(DamageSource $$0, CallbackInfo ci) {
        
        if(level.isClientSide || !level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            return;
        }
        ItemStack stack = slimyboyos$getAbsorbedItem();
        if(!stack.isEmpty()) {
            spawnAtLocation(stack);
        }
        
    }
    
    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void slimyboyos$defineSyncedData(EntityType $$0, Level $$1, CallbackInfo ci) {
        
        entityData.define(DATA_ABSORBED, ItemStack.EMPTY);
    }
    
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void slimyboyos$save(CompoundTag tag, CallbackInfo ci) {
        
        tag.put("slimyboyos:absorbed_item", slimyboyos$getAbsorbedItem().save(new CompoundTag()));
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void slimyboyos$load(CompoundTag tag, CallbackInfo ci) {
        
        slimyboyos$setAbsorbedItem(ItemStack.of(tag.getCompound("slimyboyos:absorbed_item")));
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
