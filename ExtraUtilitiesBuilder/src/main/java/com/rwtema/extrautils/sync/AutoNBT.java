// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.sync;

import net.minecraft.nbt.NBTTagCompound;

public abstract class AutoNBT<T>
{
    public abstract void writeToNBT(final NBTTagCompound p0, final T p1);
    
    public abstract void readFromNBT(final NBTTagCompound p0, final T p1);
}


