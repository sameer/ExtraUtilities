// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.nodebuffer;

import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;

public interface INode extends IPipe
{
    TileEntityTransferNode getNode();
    
    int getNodeX();
    
    int getNodeY();
    
    int getNodeZ();
    
    ForgeDirection getNodeDir();
    
    int getPipeX();
    
    int getPipeY();
    
    int getPipeZ();
    
    int getPipeDir();
    
    List<ItemStack> getUpgrades();
    
    boolean checkRedstone();
    
    BoxModel getModel(final ForgeDirection p0);
    
    String getNodeType();
    
    void bufferChanged();
    
    boolean isPowered();
    
    boolean recalcRedstone();
}
