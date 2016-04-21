// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraft.inventory.IInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.ItemBufferRetrieval;

public class TileEntityRetrievalNodeInventory extends TileEntityTransferNodeInventory
{
    public TileEntityRetrievalNodeInventory() {
        super("Inv_remote", new ItemBufferRetrieval());
        this.pr = 0.001f;
        this.pg = 1.0f;
        this.pb = 0.0f;
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
                this.coolDown += TileEntityRetrievalNodeInventory.baseMaxCoolDown;
                this.unloadbuffer();
                if (this.handleInventories()) {
                    this.advPipeSearch();
                }
            }
        }
    }
    
    private void unloadbuffer() {
        if (!this.buffer.isEmpty()) {
            final ForgeDirection d = this.getNodeDir();
            final TileEntity tile = this.worldObj.getTileEntity(this.xCoord + d.offsetX, this.yCoord + d.offsetY, this.zCoord + d.offsetZ);
            if (tile != null && tile instanceof IInventory) {
                ((ItemBufferRetrieval)this.buffer).transferBack(tile, d.getOpposite(), this, this.xCoord, this.yCoord, this.zCoord);
            }
        }
    }
}

