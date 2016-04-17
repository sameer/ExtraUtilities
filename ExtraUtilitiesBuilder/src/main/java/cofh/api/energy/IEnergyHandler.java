// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyHandler extends IEnergyProvider, IEnergyReceiver
{
    int receiveEnergy(final ForgeDirection p0, final int p1, final boolean p2);
    
    int extractEnergy(final ForgeDirection p0, final int p1, final boolean p2);
    
    int getEnergyStored(final ForgeDirection p0);
    
    int getMaxEnergyStored(final ForgeDirection p0);
}
