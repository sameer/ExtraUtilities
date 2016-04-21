// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package cofh.api.energy;

import net.minecraft.item.ItemStack;

public interface IEnergyContainerItem
{
    int receiveEnergy(final ItemStack p0, final int p1, final boolean p2);
    
    int extractEnergy(final ItemStack p0, final int p1, final boolean p2);
    
    int getEnergyStored(final ItemStack p0);
    
    int getMaxEnergyStored(final ItemStack p0);
}


