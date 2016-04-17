// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import net.minecraft.util.Facing;
import com.rwtema.extrautils.ExtraUtils;
import codechicken.multipart.TMultiPart;
import net.minecraft.item.ItemStack;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import com.rwtema.extrautils.multipart.ItemBlockMultiPart;

public class ItemBlockTransferNodeMultiPart extends ItemBlockMultiPart
{
    public ItemBlockTransferNodeMultiPart(final Block par1) {
        super(par1);
    }
    
    @Override
    public TMultiPart createMultiPart(final World world, final BlockCoord pos, final ItemStack item, final int side) {
        int metadata = item.getItemDamage() & 0xF;
        if (metadata > 12) {
            return null;
        }
        if (this.field_150939_a == ExtraUtils.transferNode) {
            TransferNodePart pipePart;
            if (metadata < 6) {
                pipePart = new TransferNodePartInventory();
            }
            else if (metadata < 12) {
                pipePart = new TransferNodePartLiquid();
            }
            else if (metadata == 13) {
                pipePart = new TransferNodePartHyperEnergy();
            }
            else {
                pipePart = new TransferNodePartEnergy();
            }
            if (metadata < 12) {
                metadata += Facing.oppositeSide[side];
            }
            pipePart.meta = (byte)metadata;
            return (TMultiPart)pipePart;
        }
        if (this.field_150939_a == ExtraUtils.transferNodeRemote) {
            TransferNodePart pipePart;
            if (metadata < 6) {
                pipePart = new TransferNodePartInventoryRemote();
            }
            else if (metadata < 12) {
                pipePart = new TransferNodePartLiquidRemote();
            }
            else {
                pipePart = new TransferNodePartEnergy();
            }
            if (metadata < 12) {
                metadata += Facing.oppositeSide[side];
            }
            pipePart.meta = (byte)metadata;
            return (TMultiPart)pipePart;
        }
        return null;
    }
}
