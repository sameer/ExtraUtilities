// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.item.ItemStack;

public class Frequency
{
    public final String freq;
    public final String owner;
    
    public Frequency(final String freq, final String owner) {
        this.freq = freq;
        this.owner = owner;
    }
    
    public Frequency(final ItemStack item) {
        this(XUHelper.getAnvilName(item), XUHelper.getPlayerOwner(item));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Frequency that = (Frequency)o;
        return this.freq.equals(that.freq) && this.owner.equals(that.owner);
    }
    
    @Override
    public int hashCode() {
        int result = this.freq.hashCode();
        result = 31 * result + this.owner.hashCode();
        return result;
    }
}

