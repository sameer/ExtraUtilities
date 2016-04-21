// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.gui;

import invtweaks.api.container.ContainerSectionCallback;
import java.util.List;
import invtweaks.api.container.ContainerSection;
import java.util.Map;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidStack;
import com.rwtema.extrautils.asm.FluidIDGetter;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import invtweaks.api.container.InventoryContainer;
import net.minecraft.inventory.Container;

@InventoryContainer
public class ContainerTransferNode extends Container
{
    public int lastenergy;
    public int lastenergycount;
    public int liquid_type;
    public int liquid_amount;
    private TileEntityTransferNode node;
    private IInventory player;
    private int lastpipe_x;
    private int lastpipe_y;
    private int lastpipe_z;
    
    public ContainerTransferNode(final IInventory player, final TileEntityTransferNode node) {
        this.lastenergy = 0;
        this.lastenergycount = 0;
        this.liquid_type = -1;
        this.liquid_amount = -1;
        this.lastpipe_x = 0;
        this.lastpipe_y = 0;
        this.lastpipe_z = 0;
        this.node = node;
        if (node instanceof IInventory) {
            this.addSlotToContainer(new Slot((IInventory)node, 0, 80, 83));
        }
        for (int i = 0; i < node.upgrades.getSizeInventory(); ++i) {
            this.addSlotToContainer((Slot)new SlotChecksValidity((IInventory)node.upgrades, i, 35 + i * 18, 121));
        }
        for (int iy = 0; iy < 3; ++iy) {
            for (int ix = 0; ix < 9; ++ix) {
                this.addSlotToContainer(new Slot(player, ix + iy * 9 + 9, 8 + ix * 18, 143 + iy * 18));
            }
        }
        for (int ix2 = 0; ix2 < 9; ++ix2) {
            this.addSlotToContainer(new Slot(player, ix2, 8 + ix2 * 18, 201));
        }
    }
    
    public void addCraftingToCrafters(final ICrafting icrafting) {
        super.addCraftingToCrafters(icrafting);
        icrafting.sendProgressBarUpdate((Container)this, 0, this.lastpipe_x = this.node.pipe_x);
        icrafting.sendProgressBarUpdate((Container)this, 1, this.lastpipe_y = this.node.pipe_y);
        icrafting.sendProgressBarUpdate((Container)this, 2, this.lastpipe_z = this.node.pipe_z);
        if (this.node instanceof TileEntityTransferNodeEnergy) {
            icrafting.sendProgressBarUpdate((Container)this, 3, this.lastenergycount = ((TileEntityTransferNodeEnergy)this.node).numMachines());
            this.lastenergy = ((TileEntityTransferNodeEnergy)this.node).getEnergyStored(null);
            for (int i = 0; i < 3; ++i) {
                icrafting.sendProgressBarUpdate((Container)this, 6 + i, (int)convToShort(this.lastenergy, i));
            }
        }
        int newliquid_type = -1;
        int newliquid_amount = -1;
        if (this.node instanceof TileEntityTransferNodeLiquid) {
            final FluidStack t = ((TileEntityTransferNodeLiquid)this.node).getTankInfo(null)[0].fluid;
            if (t != null && t.amount > 0) {
                newliquid_type = FluidIDGetter.fluidLegacy.getID(t);
                newliquid_amount = t.amount;
                icrafting.sendProgressBarUpdate((Container)this, 4, newliquid_type);
                icrafting.sendProgressBarUpdate((Container)this, 5, newliquid_amount);
            }
        }
    }
    
    public ItemStack transferStackInSlot(final EntityPlayer par1EntityPlayer, final int par2) {
        ItemStack itemstack = null;
        final Slot slot = (Slot) this.inventorySlots.get(par2);
        int start = 0;
        int end = this.node.upgrades.getSizeInventory();
        if (this.node instanceof TileEntityTransferNodeInventory) {
            ++start;
            ++end;
        }
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (par2 < end) {
                if (!this.mergeItemStack(itemstack2, end, this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if ((!this.node.upgrades.isItemValidForSlot(0, itemstack2) || !this.mergeItemStack(itemstack2, start, end, false)) && (start == 0 || !this.mergeItemStack(itemstack2, 0, start, false))) {
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
        return this.node.isUseableByPlayer(entityplayer);
    }
    
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int newenergy = -1;
        int newenergycount = -1;
        if (this.node instanceof TileEntityTransferNodeEnergy) {
            newenergy = ((TileEntityTransferNodeEnergy)this.node).getEnergyStored(null);
            newenergycount = ((TileEntityTransferNodeEnergy)this.node).numMachines();
        }
        int newliquid_type = -1;
        final int newliquid_type_metadata = -1;
        int newliquid_amount = -1;
        if (this.node instanceof TileEntityTransferNodeLiquid) {
            final FluidStack t = ((TileEntityTransferNodeLiquid)this.node).getTankInfo(null)[0].fluid;
            if (t != null && t.amount > 0) {
                newliquid_type = FluidIDGetter.fluidLegacy.getID(t);
                newliquid_amount = t.amount;
            }
        }
        for (final Object crafter : this.crafters) {
            final ICrafting icrafting = (ICrafting)crafter;
            if (this.lastpipe_x != this.node.pipe_x || this.lastpipe_y != this.node.pipe_y || this.lastpipe_z != this.node.pipe_z) {
                icrafting.sendProgressBarUpdate((Container)this, 0, this.node.pipe_x);
                icrafting.sendProgressBarUpdate((Container)this, 1, this.node.pipe_y);
                icrafting.sendProgressBarUpdate((Container)this, 2, this.node.pipe_z);
            }
            if (newenergycount != this.lastenergycount) {
                icrafting.sendProgressBarUpdate((Container)this, 3, newenergycount);
            }
            if (this.liquid_type != newliquid_type || this.liquid_amount != newliquid_amount) {
                icrafting.sendProgressBarUpdate((Container)this, 4, newliquid_type);
                icrafting.sendProgressBarUpdate((Container)this, 5, newliquid_amount);
            }
            if (convToShort(newenergy, 2) != convToShort(this.lastenergy, 0)) {
                icrafting.sendProgressBarUpdate((Container)this, 6, (int)convToShort(this.lastenergy, 0));
            }
            if (convToShort(newenergy, 1) != convToShort(this.lastenergy, 1)) {
                icrafting.sendProgressBarUpdate((Container)this, 7, (int)convToShort(this.lastenergy, 1));
            }
            if (convToShort(newenergy, 2) != convToShort(this.lastenergy, 2)) {
                icrafting.sendProgressBarUpdate((Container)this, 8, (int)convToShort(this.lastenergy, 2));
            }
        }
        this.lastpipe_x = this.node.pipe_x;
        this.lastpipe_y = this.node.pipe_y;
        this.lastpipe_z = this.node.pipe_z;
        this.lastenergy = newenergy;
        this.lastenergycount = newenergycount;
        this.liquid_type = newliquid_type;
        this.liquid_amount = newliquid_amount;
    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(final int par1, final int par2) {
        switch (par1) {
            case 0: {
                this.node.pipe_x = par2;
            }
            case 1: {
                this.node.pipe_y = par2;
            }
            case 2: {
                this.node.pipe_z = par2;
                break;
            }
            case 3: {
                this.lastenergycount = par2;
                break;
            }
            case 4: {
                this.liquid_type = par2;
                break;
            }
            case 5: {
                this.liquid_amount = par2;
                break;
            }
            case 6:
            case 7:
            case 8: {
                this.lastenergy = (int)changeShort(this.lastenergy, (short)par2, par1 - 6);
                break;
            }
        }
    }
    
    public static short convToShort(final double t, final int level) {
        switch (level) {
            case 0: {
                return (short)Math.floor((t - Math.floor(t)) * 32768.0);
            }
            case 1: {
                return (short)(Math.floor(t) % 32768.0);
            }
            case 2: {
                return (short)Math.floor(t / 32768.0);
            }
            default: {
                return 0;
            }
        }
    }
    
    public static float changeShort(final float t, final short k, final int level) {
        final short[] v = new short[3];
        for (int i = 0; i < 3; ++i) {
            if (i == level) {
                v[i] = k;
            }
            else {
                v[i] = convToShort(t, i);
            }
        }
        return v[2] * 32768 + v[1] + v[0] / 32768.0f;
    }
    
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlots() {
        return InventoryTweaksHelper.getSlots(this, false);
    }
}


