// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraftforge.common.util.ForgeDirection;
import codechicken.multipart.TMultiPart;
import java.util.List;
import java.util.ArrayList;
import codechicken.lib.vec.Cuboid6;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import cpw.mods.fml.common.Optional;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy;

@Optional.InterfaceList({ @Optional.Interface(iface = "buildcraft.api.mj.ISidedBatteryProvider", modid = "BuildCraftAPI|power") })
public class TransferNodePartEnergy extends TransferNodePart implements INodeEnergy, IEnergyHandler
{
    public TransferNodePartEnergy(final TileEntityTransferNode node) {
        super(node);
    }
    
    public TransferNodePartEnergy() {
        super(new TileEntityTransferNodeEnergy());
    }
    
    public TransferNodePartEnergy(final int meta, final TileEntityTransferNodeEnergy node) {
        super(meta, node);
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        final List<Cuboid6> s = new ArrayList<Cuboid6>();
        s.add(new Cuboid6(0.1875, 0.1875, 0.1875, 0.8125, 0.8125, 0.8125));
        return s;
    }
    
    @Override
    public TileEntityTransferNodeEnergy getNode() {
        return (TileEntityTransferNodeEnergy)this.node;
    }
    
    @Override
    public boolean occlusionTest(final TMultiPart npart) {
        return super.occlusionTest(npart);
    }
    
    public String getType() {
        return "extrautils:transfer_node_energy";
    }
    
    @Override
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        if (this.isBlocked(from)) {
            return 0;
        }
        return this.getNode().receiveEnergy(from, maxReceive, simulate);
    }
    
    @Override
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        if (this.isBlocked(from)) {
            return 0;
        }
        return this.getNode().extractEnergy(from, maxExtract, simulate);
    }
    
    public boolean canConnectEnergy(final ForgeDirection from) {
        return !this.isBlocked(from) && this.getNode().canConnectEnergy(from);
    }
    
    @Override
    public int getEnergyStored(final ForgeDirection from) {
        if (this.isBlocked(from)) {
            return 0;
        }
        return this.getNode().getEnergyStored(from);
    }
    
    @Override
    public int getMaxEnergyStored(final ForgeDirection from) {
        if (this.isBlocked(from)) {
            return 0;
        }
        return this.getNode().getMaxEnergyStored(from);
    }
}


