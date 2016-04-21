// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidHandler;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class FluidBuffer implements INodeBuffer
{
    public INode node;
    public FluidTank tank;
    
    public FluidBuffer() {
        this.tank = new FluidTank(8000);
    }
    
    @Override
    public boolean transfer(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z, final ForgeDirection travelDir) {
        if (this.isEmpty()) {
            return true;
        }
        if (tile instanceof IFluidHandler) {
            final IFluidHandler destTank = (IFluidHandler)tile;
            int filter = -1;
            boolean eof = false;
            if (insertingPipe != null) {
                filter = insertingPipe.limitTransfer(tile, side, this);
                eof = insertingPipe.getOutputDirections((IBlockAccess)tile.getWorldObj(), x, y, z, travelDir, this).isEmpty();
            }
            if (filter < 0) {
                filter = this.tank.getFluidAmount();
            }
            if (!eof && filter > 1) {
                filter /= 2;
            }
            final FluidStack b = this.tank.getFluid().copy();
            b.amount = Math.min(b.amount, filter);
            filter = destTank.fill(side, b, false);
            final FluidStack c = this.tank.drain(filter, true);
            destTank.fill(side, c, true);
        }
        return true;
    }
    
    @Override
    public Object getBuffer() {
        return this.tank;
    }
    
    @Override
    public String getBufferType() {
        return "fluid";
    }
    
    @Override
    public void setBuffer(final Object buffer) {
        if (buffer instanceof FluidTank) {
            this.tank = (FluidTank)buffer;
        }
    }
    
    @Override
    public boolean isEmpty() {
        if (this.tank.getFluid() == null) {
            return true;
        }
        if (this.tank.getFluid().amount == 0) {
            this.tank.setFluid((FluidStack)null);
            return true;
        }
        return false;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tags) {
        if (tags.hasKey("buffer")) {
            this.tank.readFromNBT(tags.getCompoundTag("buffer"));
        }
        else {
            this.tank = new FluidTank(6400);
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tags) {
        if (this.tank != null) {
            final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            this.tank.writeToNBT(nbttagcompound1);
            tags.setTag("buffer", (NBTBase)nbttagcompound1);
        }
    }
    
    @Override
    public void setNode(final INode node) {
        this.node = node;
    }
    
    @Override
    public INode getNode() {
        return this.node;
    }
    
    @Override
    public boolean transferTo(final INodeBuffer receptor, final int no) {
        if (this.getBuffer() == null || !this.getBufferType().equals(receptor.getBufferType())) {
            return false;
        }
        if (!(receptor.getNode() instanceof IFluidHandler)) {
            return false;
        }
        final ForgeDirection dir = receptor.getNode().getNodeDir();
        final IFluidHandler dest = (IFluidHandler)receptor.getNode();
        final int k = dest.fill(dir, this.tank.drain(200 * no, false), false);
        if (k <= 0) {
            return false;
        }
        dest.fill(dir, this.tank.drain(k, true), true);
        receptor.setBuffer(dest);
        receptor.markDirty();
        return true;
    }
    
    @Override
    public synchronized Object recieve(final Object a) {
        if (!(a instanceof FluidStack)) {
            return a;
        }
        final FluidStack fluidStack;
        final FluidStack c = fluidStack = (FluidStack)a;
        fluidStack.amount -= this.tank.fill(c, true);
        return c;
    }
    
    @Override
    public void markDirty() {
        this.node.bufferChanged();
    }
    
    @Override
    public boolean shouldSearch() {
        return !this.isEmpty();
    }
}

