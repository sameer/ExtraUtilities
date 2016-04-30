// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeLiquid;
import net.minecraftforge.fluids.IFluidHandler;

public interface INodeLiquid extends IFluidHandler
{
    TileEntityTransferNodeLiquid getNode();
}


