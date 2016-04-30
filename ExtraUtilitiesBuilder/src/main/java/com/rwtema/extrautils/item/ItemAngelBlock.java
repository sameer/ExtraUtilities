// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemAngelBlock extends ItemBlock
{
    public ItemAngelBlock(final Block par1) {
        super(par1);
    }
    
    public ItemStack onItemRightClick(final ItemStack item, final World world, final EntityPlayer player) {
        if (world.isRemote) {
            return item;
        }
        int x = (int)Math.floor(player.posX);
        int y = (int)Math.floor(player.posY) + 1;
        int z = (int)Math.floor(player.posZ);
        final Vec3 look = player.getLookVec();
        final double max = Math.max(Math.max(Math.abs(look.xCoord), Math.abs(look.yCoord)), Math.abs(look.zCoord));
        if (look.yCoord == max) {
            y = (int)(Math.ceil(player.boundingBox.maxY) + 1.0);
        }
        else if (-look.yCoord == max) {
            y = (int)(Math.floor(player.boundingBox.minY) - 1.0);
        }
        else if (look.xCoord == max) {
            x = (int)(Math.floor(player.boundingBox.maxX) + 1.0);
        }
        else if (-look.xCoord == max) {
            x = (int)(Math.floor(player.boundingBox.minX) - 1.0);
        }
        else if (look.zCoord == max) {
            z = (int)(Math.floor(player.boundingBox.maxZ) + 1.0);
        }
        else if (-look.zCoord == max) {
            z = (int)(Math.floor(player.boundingBox.minZ) - 1.0);
        }
        if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, 0, (Entity)player, item)) {
            item.tryPlaceItemIntoWorld(player, world, x, y, z, 0, 0.0f, 0.0f, 0.0f);
        }
        return item;
    }
}


