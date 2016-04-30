// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;
import net.minecraftforge.fluids.IFluidHandler;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;

public class FluidBufferRetrieval extends FluidBuffer
{
    @Override
    public boolean shouldSearch() {
        return this.tank.getFluidAmount() < this.tank.getCapacity();
    }
    
    @Override
    public boolean transfer(final TileEntity tile, final ForgeDirection side, final IPipe insertingPipe, final int x, final int y, final int z, final ForgeDirection travelDir) {
        if (tile instanceof IFluidHandler) {
            final IFluidHandler destTank = (IFluidHandler)tile;
            final int drainmax = (this.node.getNode().upgradeNo(3) == 0) ? 200 : this.tank.getCapacity();
            if (((TileEntityRetrievalNodeLiquid)this.node.getNode()).fill(this.node.getNodeDir(), destTank.drain(side, drainmax, true), true) > 0 && ((TileEntityRetrievalNodeLiquid)this.node.getNode()).fill(this.node.getNodeDir(), destTank.drain(side, drainmax, false), false) > 0) {
                return false;
            }
        }
        return true;
    }
}


