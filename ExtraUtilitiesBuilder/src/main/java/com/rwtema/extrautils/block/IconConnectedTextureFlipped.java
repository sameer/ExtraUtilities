// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconConnectedTextureFlipped extends IconConnectedTexture
{
    public IconConnectedTextureFlipped(final IconConnectedTexture icon) {
        super(icon.icons[3], icon.icons[1], icon.icons[2], icon.icons[0], icon.icons[4]);
    }
    
    @Override
    public float getMinV() {
        return super.getMaxV();
    }
    
    @Override
    public float getMaxV() {
        return super.getMinV();
    }
}
