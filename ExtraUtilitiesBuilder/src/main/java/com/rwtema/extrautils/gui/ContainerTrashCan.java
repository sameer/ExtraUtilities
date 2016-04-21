// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.gui;

import invtweaks.api.container.ContainerSectionCallback;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.inventory.Container;

@InventoryContainer
public class ContainerTrashCan extends Container
{
    private TileEntityTrashCan trashCan;
    private IInventory player;
    
    public ContainerTrashCan(final IInventory player, final TileEntityTrashCan trashCan) {
        this.trashCan = trashCan;
        this.addSlotToContainer((Slot)new SlotTrash((IInventory)trashCan, 0, 80, 42));
        for (int iy = 0; iy < 3; ++iy) {
            for (int ix = 0; ix < 9; ++ix) {
                this.addSlotToContainer(new Slot(player, ix + iy * 9 + 9, 8 + ix * 18, 90 + iy * 18));
            }
        }
        for (int ix2 = 0; ix2 < 9; ++ix2) {
            this.addSlotToContainer(new Slot(player, ix2, 8 + ix2 * 18, 148));
        }
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack itemstack = null;
        final Slot slot = (Slot) this.inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (par2 == 0) {
                if (!this.mergeItemStack(itemstack2, 1, this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else {
                TileEntityTrashCan.instantAdd = true;
                if (!this.mergeItemStack(itemstack2, 0, 1, false)) {
                    TileEntityTrashCan.instantAdd = false;
                    return null;
                }
                TileEntityTrashCan.instantAdd = false;
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
        return this.trashCan.isUseableByPlayer(entityplayer);
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlots() {
        return InventoryTweaksHelper.getSlots(this, true);
    }
}

