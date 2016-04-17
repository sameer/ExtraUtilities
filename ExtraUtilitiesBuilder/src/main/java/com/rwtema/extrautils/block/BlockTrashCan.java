// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import com.rwtema.extrautils.tileentity.TileEntityTrashCanEnergy;
import com.rwtema.extrautils.tileentity.TileEntityTrashCanFluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.tileentity.TileEntityTrashCan;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import java.util.Random;
import com.rwtema.extrautils.item.IBlockLocalization;
import net.minecraft.block.ITileEntityProvider;

public class BlockTrashCan extends BlockMultiBlock implements ITileEntityProvider, IBlockLocalization
{
    Random random;
    private IIcon[][] icons;
    
    public BlockTrashCan() {
        super(Material.rock);
        this.random = XURandom.getInstance();
        this.setBlockName("extrautils:trashcan");
        this.setBlockTextureName("extrautils:trashcan");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(3.5f).setStepSound(BlockTrashCan.soundTypeStone);
    }
    
    public int damageDropped(final int p_149692_1_) {
        return p_149692_1_;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        final int i = Math.min(side, 2);
        return this.icons[meta % 3][i];
    }
    
    public void getSubBlocks(final Item p_149666_1_, final CreativeTabs p_149666_2_, final List p_149666_3_) {
        for (int i = 0; i < 3; ++i) {
            p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
        }
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityTrashCan) {
            par5EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 0, par1World, par2, par3, par4);
            return true;
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.icons = new IIcon[3][];
        (this.icons[0] = new IIcon[3])[0] = par1IIconRegister.registerIcon("extrautils:trashcan_bottom");
        this.icons[0][1] = par1IIconRegister.registerIcon("extrautils:trashcan_top");
        this.icons[0][2] = par1IIconRegister.registerIcon("extrautils:trashcan");
        (this.icons[1] = new IIcon[3])[0] = par1IIconRegister.registerIcon("extrautils:trashcan1_bottom");
        this.icons[1][1] = par1IIconRegister.registerIcon("extrautils:trashcan1_top");
        this.icons[1][2] = par1IIconRegister.registerIcon("extrautils:trashcan1");
        (this.icons[2] = new IIcon[3])[0] = par1IIconRegister.registerIcon("extrautils:trashcan2_bottom");
        this.icons[2][1] = par1IIconRegister.registerIcon("extrautils:trashcan2_top");
        this.icons[2][2] = par1IIconRegister.registerIcon("extrautils:trashcan2");
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        return this.getInventoryModel(0);
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final BoxModel model = new BoxModel(0.125f, 0.0f, 0.125f, 0.875f, 0.625f, 0.875f);
        model.add(new Box(0.0625f, 0.625f, 0.0625f, 0.9375f, 0.875f, 0.9375f));
        model.add(new Box(0.3125f, 0.875f, 0.4375f, 0.6875f, 0.9375f, 0.5625f));
        return model;
    }
    
    public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5, final int par6) {
        final TileEntity tileEntity = par1World.getTileEntity(par2, par3, par4);
        if (tileEntity instanceof TileEntityTrashCan) {
            final TileEntityTrashCan tile = (TileEntityTrashCan)tileEntity;
            tile.processInv();
            final ItemStack itemstack = tile.getStackInSlot(0);
            if (itemstack != null) {
                final float f = this.random.nextFloat() * 0.8f + 0.1f;
                final float f2 = this.random.nextFloat() * 0.8f + 0.1f;
                final float f3 = this.random.nextFloat() * 0.8f + 0.1f;
                while (itemstack.stackSize > 0) {
                    int k1 = this.random.nextInt(21) + 10;
                    if (k1 > itemstack.stackSize) {
                        k1 = itemstack.stackSize;
                    }
                    final ItemStack itemStack = itemstack;
                    itemStack.stackSize -= k1;
                    final EntityItem entityitem = new EntityItem(par1World, (double)(par2 + f), (double)(par3 + f2), (double)(par4 + f3), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                    final float f4 = 0.05f;
                    entityitem.motionX = (float)this.random.nextGaussian() * f4;
                    entityitem.motionY = (float)this.random.nextGaussian() * f4 + 0.2f;
                    entityitem.motionZ = (float)this.random.nextGaussian() * f4;
                    if (itemstack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                    }
                    par1World.spawnEntityInWorld((Entity)entityitem);
                }
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    public TileEntity createNewTileEntity(final World var1, final int meta) {
        if (meta == 1) {
            return new TileEntityTrashCanFluids();
        }
        if (meta == 2) {
            return new TileEntityTrashCanEnergy();
        }
        return new TileEntityTrashCan();
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        if (par1ItemStack.getItemDamage() == 0) {
            return this.getUnlocalizedName();
        }
        return this.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
}
