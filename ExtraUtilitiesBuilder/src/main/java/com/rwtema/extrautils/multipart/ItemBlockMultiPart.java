// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart;

import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import com.rwtema.extrautils.item.ItemBlockMetadata;

public class ItemBlockMultiPart extends ItemBlockMetadata
{
    public ItemBlockMultiPart(final Block par1) {
        super(par1);
    }
    
    @SideOnly(Side.CLIENT)
    public boolean func_150936_a(final World par1World, final int par2, final int par3, final int par4, final int par5, final EntityPlayer par6EntityPlayer, final ItemStack par7ItemStack) {
        return this.tryPlaceMultiPart(par1World, new BlockCoord(par2, par3, par4).offset(par5), par7ItemStack, par5, false) || super.func_150936_a(par1World, par2, par3, par4, par5, par6EntityPlayer, par7ItemStack);
    }
    
    public TMultiPart createMultiPart(final World world, final BlockCoord pos, final ItemStack item, final int side) {
        return null;
    }
    
    public boolean tryPlaceMultiPart(final World world, final BlockCoord pos, final ItemStack item, final int side, final boolean doPlace) {
        final TileMultipart tile = TileMultipart.getOrConvertTile(world, pos);
        if (tile == null) {
            return false;
        }
        final TMultiPart part = this.createMultiPart(world, pos, item, side);
        if (part == null) {
            return false;
        }
        if (tile.canAddPart(part)) {
            if (doPlace) {
                TileMultipart.addPart(world, pos, part);
            }
            return true;
        }
        return false;
    }
    
    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World, final int par4, final int par5, final int par6, final int par7, final float par8, final float par9, final float par10) {
        final Block block = par3World.getBlock(par4, par5, par6);
        if (par8 != 0.0f && par9 != 0.0f && par10 != 0.0f && par8 != 1.0f && par9 != 1.0f && par10 != 1.0f) {
            final BlockCoord pos = new BlockCoord(par4, par5, par6);
            if (this.tryPlaceMultiPart(par3World, pos, par1ItemStack, par7, !par3World.isRemote)) {
                par3World.playSoundEffect((double)(pos.x + 0.5f), (double)(pos.y + 0.5f), (double)(pos.z + 0.5f), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
                --par1ItemStack.stackSize;
                return true;
            }
        }
        if (block != Blocks.snow || (par3World.getBlockMetadata(par4, par5, par6) & 0x7) >= 1) {
            if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && block.isReplaceable((IBlockAccess)par3World, par4, par5, par6)) {
                final BlockCoord pos = new BlockCoord(par4, par5, par6).offset(par7);
                if (this.tryPlaceMultiPart(par3World, pos, par1ItemStack, par7, !par3World.isRemote)) {
                    par3World.playSoundEffect((double)(pos.x + 0.5f), (double)(pos.y + 0.5f), (double)(pos.z + 0.5f), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0f) / 2.0f, block.stepSound.getPitch() * 0.8f);
                    --par1ItemStack.stackSize;
                    return true;
                }
            }
        }
        return super.onItemUse(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10);
    }
}


