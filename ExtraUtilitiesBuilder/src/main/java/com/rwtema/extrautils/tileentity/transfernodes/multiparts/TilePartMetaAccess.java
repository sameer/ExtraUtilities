// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.multipart.minecraft.IPartMeta;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.minecraft.PartMetaAccess;

public class TilePartMetaAccess extends PartMetaAccess
{
    TileEntity tile;
    
    public TilePartMetaAccess(final IPartMeta p, final TileEntity tileEntity) {
        super(p);
        this.tile = tileEntity;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isAirBlock(final int i, final int j, final int k) {
        return (i != this.part.getPos().x || j != this.part.getPos().y || k != this.part.getPos().z) && this.part.getWorld().isAirBlock(i, j, k);
    }
    
    public TileEntity getTileEntity(final int i, final int j, final int k) {
        if (i == this.part.getPos().x && j == this.part.getPos().y && k == this.part.getPos().z) {
            return this.tile;
        }
        return super.getTileEntity(i, j, k);
    }
}

