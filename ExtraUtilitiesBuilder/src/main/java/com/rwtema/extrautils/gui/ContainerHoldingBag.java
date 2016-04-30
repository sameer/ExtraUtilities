// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import com.rwtema.extrautils.ExtraUtils;
import invtweaks.api.container.ContainerSectionCallback;
import java.util.HashMap;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import invtweaks.api.container.ChestContainer;
import net.minecraft.inventory.Container;

@ChestContainer
public class ContainerHoldingBag extends Container
{
    private EntityPlayer player;
    private int currentFilter;
    private ItemStack itemStack;
    
    public ContainerHoldingBag(final EntityPlayer player, final int invId) {
        this.player = null;
        this.currentFilter = -1;
        this.player = player;
        this.currentFilter = invId;
        final int i = 36;
        for (int j = 0; j < 6; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer((Slot)new SlotItemContainer((IInventory)player.inventory, k + j * 9, 8 + k * 18, 18 + j * 18, this.currentFilter));
            }
        }
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot((IInventory)player.inventory, k + j * 9 + 9, 8 + k * 18, 104 + j * 18 + i));
            }
        }
        for (int j = 0; j < 9; ++j) {
            if (j == this.currentFilter) {
                this.addSlotToContainer((Slot)new SlotDisabled((IInventory)player.inventory, j, 8 + j * 18, 162 + i));
            }
            else {
                this.addSlotToContainer(new Slot((IInventory)player.inventory, j, 8 + j * 18, 162 + i));
            }
        }
    }
    
    protected void retrySlotClick(final int par1, final int par2, final boolean par3, final EntityPlayer par4EntityPlayer) {
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlotType() {
        final HashMap<ContainerSection, List<Slot>> hashMap = new HashMap<ContainerSection, List<Slot>>();
        hashMap.put(ContainerSection.CHEST, this.inventorySlots.subList(0, 54));
        hashMap.put(ContainerSection.INVENTORY_NOT_HOTBAR, this.inventorySlots.subList(54, 81));
        hashMap.put(ContainerSection.INVENTORY_HOTBAR, this.inventorySlots.subList(81, 90));
        hashMap.put(ContainerSection.INVENTORY, this.inventorySlots.subList(54, 90));
        return hashMap;
    }
    
    public ItemStack slotClick(final int par1, final int par2, final int par3, final EntityPlayer par4EntityPlayer) {
        if (par3 == 2 && par2 == this.currentFilter) {
            return null;
        }
        final ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
        if (filter == null || filter.getItem() != ExtraUtils.goldenBag) {
            return null;
        }
        return super.slotClick(par1, par2, par3, par4EntityPlayer);
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack itemstack = null;
        final Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (par2 < 54) {
                if (!this.mergeItemStack(itemstack2, 54, this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, 54, false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack((ItemStack)null);
                return null;
            }
            slot.onSlotChanged();
        }
        return itemstack;
    }
    
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        final ItemStack filter = this.player.inventory.getStackInSlot(this.currentFilter);
        return filter != null && filter.getItem() == ExtraUtils.goldenBag;
    }
}


