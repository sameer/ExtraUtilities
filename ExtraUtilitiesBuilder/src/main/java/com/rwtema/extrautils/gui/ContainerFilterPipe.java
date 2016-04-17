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
import invtweaks.api.container.InventoryContainer;
import net.minecraft.inventory.Container;

@InventoryContainer
public class ContainerFilterPipe extends Container
{
    public ContainerFilterPipe(final IInventory player, final IInventory pipe) {
        this.addSlotToContainer(new Slot(pipe, 0, 80, 90));
        this.addSlotToContainer(new Slot(pipe, 1, 80, 15));
        this.addSlotToContainer(new Slot(pipe, 2, 43, 33));
        this.addSlotToContainer(new Slot(pipe, 3, 117, 72));
        this.addSlotToContainer(new Slot(pipe, 4, 43, 72));
        this.addSlotToContainer(new Slot(pipe, 5, 117, 33));
        for (int iy = 0; iy < 3; ++iy) {
            for (int ix = 0; ix < 9; ++ix) {
                this.addSlotToContainer(new Slot(player, ix + iy * 9 + 9, 8 + ix * 18, 111 + iy * 18));
            }
        }
        for (int ix2 = 0; ix2 < 9; ++ix2) {
            this.addSlotToContainer(new Slot(player, ix2, 8 + ix2 * 18, 169));
        }
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
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
