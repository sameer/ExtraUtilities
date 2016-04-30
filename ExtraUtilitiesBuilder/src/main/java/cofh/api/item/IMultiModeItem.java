// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package cofh.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMultiModeItem
{
    int getMode(final ItemStack p0);
    
    boolean setMode(final ItemStack p0, final int p1);
    
    boolean incrMode(final ItemStack p0);
    
    boolean decrMode(final ItemStack p0);
    
    int getNumModes(final ItemStack p0);
    
    void onModeChange(final EntityPlayer p0, final ItemStack p1);
}


