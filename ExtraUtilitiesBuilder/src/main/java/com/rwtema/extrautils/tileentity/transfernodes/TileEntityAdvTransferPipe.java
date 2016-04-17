// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.util.IIcon;
import net.minecraft.inventory.IInventory;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAdvTransferPipe extends TileEntity implements IPipe, IPipeCosmetic
{
    int basePipeType;
    int outputsMask;
    
    public TileEntityAdvTransferPipe() {
        this.basePipeType = 0;
        this.outputsMask = 0;
    }
    
    public static boolean isVPipe(final World world, final int x, final int y, final int z) {
        return false;
    }
    
    public static int getType(final World world, final int x, final int y, final int z) {
        return 0;
    }
    
    public static boolean setBlockType() {
        return false;
    }
    
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return null;
    }
    
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return false;
    }
    
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return false;
    }
    
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return false;
    }
    
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        return 0;
    }
    
    public IInventory getFilterInventory(final IBlockAccess world, final int x, final int y, final int z) {
        return null;
    }
    
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return false;
    }
    
    public String getPipeType() {
        return null;
    }
    
    public IIcon baseTexture() {
        return null;
    }
    
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return null;
    }
    
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return null;
    }
    
    public IIcon socketTexture(final ForgeDirection dir) {
        return null;
    }
    
    public float baseSize() {
        return 0.0f;
    }
}
