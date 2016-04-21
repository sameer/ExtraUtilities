// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.pipes;

import com.rwtema.extrautils.tileentity.transfernodes.BlockTransferPipe;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.tileentity.transfernodes.InvHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.tileentity.TileEntity;

public class PipeRationing extends PipeBase
{
    public PipeRationing() {
        super("Rationing");
    }
    
    @Override
    public int limitTransfer(final TileEntity dest, final ForgeDirection side, final INodeBuffer buffer) {
        if (buffer.getBuffer() instanceof ItemStack && dest instanceof IInventory) {
            final ItemStack item = (ItemStack)buffer.getBuffer();
            final IInventory inv = (IInventory)dest;
            int n = Math.min(inv.getInventoryStackLimit(), item.getMaxStackSize());
            for (final int i : InvHelper.getSlots(inv, side.ordinal())) {
                if (inv.getStackInSlot(i) != null && InvHelper.canStack(inv.getStackInSlot(i), item)) {
                    n -= inv.getStackInSlot(i).stackSize;
                    if (n <= 0) {
                        return 0;
                    }
                }
            }
            return (n < 0) ? 0 : n;
        }
        return -1;
    }
    
    @Override
    public IIcon baseTexture() {
        return BlockTransferPipe.pipes_supply;
    }
    
    @Override
    public IIcon invPipeTexture(final ForgeDirection dir) {
        return BlockTransferPipe.pipes_supply;
    }
}

