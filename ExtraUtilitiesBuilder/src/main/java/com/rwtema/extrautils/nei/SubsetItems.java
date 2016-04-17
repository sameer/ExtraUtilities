// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei;

import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import java.util.ArrayList;
import codechicken.nei.api.ItemFilter;

public class SubsetItems implements ItemFilter
{
    public ArrayList<Item> items;
    
    public SubsetItems(final Item... items) {
        this.items = new ArrayList<Item>();
        for (final Item i : items) {
            if (i != null) {
                this.items.add(i);
            }
        }
    }
    
    public SubsetItems addItem(final Item item) {
        this.items.add(item);
        return this;
    }
    
    public boolean matches(final ItemStack item) {
        for (final Item i : this.items) {
            if (i.equals(item.getItem())) {
                return true;
            }
        }
        return false;
    }
}
