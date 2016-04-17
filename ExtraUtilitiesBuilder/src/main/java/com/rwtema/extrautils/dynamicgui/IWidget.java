// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.nbt.NBTTagCompound;

public interface IWidget
{
    int getX();
    
    int getY();
    
    int getW();
    
    int getH();
    
    NBTTagCompound getDescriptionPacket(final boolean p0);
    
    void handleDescriptionPacket(final NBTTagCompound p0);
    
    @SideOnly(Side.CLIENT)
    void renderForeground(final TextureManager p0, final DynamicGui p1, final int p2, final int p3);
    
    @SideOnly(Side.CLIENT)
    void renderBackground(final TextureManager p0, final DynamicGui p1, final int p2, final int p3);
    
    void addToContainer(final DynamicContainer p0);
    
    List<String> getToolTip();
}
