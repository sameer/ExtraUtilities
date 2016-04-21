// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public class PipeBase implements IPipe, IPipeCosmetic
{
    public String type;
    
    public PipeBase(final String type) {
        this.type = type;
    }
    
    @Override
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        final ArrayList<ForgeDirection> results = new ArrayList<ForgeDirection>();
        for (final ForgeDirection d : TNHelper.randomDirections()) {
            if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite())) {
                results.add(d);
            }
        }
        return results;
    }
    
    @Override
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        final IPipe pipe = TNHelper.getPipe(world, x, y, z);
        if (pipe == null) {
            return true;
        }
        boolean advance = true;
        for (final ForgeDirection d : TNHelper.randomDirections()) {
            if (pipe.shouldConnectToTile(world, x, y, z, d) && !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir)) {
                advance = false;
            }
        }
        return advance;
    }
    
    @Override
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return true;
    }
    
    @Override
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return true;
    }
    
    @Override
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        return -1;
    }
    
    @Override
    public IInventory getFilterInventory(final IBlockAccess world, final int x, final int y, final int z) {
        return null;
    }
    
    @Override
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return TNHelper.isValidTileEntity(world, x, y, z, dir);
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        if (blocked) {
            return BlockTransferPipe.pipes_1way;
        }
        return BlockTransferPipe.pipes;
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes;
    }
    
    @Override
    public IIcon socketTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_nozzle;
    }
    
    @Override
    public String getPipeType() {
        return this.type;
    }
    
    @Override
    public float baseSize() {
        return 0.125f;
    }
}


