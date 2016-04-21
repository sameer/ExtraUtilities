// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import invtweaks.api.container.ContainerSectionCallback;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.TileEntityFilingCabinet;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.inventory.Container;

@InventoryContainer
public class ContainerFilingCabinet extends Container
{
    public static boolean updated;
    private TileEntityFilingCabinet cabinet;
    private int mimicThreshold;
    private boolean client;
    
    public ContainerFilingCabinet(final IInventory player, final TileEntityFilingCabinet cabinet, final boolean client) {
        this.cabinet = cabinet;
        this.client = client;
        for (int j = 0; j < cabinet.getMaxSlots(); ++j) {
            this.addSlotToContainer((Slot)new SlotFilingCabinet((IInventory)cabinet, j, 8 + j * 18, 18));
        }
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(player, k + j * 9 + 9, 8 + k * 18, 158 + j * 18));
            }
        }
        for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(player, j, 8 + j * 18, 216));
        }
    }
    
    protected void retrySlotClick(final int par1, final int par2, final boolean par3, final EntityPlayer par4EntityPlayer) {
    }
    
    public void putStackInSlot(final int par1, final ItemStack par2ItemStack) {
        ContainerFilingCabinet.updated = true;
        super.putStackInSlot(par1, par2ItemStack);
    }
    
    @SideOnly(Side.CLIENT)
    public void putStacksInSlots(final ItemStack[] par1ArrayOfItemStack) {
        ContainerFilingCabinet.updated = true;
        for (int i = 0; i < par1ArrayOfItemStack.length; ++i) {
            this.getSlot(i).putStack(par1ArrayOfItemStack[i]);
        }
    }
    
    public boolean canDragIntoSlot(final Slot par1Slot) {
        return par1Slot.slotNumber >= this.cabinet.getMaxSlots();
    }
    
    public ItemStack slotClick(final int par1, final int par2, final int par3, final EntityPlayer par4EntityPlayer) {
        ContainerFilingCabinet.updated = true;
        ItemStack item;
        if (par1 >= 0 && par1 < this.cabinet.getMaxSlots() && par4EntityPlayer.inventory.getItemStack() != null) {
            item = super.slotClick(this.cabinet.getSizeInventory() - 1, par2, par3, par4EntityPlayer);
        }
        else {
            item = super.slotClick(par1, par2, par3, par4EntityPlayer);
        }
        this.cabinet.handleInput();
        return item;
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack itemstack = null;
        final Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (par2 < this.cabinet.getMaxSlots()) {
                final int m = Math.min(itemstack2.stackSize, itemstack2.getMaxStackSize());
                itemstack2.stackSize = m;
                if (!this.mergeItemStack(itemstack2, this.cabinet.getMaxSlots(), this.inventorySlots.size(), true)) {
                    return null;
                }
                itemstack2.stackSize += itemstack.stackSize - m;
            }
            else if (!this.cabinet.isItemValidForSlot(this.cabinet.getSizeInventory() - 1, itemstack2) || !this.mergeItemStack(itemstack2, 0, this.cabinet.getMaxSlots(), false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack((ItemStack)null);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
    public boolean canInteractWith(final EntityPlayer entityplayer) {
        return this.cabinet.isUseableByPlayer(entityplayer);
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlots() {
        return InventoryTweaksHelper.getSlots(this, true);
    }
    
    static {
        ContainerFilingCabinet.updated = false;
    }
}

