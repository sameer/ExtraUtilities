// 
// Decompiled by Procyon v0.5.30
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
