// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.util.MovingObjectPosition;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.EnumPlantType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.Block;
import net.minecraft.util.ChunkCoordinates;
import com.rwtema.extrautils.block.BlockEnderLily;
import net.minecraftforge.common.IPlantable;
import net.minecraft.item.ItemBlock;

public class ItemBlockEnderLily extends ItemBlock implements IPlantable
{
    BlockEnderLily enderLily;
    private static final ChunkCoordinates zero;
    
    public ItemBlockEnderLily(final Block par1) {
        super(par1);
        this.enderLily = (BlockEnderLily)par1;
    }
    
    public EnumPlantType getPlantType(final IBlockAccess world, final int x, final int y, final int z) {
        return EnumPlantType.Cave;
    }
    
    public Block getPlant(final IBlockAccess world, final int x, final int y, final int z) {
        return this.field_150939_a;
    }
    
    public int getPlantMetadata(final IBlockAccess world, final int x, final int y, final int z) {
        return 0;
    }
    
    public boolean onItemUse(final ItemStack itemstack, final EntityPlayer player, final World world, int x, int y, int z, int side, final float hitX, final float hitY, final float hitZ) {
        final Block block = world.getBlock(x, y, z);
        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 0x7) < 1) {
            side = 1;
        }
        else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable((IBlockAccess)world, x, y, z)) {
            if (side == 0) {
                --y;
            }
            if (side == 1) {
                ++y;
            }
            if (side == 2) {
                --z;
            }
            if (side == 3) {
                ++z;
            }
            if (side == 4) {
                --x;
            }
            if (side == 5) {
                ++x;
            }
        }
        for (int my = y + 8; y < my && this.enderLily.isFluid(world, x, y, z); ++y) {}
        final boolean b = world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, (Entity)player, itemstack);
        if (itemstack.stackSize == 0) {
            return false;
        }
        if (!player.canPlayerEdit(x, y, z, side, itemstack)) {
            return false;
        }
        if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        }
        if (b) {
            final int i1 = this.getMetadata(itemstack.getItemDamage());
            final int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, i1);
            if (this.placeBlockAt(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, j1)) {
                world.playSoundEffect((double)(x + 0.5f), (double)(y + 0.5f), (double)(z + 0.5f), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0f) / 2.0f, this.field_150939_a.stepSound.getPitch() * 0.8f);
                if (!player.capabilities.isCreativeMode) {
                    --itemstack.stackSize;
                }
            }
            return true;
        }
        return false;
    }
    
    public boolean onItemUseFirst(final ItemStack stack, final EntityPlayer player, final World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (XUHelper.isPlayerFake(player) && ItemBlockEnderLily.zero.equals((Object)player.getPlayerCoordinates())) {
            return false;
        }
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition == null) {
            return false;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (movingobjectposition.sideHit == 0) {
                return false;
            }
            side = 0;
            x = movingobjectposition.blockX;
            y = movingobjectposition.blockY;
            z = movingobjectposition.blockZ;
            if (!this.enderLily.isFluid(world, x, y, z)) {
                return false;
            }
            hitX = (float)movingobjectposition.hitVec.xCoord - x;
            hitY = (float)movingobjectposition.hitVec.yCoord - y;
            hitZ = (float)movingobjectposition.hitVec.zCoord - z;
            ++y;
            if (player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, stack)) {
                if (this.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ)) {
                    if (world.isRemote) {
                        ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, stack, hitX, hitY, hitZ);
                    }
                    return true;
                }
                return false;
            }
        }
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
    
    static {
        zero = new ChunkCoordinates(0, 0, 0);
    }
}
