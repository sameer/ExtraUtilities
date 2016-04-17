// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.tileentity.transfernodes.TNHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public class PipeEnergy extends PipeBase
{
    public PipeEnergy() {
        super("Energy");
    }
    
    public PipeEnergy(final String type) {
        super(type);
    }
    
    @Override
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        if (TNHelper.getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null) {
            return false;
        }
        final TileEntity tile = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        return TNHelper.isRFEnergy(tile, dir.getOpposite());
    }
    
    @Override
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        final IPipe pipe = TNHelper.getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
        return pipe != null && pipe.getPipeType().startsWith("Energy");
    }
    
    @Override
    public IIcon socketTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_nozzle_energy;
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes_energy;
    }
    
    @Override
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return BlockTransferPipe.pipes_energy;
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_energy;
    }
}
