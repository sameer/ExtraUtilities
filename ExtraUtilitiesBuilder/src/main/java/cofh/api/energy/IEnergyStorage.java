// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package cofh.api.energy;

public interface IEnergyStorage
{
    int receiveEnergy(final int p0, final boolean p1);
    
    int extractEnergy(final int p0, final boolean p1);
    
    int getEnergyStored();
    
    int getMaxEnergyStored();
}


