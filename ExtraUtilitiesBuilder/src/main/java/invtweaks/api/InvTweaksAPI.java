// 
// Decompiled by Procyon v0.5.30
// 

package invtweaks.api;

import invtweaks.api.container.ContainerSection;
import net.minecraft.item.ItemStack;

public interface InvTweaksAPI
{
    void addOnLoadListener(final IItemTreeListener p0);
    
    boolean removeOnLoadListener(final IItemTreeListener p0);
    
    void setSortKeyEnabled(final boolean p0);
    
    void setTextboxMode(final boolean p0);
    
    int compareItems(final ItemStack p0, final ItemStack p1);
    
    void sort(final ContainerSection p0, final SortingMethod p1);
}
