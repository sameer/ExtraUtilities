// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.item.ItemBlock;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.inventory.InventoryBasic;

public class TileEntityTransferNodeUpgradeInventory extends InventoryBasic
{
    private final boolean isEnergy;
    public TileEntityTransferNode node;
    
    public TileEntityTransferNodeUpgradeInventory(final int par3, final TileEntityTransferNode node) {
        super("Upgrade Inventory", true, par3);
        this.node = node;
        this.isEnergy = (node.getNode() instanceof IEnergyHandler);
    }
    
    public boolean isItemValidForSlot(final int par1, final ItemStack item) {
        return item != null && (this.pipeType(item) > 0 || this.isEnergy(item) || this.hasUpgradeNo(item));
    }
    
    public boolean hasUpgradeNo(final ItemStack item) {
        return item != null && item.getItem() != null && (item.getItem() == Items.glowstone_dust || item.getItem() == ExtraUtils.nodeUpgrade || item.getItem() == Item.getItemFromBlock(Blocks.redstone_torch));
    }
    
    public int getUpgradeNo(final ItemStack item) {
        if (item.getItem() == Items.glowstone_dust) {
            return -1;
        }
        if (item.getItem() == Item.getItemFromBlock(Blocks.redstone_torch)) {
            return -2;
        }
        return item.getItemDamage();
    }
    
    public boolean isEnergy(final ItemStack item) {
        return this.isEnergy && item.getItem() instanceof IEnergyContainerItem;
    }
    
    public int pipeType(final ItemStack item) {
        if (item == null || !(item.getItem() instanceof ItemBlock) || !(((ItemBlock)item.getItem()).field_150939_a instanceof BlockTransferPipe)) {
            return -1;
        }
        final int i = item.getItemDamage() + ((BlockTransferPipe)((ItemBlock)item.getItem()).field_150939_a).pipePage * 16;
        return this.isValidPipeType(i) ? i : -1;
    }
    
    public boolean isValidPipeType(final int i) {
        return (i <= 0 || i > 7) && i != 9 && i != 15;
    }
    
    public void markDirty() {
        this.node.calcUpgradeModifiers();
        this.node.markDirty();
        super.markDirty();
    }
}

