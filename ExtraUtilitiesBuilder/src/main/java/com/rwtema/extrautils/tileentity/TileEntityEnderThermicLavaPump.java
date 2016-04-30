// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.util.Facing;
import net.minecraft.init.Blocks;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.world.ChunkCoordIntPair;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import com.rwtema.extrautils.ExtraUtils;
import cofh.api.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.Optional;
import cofh.api.energy.IEnergyHandler;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.TileFluidHandler;

@Optional.InterfaceList({ @Optional.Interface(iface = "buildcraft.api.mj.IBatteryProvider", modid = "BuildCraftAPI|power") })
public class TileEntityEnderThermicLavaPump extends TileFluidHandler implements IFluidHandler, IEnergyHandler
{
    public EntityPlayer owner;
    public boolean finished;
    private ForgeChunkManager.Ticket chunkTicket;
    private FluidTank tank;
    private int pump_y;
    private int chunk_x;
    private int chunk_z;
    private int b;
    private boolean find_new_block;
    private boolean init;
    private int chunk_no;
    private float p;
    private EnergyStorage cofhEnergy;
    
    public TileEntityEnderThermicLavaPump() {
        this.owner = null;
        this.finished = false;
        this.pump_y = -1;
        this.chunk_x = 0;
        this.chunk_z = 0;
        this.b = 0;
        this.find_new_block = false;
        this.init = false;
        this.chunk_no = -1;
        this.p = 0.95f;
        this.cofhEnergy = new EnergyStorage(10000);
        this.tank = new FluidTank(1000);
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }
        if (this.finished) {
            if (this.chunkTicket != null) {
                ForgeChunkManager.releaseTicket(this.chunkTicket);
                this.chunkTicket = null;
            }
            return;
        }
        if (this.chunkTicket == null) {
            boolean valid = false;
            if (ExtraUtils.validDimensionsForEnderPump != null) {
                if (ExtraUtils.allNonVanillaDimensionsValidForEnderPump) {
                    valid = true;
                }
                for (int i = 0; i < ExtraUtils.validDimensionsForEnderPump.length; ++i) {
                    if (ExtraUtils.validDimensionsForEnderPump[i] == this.worldObj.provider.dimensionId) {
                        valid = !valid;
                        break;
                    }
                }
            }
            if (!valid) {
                this.finished = true;
                if (this.owner != null) {
                    this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Pump will not function in this dimension"));
                    this.owner = null;
                }
                this.markDirty();
                return;
            }
            this.chunkTicket = ForgeChunkManager.requestTicket((Object)ExtraUtilsMod.instance, this.worldObj, ForgeChunkManager.Type.NORMAL);
            if (this.chunkTicket == null) {
                this.finished = true;
                if (this.owner != null) {
                    this.owner.addChatComponentMessage((IChatComponent)new ChatComponentText("Unable to assign Chunkloader, this pump will not work"));
                    this.owner = null;
                }
                this.markDirty();
                return;
            }
            this.owner = null;
            this.chunkTicket.getModData().setString("id", "pump");
            this.chunkTicket.getModData().setInteger("pumpX", this.xCoord);
            this.chunkTicket.getModData().setInteger("pumpY", this.yCoord);
            this.chunkTicket.getModData().setInteger("pumpZ", this.zCoord);
            ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
        }
        boolean goAgain = true;
        for (int t = 0; t < 16 && goAgain; ++t) {
            goAgain = false;
            final int bx = this.b >> 4;
            final int bz = this.b & 0xF;
            final int pump_x = (this.chunk_x << 4) + bx;
            final int pump_z = (this.chunk_z << 4) + bz;
            final Block id = this.worldObj.getBlock(pump_x, this.pump_y, pump_z);
            if (this.pump_y >= 0 && XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, false) != null) {
                if ((this.tank.getInfo().fluid == null || this.tank.getInfo().fluid.amount <= 0) && this.cofhEnergy.extractEnergy(100, true) == 100 && this.cofhEnergy.extractEnergy(100, false) > 0) {
                    final FluidStack liquid = XUHelper.drainBlock(this.worldObj, pump_x, this.pump_y, pump_z, true);
                    this.tank.fill(liquid, true);
                    if (this.worldObj.isAirBlock(pump_x, this.pump_y, pump_z)) {
                        if (this.worldObj.rand.nextDouble() < this.p) {
                            this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.stone, 0, 2);
                        }
                        else {
                            this.worldObj.setBlock(pump_x, this.pump_y, pump_z, Blocks.cobblestone, 0, 2);
                        }
                    }
                    --this.pump_y;
                    this.markDirty();
                }
            }
            else {
                goAgain = true;
                if (!this.init) {
                    this.b = 256;
                }
                ++this.b;
                if (this.b >= 256) {
                    this.b = 0;
                    goAgain = false;
                    if (this.init && this.chunk_no > 0) {
                        for (int dx = -2; dx <= 2; ++dx) {
                            for (int dz = -2; dz <= 2; ++dz) {
                                ForgeChunkManager.unforceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
                            }
                        }
                    }
                    this.setChunk(++this.chunk_no);
                    for (int dx = -2; dx <= 2; ++dx) {
                        for (int dz = -2; dz <= 2; ++dz) {
                            ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
                            this.worldObj.getChunkFromChunkCoords(this.chunk_x + dx, this.chunk_z + dz);
                        }
                    }
                    ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
                }
                this.pump_y = this.yCoord - 1;
                this.init = true;
                this.markDirty();
            }
        }
        FluidStack liquid2 = this.tank.getInfo().fluid;
        if (liquid2 != null && liquid2.amount > 0) {
            final int[] seq = XUHelper.rndSeq(6, this.worldObj.rand);
            for (int j = 0; j < 6; ++j) {
                final TileEntity tile = this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[seq[j]], this.yCoord + Facing.offsetsYForSide[seq[j]], this.zCoord + Facing.offsetsZForSide[seq[j]]);
                if (tile instanceof IFluidHandler) {
                    final int moved = ((IFluidHandler)tile).fill(ForgeDirection.values()[seq[j]].getOpposite(), liquid2, true);
                    this.markDirty();
                    this.tank.drain(moved, true);
                    liquid2 = this.tank.getInfo().fluid;
                    if (liquid2 == null) {
                        break;
                    }
                    if (liquid2.amount <= 0) {
                        break;
                    }
                }
            }
        }
    }
    
    public void invalidate() {
        ForgeChunkManager.releaseTicket(this.chunkTicket);
        super.invalidate();
    }
    
    public void onChunkUnload() {
    }
    
    public void setChunk(int chunk_no) {
        final int base_chunk_x = this.xCoord >> 4;
        final int base_chunk_z = this.zCoord >> 4;
        int j = chunk_no;
        if (j == 0) {
            this.chunk_x = base_chunk_x;
            this.chunk_z = base_chunk_z;
            return;
        }
        --j;
        for (int k = 1; k <= 5; ++k) {
            if (j < 4 * k) {
                if (j < k) {
                    this.chunk_x = base_chunk_x + j;
                    this.chunk_z = base_chunk_z + k - j;
                }
                else if (j < 2 * k) {
                    j -= k;
                    this.chunk_x = base_chunk_x + k - j;
                    this.chunk_z = base_chunk_z - j;
                }
                else if (j < 3 * k) {
                    j -= 2 * k;
                    this.chunk_x = base_chunk_x - j;
                    this.chunk_z = base_chunk_z - (k - j);
                }
                else {
                    j -= 3 * k;
                    this.chunk_x = base_chunk_x - (k - j);
                    this.chunk_z = base_chunk_z + j;
                }
                return;
            }
            j -= 4 * k;
        }
        this.finished = true;
        this.markDirty();
        chunk_no = 255;
    }
    
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        if (par1NBTTagCompound.hasKey("block_no") && par1NBTTagCompound.getTag("block_no") instanceof NBTTagInt) {
            this.b = par1NBTTagCompound.getInteger("block_no");
        }
        else {
            LogHelper.info("Extra Utilities: Problem loading EnderPump TileEntity Tag (block_no)", new Object[0]);
        }
        if (par1NBTTagCompound.hasKey("chunk_no") && par1NBTTagCompound.getTag("chunk_no") instanceof NBTTagByte) {
            this.chunk_no = par1NBTTagCompound.getByte("chunk_no");
        }
        else {
            LogHelper.info("Extra Utilities: Problem loading EnderPump TileEntity Tag (chunk_no)", new Object[0]);
        }
        if (this.chunk_no == -128) {
            this.finished = true;
        }
        else {
            this.setChunk(this.chunk_no);
        }
        this.tank.readFromNBT(par1NBTTagCompound.getCompoundTag("tank"));
        this.init = true;
    }
    
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("block_no", this.b);
        if (this.finished) {
            par1NBTTagCompound.setByte("chunk_no", (byte)(-128));
        }
        else {
            par1NBTTagCompound.setByte("chunk_no", (byte)this.chunk_no);
        }
        final NBTTagCompound tank_tags = new NBTTagCompound();
        this.tank.writeToNBT(tank_tags);
        par1NBTTagCompound.setTag("tank", (NBTBase)tank_tags);
        final NBTTagCompound power_tags = new NBTTagCompound();
        par1NBTTagCompound.setTag("power", (NBTBase)power_tags);
    }
    
    public void forceChunkLoading(final ForgeChunkManager.Ticket ticket) {
        if (this.chunkTicket == null) {
            this.chunkTicket = ticket;
        }
        ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.xCoord >> 4, this.zCoord >> 4));
        for (int dx = -2; dx <= 2; ++dx) {
            for (int dz = -2; dz <= 2; ++dz) {
                ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(this.chunk_x + dx, this.chunk_z + dz));
                this.worldObj.getChunkFromChunkCoords(this.chunk_x + dx, this.chunk_z + dz);
            }
        }
    }
    
    public Packet getDescriptionPacket() {
        if (this.finished) {
            final NBTTagCompound t = new NBTTagCompound();
            t.setBoolean("finished", true);
        }
        return null;
    }
    
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        return this.cofhEnergy.receiveEnergy(maxReceive, simulate);
    }
    
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        return this.cofhEnergy.extractEnergy(maxExtract, simulate);
    }
    
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }
    
    public int getEnergyStored(final ForgeDirection from) {
        return this.cofhEnergy.getEnergyStored();
    }
    
    public int getMaxEnergyStored(final ForgeDirection from) {
        return this.cofhEnergy.getMaxEnergyStored();
    }
}


