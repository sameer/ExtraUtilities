// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconMultiIcon implements IIcon
{
    public int grid_x;
    public int grid_y;
    public int grid_w;
    public int grid_h;
    public IIcon icon;
    
    public IconMultiIcon(final IIcon icon, final int grid_x, final int grid_y, final int grid_w, final int grid_h) {
        this.grid_x = grid_x;
        this.grid_y = grid_y;
        this.grid_w = grid_w;
        this.grid_h = grid_h;
        this.icon = icon;
    }
    
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return this.icon.getIconWidth() / this.grid_w;
    }
    
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return this.icon.getIconHeight() / this.grid_h;
    }
    
    @SideOnly(Side.CLIENT)
    public float getMinU() {
        return this.icon.getInterpolatedU(this.grid_x / this.grid_w * 16.0);
    }
    
    @SideOnly(Side.CLIENT)
    public float getMaxU() {
        return this.icon.getInterpolatedU((this.grid_x + 1) / this.grid_w * 16.0);
    }
    
    @SideOnly(Side.CLIENT)
    public float getInterpolatedU(final double par1) {
        final float f = this.getMaxU() - this.getMinU();
        return this.getMinU() + f * (float)par1 / 16.0f;
    }
    
    @SideOnly(Side.CLIENT)
    public float getMinV() {
        return this.icon.getInterpolatedV(this.grid_y / this.grid_h * 16.0);
    }
    
    @SideOnly(Side.CLIENT)
    public float getMaxV() {
        return this.icon.getInterpolatedV((this.grid_y + 1) / this.grid_h * 16.0);
    }
    
    @SideOnly(Side.CLIENT)
    public float getInterpolatedV(final double par1) {
        final float f = this.getMaxV() - this.getMinV();
        return this.getMinV() + f * (float)par1 / 16.0f;
    }
    
    @SideOnly(Side.CLIENT)
    public String getIconName() {
        return this.icon.getIconName();
    }
}
