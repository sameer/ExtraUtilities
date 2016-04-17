// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import codechicken.multipart.TMultiPart;
import java.util.List;
import java.util.ArrayList;
import com.rwtema.extrautils.block.Box;
import codechicken.lib.vec.Cuboid6;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid;

public class TransferNodePartLiquid extends TransferNodePart implements INodeLiquid
{
    public TransferNodePartLiquid() {
        super(new TileEntityTransferNodeLiquid());
    }
    
    public TransferNodePartLiquid(final TileEntityTransferNode node) {
        super(node);
    }
    
    public TransferNodePartLiquid(final int meta, final TileEntityTransferNodeLiquid node) {
        super(meta, node);
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        final Box t = new Box(0.125f, 0.0f, 0.125f, 0.875f, 0.375f, 0.875f);
        t.rotateToSide(this.getNodeDir());
        final List<Cuboid6> s = new ArrayList<Cuboid6>();
        s.add(new Cuboid6((double)t.minX, (double)t.minY, (double)t.minZ, (double)t.maxX, (double)t.maxY, (double)t.maxZ));
        return s;
    }
    
    @Override
    public TileEntityTransferNodeLiquid getNode() {
        return (TileEntityTransferNodeLiquid)this.node;
    }
    
    @Override
    public boolean occlusionTest(final TMultiPart npart) {
        return !(npart instanceof IFluidHandler) && super.occlusionTest(npart);
    }
    
    public String getType() {
        return "extrautils:transfer_node_liquid";
    }
    
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        return this.getNode().fill(from, resource, doFill);
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return this.getNode().drain(from, resource, doDrain);
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return this.getNode().drain(from, maxDrain, doDrain);
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return this.getNode().canFill(from, fluid);
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return this.getNode().canDrain(from, fluid);
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return this.getNode().getTankInfo(from);
    }
}
