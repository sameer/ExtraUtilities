// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import net.minecraftforge.fluids.IFluidHandler;

public interface INodeLiquid extends IFluidHandler
{
    TileEntityTransferNodeLiquid getNode();
}
