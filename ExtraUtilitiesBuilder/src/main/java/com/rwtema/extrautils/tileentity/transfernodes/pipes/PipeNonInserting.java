// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public class PipeNonInserting extends PipeBase
{
    public PipeNonInserting() {
        super("NonInserting");
    }
    
    @Override
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return true;
    }
    
    @Override
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return false;
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes_noninserting;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return BlockTransferPipe.pipes_noninserting;
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_noninserting;
    }
}


