// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.energy;

import net.minecraft.nbt.NBTTagCompound;

public class EnergyStorage implements IEnergyStorage
{
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    
    public EnergyStorage(final int capacity) {
        this(capacity, capacity, capacity);
    }
    
    public EnergyStorage(final int capacity, final int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }
    
    public EnergyStorage(final int capacity, final int maxReceive, final int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }
    
    public EnergyStorage readFromNBT(final NBTTagCompound nbt) {
        this.energy = nbt.getInteger("Energy");
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }
        return this;
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
        if (this.energy < 0) {
            this.energy = 0;
        }
        nbt.setInteger("Energy", this.energy);
        return nbt;
    }
    
    public void setCapacity(final int capacity) {
        this.capacity = capacity;
        if (this.energy > capacity) {
            this.energy = capacity;
        }
    }
    
    public void setMaxTransfer(final int maxTransfer) {
        this.setMaxReceive(maxTransfer);
        this.setMaxExtract(maxTransfer);
    }
    
    public void setMaxReceive(final int maxReceive) {
        this.maxReceive = maxReceive;
    }
    
    public void setMaxExtract(final int maxExtract) {
        this.maxExtract = maxExtract;
    }
    
    public int getMaxReceive() {
        return this.maxReceive;
    }
    
    public int getMaxExtract() {
        return this.maxExtract;
    }
    
    public void setEnergyStored(final int energy) {
        this.energy = energy;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }
        else if (this.energy < 0) {
            this.energy = 0;
        }
    }
    
    public void modifyEnergyStored(final int energy) {
        this.energy += energy;
        if (this.energy > this.capacity) {
            this.energy = this.capacity;
        }
        else if (this.energy < 0) {
            this.energy = 0;
        }
    }
    
    @Override
    public int receiveEnergy(final int maxReceive, final boolean simulate) {
        final int energyReceived = Math.min(this.capacity - this.energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            this.energy += energyReceived;
        }
        return energyReceived;
    }
    
    @Override
    public int extractEnergy(final int maxExtract, final boolean simulate) {
        final int energyExtracted = Math.min(this.energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            this.energy -= energyExtracted;
        }
        return energyExtracted;
    }
    
    @Override
    public int getEnergyStored() {
        return this.energy;
    }
    
    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }
}

