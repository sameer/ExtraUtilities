// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRainMuffler extends TileEntity
{
    public static final int range = 8;
    public static final int rain_range = 4096;
    public static boolean playerNeedsMuffler;
    public static boolean playerNeedsMufflerInstantCheck;
    public static int curDimension;
    public static int curX;
    public static int curY;
    public static int curZ;
    
    public TileEntityRainMuffler() {
        if (this.worldObj == null || !this.worldObj.isRemote) {
            return;
        }
        if ((TileEntityRainMuffler.curDimension != this.worldObj.provider.dimensionId || TileEntityRainMuffler.playerNeedsMuffler) && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 1 && this.getDistanceFrom(ExtraUtilsMod.proxy.getClientPlayer().posX, ExtraUtilsMod.proxy.getClientPlayer().posY, ExtraUtilsMod.proxy.getClientPlayer().posZ) < 4096.0) {
            TileEntityRainMuffler.curX = this.xCoord;
            TileEntityRainMuffler.curY = this.yCoord;
            TileEntityRainMuffler.curZ = this.zCoord;
            TileEntityRainMuffler.curDimension = this.worldObj.provider.dimensionId;
            TileEntityRainMuffler.playerNeedsMuffler = false;
        }
    }
    
    public void updateEntity() {
        if (this.worldObj == null || !this.worldObj.isRemote) {
            return;
        }
        if (!TileEntityRainMuffler.playerNeedsMufflerInstantCheck && this.worldObj.getWorldTime() % 100L != 0L) {
            return;
        }
        if (TileEntityRainMuffler.playerNeedsMuffler && this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) == 1 && this.getDistanceFrom(ExtraUtilsMod.proxy.getClientPlayer().posX, ExtraUtilsMod.proxy.getClientPlayer().posY, ExtraUtilsMod.proxy.getClientPlayer().posZ) < 4096.0) {
            TileEntityRainMuffler.curX = this.xCoord;
            TileEntityRainMuffler.curY = this.yCoord;
            TileEntityRainMuffler.curZ = this.zCoord;
            TileEntityRainMuffler.curDimension = this.worldObj.provider.dimensionId;
            TileEntityRainMuffler.playerNeedsMuffler = false;
        }
    }
    
    static {
        TileEntityRainMuffler.playerNeedsMuffler = true;
        TileEntityRainMuffler.playerNeedsMufflerInstantCheck = false;
        TileEntityRainMuffler.curDimension = -100;
        TileEntityRainMuffler.curX = -1;
        TileEntityRainMuffler.curY = -1;
        TileEntityRainMuffler.curZ = -1;
    }
}
