// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IEmpowerableItem
{
    boolean isEmpowered(final ItemStack p0);
    
    boolean setEmpoweredState(final ItemStack p0, final boolean p1);
    
    void onStateChange(final EntityPlayer p0, final ItemStack p1);
}

