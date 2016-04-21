// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart;

import net.minecraft.item.ItemStack;
import scala.collection.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemBlock;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.Microblock;
import codechicken.microblock.HollowMicroblockClient;
import codechicken.microblock.FaceMicroblockClient;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;
import codechicken.lib.vec.Cuboid6;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.block.render.FakeRenderBlocks;

@SideOnly(Side.CLIENT)
public class FakeRenderBlocksMultiPart extends FakeRenderBlocks
{
    public int getSideFromBounds(final Cuboid6 bounds) {
        if (bounds.max.y != 1.0) {
            return 0;
        }
        if (bounds.min.y != 0.0) {
            return 1;
        }
        if (bounds.max.z != 1.0) {
            return 2;
        }
        if (bounds.min.z != 0.0) {
            return 3;
        }
        if (bounds.max.x != 1.0) {
            return 4;
        }
        if (bounds.min.x != 0.0) {
            return 5;
        }
        return -1;
    }
    
    @Override
    public boolean matchBlock(final int side2, final int x2, final int y2, final int z2) {
        if (this.isOpaque) {
            final TileEntity tile_base = this.blockAccess.getTileEntity(x2, y2, z2);
            if (tile_base != null && tile_base instanceof TileMultipart) {
                final TileMultipart tile = (TileMultipart)tile_base;
                final Iterator<TMultiPart> parts = (Iterator<TMultiPart>)tile.partList().toIterator();
                while (parts.hasNext()) {
                    final TMultiPart part = (TMultiPart)parts.next();
                    if ((part instanceof FaceMicroblockClient || part instanceof HollowMicroblockClient) && (side2 == -1 || this.getSideFromBounds(((Microblock)part).getBounds()) == side2)) {
                        final ItemStack t = MicroMaterialRegistry.getMaterial(((Microblock)part).getMaterial()).getItem();
                        if (t.getItem() instanceof ItemBlock && this.curBlock == ((ItemBlock)t.getItem()).field_150939_a && t.getItemDamage() == this.curMeta) {
                            return true;
                        }
                        continue;
                    }
                }
            }
        }
        return super.matchBlock(side2, x2, y2, z2);
    }
}

