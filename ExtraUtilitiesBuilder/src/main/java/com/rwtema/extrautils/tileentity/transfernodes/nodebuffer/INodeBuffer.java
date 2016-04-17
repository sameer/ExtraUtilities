// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;

public interface INodeBuffer
{
    boolean transfer(final TileEntity p0, final ForgeDirection p1, final IPipe p2, final int p3, final int p4, final int p5, final ForgeDirection p6);
    
    Object getBuffer();
    
    String getBufferType();
    
    void setBuffer(final Object p0);
    
    boolean isEmpty();
    
    boolean shouldSearch();
    
    void readFromNBT(final NBTTagCompound p0);
    
    void writeToNBT(final NBTTagCompound p0);
    
    void setNode(final INode p0);
    
    INode getNode();
    
    boolean transferTo(final INodeBuffer p0, final int p1);
    
    Object recieve(final Object p0);
    
    void markDirty();
}
