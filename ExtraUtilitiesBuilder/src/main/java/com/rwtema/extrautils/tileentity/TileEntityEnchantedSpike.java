// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnchantedSpike extends TileEntity
{
    NBTTagList enchants;
    
    public boolean canUpdate() {
        return false;
    }
    
    public void setEnchantmentTagList(final NBTTagList enchants) {
        this.enchants = enchants;
    }
    
    public void readFromNBT(final NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.enchants = tags.getTagList("ench", 10);
    }
    
    public void writeToNBT(final NBTTagCompound tags) {
        super.writeToNBT(tags);
        if (this.enchants != null && this.enchants.tagCount() > 0) {
            tags.setTag("ench", (NBTBase)this.enchants);
        }
    }
    
    public NBTTagCompound getEnchantmentTagList() {
        if (this.enchants == null || this.enchants.tagCount() == 0) {
            return null;
        }
        final NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setTag("ench", this.enchants.copy());
        return tagCompound;
    }
}
