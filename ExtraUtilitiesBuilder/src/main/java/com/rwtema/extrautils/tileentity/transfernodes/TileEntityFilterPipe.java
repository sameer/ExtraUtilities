// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.util.IIcon;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.IBlockAccess;
import net.minecraft.nbt.NBTBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.InventoryBasic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IFilterPipe;
import net.minecraft.tileentity.TileEntity;

public class TileEntityFilterPipe extends TileEntity implements IFilterPipe, IPipe, IPipeCosmetic
{
    public InventoryBasic items;
    
    public TileEntityFilterPipe() {
        this.items = new InventoryBasic("Sorting Pipe", true, 6);
    }
    
    public boolean canUpdate() {
        return false;
    }
    
    public void readFromNBT(final NBTTagCompound tags) {
        super.readFromNBT(tags);
        if (tags.hasKey("items")) {
            final NBTTagCompound item_tags = tags.getCompoundTag("items");
            for (int i = 0; i < this.items.getSizeInventory(); ++i) {
                if (item_tags.hasKey("item_" + i)) {
                    this.items.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(item_tags.getCompoundTag("item_" + i)));
                }
            }
        }
    }
    
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        final NBTTagCompound item_tags = new NBTTagCompound();
        for (int i = 0; i < this.items.getSizeInventory(); ++i) {
            if (this.items.getStackInSlot(i) != null) {
                final NBTTagCompound item = new NBTTagCompound();
                this.items.getStackInSlot(i).writeToNBT(item);
                item_tags.setTag("item_" + i, (NBTBase)item);
            }
        }
        par1NBTTagCompound.setTag("items", (NBTBase)item_tags);
    }
    
    public IInventory getFilterInventory(final IBlockAccess world, final int x, final int y, final int z) {
        return (IInventory)this.items;
    }
    
    public ArrayList<ForgeDirection> getOutputDirections(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return StdPipes.getPipeType(9).getOutputDirections(world, x, y, z, dir, buffer);
    }
    
    public boolean transferItems(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir, final INodeBuffer buffer) {
        return StdPipes.getPipeType(9).transferItems(world, x, y, z, dir, buffer);
    }
    
    public boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return StdPipes.getPipeType(9).canInput(world, x, y, z, dir);
    }
    
    public boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return StdPipes.getPipeType(9).canOutput(world, x, y, z, dir);
    }
    
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        return StdPipes.getPipeType(9).limitTransfer(dest, side, buffer);
    }
    
    public boolean shouldConnectToTile(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return StdPipes.getPipeType(9).shouldConnectToTile(world, x, y, z, dir);
    }
    
    public IIcon baseTexture() {
        return ((IPipeCosmetic)StdPipes.getPipeType(9)).baseTexture();
    }
    
    public IIcon pipeTexture(final ForgeDirection dir, final boolean blocked) {
        return ((IPipeCosmetic)StdPipes.getPipeType(9)).pipeTexture(dir, blocked);
    }
    
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return ((IPipeCosmetic)StdPipes.getPipeType(9)).invPipeTexture(dir);
    }
    
    public IIcon socketTexture(final ForgeDirection dir) {
        return ((IPipeCosmetic)StdPipes.getPipeType(9)).socketTexture(dir);
    }
    
    public String getPipeType() {
        return StdPipes.getPipeType(9).getPipeType();
    }
    
    public float baseSize() {
        return ((IPipeCosmetic)StdPipes.getPipeType(9)).baseSize();
    }
}


