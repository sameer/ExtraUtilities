// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeLiquid;

public class TransferNodePartLiquidRemote extends TransferNodePartLiquid
{
    public TransferNodePartLiquidRemote() {
        super(new TileEntityRetrievalNodeLiquid());
    }
    
    public TransferNodePartLiquidRemote(final int meta, final TileEntityRetrievalNodeLiquid node) {
        super(meta, node);
    }
    
    @Override
    public String getType() {
        return "extrautils:transfer_node_liquid_remote";
    }
    
    @Override
    public Block getBlock() {
        return ExtraUtils.transferNodeRemote;
    }
}
