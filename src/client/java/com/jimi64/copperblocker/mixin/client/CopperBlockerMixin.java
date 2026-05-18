package com.jimi64.copperblocker.mixin.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.function.Supplier;

import static com.jimi64.copperblocker.client.CopperblockerClient.denyList;
import static com.jimi64.copperblocker.client.CopperblockerClient.LOGGER;

@Mixin(value = CreativeModeTab.class)
public abstract class CopperBlockerMixin {


    @Shadow private Collection<ItemStack> displayItems;
    @Shadow @Final private Component displayName;
    @Unique private Collection<ItemStack> filteredDisplayItems;


    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void CreativeModeTab(CreativeModeTab.Row row, int column, CreativeModeTab.Type type, Component displayName, Supplier<ItemStack> iconGenerator, CreativeModeTab.DisplayItemsGenerator displayItemsGenerator, CallbackInfo ci) {
        this.filteredDisplayItems = ItemStackLinkedSet.createTypeAndComponentsSet();
    }


    /**
     * @author jimi64
     * @reason prevents all items on the denyList from being shown in the creative menu
     */
    @Overwrite
    public Collection<ItemStack> getDisplayItems() {

        if (this.filteredDisplayItems.isEmpty()) {

            // copies all entries of displayStacks into filteredDisplayItems
            this.filteredDisplayItems.addAll(this.displayItems);

            for (ItemStack displayStack : this.filteredDisplayItems) {

                // gets the item name of the displayStack object
                String itemName = displayStack.getItem().toString();

                if (denyList.contains(itemName)) {
                    this.filteredDisplayItems.remove(displayStack);
                }
            }
            LOGGER.info("Filtered CreativeModeTab '" + this.displayName.getString() + "' successfully!");
        }
        return this.filteredDisplayItems;
    }
}