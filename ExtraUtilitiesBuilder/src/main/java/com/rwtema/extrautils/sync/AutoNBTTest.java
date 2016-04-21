// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.sync;

import net.minecraft.nbt.NBTTagCompound;

public class AutoNBTTest extends AutoNBT
{
    @Override
    public void writeToNBT(final NBTTagCompound tag, final Object instance) {
        final NBTTest t = (NBTTest)instance;
        NBTHandlers.NBTHandlerFloat.save(tag, "f", t.hat);
        NBTHandlers.NBTHandlerInt.save(tag, "i", t.hello);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tag, final Object instance) {
    }
}

