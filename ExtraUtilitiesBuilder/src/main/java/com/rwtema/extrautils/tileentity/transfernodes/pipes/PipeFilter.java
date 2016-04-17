// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public class PipeFilter extends PipeBase
{
    public PipeFilter() {
        super("Filter");
    }
    
    @Override
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        final ArrayList<ForgeDirection> dirs = new ArrayList<ForgeDirection>();
        if (TNHelper.getPipe(world, x, y, z) != null) {
            final IInventory inv = TNHelper.getPipe(world, x, y, z).getFilterInventory(world, x, y, z);
            if (inv == null) {
                return super.getOutputDirections(world, x, y, z, dir, buffer);
            }
            for (final ForgeDirection d : TNHelper.randomDirections()) {
                if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite())) {
                    final ItemStack filter = inv.getStackInSlot(d.ordinal());
                    if (filter != null && ItemNodeUpgrade.matchesFilterBuffer(buffer, filter)) {
                        dirs.add(d);
                    }
                }
            }
            for (final ForgeDirection d : TNHelper.randomDirections()) {
                if (d != dir.getOpposite() && TNHelper.canOutput(world, x, y, z, d) && TNHelper.canInput(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite()) && inv.getStackInSlot(d.ordinal()) == null) {
                    dirs.add(d);
                }
            }
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
        final IInventory inv = pipe.getFilterInventory(world, x, y, z);
        if (inv == null) {
            return super.transferItems(world, x, y, z, dir, buffer);
        }
        for (final ForgeDirection d : TNHelper.randomDirections()) {
            if (d.getOpposite() != dir) {
                final ItemStack filter = inv.getStackInSlot(d.ordinal());
                if (filter != null && pipe.shouldConnectToTile(world, x, y, z, d) && ItemNodeUpgrade.matchesFilterBuffer(buffer, filter) && !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir)) {
                    advance = false;
                }
            }
        }
        for (final ForgeDirection d : TNHelper.randomDirections()) {
            if (d.getOpposite() != dir) {
                final ItemStack filter = inv.getStackInSlot(d.ordinal());
                if (filter == null && pipe.shouldConnectToTile(world, x, y, z, d) && !buffer.transfer(world.getTileEntity(x + d.offsetX, y + d.offsetY, z + d.offsetZ), d.getOpposite(), pipe, x, y, z, dir)) {
                    advance = false;
                }
            }
        }
        return advance;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        if (dir.ordinal() == 6) {
            return BlockTransferPipe.pipes;
        }
        return BlockTransferPipe.pipes_diamond[dir.ordinal()];
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return this.pipeTexture(dir, false);
    }
}
