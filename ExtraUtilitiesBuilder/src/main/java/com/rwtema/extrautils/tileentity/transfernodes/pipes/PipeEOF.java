// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public class PipeEOF extends PipeBase
{
    public PipeEOF() {
        super("eof");
    }
    
    @Override
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return false;
    }
    
    @Override
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return new ArrayList<ForgeDirection>();
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return BlockTransferPipe.pipes_oneway;
    }
}

