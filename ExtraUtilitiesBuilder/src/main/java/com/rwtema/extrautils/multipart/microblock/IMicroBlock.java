// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.microblock.MicroMaterialRegistry;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import codechicken.multipart.TMultiPart;

public interface IMicroBlock
{
    int getMetadata();
    
    String getType();
    
    TMultiPart newPart(final boolean p0);
    
    TMultiPart placePart(final ItemStack p0, final EntityPlayer p1, final World p2, final BlockCoord p3, final int p4, final Vector3 p5, final int p6);
    
    void registerPassThroughs();
    
    void renderItem(final ItemStack p0, final MicroMaterialRegistry.IMicroMaterial p1);
    
    boolean hideCreativeTab();
}

