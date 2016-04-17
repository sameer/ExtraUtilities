// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.texture.LiquidColorRegistry;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDrum extends TileEntity implements IFluidHandler
{
    public static final int numBuckets = 256;
    public static final int defaultCapacity = 256000;
    private FluidTank tank;
    public static int numTicksTilDisplayEmpty;
    public boolean recentlyDrained;
    public boolean recentlyFilled;
    public FluidStack prevFluid;
    boolean sided;
    
    public TileEntityDrum() {
        this.tank = new FluidTank(256000);
        this.recentlyDrained = false;
        this.recentlyFilled = false;
        this.prevFluid = null;
        this.sided = false;
    }
    
    public TileEntityDrum(final int metadata) {
        this.tank = new FluidTank(256000);
        this.recentlyDrained = false;
        this.recentlyFilled = false;
        this.prevFluid = null;
        this.sided = false;
        this.setCapacityFromMetadata(this.blockMetadata = metadata);
    }
    
    public static int getCapacityFromMetadata(final int meta) {
        if (meta == 1) {
            return 65536000;
        }
        return 256000;
    }
    
    public void setCapacityFromMetadata(final int meta) {
        if (meta == 1) {
            this.tank.setCapacity(getCapacityFromMetadata(meta));
        }
    }
    
    public void loadDrumFromNBT(final NBTTagCompound par1NBTTagCompound) {
        this.tank.setFluid((FluidStack)null);
        this.tank.readFromNBT(par1NBTTagCompound.getCompoundTag("tank"));
    }
    
    public void writeDrumToNBT(final NBTTagCompound par1NBTTagCompound) {
        final NBTTagCompound tag = new NBTTagCompound();
        this.tank.writeToNBT(tag);
        par1NBTTagCompound.setTag("tank", (NBTBase)tag);
    }
    
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.loadDrumFromNBT(tag);
        this.tank.setCapacity(tag.getCompoundTag("tank").getInteger("capacity"));
        if (this.tank.getFluid() != null) {
            this.prevFluid = this.tank.getFluid().copy();
        }
    }
    
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);
        this.writeDrumToNBT(tag);
        final NBTTagCompound tag2 = tag.getCompoundTag("tank");
        tag2.setInteger("capacity", this.tank.getCapacity());
        tag.setTag("tank", (NBTBase)tag2);
    }
    
    public boolean canUpdate() {
        return false;
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound t = new NBTTagCompound();
        this.writeDrumToNBT(t);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
    }
    
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        if (!this.worldObj.isRemote) {
            return;
        }
        this.loadDrumFromNBT(pkt.func_148857_g());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }
    
    public void ticked() {
        if (this.recentlyDrained) {
            this.recentlyDrained = false;
            if (this.recentlyFilled) {
                this.recentlyFilled = false;
                this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), TileEntityDrum.numTicksTilDisplayEmpty);
            }
            else {
                this.prevFluid = null;
                this.markDirty();
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }
    
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (this.sided && from.ordinal() > 1) {
            return 0;
        }
        final boolean t2 = this.tank.getFluid() == null;
        final int t3 = this.tank.fill(resource, doFill);
        if (doFill) {
            if (t2 && this.tank.getFluid() != null && !this.tank.getFluid().isFluidEqual(this.prevFluid)) {
                this.prevFluid = this.tank.getFluid().copy();
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }
            if (t3 != 0) {
                this.recentlyFilled = true;
                this.markDirty();
            }
        }
        return t3;
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        if ((this.sided && from.ordinal() > 1) || resource == null || !resource.isFluidEqual(this.tank.getFluid())) {
            return null;
        }
        if (doDrain) {
            this.markDirty();
        }
        return this.drain(from, resource.amount, doDrain);
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        if ((!this.sided || from.ordinal() <= 1) && this.tank.getFluidAmount() > 0) {
            final FluidStack t = this.tank.drain(maxDrain, doDrain);
            if (doDrain && t != null) {
                if (this.tank.getFluidAmount() == 0) {
                    this.recentlyFilled = false;
                    this.recentlyDrained = true;
                    this.worldObj.scheduleBlockUpdate(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), TileEntityDrum.numTicksTilDisplayEmpty);
                }
                this.markDirty();
            }
            return t;
        }
        return null;
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return !this.sided || from.ordinal() <= 1;
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return true;
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return new FluidTankInfo[] { this.tank.getInfo() };
    }
    
    @SideOnly(Side.CLIENT)
    public int getColor() {
        return LiquidColorRegistry.getFluidColor(this.tank.getFluid());
    }
    
    static {
        TileEntityDrum.numTicksTilDisplayEmpty = 100;
    }
}
