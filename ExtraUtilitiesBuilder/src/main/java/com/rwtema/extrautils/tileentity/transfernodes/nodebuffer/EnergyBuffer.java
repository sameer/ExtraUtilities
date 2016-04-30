// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import net.minecraft.nbt.NBTTagCompound;
import cofh.api.energy.EnergyStorage;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;

public class EnergyBuffer implements INodeBuffer
{
    TileEntityTransferNodeEnergy node;
    
    @Override
    public boolean transfer(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z, final ForgeDirection travelDir) {
        return false;
    }
    
    @Override
    public EnergyStorage getBuffer() {
        return this.node.powerHandler;
    }
    
    @Override
    public String getBufferType() {
        return "energy";
    }
    
    @Override
    public void setBuffer(final Object buffer) {
        this.node.powerHandler = (EnergyStorage)buffer;
    }
    
    @Override
    public boolean isEmpty() {
        return this.node.powerHandler.getEnergyStored() == 0;
    }
    
    @Override
    public boolean shouldSearch() {
        return false;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tags) {
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tags) {
    }
    
    @Override
    public void setNode(final INode node) {
        this.node = (TileEntityTransferNodeEnergy)node;
    }
    
    @Override
    public INode getNode() {
        return this.node;
    }
    
    @Override
    public boolean transferTo(final INodeBuffer receptor, final int no) {
        if (this.isEmpty() || !this.getBufferType().equals(receptor.getBufferType())) {
            return false;
        }
        final EnergyStorage t = (EnergyStorage)receptor.getBuffer();
        final int e = t.receiveEnergy(this.getBuffer().extractEnergy(no * 240, true), true);
        if (e <= 0) {
            return false;
        }
        t.receiveEnergy(this.getBuffer().extractEnergy(e, false), false);
        return true;
    }
    
    @Override
    public synchronized Object recieve(final Object a) {
        if (!(a instanceof Integer)) {
            return a;
        }
        final int c = (Integer)a;
        final int b = this.node.powerHandler.receiveEnergy(c, false);
        return c - b;
    }
    
    @Override
    public void markDirty() {
        this.node.bufferChanged();
    }
}


