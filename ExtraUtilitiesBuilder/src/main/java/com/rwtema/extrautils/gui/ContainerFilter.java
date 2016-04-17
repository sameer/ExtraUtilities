// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import invtweaks.api.container.ContainerSectionCallback;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.nbt.NBTBase;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayer;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.inventory.Container;

@InventoryContainer
public class ContainerFilter extends Container
{
    private EntityPlayer player;
    private int currentFilter;
    
    public ContainerFilter(final EntityPlayer player, final int invId) {
        this.player = null;
        this.currentFilter = -1;
        this.player = player;
        this.currentFilter = invId;
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer((Slot)new SlotGhostItemContainer((IInventory)player.inventory, k, 8 + k * 18, 18, this.currentFilter));
        }
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot((IInventory)player.inventory, k + j * 9 + 9, 8 + k * 18, 50 + j * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            if (j == this.currentFilter) {
                this.addSlotToContainer((Slot)new SlotDisabled((IInventory)player.inventory, j, 8 + j * 18, 108));
            }
            else {
                this.addSlotToContainer(new Slot((IInventory)player.inventory, j, 8 + j * 18, 108));
            }
        }
    }
    
    public ItemStack slotClick(final int par1, final int par2, final int par3, final EntityPlayer par4EntityPlayer) {
        if (par1 >= 0 && par1 < 9) {
            final ItemStack item = par4EntityPlayer.inventory.getItemStack();
            return this.clickItemStack(par1, item);
        }
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
    
    public ItemStack clickItemStack(final int par1, ItemStack item) {
        if (item != null) {
            item = item.copy();
            item.stackSize = 1;
        }
        final String keyname = "items_" + par1;
        final ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
        if (filter == null) {
            return item;
        }
        NBTTagCompound tags = filter.getTagCompound();
        if (item != null) {
            if (tags == null) {
                tags = new NBTTagCompound();
            }
            if (tags.hasKey(keyname)) {
                if (FluidContainerRegistry.isFilledContainer(item) && ItemStack.areItemStacksEqual(ItemStack.loadItemStackFromNBT(tags.getCompoundTag(keyname)), item)) {
                    final NBTTagCompound fluidTags = new NBTTagCompound();
                    if (tags.hasKey("isLiquid_" + par1)) {
                        tags.removeTag("isLiquid_" + par1);
                    }
                    else {
                        tags.setBoolean("isLiquid_" + par1, true);
                    }
                    return item;
                }
                if (tags.hasKey("isLiquid_" + par1)) {
                    tags.removeTag("isLiquid_" + par1);
                }
                tags.removeTag(keyname);
            }
            else if (tags.hasKey("isLiquid_" + par1)) {
                tags.removeTag("isLiquid_" + par1);
            }
            final NBTTagCompound itemTags = new NBTTagCompound();
            item.writeToNBT(itemTags);
            tags.setTag(keyname, (NBTBase)itemTags);
            filter.setTagCompound(tags);
        }
        else if (tags != null) {
            if (tags.hasKey("isLiquid_" + par1)) {
                tags.removeTag("isLiquid_" + par1);
            }
            tags.removeTag(keyname);
            if (tags.hasNoTags()) {
                filter.setTagCompound((NBTTagCompound)null);
            }
            else {
                filter.setTagCompound(tags);
            }
        }
        return item;
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        final Slot slot = this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            if (slot instanceof SlotGhostItemContainer) {
                this.slotClick(slot.slotNumber, 0, 0, par1EntityPlayer);
            }
            else {
                for (int i = 0; i < 9; ++i) {
                    if (!this.inventorySlots.get(i).getHasStack()) {
                        this.clickItemStack(i, slot.getStack());
                        return null;
                    }
                    if (XUHelper.canItemsStack(slot.getStack(), this.inventorySlots.get(i).getStack())) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        return true;
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlots() {
        return InventoryTweaksHelper.getSlots(this, true);
    }
}
