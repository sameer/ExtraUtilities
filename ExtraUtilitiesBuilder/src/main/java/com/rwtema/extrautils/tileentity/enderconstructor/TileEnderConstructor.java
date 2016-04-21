// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.nbt.NBTBase;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.sounds.Sounds;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import cofh.api.energy.EnergyStorage;
import java.util.Random;
import com.rwtema.extrautils.sounds.ISoundTile;
import com.rwtema.extrautils.tileentity.enderquarry.IChunkLoad;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;

public class TileEnderConstructor extends TileEntity implements IEnderFluxHandler, ISidedInventory, IChunkLoad, ISoundTile
{
    static Random rand;
    public EnergyStorage energy;
    public InventoryKraft inv;
    public ItemStack outputslot;
    int coolDown;
    ResourceLocation location;
    
    public TileEnderConstructor() {
        this.energy = new CustomEnergy(20000);
        this.inv = new InventoryKraft(this);
        this.outputslot = null;
        this.coolDown = 0;
        this.location = new ResourceLocation("extrautils", "ambient.qed");
    }
    
    public void setWorldObj(final World p_145834_1_) {
        super.setWorldObj(p_145834_1_);
        if (p_145834_1_ != null && p_145834_1_.isRemote) {
            Sounds.registerSoundTile(this);
        }
    }
    
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        this.energy.readFromNBT(par1NBTTagCompound);
        this.inv.readFromNBT(par1NBTTagCompound);
        this.outputslot = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("output"));
    }
    
    public void onChunkLoad() {
        this.inv.markDirty();
    }
    
    public void markDirty() {
        if (this.worldObj != null) {
            super.markDirty();
            this.inv.markDirty();
        }
    }
    
    public int getSizeInventory() {
        return 10;
    }
    
    public ItemStack getStackInSlot(final int i) {
        if (i >= 9) {
            return this.outputslot;
        }
        return this.inv.matrix.getStackInSlot(i);
    }
    
    public ItemStack decrStackSize(final int i, final int j) {
        if (i != 9) {
            return null;
        }
        if (this.outputslot == null) {
            return null;
        }
        ItemStack itemstack;
        if (this.outputslot.stackSize <= j) {
            itemstack = this.outputslot;
            this.outputslot = null;
            this.markDirty();
        }
        else {
            itemstack = this.outputslot.splitStack(j);
            if (this.outputslot.stackSize == 0) {
                this.outputslot = null;
            }
            this.markDirty();
        }
        return itemstack;
    }
    
    public ItemStack getStackInSlotOnClosing(final int i) {
        return this.getStackInSlot(i);
    }
    
    public void setInventorySlotContents(final int i, final ItemStack itemstack) {
        if (i == 9) {
            this.outputslot = itemstack;
        }
        else {
            this.inv.setInventorySlotContents(i, itemstack);
        }
    }
    
    public String getInventoryName() {
        return this.inv.getInventoryName();
    }
    
    public boolean hasCustomInventoryName() {
        return false;
    }
    
    public int getInventoryStackLimit() {
        return 64;
    }
    
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return true;
    }
    
    public void openInventory() {
    }
    
    public void closeInventory() {
    }
    
    public boolean isItemValidForSlot(final int i, final ItemStack itemstack) {
        return i != 9 && this.inv.getStackInSlot(i) != null && itemstack != null && XUHelper.canItemsStack(itemstack, this.inv.getStackInSlot(i));
    }
    
    public int[] getAccessibleSlotsFromSide(final int var1) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
    }
    
    public boolean canInsertItem(final int i, final ItemStack itemstack, final int j) {
        return false;
    }
    
    public boolean canExtractItem(final int i, final ItemStack itemstack, final int j) {
        return i == 9;
    }
    
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        this.energy.writeToNBT(par1NBTTagCompound);
        this.inv.writeToNBT(par1NBTTagCompound);
        if (this.outputslot != null) {
            final NBTTagCompound output = new NBTTagCompound();
            this.outputslot.writeToNBT(output);
            par1NBTTagCompound.setTag("output", (NBTBase)output);
        }
    }
    
    public int getDisplayProgress() {
        return this.energy.getEnergyStored() * 22 / this.energy.getMaxEnergyStored();
    }
    
    public boolean isActive() {
        return (this.getBlockMetadata() == 1 && this.getWorldObj().isRemote) || (!this.getWorldObj().isRemote && this.canAddMorez());
    }
    
    public int recieveEnergy(final int amount, final Transfer simulate) {
        return this.energy.receiveEnergy(amount, simulate == Transfer.SIMULATE);
    }
    
    public float getAmountRequested() {
        return this.energy.getMaxEnergyStored() - this.energy.getEnergyStored();
    }
    
    public boolean canAddMorez() {
        final ItemStack item = this.inv.getStackInSlot(9);
        return item != null && (this.outputslot == null || (XUHelper.canItemsStack(item, this.outputslot) && this.outputslot.stackSize + item.stackSize <= this.outputslot.getMaxStackSize() && this.outputslot.stackSize + item.stackSize <= this.getInventoryStackLimit()));
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote && this.getBlockMetadata() == 1) {
            final double dx1 = this.xCoord + TileEnderConstructor.rand.nextDouble();
            final double dy1 = this.yCoord + TileEnderConstructor.rand.nextDouble();
            final double dz1 = this.zCoord + TileEnderConstructor.rand.nextDouble();
            final double dx2 = this.xCoord + TileEnderConstructor.rand.nextDouble();
            final double dy2 = this.yCoord + TileEnderConstructor.rand.nextDouble();
            final double dz2 = this.zCoord + TileEnderConstructor.rand.nextDouble();
            this.worldObj.spawnParticle("portal", dx1, dy1, dz1, dx2 - dx1, dy2 - dy1, dz2 - dz1);
        }
        if (!this.worldObj.isRemote) {
            int newMeta = -1;
            if (this.energy.getEnergyStored() == this.energy.getMaxEnergyStored() && this.canAddMorez()) {
                final ItemStack result = this.inv.result.getStackInSlot(0).copy();
                for (int i = 0; i < 9; ++i) {
                    this.inv.matrix.decrStackSize(i, 1);
                }
                this.inv.result.markDirty(this.inv.matrix);
                if (this.outputslot == null) {
                    this.outputslot = result;
                }
                else {
                    final ItemStack outputslot = this.outputslot;
                    outputslot.stackSize += result.stackSize;
                }
                this.energy.setEnergyStored(0);
                if (!this.canAddMorez()) {
                    newMeta = 4;
                }
            }
            if (this.energy.getEnergyStored() > 0) {
                if (this.canAddMorez()) {
                    newMeta = 1;
                    this.coolDown = 20;
                }
                else {
                    this.energy.extractEnergy(1, false);
                }
            }
            if (this.coolDown > 0) {
                --this.coolDown;
                if (this.coolDown == 0) {
                    newMeta = 0;
                }
            }
            if (newMeta != -1 && newMeta != this.getBlockMetadata()) {
                this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, newMeta, 2);
            }
        }
    }
    
    public boolean shouldSoundPlay() {
        return this.getBlockMetadata() == 1;
    }
    
    public ResourceLocation getSound() {
        return this.location;
    }
    
    public TileEntity getTile() {
        return this;
    }
    
    static {
        TileEnderConstructor.rand = XURandom.getInstance();
    }
    
    public static class CustomEnergy extends EnergyStorage
    {
        public CustomEnergy(final int capacity) {
            super(capacity);
        }
        
        @Override
        public EnergyStorage readFromNBT(final NBTTagCompound nbt) {
            return super.readFromNBT(nbt);
        }
        
        @Override
        public void setEnergyStored(final int energy) {
            super.setEnergyStored(energy);
        }
        
        @Override
        public int receiveEnergy(final int maxReceive, final boolean simulate) {
            return super.receiveEnergy(maxReceive, simulate);
        }
        
        @Override
        public int extractEnergy(final int maxExtract, final boolean simulate) {
            return super.extractEnergy(maxExtract, simulate);
        }
        
        @Override
        public void setCapacity(final int capacity) {
            super.setCapacity(capacity);
        }
    }
}

