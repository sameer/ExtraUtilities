// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package cofh.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;

public class ItemEnergyContainer extends Item implements IEnergyContainerItem
{
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;
    
    public ItemEnergyContainer() {
    }
    
    public ItemEnergyContainer(final int capacity) {
        this(capacity, capacity, capacity);
    }
    
    public ItemEnergyContainer(final int capacity, final int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }
    
    public ItemEnergyContainer(final int capacity, final int maxReceive, final int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }
    
    public ItemEnergyContainer setCapacity(final int capacity) {
        this.capacity = capacity;
        return this;
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
    
    public int receiveEnergy(final ItemStack container, final int maxReceive, final boolean simulate) {
        if (container.stackTagCompound == null) {
            container.stackTagCompound = new NBTTagCompound();
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        final int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyReceived;
    }
    
    public int extractEnergy(final ItemStack container, final int maxExtract, final boolean simulate) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        int energy = container.stackTagCompound.getInteger("Energy");
        final int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate) {
            energy -= energyExtracted;
            container.stackTagCompound.setInteger("Energy", energy);
        }
        return energyExtracted;
    }
    
    public int getEnergyStored(final ItemStack container) {
        if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
            return 0;
        }
        return container.stackTagCompound.getInteger("Energy");
    }
    
    public int getMaxEnergyStored(final ItemStack container) {
        return this.capacity;
    }
}


