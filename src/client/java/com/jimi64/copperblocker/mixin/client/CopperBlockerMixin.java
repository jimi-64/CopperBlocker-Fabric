package com.jimi64.copperblocker.mixin.client;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(value = ItemGroup.class)
public abstract class CopperBlockerMixin {

    @Shadow
    private Collection<ItemStack> displayStacks;

    @Inject(method = "getDisplayStacks", at = @At("HEAD"))
    void getDisplayStacks(CallbackInfoReturnable<Collection<ItemStack>> cir) {

        //Collection<ItemStack> displayStacks = this.displayStacks.clone();

        for (ItemStack displayStack : this.displayStacks) {

            String itemName = displayStack.getName().toString();

            if ((itemName.contains("copper") || itemName.contains("lightning_rod")) && !(itemName.contains("waxed"))) {
                this.displayStacks.remove(displayStack);
            }
        }
    }
}