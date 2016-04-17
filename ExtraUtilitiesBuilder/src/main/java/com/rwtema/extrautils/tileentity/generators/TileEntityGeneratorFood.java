// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class TileEntityGeneratorFood extends TileEntityGeneratorFurnace
{
    double curLevel;
    
    public TileEntityGeneratorFood() {
        this.curLevel = 0.0;
    }
    
    @Override
    public int transferLimit() {
        return 160;
    }
    
    @Override
    public double genLevel() {
        return this.curLevel;
    }
    
    @Override
    public double getGenLevelForStack(final ItemStack item) {
        if (item != null) {
            if (item.getItem() instanceof ItemFood) {
                return this.scale(((ItemFood)item.getItem()).func_150905_g(item), 8.0) * 4.0;
            }
            if (item.getItem() == Items.cake) {
                return 64.0;
            }
        }
        return 0.0;
    }
    
    public double scale(final double a, final double h) {
        if (a < h) {
            return a;
        }
        double b = 0.0;
        double s = 1.0;
        for (double i = 0.0; i <= a; i += h) {
            final double da = Math.min(h, a - i);
            b += da * s;
            s *= 0.75;
        }
        return b;
    }
    
    @Override
    public void adjustGenLevel(final ItemStack item) {
        this.curLevel = this.getGenLevelForStack(item);
    }
    
    @Override
    public double getFuelBurn(final ItemStack item) {
        if (item != null) {
            if (item.getItem() instanceof ItemFood) {
                if (((ItemFood)item.getItem()).func_150905_g(item) > 0) {
                    return Math.ceil(this.scale(((ItemFood)item.getItem()).func_150906_h(item), 0.8) * 1800.0);
                }
                return 0.0;
            }
            else if (item.getItem() == Items.cake) {
                return 1500.0;
            }
        }
        return 0.0;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.curLevel = nbt.getDouble("curLevel");
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("curLevel", this.curLevel);
    }
}
