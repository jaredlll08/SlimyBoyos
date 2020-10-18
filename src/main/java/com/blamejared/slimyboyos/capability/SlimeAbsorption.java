package com.blamejared.slimyboyos.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface SlimeAbsorption extends INBTSerializable<CompoundNBT> {

    ItemStack getAbsorbedStack();

    void setAbsorbedStack(ItemStack stack);

    class Impl implements SlimeAbsorption {

        private ItemStack absorbedStack;

        public Impl() {
            this(ItemStack.EMPTY);
        }

        public Impl(ItemStack absorbedStack) {
            this.absorbedStack = absorbedStack;
        }

        @Override
        public ItemStack getAbsorbedStack() {
            return absorbedStack;
        }

        @Override
        public void setAbsorbedStack(ItemStack stack) {
            this.absorbedStack = stack;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
            if (!absorbedStack.isEmpty()) {
                absorbedStack.write(nbt);
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt.isEmpty()) {
                absorbedStack = ItemStack.EMPTY;
            } else {
                absorbedStack = ItemStack.read(nbt);
            }
        }
    }

    class Provider implements ICapabilitySerializable<CompoundNBT> {

        public static final ResourceLocation NAME = new ResourceLocation("slimyboyos:slime_absorption");

        private final SlimeAbsorption impl = new Impl();
        private final LazyOptional<SlimeAbsorption> cap = LazyOptional.of(() -> impl);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction facing) {
            if (capability == SlimeAbsorptionCapability.SLIME_ABSORPTION) {
                return cap.cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return impl.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            impl.deserializeNBT(nbt);
        }
    }
}
