// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.sync;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AutoNBT<T>
{
    public abstract void writeToNBT(final NBTTagCompound p0, final T p1);
    
    public abstract void readFromNBT(final NBTTagCompound p0, final T p1);
}
