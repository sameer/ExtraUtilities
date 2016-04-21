// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import codechicken.multipart.TMultiPart;
import net.minecraft.item.ItemStack;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.block.Block;
import com.rwtema.extrautils.multipart.ItemBlockMultiPart;

public class ItemBlockTransferPipeMultiPart extends ItemBlockMultiPart
{
    public final int pipePage;
    
    public ItemBlockTransferPipeMultiPart(final Block par1) {
        super(par1);
        this.pipePage = ((BlockTransferPipe)par1).pipePage;
    }
    
    @Override
    public TMultiPart createMultiPart(final World world, final BlockCoord pos, final ItemStack item, final int side) {
        return (TMultiPart)new PipePart(this.pipePage * 16 + this.getMetadata(item.getItemDamage()));
    }
}


