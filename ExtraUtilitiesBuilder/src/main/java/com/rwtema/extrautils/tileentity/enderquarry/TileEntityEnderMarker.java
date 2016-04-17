// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import java.util.List;
import net.minecraft.tileentity.TileEntity;

public class TileEntityEnderMarker extends TileEntity implements IChunkLoad
{
    public static List<int[]> markers;
    public boolean init;
    
    public TileEntityEnderMarker() {
        this.init = false;
    }
    
    public static int[] getCoord(final TileEntity tile) {
        return new int[] { tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord };
    }
    
    public boolean canUpdate() {
        return true;
    }
    
    public boolean shouldRefresh(final Block oldID, final Block newID, final int oldMeta, final int newMeta, final World world, final int x, final int y, final int z) {
        return oldID != newID;
    }
    
    public void updateEntity() {
        if (!this.init) {
            this.onChunkLoad();
        }
        super.updateEntity();
    }
    
    public void invalidate() {
        super.invalidate();
        if (this.worldObj.isRemote) {
            return;
        }
        final int[] myCoord = this.getCoord();
        final List<int[]> toUpdate = new ArrayList<int[]>();
        for (int i = 0; i < TileEntityEnderMarker.markers.size(); ++i) {
            final int[] coord = TileEntityEnderMarker.markers.get(i);
            if (myCoord[0] == coord[0] && myCoord[2] == coord[2]) {
                if (myCoord[3] == coord[3] && myCoord[1] == coord[1]) {
                    TileEntityEnderMarker.markers.remove(i);
                    --i;
                }
                else if (myCoord[3] == coord[3] || myCoord[1] == coord[1]) {
                    toUpdate.add(coord);
                }
            }
        }
        final Iterator<int[]> i$ = toUpdate.iterator();
        while (i$.hasNext()) {
            final int[] coord = i$.next();
            final TileEntity tile = this.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
            if (tile instanceof TileEntityEnderMarker) {
                ((TileEntityEnderMarker)tile).recheck();
            }
        }
    }
    
    public int[] getCoord() {
        return getCoord(this);
    }
    
    public void recheck() {
        final int[] myCoord = this.getCoord();
        int flag = 0;
        for (final int[] coord : TileEntityEnderMarker.markers) {
            if (myCoord[0] == coord[0] && myCoord[2] == coord[2]) {
                if (myCoord[1] == coord[1] && myCoord[3] == coord[3]) {
                    continue;
                }
                if (myCoord[1] == coord[1]) {
                    flag |= ((myCoord[3] < coord[3]) ? 1 : 2);
                }
                else {
                    if (myCoord[3] != coord[3]) {
                        continue;
                    }
                    flag |= ((myCoord[1] < coord[1]) ? 4 : 8);
                }
            }
        }
        this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, flag, 2);
    }
    
    public void onChunkLoad() {
        if (this.init) {
            return;
        }
        this.init = true;
        if (this.worldObj == null || this.worldObj.isRemote) {
            return;
        }
        final int[] myCoord = this.getCoord();
        final List<int[]> toUpdate = new ArrayList<int[]>();
        for (final int[] coord : TileEntityEnderMarker.markers) {
            if (myCoord[0] == coord[0] && myCoord[2] == coord[2]) {
                if (myCoord[3] == coord[3] && myCoord[1] == coord[1]) {
                    return;
                }
                if (myCoord[3] != coord[3] && myCoord[1] != coord[1]) {
                    continue;
                }
                toUpdate.add(coord);
            }
        }
        TileEntityEnderMarker.markers.add(myCoord);
        this.recheck();
        for (final int[] coord : toUpdate) {
            final TileEntity tile = this.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
            if (tile instanceof TileEntityEnderMarker) {
                ((TileEntityEnderMarker)tile).recheck();
            }
        }
    }
    
    static {
        TileEntityEnderMarker.markers = new ArrayList<int[]>();
    }
}
