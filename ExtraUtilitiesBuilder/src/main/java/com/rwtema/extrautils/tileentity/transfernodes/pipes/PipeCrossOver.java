// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;

public class PipeCrossOver extends PipeBase
{
    public PipeCrossOver() {
        super("Crossover");
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes_xover;
    }
    
    @Override
    public float baseSize() {
        return 0.1875f;
    }
    
    @Override
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        final ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
        if (TNHelper.canOutput(world, x, y, z, dir) && TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite())) {
            dirs.add(dir);
        }
        return dirs;
    }
    
    @Override
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        boolean advance = true;
        final IPipe pipe = TNHelper.getPipe(world, x, y, z);
        if (pipe == null) {
            return true;
        }
        if (pipe.shouldConnectToTile(world, x, y, z, dir) && !buffer.transfer(world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ), dir.getOpposite(), pipe, x, y, z, dir)) {
            advance = false;
        }
        return advance;
    }
    
    @Override
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        if (dir == ForgeDirection.UNKNOWN) {
            return false;
        }
        final IPipe pipe = TNHelper.getPipe(world, x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ);
        return pipe != null && (pipe.getPipeType().equals(this.getPipeType()) || pipe.canOutput(world, x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ, dir));
    }
    
    @Override
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        if (dir == ForgeDirection.UNKNOWN) {
            return false;
        }
        final IPipe pipe = TNHelper.getPipe(world, x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ);
        if (pipe == null) {
            return super.shouldConnectToTile(world, x, y, z, dir.getOpposite());
        }
        return pipe.getPipeType().equals(this.getPipeType()) || pipe.canInput(world, x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ, dir);
    }
    
    @Override
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return TNHelper.canOutput(world, x + dir.getOpposite().offsetX, y + dir.getOpposite().offsetY, z + dir.getOpposite().offsetZ, dir) && TNHelper.isValidTileEntity(world, x, y, z, dir);
    }
}
