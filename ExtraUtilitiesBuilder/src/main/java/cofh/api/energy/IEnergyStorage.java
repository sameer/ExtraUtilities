// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.energy;

public interface IEnergyStorage
{
    int receiveEnergy(final int p0, final boolean p1);
    
    int extractEnergy(final int p0, final boolean p1);
    
    int getEnergyStored();
    
    int getMaxEnergyStored();
}
