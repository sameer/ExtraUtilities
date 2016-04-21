// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraftforge.fluids.FluidTank;
import net.minecraft.util.Facing;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.FluidBufferRetrieval;

public class TileEntityRetrievalNodeLiquid extends TileEntityTransferNodeLiquid
{
    public TileEntityRetrievalNodeLiquid() {
        super("liquid_remote", new FluidBufferRetrieval());
        this.pr = 0.001f;
        this.pg = 1.0f;
        this.pb = 1.0f;
    }
    
    @Override
    public void processBuffer() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (this.coolDown > 0) {
                this.coolDown -= this.stepCoolDown;
            }
            if (this.checkRedstone()) {
                return;
            }
            while (this.coolDown <= 0) {
                this.coolDown += TileEntityRetrievalNodeLiquid.baseMaxCoolDown;
                this.unloadTank();
                if (this.handleInventories()) {
                    this.advPipeSearch();
                }
            }
        }
    }
    
    public void unloadTank() {
        if (this.buffer.isEmpty()) {
            return;
        }
        final int dir = this.getBlockMetadata() % 6;
        final ForgeDirection side = ForgeDirection.getOrientation(dir).getOpposite();
        if (this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) instanceof IFluidHandler) {
            final IFluidHandler dest = (IFluidHandler)this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]);
            final FluidTank tank = (FluidTank)this.buffer.getBuffer();
            final int a = dest.fill(side, tank.getFluid(), this.initDirection());
            if (a > 0) {
                dest.fill(side, tank.drain(a, true), true);
            }
        }
    }
}

