// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraft.inventory.InventoryBasic;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityFilterPipe;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import java.util.Set;
import com.rwtema.extrautils.ExtraUtils;
import java.util.HashSet;
import net.minecraft.block.Block;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.MultiPartRegistry;

public class RegisterPipeParts implements MultiPartRegistry.IPartFactory, MultiPartRegistry.IPartConverter
{
    public TMultiPart createPart(final String name, final boolean client) {
        if (name.equals("extrautils:transfer_pipe_filter")) {
            return (TMultiPart)new FilterPipePart();
        }
        if (name.equals("extrautils:transfer_pipe")) {
            return (TMultiPart)new PipePart();
        }
        return null;
    }
    
    public void init() {
        MultiPartRegistry.registerConverter((MultiPartRegistry.IPartConverter)this);
        MultiPartRegistry.registerParts((MultiPartRegistry.IPartFactory)this, new String[] { "extrautils:transfer_pipe", "extrautils:transfer_pipe_filter" });
    }
    
    public Iterable<Block> blockTypes() {
        final Set<Block> set = new HashSet<Block>();
        set.add(ExtraUtils.transferPipe);
        set.add(ExtraUtils.transferPipe2);
        return set;
    }
    
    public TMultiPart convert(final World world, final BlockCoord pos) {
        final Block id = world.getBlock(pos.x, pos.y, pos.z);
        int meta = world.getBlockMetadata(pos.x, pos.y, pos.z);
        if (id != ExtraUtils.transferPipe && id != ExtraUtils.transferPipe2) {
            return null;
        }
        if (id == ExtraUtils.transferPipe2) {
            meta += 16;
        }
        if (meta != 9) {
            return (TMultiPart)new PipePart(meta);
        }
        if (world.getTileEntity(pos.x, pos.y, pos.z) instanceof TileEntityFilterPipe) {
            final InventoryBasic t = ((TileEntityFilterPipe)world.getTileEntity(pos.x, pos.y, pos.z)).items;
            return (TMultiPart)new FilterPipePart(t);
        }
        return (TMultiPart)new FilterPipePart();
    }
}


