// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.item;

import java.util.Set;
import net.minecraft.item.ItemStack;

public interface IAugmentItem
{
    int getAugmentLevel(final ItemStack p0, final String p1);
    
    Set<String> getAugmentTypes(final ItemStack p0);
}
