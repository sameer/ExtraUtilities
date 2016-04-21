// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconConnectedTexture implements IIcon
{
    public final IIcon[] icons;
    private int n;
    
    public IconConnectedTexture(final IIconRegister r, final String texture) {
        this(r.registerIcon(texture), r.registerIcon(texture + "_vertical"), r.registerIcon(texture + "_horizontal"), r.registerIcon(texture + "_corners"), r.registerIcon(texture + "_anticorners"));
    }
    
    public IconConnectedTexture(final IIcon baseIcon, final IIcon verticalIcon, final IIcon horizontalIcon, final IIcon cornerIcon, final IIcon anticornerIcon) {
        (this.icons = new IIcon[5])[0] = cornerIcon;
        this.icons[1] = verticalIcon;
        this.icons[2] = horizontalIcon;
        this.icons[3] = baseIcon;
        this.icons[4] = anticornerIcon;
    }
    
    public void setType(final int i) {
        this.n = i;
    }
    
    public void resetType() {
        this.setType(0);
    }
    
    public float getMinU() {
        return this.icons[this.n].getMinU();
    }
    
    public float getMaxU() {
        return this.icons[this.n].getMaxU();
    }
    
    public float getInterpolatedU(final double par1) {
        final float f = this.getMaxU() - this.getMinU();
        return this.getMinU() + f * ((float)par1 / 16.0f);
    }
    
    public float getMinV() {
        return this.icons[this.n].getMinV();
    }
    
    public float getMaxV() {
        return this.icons[this.n].getMaxV();
    }
    
    public float getInterpolatedV(final double par1) {
        final float f = this.getMaxV() - this.getMinV();
        return this.getMinV() + f * ((float)par1 / 16.0f);
    }
    
    public String getIconName() {
        return this.icons[this.n].getIconName();
    }
    
    @SideOnly(Side.CLIENT)
    public int getIconWidth() {
        return this.icons[this.n].getIconWidth();
    }
    
    @SideOnly(Side.CLIENT)
    public int getIconHeight() {
        return this.icons[this.n].getIconHeight();
    }
}


