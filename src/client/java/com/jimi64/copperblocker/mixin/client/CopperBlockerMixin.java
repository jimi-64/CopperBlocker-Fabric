package com.jimi64.copperblocker.mixin.client;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.function.Supplier;

import static com.jimi64.copperblocker.client.CopperblockerClient.denyList;
import static com.jimi64.copperblocker.client.CopperblockerClient.LOGGER;

@Mixin(value = ItemGroup.class)
public abstract class CopperBlockerMixin {


    @Shadow private Collection<ItemStack> displayStacks;
    @Shadow @Final private Text displayName;
    @Unique private Collection<ItemStack> filteredDisplayStacks;


    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void ItemGroup(ItemGroup.Row row, int column, ItemGroup.Type type, Text displayName, Supplier iconSupplier, ItemGroup.EntryCollector entryCollector, CallbackInfo ci) {
        this.filteredDisplayStacks = ItemStackSet.create();
    }


    /**
     * @author jimi64
     * @reason prevents all items on the denyList from being shown in the creative menu
     */
    @Overwrite
    public Collection<ItemStack> getDisplayStacks() {

        if (this.filteredDisplayStacks.isEmpty()) {

            // copies all entries of displayStacks into filteredDisplayStacks
            this.filteredDisplayStacks.addAll(this.displayStacks);

            for (ItemStack displayStack : this.filteredDisplayStacks) {

                // gets the item name of the displayStack object
                String itemName = displayStack.getItem().toString();

                if (denyList.contains(itemName)) {
                    this.filteredDisplayStacks.remove(displayStack);
                }
            }
            LOGGER.info("Filtered ItemGroup '" + this.displayName.getString() + "' successfully!");
        }
        return this.filteredDisplayStacks;
    }
}