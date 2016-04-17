// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeEnergy;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeHyperEnergy;

public class TransferNodePartHyperEnergy extends TransferNodePartEnergy
{
    public TransferNodePartHyperEnergy() {
        super(new TileEntityTransferNodeHyperEnergy());
    }
    
    public TransferNodePartHyperEnergy(final int meta, final TileEntityTransferNodeHyperEnergy node) {
        super(meta, node);
    }
    
    @Override
    public String getType() {
        return "extrautils:transfer_node_energy_hyper";
    }
}
