// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.item.filters.Matcher;
import codechicken.nei.api.ItemFilter;

public class ItemFilterWrapper implements ItemFilter
{
    private final Matcher base;
    private final boolean invert;
    
    public ItemFilterWrapper(final Matcher base, final boolean invert) {
        this.base = base;
        this.invert = invert;
    }
    
    public ItemFilterWrapper(final Matcher matcher) {
        this(matcher, false);
    }
    
    public boolean matches(final ItemStack itemStack) {
        return this.base.matchItem(itemStack) != this.invert;
    }
}

