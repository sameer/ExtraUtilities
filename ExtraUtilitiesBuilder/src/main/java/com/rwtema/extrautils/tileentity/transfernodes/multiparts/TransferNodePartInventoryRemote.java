// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.Block;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityRetrievalNodeInventory;

public class TransferNodePartInventoryRemote extends TransferNodePartInventory
{
    public TransferNodePartInventoryRemote() {
        super(new TileEntityRetrievalNodeInventory());
    }
    
    public TransferNodePartInventoryRemote(final int meta, final TileEntityRetrievalNodeInventory node) {
        super(meta, node);
    }
    
    @Override
    public String getType() {
        return "extrautils:transfer_node_inv_remote";
    }
    
    @Override
    public Block getBlock() {
        return ExtraUtils.transferNodeRemote;
    }
}


