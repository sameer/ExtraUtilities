// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.energy;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEnergyHandler extends TileEntity implements IEnergyHandler
{
    protected EnergyStorage storage;
    
    public TileEnergyHandler() {
        this.storage = new EnergyStorage(32000);
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.storage.readFromNBT(nbt);
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        this.storage.writeToNBT(nbt);
    }
    
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }
    
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        return this.storage.receiveEnergy(maxReceive, simulate);
    }
    
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        return this.storage.extractEnergy(maxExtract, simulate);
    }
    
    public int getEnergyStored(final ForgeDirection from) {
        return this.storage.getEnergyStored();
    }
    
    public int getMaxEnergyStored(final ForgeDirection from) {
        return this.storage.getMaxEnergyStored();
    }
}
