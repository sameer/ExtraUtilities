// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import com.rwtema.extrautils.block.Box;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.block.BoxModel;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.block.BlockMultiBlock;

public class BlockEnderConstructor extends BlockMultiBlock
{
    public IIcon[] icons;
    Random rand;
    
    public BlockEnderConstructor() {
        super(Material.rock);
        this.icons = new IIcon[10];
        this.rand = XURandom.getInstance();
        this.setBlockName("extrautils:endConstructor");
        this.setBlockTextureName("extrautils:enderConstructor_pillar_bottom");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setResistance(10.0f).setStepSound(BlockEnderConstructor.soundTypeStone);
    }
    
    public void breakBlock(final World p_149749_1_, final int p_149749_2_, final int p_149749_3_, final int p_149749_4_, final Block p_149749_5_, final int p_149749_6_) {
        final TileEntity tile = p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);
        if (tile != null && tile instanceof TileEnderConstructor) {
            final IInventory inv = (IInventory)((TileEnderConstructor)tile).inv;
            for (int i1 = 0; i1 < 9; ++i1) {
                final ItemStack itemstack = inv.getStackInSlot(i1);
                this.dropItem(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, itemstack);
            }
            if (((TileEnderConstructor)tile).outputslot != null) {
                this.dropItem(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, ((TileEnderConstructor)tile).outputslot);
            }
            p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
        }
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }
    
    public void dropItem(final World p_149749_1_, final float p_149749_2_, final float p_149749_3_, final float p_149749_4_, final ItemStack itemstack) {
        if (itemstack != null) {
            final float f = this.rand.nextFloat() * 0.8f + 0.1f;
            final float f2 = this.rand.nextFloat() * 0.8f + 0.1f;
            final float f3 = this.rand.nextFloat() * 0.8f + 0.1f;
            while (itemstack.stackSize > 0) {
                int j1 = this.rand.nextInt(21) + 10;
                if (j1 > itemstack.stackSize) {
                    j1 = itemstack.stackSize;
                }
                itemstack.stackSize -= j1;
                final EntityItem entityitem = new EntityItem(p_149749_1_, (double)(p_149749_2_ + f), (double)(p_149749_3_ + f2), (double)(p_149749_4_ + f3), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                final float f4 = 0.05f;
                entityitem.motionX = (float)this.rand.nextGaussian() * f4;
                entityitem.motionY = (float)this.rand.nextGaussian() * f4 + 0.2f;
                entityitem.motionZ = (float)this.rand.nextGaussian() * f4;
                if (itemstack.hasTagCompound()) {
                    entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                }
                p_149749_1_.spawnEntityInWorld((Entity)entityitem);
            }
        }
    }
    
    public int getLightValue(final IBlockAccess world, final int x, final int y, final int z) {
        final Block block = world.getBlock(x, y, z);
        if (block != null && block != this) {
            return block.getLightValue(world, x, y, z);
        }
        return (world.getBlockMetadata(x, y, z) % 2 == 1) ? 10 : 0;
    }
    
    public void prepareForRender(final String label) {
    }
    
    public int damageDropped(final int par1) {
        return par1 - par1 % 2 & 0xF;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        final IIcon[] icons = this.icons;
        final int n = 0;
        final IIcon[] icons2 = this.icons;
        final int n2 = 1;
        final IIcon registerIcon = par1IIconRegister.registerIcon(this.getTextureName());
        this.blockIcon = registerIcon;
        icons[n] = (icons2[n2] = registerIcon);
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:enderConstructor_top");
        this.icons[3] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_top");
        this.icons[4] = par1IIconRegister.registerIcon("extrautils:enderConstructor_side");
        this.icons[5] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar");
        this.icons[6] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_enabled");
        this.icons[7] = par1IIconRegister.registerIcon("extrautils:enderConstructor_pillar_top_enabled");
        this.icons[8] = par1IIconRegister.registerIcon("extrautils:enderConstructor_top_enabled");
        this.icons[9] = par1IIconRegister.registerIcon("extrautils:enderConstructor_side_enabled");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par2 == 2) {
            if (par1 == 0) {
                return this.icons[1];
            }
            if (par1 == 1) {
                return this.icons[3];
            }
            return this.icons[5];
        }
        else if (par2 == 3) {
            if (par1 == 0) {
                return this.icons[1];
            }
            if (par1 == 1) {
                return this.icons[7];
            }
            return this.icons[6];
        }
        else if (par2 == 1) {
            if (par1 == 0) {
                return this.icons[0];
            }
            if (par1 == 1) {
                return this.icons[8];
            }
            return this.icons[9];
        }
        else {
            if (par1 == 0) {
                return this.icons[0];
            }
            if (par1 == 1) {
                return this.icons[2];
            }
            return this.icons[4];
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 2));
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final int metadata = world.getBlockMetadata(x, y, z);
        final BoxModel model = this.getInventoryModel(metadata);
        if (metadata == 2 || metadata == 3) {
            model.fillIcons(this, metadata);
            for (final ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite(), false)) {
                    model.rotateToSideTex(d);
                    return model;
                }
            }
        }
        return model;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        if (metadata == 2 || metadata == 3) {
            final BoxModel model = new BoxModel();
            model.add(new Box(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.4375f, 0.9375f));
            model.add(new Box(0.25f, 0.4375f, 0.25f, 0.75f, 0.9375f, 0.75f));
            return model;
        }
        final BoxModel box = BoxModel.newStandardBlock();
        return box;
    }
    
    public void miscInit() {
    }
    
    @Override
    public void addCollisionBoxesToList(final World par1World, final int par2, final int par3, final int par4, final AxisAlignedBB par5AxisAlignedBB, final List par6List, final Entity par7Entity) {
        final List models = this.getWorldModel((IBlockAccess)par1World, par2, par3, par4);
        if (models == null || models.size() == 0) {
            return;
        }
        final Box b = BoxModel.boundingBox(models);
        final AxisAlignedBB axisalignedbb1 = AxisAlignedBB.getBoundingBox((double)(par2 + b.offsetx + b.minX), (double)(par3 + b.offsety + b.minY), (double)(par4 + b.offsetz + b.minZ), (double)(par2 + b.offsetx + b.maxX), (double)(par3 + b.offsety + b.maxY), (double)(par4 + b.offsetz + b.maxZ));
        if (axisalignedbb1 != null && par5AxisAlignedBB.intersectsWith(axisalignedbb1)) {
            par6List.add(axisalignedbb1);
        }
    }
    
    public boolean onBlockActivated(final World worldObj, final int x, final int y, final int z, final EntityPlayer player, final int side, final float dx, final float dy, final float dz) {
        final int metadata = worldObj.getBlockMetadata(x, y, z);
        if (metadata > 1 && metadata != 4) {
            return false;
        }
        if (worldObj.isRemote) {
            return true;
        }
        player.openGui((Object)ExtraUtilsMod.instance, 0, worldObj, x, y, z);
        return true;
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 2 || metadata == 3) {
            return new TileEnderPillar();
        }
        return new TileEnderConstructor();
    }
}
