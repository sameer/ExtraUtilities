// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;

public class InvCrafting extends InventoryCrafting
{
    public InvCrafting(final int par2, final int par3) {
        super((Container)fakeContainer.instance, par2, par3);
    }
    
    public int hashCode() {
        int hash = 0;
        int n = 1;
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            n *= 31;
            hash += n * this.hashItemStack(this.getStackInSlot(i));
        }
        return hash;
    }
    
    public int hashItemStack(final ItemStack item) {
        if (item == null) {
            return 0;
        }
        int k = Item.getIdFromItem(item.getItem());
        k = k * 31 + item.getItemDamage();
        k *= 31;
        if (item.hasTagCompound()) {
            k += item.getTagCompound().hashCode();
        }
        return k;
    }
    
    public static class fakeContainer extends Container
    {
        public static fakeContainer instance;
        
        public boolean canInteractWith(final EntityPlayer var1) {
            return false;
        }
        
        static {
            fakeContainer.instance = new fakeContainer();
        }
    }
}
