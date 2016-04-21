// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.RenderBlocks;
import codechicken.multipart.minecraft.IPartMeta;
import codechicken.lib.vec.Vector3;
import net.minecraft.tileentity.TileEntity;
import codechicken.multipart.minecraft.McMetaPart;

public abstract class MCMetaTilePart extends McMetaPart
{
    public MCMetaTilePart(final int meta) {
        super(meta);
    }
    
    public MCMetaTilePart() {
    }
    
    public abstract TileEntity getBlockTile();
    
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(final Vector3 pos, final int pass) {
        if (pass == 0) {
            new RenderBlocks((IBlockAccess)new TilePartMetaAccess((IPartMeta)this, this.getBlockTile())).renderBlockByRenderType(this.getBlock(), this.x(), this.y(), this.z());
            return true;
        }
        return false;
    }
}

