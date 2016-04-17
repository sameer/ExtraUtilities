// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraft.inventory.IInventory;
import codechicken.multipart.TMultiPart;
import com.rwtema.extrautils.block.Box;
import codechicken.lib.vec.Cuboid6;
import java.util.List;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNode;
import com.rwtema.extrautils.tileentity.transfernodes.TileEntityTransferNodeInventory;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeInventory;

public class TransferNodePartInventory extends TransferNodePart implements INodeInventory
{
    public TransferNodePartInventory() {
        super(new TileEntityTransferNodeInventory());
    }
    
    public TransferNodePartInventory(final int meta) {
        super(meta, new TileEntityTransferNodeInventory());
    }
    
    public TransferNodePartInventory(final TileEntityTransferNode node) {
        super(node);
    }
    
    public TransferNodePartInventory(final int meta, final TileEntityTransferNodeInventory node) {
        super(meta, node);
    }
    
    @Override
    public void onRemoved() {
        if (!this.getWorld().isRemote && !this.node.buffer.isEmpty()) {
            final List<ItemStack> drops = new ArrayList<ItemStack>();
            final ItemStack item = (ItemStack)this.getNode().buffer.getBuffer();
            drops.add(item);
            this.tile().dropItems((Iterable)drops);
        }
        super.onRemoved();
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        final Box t = new Box(0.125f, 0.0f, 0.125f, 0.875f, 0.375f, 0.875f);
        t.rotateToSide(this.getNodeDir());
        final List<Cuboid6> s = new ArrayList<Cuboid6>();
        s.add(new Cuboid6((double)t.minX, (double)t.minY, (double)t.minZ, (double)t.maxX, (double)t.maxY, (double)t.maxZ));
        return s;
    }
    
    @Override
    public TileEntityTransferNodeInventory getNode() {
        return (TileEntityTransferNodeInventory)this.node;
    }
    
    @Override
    public boolean occlusionTest(final TMultiPart npart) {
        return !(npart instanceof IInventory) && super.occlusionTest(npart);
    }
    
    public String getType() {
        return "extrautils:transfer_node_inv";
    }
}
