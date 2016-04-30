// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class PipeDirectional extends PipeBase
{
    ForgeDirection outDir;
    
    public PipeDirectional(final ForgeDirection outDir) {
        super("Directional_" + outDir);
        this.outDir = outDir;
    }
    
    @Override
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        final ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
        if (this.canOutput(world, x, y, z, this.outDir) && TNHelper.canInput(world, x + this.outDir.offsetX, y + this.outDir.offsetY, z + this.outDir.offsetZ, this.outDir.getOpposite())) {
            dirs.add(this.outDir);
            return dirs;
        }
        return dirs;
    }
    
    @Override
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return dir == this.outDir;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return (dir == this.outDir) ? BlockTransferPipe.pipes : BlockTransferPipe.pipes_oneway;
    }
}


