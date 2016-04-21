// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import codechicken.lib.render.uv.UVTransformation;
import codechicken.microblock.MaterialRenderHelper;
import com.rwtema.extrautils.tileentity.TileEntityBlockColorData;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.block.BlockColorData;
import com.rwtema.extrautils.helper.XUHelperClient;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BlockColor;
import codechicken.microblock.BlockMicroMaterial;

public class ColoredBlockMicroMaterial extends BlockMicroMaterial
{
    public ColoredBlockMicroMaterial(final BlockColor block, final int meta) {
        super((Block)block, meta);
    }
    
    @SideOnly(Side.CLIENT)
    public void renderMicroFace(final Vector3 pos, final int pass, final Cuboid6 bounds) {
        float[] col = BlockColor.initColor[this.meta()];
        if (XUHelperClient.clientPlayer().getEntityWorld() != null && pass != -1) {
            col = BlockColorData.getColorData((IBlockAccess)XUHelperClient.clientPlayer().getEntityWorld(), (int)pos.x, (int)pos.y, (int)pos.z, this.meta());
        }
        else {
            final Entity holder = (Entity)XUHelperClient.clientPlayer();
            if (holder != null) {
                final TileEntity tiledata = holder.worldObj.getTileEntity(BlockColorData.dataBlockX((int)Math.floor(holder.posX)), BlockColorData.dataBlockY((int)holder.posY), BlockColorData.dataBlockZ((int)Math.floor(holder.posZ)));
                if (tiledata instanceof TileEntityBlockColorData) {
                    col = ((TileEntityBlockColorData)tiledata).palette[this.meta()];
                }
            }
        }
        final int c = (int)(col[0] * 255.0f) << 24 | (int)(col[1] * 255.0f) << 16 | (int)(col[2] * 255.0f) << 8 | 0xFF;
        MaterialRenderHelper.start(pos, pass, (UVTransformation)this.icont()).blockColour(c).lighting().render();
    }
}

