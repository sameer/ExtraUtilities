// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes;

public class TileEntityTransferNodeHyperEnergy extends TileEntityTransferNodeEnergy
{
    public TileEntityTransferNodeHyperEnergy() {
        final int capacity = 1000000;
        this.powerHandler.setCapacity(1000000);
        this.powerHandler.setMaxExtract(1000000);
        this.powerHandler.setMaxReceive(1000000);
    }
}


