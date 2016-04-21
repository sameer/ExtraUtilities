// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.dispenser;

import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.init.Items;
import net.minecraft.block.BlockDispenser;
import net.minecraft.util.RegistrySimple;

public class DispenserStuff
{
    public static void registerItems() {
        if (!((RegistrySimple)BlockDispenser.dispenseBehaviorRegistry).containsKey((Object)Items.glass_bottle)) {
            BlockDispenser.dispenseBehaviorRegistry.putObject((Object)Items.glass_bottle, (Object)new BehaviorDefaultDispenseItem() {
                private final BehaviorDefaultDispenseItem field_150840_b = new BehaviorDefaultDispenseItem();
                
                public ItemStack dispenseStack(final IBlockSource par1IBlockSource, final ItemStack par2ItemStack) {
                    final EnumFacing enumfacing = BlockDispenser.func_149937_b(par1IBlockSource.getBlockMetadata());
                    final World world = par1IBlockSource.getWorld();
                    final int i = par1IBlockSource.getXInt() + enumfacing.getFrontOffsetX();
                    final int j = par1IBlockSource.getYInt() + enumfacing.getFrontOffsetY();
                    final int k = par1IBlockSource.getZInt() + enumfacing.getFrontOffsetZ();
                    final Material material = world.getBlock(i, j, k).getMaterial();
                    final int l = world.getBlockMetadata(i, j, k);
                    if (Material.water.equals(material) && l == 0) {
                        final Item item = (Item)Items.potionitem;
                        if (--par2ItemStack.stackSize == 0) {
                            par2ItemStack.func_150996_a(item);
                            par2ItemStack.stackSize = 1;
                            par2ItemStack.setTagCompound((NBTTagCompound)null);
                        }
                        else if (((TileEntityDispenser)par1IBlockSource.getBlockTileEntity()).func_146019_a(new ItemStack(item)) < 0) {
                            this.field_150840_b.dispense(par1IBlockSource, new ItemStack(item));
                        }
                        return par2ItemStack;
                    }
                    return super.dispenseStack(par1IBlockSource, par2ItemStack);
                }
            });
        }
    }
}


