// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import java.util.ArrayList;
import java.util.HashSet;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Collections;
import java.util.Iterator;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeBlock;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import cofh.api.energy.IEnergyConnection;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraft.inventory.ISidedInventory;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.world.IBlockAccess;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;
import net.minecraft.block.Block;
import java.util.Set;
import java.util.Random;

public class TNHelper
{
    public static Random rand;
    public static Set<Block> pipeBlocks;
    public static List<ForgeDirection> directions;
    
    public static IInventory getInventory(final TileEntity tile) {
        if (tile instanceof IInventory) {
            if (tile instanceof TileEntityChest) {
                final int x = tile.xCoord;
                final int y = tile.yCoord;
                final int z = tile.zCoord;
                final Block blockID = tile.getWorldObj().getBlock(x, y, z);
                if (!tile.getWorldObj().isAirBlock(x, y, z) && blockID instanceof BlockChest) {
                    if (tile.getWorldObj().getBlock(x - 1, y, z) == blockID) {
                        return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile.getWorldObj().getTileEntity(x - 1, y, z), (IInventory)tile);
                    }
                    if (tile.getWorldObj().getBlock(x + 1, y, z) == blockID) {
                        return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile, (IInventory)tile.getWorldObj().getTileEntity(x + 1, y, z));
                    }
                    if (tile.getWorldObj().getBlock(x, y, z - 1) == blockID) {
                        return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile.getWorldObj().getTileEntity(x, y, z - 1), (IInventory)tile);
                    }
                    if (tile.getWorldObj().getBlock(x, y, z + 1) == blockID) {
                        return (IInventory)new InventoryLargeChest("container.chestDouble", (IInventory)tile, (IInventory)tile.getWorldObj().getTileEntity(x, y, z + 1));
                    }
                }
            }
            return (IInventory)tile;
        }
        return null;
    }
    
    public static boolean isValidTileEntity(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) == null && isValidTileEntity(world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ), dir.getOpposite().ordinal());
    }
    
    public static boolean isValidTileEntity(final TileEntity inv, final int side) {
        if (inv == null) {
            return false;
        }
        final ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
        final String classname = inv.getClass().toString();
        if (classname.contains("thermalexpansion") && classname.contains("conduit")) {
            return false;
        }
        if (inv instanceof IFluidHandler) {
            final FluidTankInfo[] t = ((IFluidHandler)inv).getTankInfo(forgeSide);
            if (t != null && t.length != 0) {
                return true;
            }
        }
        if (inv instanceof IInventory && ((IInventory)inv).getSizeInventory() > 0) {
            if (!(inv instanceof ISidedInventory)) {
                return true;
            }
            final int[] t2 = ((ISidedInventory)inv).getAccessibleSlotsFromSide(side);
            if (t2 != null && t2.length != 0) {
                return true;
            }
        }
        return isRFEnergy(inv, forgeSide);
    }
    
    public static boolean isRFEnergy(final TileEntity inv, final ForgeDirection forgeSide) {
        return inv instanceof IEnergyConnection && ((IEnergyConnection)inv).canConnectEnergy(forgeSide);
    }
    
    public static boolean isEnergy(final TileEntity inv, final ForgeDirection forgeSide) {
        return isRFEnergy(inv, forgeSide);
    }
    
    public static void initBlocks() {
        for (final Object aBlockRegistry : Block.blockRegistry) {
            final Block i = (Block)aBlockRegistry;
            if (i instanceof IPipe || i instanceof IPipeBlock) {
                TNHelper.pipeBlocks.add(i);
            }
        }
    }
    
    public static IPipe getPipe(final IBlockAccess world, final int x, final int y, final int z) {
        if (world == null) {
            return null;
        }
        if (y < 0 || y >= 256) {
            return null;
        }
        final TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null) {
            if (tile instanceof IPipe) {
                return (IPipe)tile;
            }
            if (tile instanceof IPipeBlock) {
                return ((IPipeBlock)tile).getPipe(world.getBlockMetadata(x, y, z));
            }
        }
        final Block id = world.getBlock(x, y, z);
        if (!id.isAir(world, x, y, z) && TNHelper.pipeBlocks.contains(id)) {
            if (id instanceof IPipe) {
                return (IPipe)id;
            }
            if (id instanceof IPipeBlock) {
                return ((IPipeBlock)id).getPipe(world.getBlockMetadata(x, y, z));
            }
        }
        return null;
    }
    
    public static boolean canInput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        final IPipe pipe = getPipe(world, x, y, z);
        return pipe != null && pipe.canInput(world, x, y, z, dir);
    }
    
    public static boolean canOutput(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        final IPipe pipe = getPipe(world, x, y, z);
        return pipe != null && pipe.canOutput(world, x, y, z, dir);
    }
    
    public static boolean doesPipeConnect(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return (canOutput(world, x, y, z, dir) && canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite())) || (canInput(world, x, y, z, dir) && canOutput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()));
    }
    
    public static ForgeDirection[] randomDirections() {
        Collections.shuffle(TNHelper.directions, TNHelper.rand);
        return TNHelper.directions.toArray(new ForgeDirection[TNHelper.directions.size()]);
    }
    
    static {
        TNHelper.rand = XURandom.getInstance();
        TNHelper.pipeBlocks = new HashSet<Block>();
        TNHelper.directions = new ArrayList<ForgeDirection>();
        for (int i = 0; i < 6; ++i) {
            TNHelper.directions.add(ForgeDirection.getOrientation(i));
        }
    }
}

