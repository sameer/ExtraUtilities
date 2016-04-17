// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import com.rwtema.extrautils.block.Box;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import com.rwtema.extrautils.item.ItemNodeUpgrade;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidRegistry;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.util.Facing;
import net.minecraftforge.fluids.IFluidHandler;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.FluidBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeLiquid;

public class TileEntityTransferNodeLiquid extends TileEntityTransferNode implements INodeLiquid
{
    public boolean nearSource;
    public long checkTimer;
    
    public TileEntityTransferNodeLiquid() {
        super("Liquid", new FluidBuffer());
        this.nearSource = false;
        this.checkTimer = 0L;
        this.pr = 0.001f;
        this.pg = 0.0f;
        this.pb = 1.0f;
    }
    
    public TileEntityTransferNodeLiquid(final String s, final INodeBuffer buffer) {
        super(s, buffer);
        this.nearSource = false;
        this.checkTimer = 0L;
        this.pr = 0.001f;
        this.pg = 0.0f;
        this.pb = 1.0f;
    }
    
    @Override
    public void processBuffer() {
        if (this.worldObj != null && !this.worldObj.isRemote) {
            if (this.coolDown > 0) {
                this.coolDown -= this.stepCoolDown;
            }
            if (this.checkRedstone()) {
                return;
            }
            while (this.coolDown <= 0) {
                this.coolDown += TileEntityTransferNodeLiquid.baseMaxCoolDown;
                if (this.handleInventories()) {
                    this.advPipeSearch();
                }
                this.loadTank();
            }
        }
    }
    
    public void loadTank() {
        final int dir = this.getBlockMetadata() % 6;
        if (this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]) instanceof IFluidHandler) {
            final IFluidHandler source = (IFluidHandler)this.worldObj.getTileEntity(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir]);
            final FluidStack liquid = source.drain(ForgeDirection.getOrientation(dir).getOpposite(), (this.upgradeNo(3) == 0) ? 200 : ((FluidTank)this.buffer.getBuffer()).getCapacity(), false);
            final int k = this.fill(this.getNodeDir(), liquid, false);
            if (k > 0) {
                this.fill(this.getNodeDir(), source.drain(ForgeDirection.getOrientation(dir).getOpposite(), k, true), true);
            }
        }
        else if (!ExtraUtils.disableInfiniteWater && this.upgradeNo(2) > 0) {
            if (this.worldObj.getTotalWorldTime() - this.checkTimer > 20L) {
                this.checkTimer = this.worldObj.getTotalWorldTime();
                this.nearSource = false;
                if (this.isWaterSource(this.xCoord + Facing.offsetsXForSide[dir], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir])) {
                    int n = 0;
                    for (int i = 2; i < 6; ++i) {
                        if (this.isWaterSource(this.xCoord + Facing.offsetsXForSide[dir] + Facing.offsetsXForSide[i], this.yCoord + Facing.offsetsYForSide[dir], this.zCoord + Facing.offsetsZForSide[dir] + Facing.offsetsZForSide[i])) {
                            ++n;
                        }
                    }
                    if (n >= 2) {
                        this.nearSource = true;
                    }
                }
            }
            if (this.nearSource) {
                final long t = this.worldObj.getTotalWorldTime() / TileEntityTransferNode.baseMaxCoolDown * TileEntityTransferNode.baseMaxCoolDown;
                int a = 1000 * TileEntityTransferNode.baseMaxCoolDown / (20 * this.stepCoolDown);
                final float b = 1000.0f * TileEntityTransferNode.baseMaxCoolDown / (20 * this.stepCoolDown);
                if (a != b && b - a > this.worldObj.rand.nextFloat()) {
                    ++a;
                }
                if (a > 0) {
                    this.fill(this.getNodeDir(), new FluidStack(FluidRegistry.WATER, a * (1 + this.upgradeNo(2))), true);
                }
            }
        }
    }
    
    public boolean isWaterSource(final int x, final int y, final int z) {
        final Block id = this.worldObj.getBlock(x, y, z);
        return (id == Blocks.water || id == Blocks.flowing_water) && this.worldObj.getBlockMetadata(x, y, z) == 0;
    }
    
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (from != this.getNodeDir()) {
            return 0;
        }
        if (resource == null) {
            return 0;
        }
        for (int j = 0; j < this.upgrades.getSizeInventory(); ++j) {
            if (this.upgrades.getStackInSlot(j) != null && this.upgrades.getStackInSlot(j).getItemDamage() == 1 && this.upgrades.getStackInSlot(j).getTagCompound() != null && !ItemNodeUpgrade.matchesFilterLiquid(resource, this.upgrades.getStackInSlot(j))) {
                return 0;
            }
        }
        return ((FluidTank)this.buffer.getBuffer()).fill(resource, doFill);
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return null;
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return null;
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return from.ordinal() == this.getBlockMetadata() % 6;
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        return new FluidTankInfo[] { ((FluidTank)this.buffer.getBuffer()).getInfo() };
    }
    
    @Override
    public TileEntityTransferNodeLiquid getNode() {
        return this;
    }
    
    public BoxModel getModel(final ForgeDirection dir) {
        final BoxModel boxes = new BoxModel();
        final float w = 0.125f;
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f).rotateToSide(dir).setTextureSides(dir.ordinal(), BlockTransferNode.nodeBase));
        boxes.add(new Box(0.1875f, 0.0625f, 0.1875f, 0.8125f, 0.25f, 0.8125f).rotateToSide(dir));
        boxes.add(new Box(0.3125f, 0.25f, 0.3125f, 0.6875f, 0.375f, 0.6875f).rotateToSide(dir));
        boxes.add(new Box(0.375f, 0.25f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(BlockTransferNode.nodeBase).setAllSideInvisible().setSideInvisible(dir.getOpposite().ordinal(), false));
        return boxes;
    }
}
