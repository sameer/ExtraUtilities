// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import net.minecraft.world.IBlockAccess;
import java.util.Collection;
import java.util.Iterator;
import cofh.api.energy.IEnergyReceiver;
import cofh.api.energy.IEnergyProvider;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Collections;
import net.minecraft.item.ItemStack;
import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.Loader;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.EnergyBuffer;
import com.rwtema.extrautils.ChunkPos;
import java.util.List;
import com.rwtema.extrautils.modintegration.IC2EnergyHandler;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeEnergy;

public class TileEntityTransferNodeEnergy extends TileEntityTransferNode implements INodeEnergy, IEnergyHandler
{
    public static final int RF_PER_MJ = 10;
    public static final int RF_PER_EU = 4;
    public static final double RFtoBC = 0.1;
    public static final double BCtoRF = 10.0;
    public EnergyStorage powerHandler;
    public IC2EnergyHandler powerHandlerIC2;
    public int powerInserted;
    public boolean lastStep;
    public List<ChunkPos> searchLocations;
    public List<EnergyPosition> clientele;
    public List<EnergyPosition> clientele_temp;
    public Object battery;
    private int search_i;
    
    public TileEntityTransferNodeEnergy() {
        this("Energy", new EnergyBuffer());
    }
    
    public TileEntityTransferNodeEnergy(final String s, final INodeBuffer buffer) {
        super(s, buffer);
        this.powerHandler = new EnergyStorage(10000);
        this.powerHandlerIC2 = null;
        this.powerInserted = 0;
        this.lastStep = false;
        this.searchLocations = new ArrayList<ChunkPos>();
        this.clientele = new ArrayList<EnergyPosition>();
        this.clientele_temp = new ArrayList<EnergyPosition>();
        this.battery = null;
        this.pr = 1.0f;
        this.pg = 0.7f;
        this.pb = 0.0f;
        if (Loader.isModLoaded("IC2")) {
            this.powerHandlerIC2 = new IC2EnergyHandler(this, 512, 1);
        }
    }
    
    public static double RFtoBC(final int RF) {
        return RF * 0.1;
    }
    
    public static int toRF2(final double BC) {
        return (int)Math.floor(BC * 10.0);
    }
    
    public static int toRF3(final double BC) {
        return (int)Math.round(BC * 10.0);
    }
    
    public static int BCtoRF(final double BC) {
        return (int)Math.floor(BC * 10.0);
    }
    
    public static double toIC2(final int k) {
        return k / 4.0;
    }
    
    public static int fromIC2(final double t) {
        return (int)Math.floor(t * 4.0);
    }
    
    public int numMachines() {
        return this.clientele.size();
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.clientele.clear();
        for (int i = 0; tag.hasKey("cx" + i); ++i) {
            this.clientele.add(new EnergyPosition(tag.getInteger("cx" + i), tag.getInteger("cy" + i), tag.getInteger("cz" + i), tag.getByte("cs" + i), tag.getBoolean("ce")));
            this.clientele_temp.add(new EnergyPosition(tag.getInteger("cx" + i), tag.getInteger("cy" + i), tag.getInteger("cz" + i), tag.getByte("cs" + i), tag.getBoolean("ce")));
        }
        this.powerHandler.readFromNBT(tag);
        if (this.powerHandlerIC2 != null) {
            this.powerHandlerIC2.readFromNBT(tag);
        }
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        for (int i = 0; i < this.clientele.size(); ++i) {
            tag.setInteger("cx" + i, this.clientele.get(i).x);
            tag.setInteger("cy" + i, this.clientele.get(i).y);
            tag.setInteger("cz" + i, this.clientele.get(i).z);
            tag.setByte("cs" + i, this.clientele.get(i).side);
            tag.setBoolean("ce", this.clientele.get(i).extract);
        }
        this.powerHandler.writeToNBT(tag);
        if (this.powerHandlerIC2 != null) {
            final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            this.powerHandlerIC2.writeToNBT(nbttagcompound1);
            tag.setTag("buffer", (NBTBase)nbttagcompound1);
        }
    }
    
    @Override
    public boolean checkRedstone() {
        return false;
    }
    
    @Override
    public void processBuffer() {
        if (this.powerHandlerIC2 != null) {
            this.powerHandlerIC2.updateEntity();
            this.powerHandlerIC2.useEnergy(toIC2(this.powerHandler.receiveEnergy(fromIC2(this.powerHandlerIC2.getEnergyStored()), false)));
        }
        if (this.powerHandler.getEnergyStored() > 0) {
            for (int i = 0; i < this.upgrades.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.upgrades.getStackInSlot(i);
                if (itemstack != null && itemstack.getItem() instanceof IEnergyContainerItem) {
                    final int a = ((IEnergyContainerItem)itemstack.getItem()).receiveEnergy(itemstack, this.powerHandler.extractEnergy(10000, true), false);
                    this.powerHandler.extractEnergy(a, false);
                    if (a > 0) {
                        this.markDirty();
                    }
                }
            }
        }
        if (this.upgradeNo(4) > 0) {
            this.powerHandler.setEnergyStored(6400000);
        }
        if (this.coolDown > 0) {
            this.coolDown -= this.stepCoolDown;
        }
        while (this.coolDown <= 0) {
            this.coolDown += TileEntityTransferNodeEnergy.baseMaxCoolDown;
            this.findMachines();
        }
        this.sendEnergy();
    }
    
    @Override
    public void onChunkUnload() {
        if (this.powerHandlerIC2 != null) {
            this.powerHandlerIC2.onChunkUnload();
        }
        super.onChunkUnload();
    }
    
    @Override
    public void invalidate() {
        if (this.powerHandlerIC2 != null) {
            this.powerHandlerIC2.invalidate();
        }
        super.invalidate();
    }
    
    private void sendEnergy() {
        if (this.clientele.size() > 0) {
            Collections.shuffle(this.clientele);
            final List<EnergyPosition> repeat = new ArrayList<EnergyPosition>();
            final int m1 = this.powerHandler.getEnergyStored() / this.clientele.size();
            for (final EnergyPosition client : this.clientele) {
                final int x = this.xCoord + client.x;
                final int y = this.yCoord + client.y;
                final int z = this.zCoord + client.z;
                final TileEntity tile = this.worldObj.getTileEntity(x, y, z);
                final ForgeDirection from = ForgeDirection.getOrientation((int)client.side);
                if (client.extract) {
                    if (!(tile instanceof IEnergyProvider) || !((IEnergyProvider)tile).canConnectEnergy(from)) {
                        continue;
                    }
                    final int a = this.powerHandler.receiveEnergy(((IEnergyProvider)tile).extractEnergy(from, this.powerHandler.getMaxEnergyStored(), true), true);
                    if (a <= 0) {
                        continue;
                    }
                    this.powerHandler.receiveEnergy(((IEnergyProvider)tile).extractEnergy(from, a, false), false);
                }
                else {
                    if (this.powerHandler.getEnergyStored() <= 0 || !(tile instanceof IEnergyReceiver) || !((IEnergyReceiver)tile).canConnectEnergy(from)) {
                        continue;
                    }
                    final IEnergyReceiver machine = (IEnergyReceiver)tile;
                    int a2 = machine.receiveEnergy(from, this.powerHandler.getEnergyStored(), true);
                    if (a2 > m1) {
                        if (m1 >= 1) {
                            a2 = machine.receiveEnergy(from, this.powerHandler.extractEnergy(m1, true), false);
                            this.powerHandler.extractEnergy(a2, false);
                        }
                        repeat.add(client);
                    }
                    else {
                        if (a2 <= 0) {
                            continue;
                        }
                        a2 = machine.receiveEnergy(from, this.powerHandler.extractEnergy(a2, true), false);
                        this.powerHandler.extractEnergy(a2, false);
                    }
                }
            }
            if (this.powerHandler.getEnergyStored() > 0) {
                for (final EnergyPosition aRepeat : repeat) {
                    final int x = this.xCoord + aRepeat.x;
                    final int y = this.yCoord + aRepeat.y;
                    final int z = this.zCoord + aRepeat.z;
                    final TileEntity tile = this.worldObj.getTileEntity(x, y, z);
                    final ForgeDirection from = ForgeDirection.getOrientation((int)aRepeat.side);
                    if (tile instanceof IEnergyReceiver) {
                        final IEnergyReceiver machine = (IEnergyReceiver)tile;
                        final int e = machine.receiveEnergy(from, this.powerHandler.getEnergyStored(), true);
                        if (e <= 0) {
                            continue;
                        }
                        final int a3 = machine.receiveEnergy(from, this.powerHandler.extractEnergy(e, true), false);
                        this.powerHandler.extractEnergy(a3, false);
                    }
                }
            }
        }
    }
    
    public int hashCode() {
        return this.xCoord * 8976890 + this.yCoord * 981131 + this.zCoord;
    }
    
    private void findMachines() {
        ++this.search_i;
        if (this.searchLocations.size() == 0 || this.search_i >= this.searchLocations.size()) {
            this.clientele.clear();
            this.clientele.addAll(this.clientele_temp);
            this.clientele_temp.clear();
            this.search_i = 0;
            this.searchLocations.clear();
            this.searchLocations.add(new ChunkPos(0, 0, 0));
        }
        this.pipe_x = this.searchLocations.get(this.search_i).x;
        this.pipe_y = this.searchLocations.get(this.search_i).y;
        this.pipe_z = this.searchLocations.get(this.search_i).z;
        final int x = this.pipe_x + this.xCoord;
        final int y = this.pipe_y + this.yCoord;
        final int z = this.pipe_z + this.zCoord;
        this.sendParticleUpdate();
        final Block id = this.worldObj.getBlock(x, y, z);
        final IPipe pipeBlock = TNHelper.getPipe((IBlockAccess)this.worldObj, this.xCoord + this.pipe_x, this.yCoord + this.pipe_y, this.zCoord + this.pipe_z);
        if (pipeBlock != null) {
            for (int i = 0; i < 6; ++i) {
                if (pipeBlock.shouldConnectToTile((IBlockAccess)this.worldObj, x, y, z, ForgeDirection.getOrientation(i)) || (pipeBlock instanceof INode && ((INode)pipeBlock).getNodeDir() == ForgeDirection.getOrientation(i))) {
                    final TileEntity tile = this.worldObj.getTileEntity(x + Facing.offsetsXForSide[i], y + Facing.offsetsYForSide[i], z + Facing.offsetsZForSide[i]);
                    if (TNHelper.isEnergy(tile, ForgeDirection.getOrientation(i).getOpposite())) {
                        final EnergyPosition pos = new EnergyPosition(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i], (byte)Facing.oppositeSide[i], "Energy_Extract".equals(pipeBlock.getPipeType()));
                        if (!this.clientele_temp.contains(pos)) {
                            this.clientele_temp.add(pos);
                        }
                        if (!this.clientele.contains(pos)) {
                            this.clientele.add(pos);
                        }
                    }
                }
                else if (TNHelper.doesPipeConnect((IBlockAccess)this.worldObj, x, y, z, ForgeDirection.getOrientation(i)) && !this.searchLocations.contains(new ChunkPos(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i]))) {
                    this.searchLocations.add(new ChunkPos(this.pipe_x + Facing.offsetsXForSide[i], this.pipe_y + Facing.offsetsYForSide[i], this.pipe_z + Facing.offsetsZForSide[i]));
                }
            }
        }
    }
    
    @Override
    public ForgeDirection getNodeDir() {
        return ForgeDirection.UNKNOWN;
    }
    
    @Override
    public void resetSearch() {
        this.powerInserted = 0;
        super.resetSearch();
    }
    
    @Override
    public TileEntityTransferNodeEnergy getNode() {
        return this;
    }
    
    public BoxModel getModel(final ForgeDirection dir) {
        final BoxModel boxes = new BoxModel();
        boxes.add(new Box(0.1875f, 0.3125f, 0.3125f, 0.8125f, 0.6875f, 0.6875f));
        boxes.add(new Box(0.3125f, 0.1875f, 0.3125f, 0.6875f, 0.8125f, 0.6875f));
        boxes.add(new Box(0.3125f, 0.3125f, 0.1875f, 0.6875f, 0.6875f, 0.8125f));
        boxes.add(new Box(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f));
        return boxes;
    }
    
    @Override
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        return this.powerHandler.receiveEnergy(maxReceive, simulate);
    }
    
    @Override
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        return 0;
    }
    
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }
    
    @Override
    public int getEnergyStored(final ForgeDirection from) {
        return this.powerHandler.getEnergyStored();
    }
    
    @Override
    public int getMaxEnergyStored(final ForgeDirection from) {
        return this.powerHandler.getMaxEnergyStored();
    }
    
    public static class EnergyPosition extends ChunkPos
    {
        public byte side;
        public boolean extract;
        
        public EnergyPosition(final int par1, final int par2, final int par3, final byte par4, final boolean extract) {
            super(par1, par2, par3);
            this.side = 7;
            this.extract = false;
            this.side = par4;
            this.extract = extract;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            final EnergyPosition that = (EnergyPosition)o;
            return this.extract == that.extract && this.side == that.side;
        }
        
        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + this.side;
            result = 31 * result + (this.extract ? 1 : 0);
            return result;
        }
    }
}

