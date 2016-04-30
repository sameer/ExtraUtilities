// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.block.BlockColor;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBlockColorData extends TileEntity
{
    public float[][] palette;
    private int rerenderTimer;
    private int rerenderDelay;
    
    public TileEntityBlockColorData() {
        this.palette = new float[16][3];
        this.rerenderTimer = 0;
        this.rerenderDelay = 20;
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (this.palette[i][j] == 0.0f) {
                    this.palette[i][j] = BlockColor.initColor[i][j];
                }
            }
        }
    }
    
    public void readFromNBT(final NBTTagCompound par1NBTTagCompound) {
        super.readFromNBT(par1NBTTagCompound);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (par1NBTTagCompound.hasKey("col" + i + "_" + j)) {
                    if (par1NBTTagCompound.getTag("col" + i + "_" + j) instanceof NBTTagFloat) {
                        this.palette[i][j] = par1NBTTagCompound.getFloat("col" + i + "_" + j);
                    }
                }
                else {
                    this.palette[i][j] = BlockColor.initColor[i][j];
                }
            }
        }
    }
    
    public void writeToNBT(final NBTTagCompound par1NBTTagCompound) {
        super.writeToNBT(par1NBTTagCompound);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 3; ++j) {
                par1NBTTagCompound.setFloat("col" + i + "_" + j, this.palette[i][j]);
            }
        }
    }
    
    public void setColor(final int metadata, final float r, final float g, final float b) {
        if (this.worldObj.isRemote) {
            return;
        }
        if (this.palette[metadata][0] == r && this.palette[metadata][1] == g && this.palette[metadata][2] == b) {
            return;
        }
        this.palette[metadata][0] = r;
        this.palette[metadata][1] = g;
        this.palette[metadata][2] = b;
        boolean notDefault = false;
        for (int i = 0; i < 16 && !notDefault; ++i) {
            for (int j = 0; j < 3 && !notDefault; ++j) {
                if (this.palette[i][j] != BlockColor.initColor[i][j]) {
                    notDefault = true;
                    break;
                }
            }
        }
        if (notDefault) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
        else {
            this.worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
        }
        this.markDirty();
    }
    
    public Packet getDescriptionPacket() {
        final NBTTagCompound t = new NBTTagCompound();
        this.writeToNBT(t);
        return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 4, t);
    }
    
    @SideOnly(Side.CLIENT)
    public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        if (this.worldObj.isRemote && this.rerenderTimer == 0) {
            this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, 0, this.zCoord, this.xCoord + 16, 255, this.zCoord + 16);
            this.rerenderTimer = this.rerenderDelay;
            this.rerenderDelay *= (int)1.1;
        }
    }
    
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            if (this.rerenderTimer > 0) {
                --this.rerenderTimer;
                if (this.rerenderTimer == 0) {
                    this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, 0, this.zCoord, this.xCoord + 16, 255, this.zCoord + 16);
                }
            }
            else if (this.rerenderDelay > 10) {
                --this.rerenderDelay;
            }
            else {
                this.rerenderDelay = 10;
            }
        }
    }
}


