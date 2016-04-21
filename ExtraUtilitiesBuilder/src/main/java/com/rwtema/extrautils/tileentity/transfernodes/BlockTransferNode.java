// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.Item;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.multipart.FMPBase;
import net.minecraft.block.Block;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;
import com.rwtema.extrautils.block.Box;
import java.util.Collection;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INode;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import java.util.Random;
import net.minecraft.util.IIcon;

public class BlockTransferNode extends BlockTransferPipe
{
    public static IIcon nodeBase;
    public static IIcon nodeSideInsert;
    public static IIcon nodeSideExtract;
    public static IIcon nodeSideLiquid;
    public static IIcon nodeSideEnergy;
    public static IIcon nodeSideEnergyHyper;
    public static IIcon particle;
    private final Random random;
    private String curBlockLabel;
    
    public BlockTransferNode() {
        super(0);
        this.random = XURandom.getInstance();
        this.curBlockLabel = "";
        this.setBlockName("extrautils:extractor_base");
        this.setBlockTextureName("extrautils:extractor_base");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(0.5f);
        this.setStepSound(BlockTransferNode.soundTypeStone);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        BlockTransferNode.nodeBase = par1IIconRegister.registerIcon("extrautils:extractor_base");
        BlockTransferNode.nodeSideEnergy = par1IIconRegister.registerIcon("extrautils:extractor_energy");
        BlockTransferNode.nodeSideEnergyHyper = par1IIconRegister.registerIcon("extrautils:extractor_energy_hyper");
        BlockTransferNode.nodeSideLiquid = par1IIconRegister.registerIcon("extrautils:extractor_liquid");
        BlockTransferNode.nodeSideExtract = par1IIconRegister.registerIcon("extrautils:extractor_extract");
        BlockTransferNode.particle = par1IIconRegister.registerIcon("extrautils:particle");
        super.registerBlockIcons(par1IIconRegister);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par2 < 6) {
            return (par1 == par2 % 6) ? BlockTransferNode.nodeBase : BlockTransferNode.nodeSideExtract;
        }
        if (par2 < 12) {
            return (par1 == par2 % 6) ? BlockTransferNode.nodeBase : BlockTransferNode.nodeSideLiquid;
        }
        if (par2 == 13) {
            return BlockTransferNode.nodeSideEnergyHyper;
        }
        return BlockTransferNode.nodeSideEnergy;
    }
    
    @Override
    public void prepareForRender(final String label) {
        this.curBlockLabel = label;
    }
    
    @Override
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final int pipe_type = 0;
        final int metadata = 0;
        final int node_dir = 0;
        if (!(world.getTileEntity(x, y, z) instanceof INode)) {
            return new BoxModel();
        }
        final INode node = (INode)world.getTileEntity(x, y, z);
        final BoxModel boxes = node.getModel(node.getNodeDir());
        final List pipe_boxes = this.getPipeModel(world, x, y, z, null);
        if (pipe_boxes.size() > 1) {
            boxes.addAll(pipe_boxes);
        }
        return boxes;
    }
    
    @Override
    public BoxModel getInventoryModel(final int metadata) {
        if (metadata == 12 || metadata == 13) {
            return this.getEnergyModel();
        }
        return this.getModel(0);
    }
    
    public BoxModel getEnergyModel() {
        final BoxModel boxes = new BoxModel();
        boxes.add(new Box(0.1875f, 0.3125f, 0.3125f, 0.8125f, 0.6875f, 0.6875f));
        boxes.add(new Box(0.3125f, 0.1875f, 0.3125f, 0.6875f, 0.8125f, 0.6875f));
        boxes.add(new Box(0.3125f, 0.3125f, 0.1875f, 0.6875f, 0.6875f, 0.8125f));
        boxes.add(new Box(0.25f, 0.25f, 0.25f, 0.75f, 0.75f, 0.75f));
        return boxes;
    }
    
    public BoxModel getModel(final int metadata) {
        final ForgeDirection dir = ForgeDirection.getOrientation(metadata);
        final BoxModel boxes = new BoxModel();
        final float w = 0.125f;
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.9375f, 0.0625f, 0.9375f).rotateToSide(dir).setTextureSides(dir.ordinal(), BlockTransferNode.nodeBase));
        boxes.add(new Box(0.1875f, 0.0625f, 0.1875f, 0.8125f, 0.25f, 0.8125f).rotateToSide(dir));
        boxes.add(new Box(0.3125f, 0.25f, 0.3125f, 0.6875f, 0.375f, 0.6875f).rotateToSide(dir));
        boxes.add(new Box(0.375f, 0.25f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(BlockTransferNode.nodeBase).setAllSideInvisible().setSideInvisible(dir.getOpposite().ordinal(), false));
        return boxes;
    }
    
    @Override
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        final TileEntityTransferNode node = (TileEntityTransferNode)par1World.getTileEntity(par2, par3, par4);
        if (node == null) {
            return false;
        }
        if (par5EntityPlayer.getCurrentEquippedItem() != null && XUHelper.isWrench(par5EntityPlayer.getCurrentEquippedItem())) {
            node.pipe_type = StdPipes.getNextPipeType((IBlockAccess)par1World, par2, par3, par4, node.pipe_type);
            node.markDirty();
            par1World.markBlockForUpdate(par2, par3, par4);
            return true;
        }
        par5EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 0, node.getWorldObj(), node.xCoord, node.yCoord, node.zCoord);
        return true;
    }
    
    @Override
    public int damageDropped(final int par1) {
        if (par1 < 6) {
            return 0;
        }
        if (par1 < 12) {
            return 6;
        }
        return par1;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public int onBlockPlaced(final World par1World, final int par2, final int par3, final int par4, final int par5, final float par6, final float par7, final float par8, final int par9) {
        if (par9 < 12) {
            return ForgeDirection.OPPOSITES[par5] + par9 & 0xF;
        }
        return par9 & 0xF;
    }
    
    public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final Block par5) {
        if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityTransferNode) {
            ((TileEntityTransferNode)par1World.getTileEntity(par2, par3, par4)).updateRedstone();
        }
    }
    
    @Override
    public void breakBlock(final World world, final int x, final int y, final int z, final Block par5, final int par6) {
        if (world.getBlock(x, y, z) != FMPBase.getFMPBlockId() && world.getTileEntity(x, y, z) instanceof TileEntityTransferNode) {
            final TileEntityTransferNode tile = (TileEntityTransferNode)world.getTileEntity(x, y, z);
            if (!tile.getUpgrades().isEmpty()) {
                for (int i = 0; i < tile.getUpgrades().size(); ++i) {
                    final ItemStack itemstack = tile.getUpgrades().get(i);
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
                        final EntityItem entityitem = new EntityItem(world, (double)(x + f), (double)(y + f2), (double)(z + f3), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        final float f4 = 0.05f;
                        entityitem.motionX = (float)this.random.nextGaussian() * f4;
                        entityitem.motionY = (float)this.random.nextGaussian() * f4 + 0.2f;
                        entityitem.motionZ = (float)this.random.nextGaussian() * f4;
                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
            }
            if (tile instanceof TileEntityTransferNodeInventory) {
                final TileEntityTransferNodeInventory tileentity = (TileEntityTransferNodeInventory)tile;
                final ItemStack itemstack = tileentity.getStackInSlot(0);
                if (itemstack != null) {
                    final float f = this.random.nextFloat() * 0.8f + 0.1f;
                    final float f2 = this.random.nextFloat() * 0.8f + 0.1f;
                    final float f3 = this.random.nextFloat() * 0.8f + 0.1f;
                    while (itemstack.stackSize > 0) {
                        int k1 = this.random.nextInt(21) + 10;
                        if (k1 > itemstack.stackSize) {
                            k1 = itemstack.stackSize;
                        }
                        final ItemStack itemStack2 = itemstack;
                        itemStack2.stackSize -= k1;
                        final EntityItem entityitem = new EntityItem(world, (double)(x + f), (double)(y + f2), (double)(z + f3), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                        final float f4 = 0.05f;
                        entityitem.motionX = (float)this.random.nextGaussian() * f4;
                        entityitem.motionY = (float)this.random.nextGaussian() * f4 + 0.2f;
                        entityitem.motionZ = (float)this.random.nextGaussian() * f4;
                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                        world.spawnEntityInWorld((Entity)entityitem);
                    }
                }
                world.func_147453_f(x, y, z, par5);
            }
        }
        super.breakBlock(world, x, y, z, par5, par6);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 6));
        par3List.add(new ItemStack(par1, 1, 12));
        par3List.add(new ItemStack(par1, 1, 13));
    }
    
    @Override
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata == 13) {
            return new TileEntityTransferNodeHyperEnergy();
        }
        if (metadata == 12) {
            return new TileEntityTransferNodeEnergy();
        }
        if (metadata >= 6 && metadata < 12) {
            return new TileEntityTransferNodeLiquid();
        }
        return new TileEntityTransferNodeInventory();
    }
    
    public boolean getWeakChanges(final IBlockAccess world, final int x, final int y, final int z) {
        return true;
    }
    
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int par5) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityTransferNode) {
            final TileEntityTransferNode tile = (TileEntityTransferNode)world.getTileEntity(x, y, z);
            if (tile.buffer.shouldSearch()) {
                return 15;
            }
        }
        return 0;
    }
}

