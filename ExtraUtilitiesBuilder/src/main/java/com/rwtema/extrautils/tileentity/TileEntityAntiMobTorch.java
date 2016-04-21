// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity;

import com.rwtema.extrautils.EventHandlerServer;
import com.rwtema.extrautils.block.BlockChandelier;
import com.rwtema.extrautils.block.BlockMagnumTorch;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAntiMobTorch extends TileEntity implements IAntiMobTorch
{
    public static int[] getCoord(final TileEntity tile) {
        return new int[] { tile.getWorldObj().provider.dimensionId, tile.xCoord, tile.yCoord, tile.zCoord };
    }
    
    public float getHorizontalTorchRangeSquared() {
        if (this.getBlockType() instanceof BlockMagnumTorch) {
            return 16384.0f;
        }
        if (this.getBlockType() instanceof BlockChandelier) {
            return 256.0f;
        }
        return -1.0f;
    }
    
    public float getVerticalTorchRangeSquared() {
        if (this.getBlockType() instanceof BlockMagnumTorch) {
            return 1024.0f;
        }
        if (this.getBlockType() instanceof BlockChandelier) {
            return 256.0f;
        }
        return -1.0f;
    }
    
    public void invalidate() {
        EventHandlerServer.magnumTorchRegistry.remove(this.getCoord());
        super.invalidate();
    }
    
    public void onChunkUnload() {
        super.onChunkUnload();
        EventHandlerServer.magnumTorchRegistry.remove(this.getCoord());
    }
    
    public int[] getCoord() {
        return getCoord(this);
    }
    
    public void validate() {
        final int[] myCoord = this.getCoord();
        for (int i = 0; i < EventHandlerServer.magnumTorchRegistry.size(); ++i) {
            final int[] coord = EventHandlerServer.magnumTorchRegistry.get(i);
            if (myCoord[0] == coord[0] && myCoord[1] == coord[1] && myCoord[2] == coord[2] && myCoord[3] == coord[3]) {
                return;
            }
        }
        EventHandlerServer.magnumTorchRegistry.add(myCoord);
    }
}


