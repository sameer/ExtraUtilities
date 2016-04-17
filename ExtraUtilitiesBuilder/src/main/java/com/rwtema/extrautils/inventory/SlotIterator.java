// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.inventory;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.IInventory;
import java.util.Iterator;

public class SlotIterator implements Iterator<Integer>, Iterable<Integer>
{
    int[] sided;
    int i;
    int size;
    
    public SlotIterator(final IInventory inv, final int side) {
        this.sided = null;
        this.i = 0;
        this.size = 0;
        if (inv instanceof ISidedInventory) {
            this.sided = ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
        }
        else {
            this.size = inv.getSizeInventory();
        }
    }
    
    @Override
    public boolean hasNext() {
        return (this.sided == null) ? (this.i + 1 < this.size) : (this.i + 1 < this.sided.length);
    }
    
    @Override
    public Integer next() {
        ++this.i;
        return (this.sided == null) ? this.i : this.sided[this.i];
    }
    
    @Override
    public void remove() {
    }
    
    @Override
    public Iterator<Integer> iterator() {
        return this;
    }
}
