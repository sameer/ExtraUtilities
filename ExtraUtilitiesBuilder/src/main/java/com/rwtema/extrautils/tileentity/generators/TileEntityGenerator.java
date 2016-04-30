// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidTank;
import cofh.api.energy.IEnergyReceiver;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import com.rwtema.extrautils.sounds.Sounds;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import cofh.api.energy.EnergyStorage;
import com.rwtema.extrautils.sounds.ISoundTile;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGenerator extends TileEntity implements IEnergyHandler, ISoundTile
{
    public static final int capacity = 100000;
    public EnergyStorage storage;
    public byte rotation;
    public double coolDown;
    int c;
    public boolean initPower;
    public boolean playSound;
    private int multiplier;
    private double divisor;
    public static ResourceLocation hum;
    private boolean shouldInit;
    
    public TileEntityGenerator() {
        this.storage = new EnergyStorage(100000);
        this.rotation = 0;
        this.coolDown = 0.0;
        this.c = -1;
        this.initPower = true;
        this.playSound = false;
        this.multiplier = -1;
        this.divisor = -1.0;
        this.shouldInit = true;
    }
    
    public EnergyStorage getStorage() {
        if (this.initPower && this.worldObj != null && !this.worldObj.isRemote && this.worldObj.blockExists(this.x(), this.y(), this.z())) {
            this.initPower = false;
            this.storage.setCapacity(100000 * this.getMultiplier());
            this.storage.setMaxTransfer(100000 * this.getMultiplier());
        }
        return this.storage;
    }
    
    public int getMultiplier() {
        if (this.multiplier == -1) {
            final Block b = this.getBlockType();
            if (b instanceof BlockGenerator) {
                this.multiplier = ((BlockGenerator)b).numGenerators;
            }
            else {
                this.multiplier = 1;
            }
        }
        return this.multiplier;
    }
    
    public void invalidate() {
        super.invalidate();
    }
    
    public double getDivisor() {
        if (this.divisor == -1.0) {
            this.divisor = 1.0 / this.getMultiplier();
        }
        return this.divisor;
    }
    
    public static int getFurnaceBurnTime(final ItemStack item) {
        if (item == null) {
            return 0;
        }
        if (item.getItem() == Items.lava_bucket) {
            return 0;
        }
        return TileEntityFurnace.getItemBurnTime(item);
    }
    
    public TileEntity getTile() {
        return this;
    }
    
    public ResourceLocation getSound() {
        return TileEntityGenerator.hum;
    }
    
    public boolean shouldSoundPlay() {
        return this.playSound;
    }
    
    public static String getCoolDownString(final double time) {
        String s = String.format("%.1f", time % 60.0) + "s";
        int t = (int)time / 60;
        if (t == 0) {
            return s;
        }
        s = t % 60 + "m " + s;
        t /= 60;
        if (t == 0) {
            return s;
        }
        s = t % 24 + "h " + s;
        t /= 24;
        if (t == 0) {
            return s;
        }
        s = t + "d " + s;
        return s;
    }
    
    public int x() {
        return super.xCoord;
    }
    
    public int y() {
        return super.yCoord;
    }
    
    public int z() {
        return super.zCoord;
    }
    
    public boolean isPowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(this.x(), this.y(), this.z());
    }
    
    public String getBlurb(final double coolDown, final double energy) {
        if (coolDown == 0.0) {
            return "";
        }
        return "PowerLevel:\n" + String.format("%.1f", energy) + "\nTime Remaining:\n" + getCoolDownString(coolDown);
    }
    
    public double stepCoolDown() {
        return 1.0;
    }
    
    public int getCompLevel() {
        if (this.c == -1) {
            this.c = this.getStorage().getEnergyStored() * 15 / this.getStorage().getMaxEnergyStored();
        }
        return this.c;
    }
    
    public void checkCompLevel() {
        if (this.getCompLevel() != this.getStorage().getEnergyStored() * 15 / this.getStorage().getMaxEnergyStored()) {
            this.c = this.getStorage().getEnergyStored() * 15 / this.getStorage().getMaxEnergyStored();
            this.worldObj.notifyBlocksOfNeighborChange(this.x(), this.y(), this.z(), this.getBlockType());
        }
    }
    
    public boolean shouldProcess() {
        return false;
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            if (this.shouldInit) {
                this.shouldInit = false;
                Sounds.addGenerator(this);
            }
            return;
        }
        if (this.coolDown > 0.0) {
            if (this.coolDown > 1.0) {
                this.getStorage().receiveEnergy((int)Math.floor(this.genLevel() * this.getMultiplier()), false);
                this.coolDown -= this.stepCoolDown();
            }
            else {
                this.getStorage().receiveEnergy((int)Math.floor(this.coolDown * this.genLevel() * this.getMultiplier()), false);
                this.coolDown = 0.0;
            }
        }
        else {
            this.coolDown = 0.0;
        }
        this.doSpecial();
        if (this.shouldProcess() && (this.getStorage().getEnergyStored() == 0 || this.getStorage().getEnergyStored() < Math.min(this.getStorage().getMaxEnergyStored() - 1000, this.getStorage().getMaxEnergyStored() - this.getMultiplier() * this.genLevel()))) {
            this.processInput();
        }
        if (this.coolDown > 0.0 != this.playSound) {
            this.worldObj.markBlockForUpdate(this.x(), this.y(), this.z());
            this.playSound = (this.coolDown > 0.0);
        }
        if (this.shouldTransmit() && this.getStorage().getEnergyStored() > 0) {
            this.transmitEnergy();
        }
        this.checkCompLevel();
    }
    
    public void doSpecial() {
    }
    
    @SideOnly(Side.CLIENT)
    public void doRandomDisplayTickR(final Random random) {
    }
    
    private void transmitEnergy() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final TileEntity tile = this.worldObj.getTileEntity(this.x() + side.offsetX, this.y() + side.offsetY, this.z() + side.offsetZ);
            if (!(tile instanceof TileEntityGenerator)) {
                if (tile instanceof IEnergyReceiver) {
                    this.getStorage().extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), this.getStorage().extractEnergy(this.transferLimit() * this.getMultiplier(), true), false), false);
                }
            }
        }
    }
    
    public int transferLimit() {
        return this.getStorage().getMaxEnergyStored();
    }
    
    public boolean shouldTransmit() {
        return true;
    }
    
    public int getMaxCoolDown() {
        return 200;
    }
    
    public double getNerfVisor() {
        if (this.getMultiplier() == 1) {
            return 1.0;
        }
        if (this.getMultiplier() <= 8) {
            return 1.0;
        }
        return 1.0;
    }
    
    public final boolean addCoolDown(final double coolDown, final boolean simulate) {
        if (!simulate) {
            this.coolDown += coolDown * this.getDivisor() * this.getNerfVisor();
        }
        return true;
    }
    
    public boolean processInput() {
        return false;
    }
    
    public double genLevel() {
        return 0.0;
    }
    
    public FluidTank[] getTanks() {
        return new FluidTank[0];
    }
    
    public InventoryGeneric getInventory() {
        return null;
    }
    
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        final int energy = nbt.getInteger("Energy");
        if (energy > this.storage.getMaxEnergyStored()) {
            this.storage.setCapacity(energy);
            this.initPower = true;
        }
        this.storage.setEnergyStored(energy);
        if (this.getInventory() != null) {
            this.getInventory().readFromNBT(nbt);
        }
        if (this.getTanks() != null) {
            for (int i = 0; i < this.getTanks().length; ++i) {
                this.getTanks()[i].readFromNBT(nbt.getCompoundTag("Tank_" + i));
            }
        }
        this.rotation = (byte)nbt.getInteger("rotation");
        this.coolDown = nbt.getDouble("coolDown");
        this.playSound = (this.coolDown > 0.0);
    }
    
    public void writeToNBT(final NBTTagCompound nbt) {
        this.getStorage().writeToNBT(nbt);
        if (this.getInventory() != null) {
            this.getInventory().writeToNBT(nbt);
        }
        if (this.getTanks() != null) {
            for (int i = 0; i < this.getTanks().length; ++i) {
                final NBTTagCompound t = new NBTTagCompound();
                this.getTanks()[i].writeToNBT(t);
                nbt.setTag("Tank_" + i, (NBTBase)t);
            }
        }
        nbt.setInteger("rotation", (int)this.rotation);
        nbt.setDouble("coolDown", this.coolDown);
        super.writeToNBT(nbt);
        final NBTTagCompound backup = new NBTTagCompound();
        super.writeToNBT(backup);
        nbt.setTag("backup", (NBTBase)backup);
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound t = new NBTTagCompound();
        t.setByte("d", this.rotation);
        t.setBoolean("s", this.coolDown > 0.0);
        this.playSound = (this.coolDown > 0.0);
        return (Packet)new S35PacketUpdateTileEntity(this.x(), this.y(), this.z(), 4, t);
    }
    
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        if (!this.worldObj.isRemote) {
            return;
        }
        final NBTTagCompound tags = pkt.func_148857_g();
        if (tags.hasKey("d")) {
            if (tags.getByte("d") != this.rotation) {
                this.worldObj.markBlockForUpdate(this.x(), this.y(), this.z());
            }
            this.rotation = tags.getByte("d");
        }
        if (tags.hasKey("s")) {
            this.playSound = tags.getBoolean("s");
            Sounds.refresh();
        }
    }
    
    public int receiveEnergy(final ForgeDirection from, final int maxReceive, final boolean simulate) {
        return 0;
    }
    
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        return this.shouldTransmit() ? this.getStorage().extractEnergy(Math.min(this.transferLimit() * this.getMultiplier(), maxExtract), simulate) : 0;
    }
    
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }
    
    public int getEnergyStored(final ForgeDirection from) {
        return this.getStorage().getEnergyStored();
    }
    
    public int getMaxEnergyStored(final ForgeDirection from) {
        return this.getStorage().getMaxEnergyStored();
    }
    
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        int c = 0;
        for (final FluidTank tank : this.getTanks()) {
            c += tank.fill(resource, doFill);
        }
        return c;
    }
    
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return null;
    }
    
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return null;
    }
    
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return true;
    }
    
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }
    
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
        final FluidTankInfo[] info = new FluidTankInfo[this.getTanks().length];
        for (int i = 0; i < this.getTanks().length; ++i) {
            info[i] = this.getTanks()[i].getInfo();
        }
        return info;
    }
    
    public boolean canExtractItem(final int i, final ItemStack itemstack, final int j) {
        return true;
    }
    
    public void readInvFromTags(final NBTTagCompound tags) {
        if (tags.hasKey("Energy")) {
            this.getStorage().readFromNBT(tags);
        }
    }
    
    public void writeInvToTags(final NBTTagCompound tags) {
        if (this.getStorage().getEnergyStored() > 0) {
            this.getStorage().writeToNBT(tags);
        }
    }
    
    static {
        TileEntityGenerator.hum = new ResourceLocation("extrautils", "ambient.hum");
    }
}


