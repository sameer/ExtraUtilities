// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import net.minecraft.nbt.NBTTagCompound;

public abstract class WidgetBase implements IWidget
{
    int x;
    int y;
    int w;
    int h;
    
    public WidgetBase(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    @Override
    public int getX() {
        return this.x;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    @Override
    public int getW() {
        return this.w;
    }
    
    @Override
    public int getH() {
        return this.h;
    }
    
    @Override
    public NBTTagCompound getDescriptionPacket(final boolean changesOnly) {
        return null;
    }
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
    }
    
    @Override
    public void addToContainer(final DynamicContainer container) {
    }
}
