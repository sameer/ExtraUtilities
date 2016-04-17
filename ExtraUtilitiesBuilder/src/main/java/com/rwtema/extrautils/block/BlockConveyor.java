// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.IconFlipped;
import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.init.Items;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;

public class BlockConveyor extends Block implements IMultiBoxBlock
{
    ItemStack potionEmptyStack;
    private IIcon[] icons;
    
    public BlockConveyor() {
        super(Material.iron);
        this.potionEmptyStack = new ItemStack(Items.glass_bottle);
        this.setBlockName("extrautils:conveyor");
        this.setBlockTextureName("extrautils:conveyor");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(5.0f);
        this.setStepSound(BlockConveyor.soundTypeStone);
    }
    
    public int getRenderType() {
        return ExtraUtilsProxy.multiBlockID;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        (this.icons = new IIcon[3])[0] = par1IIconRegister.registerIcon("extrautils:conveyor_top");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:conveyor_side");
        this.icons[2] = (IIcon)new IconFlipped(this.icons[1], true, false);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        IIcon t = this.prevIcon(par1, par2);
        if (par2 % 2 == 1) {
            if (t == this.icons[1]) {
                t = this.icons[2];
            }
            else if (t == this.icons[2]) {
                t = this.icons[1];
            }
        }
        if (this.shouldFlip(par1)) {
            if (t == this.icons[1]) {
                t = this.icons[2];
            }
            else if (t == this.icons[2]) {
                t = this.icons[1];
            }
        }
        return t;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon prevIcon(final int par1, final int par2) {
        if (par1 <= 1) {
            return this.icons[0];
        }
        if (par2 % 2 == 0) {
            if (par1 <= 3) {
                return this.icons[0];
            }
            if (par1 == 4 && par2 == 0) {
                return this.icons[2];
            }
            if (par1 == 5 && par2 == 2) {
                return this.icons[2];
            }
        }
        if (par2 % 2 == 1) {
            if (par1 > 3) {
                return this.icons[0];
            }
            if (par1 == 2 && par2 == 1) {
                return this.icons[2];
            }
            if (par1 == 3 && par2 == 3) {
                return this.icons[2];
            }
        }
        return this.icons[1];
    }
    
    public boolean shouldFlip(final int side) {
        return side == 3 || side == 2;
    }
    
    public void onBlockPlacedBy(final World par1World, final int par2, final int par3, final int par4, final EntityLivingBase par5EntityLivingBase, final ItemStack par6ItemStack) {
        final int var6 = ((MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3) + 2) % 4;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 2);
    }
    
    public void onEntityCollidedWithBlock(final World par1World, final int x, final int y, final int z, final Entity par5Entity) {
        final double m_speed = 0.05;
        int a = par1World.getBlockMetadata(x, y, z);
        final int[] ax = { 0, 1, 0, -1 };
        final int[] az = { -1, 0, 1, 0 };
        if (a > 3) {
            return;
        }
        if (par5Entity != null && par5Entity.posY > y + 0.5 && !par5Entity.isSneaking()) {
            if (par5Entity instanceof EntityItem) {
                ItemStack my_item = null;
                my_item = ((EntityItem)par5Entity).getEntityItem();
                if (my_item != null) {
                    for (int j = 0; j < 4; ++j) {
                        if (a % 2 != j % 2 && par1World.getTileEntity(x + ax[j % 4], y - 1, z + az[j % 4]) instanceof IInventory) {
                            final IInventory chest = (IInventory)par1World.getTileEntity(x + ax[j % 4], y - 1, z + az[j % 4]);
                            boolean hasItem = false;
                            boolean hasSpace = false;
                            for (int i = 0; i < chest.getSizeInventory(); ++i) {
                                final ItemStack ch_item = chest.getStackInSlot(i);
                                if (ch_item != null) {
                                    if (ch_item.getItem() == my_item.getItem() && (ch_item.getItem().isDamageable() || ch_item.getItemDamage() == my_item.getItemDamage())) {
                                        hasItem = true;
                                        if (ch_item.stackSize < ch_item.getItem().getItemStackLimit(ch_item) && ch_item.stackSize < chest.getInventoryStackLimit()) {
                                            hasSpace = true;
                                        }
                                    }
                                }
                                else {
                                    hasSpace = true;
                                }
                                if (hasItem && hasSpace) {
                                    a = j % 4;
                                    par5Entity.isAirBorne = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (ax[a] == 0 && Math.abs(x + 0.5 - par5Entity.posX) < 0.5 && Math.abs(x + 0.5 - par5Entity.posX) > 0.1) {
                par5Entity.motionX += Math.signum(x + 0.5 - par5Entity.posX) * Math.min(m_speed, Math.abs(x + 0.5 - par5Entity.posX)) / 1.2;
            }
            if (az[a] == 0 && Math.abs(z + 0.5 - par5Entity.posZ) < 0.5 && Math.abs(z + 0.5 - par5Entity.posZ) > 0.1) {
                par5Entity.motionZ += Math.signum(z + 0.5 - par5Entity.posZ) * Math.min(m_speed, Math.abs(z + 0.5 - par5Entity.posZ)) / 1.2;
            }
            if (par5Entity instanceof EntityItem) {
                double jump_vel = 0.19;
                double jump_point = 0.0;
                boolean jump = par1World.isAirBlock(x, y + 2, z) && (par1World.getBlock(x + ax[a], y + 1, z + az[a]) == this || par1World.getBlock(x + ax[a], y + 1, z + az[a]) == Blocks.hopper);
                if (!jump && !par1World.isAirBlock(x + ax[a], y, z + az[a]) && par1World.getBlock(x + ax[a], y, z + az[a]) != this && !par1World.isAirBlock(x + ax[a], y + 1, z)) {
                    jump = true;
                    jump_vel = 0.07;
                    jump_point = 0.3;
                }
                if (jump) {
                    final double progress = (par5Entity.posX - x - 0.5) * ax[a] + (par5Entity.posZ - z - 0.5) * az[a];
                    final double prog_speed = par5Entity.motionX * ax[a] + par5Entity.motionZ * az[a];
                    final double prog_counterspeed = Math.abs(par5Entity.motionX * az[a] + par5Entity.motionZ * ax[a]);
                    if (progress > jump_point || (progress > jump_point - 0.2 && prog_speed < 0.0)) {
                        a = (a + 2) % 4;
                    }
                    else if (progress + 1.5 * prog_speed > jump_point && prog_speed >= m_speed && prog_counterspeed < 0.2) {
                        par5Entity.onGround = false;
                        par5Entity.isAirBorne = true;
                        if (ax[a] == 0) {
                            par5Entity.motionX = 0.0;
                        }
                        if (az[a] == 0) {
                            par5Entity.motionZ = 0.0;
                        }
                        par5Entity.addVelocity(0.0, jump_vel * 2.0, 0.0);
                        return;
                    }
                }
            }
            par5Entity.motionX += ax[a] * m_speed;
            par5Entity.motionZ += az[a] * m_speed;
        }
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
        final float var5 = 0.0625f;
        return AxisAlignedBB.getBoundingBox((double)par2, (double)par3, (double)par4, (double)(par2 + 1), (double)(par3 + 1 - var5), (double)(par4 + 1));
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
        return AxisAlignedBB.getBoundingBox((double)par2, (double)par3, (double)par4, (double)(par2 + 1), (double)(par3 + 1), (double)(par4 + 1));
    }
    
    public boolean canCreatureSpawn(final EnumCreatureType type, final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        return this.getModel(world.getBlockMetadata(x, y, z));
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        return this.getModel(1);
    }
    
    public BoxModel getModel(final int metadata) {
        final Box main = new Box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        main.renderAsNormalBlock = true;
        final Box box = main;
        final Box box2 = main;
        final int n = metadata % 2;
        box2.uvRotateTop = n;
        box.uvRotateBottom = n;
        return new BoxModel(main);
    }
}
