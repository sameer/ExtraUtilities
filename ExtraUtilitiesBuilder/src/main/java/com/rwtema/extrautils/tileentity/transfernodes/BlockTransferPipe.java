// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.transfernodes;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.item.EntityItem;
import com.rwtema.extrautils.multipart.FMPBase;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.block.Box;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeCosmetic;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.StdPipes;
import com.rwtema.extrautils.helper.XUHelper;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.block.material.Material;
import java.util.Random;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipeBlock;
import com.rwtema.extrautils.ICreativeTabSorting;
import com.rwtema.extrautils.block.BlockMultiBlockSelection;

public class BlockTransferPipe extends BlockMultiBlockSelection implements ICreativeTabSorting, IPipeBlock
{
    public static final float pipe_width = 0.125f;
    public static IIcon pipes_oneway;
    public static IIcon pipes_nozzle;
    public static IIcon pipes_grouping;
    public static IIcon pipes;
    public static IIcon pipes_noninserting;
    public static IIcon pipes_xover;
    public static IIcon pipes_1way;
    public static IIcon[] pipes_diamond;
    public static IIcon pipes_supply;
    public static IIcon pipes_modsorting;
    public static IIcon pipes_energy;
    public static IIcon pipes_nozzle_energy;
    public static IIcon pipes_energy_extract;
    public static IIcon pipes_nozzle_energy_extract;
    public static IIcon pipes_hyperrationing;
    private Random random;
    public final int pipePage;
    
    public BlockTransferPipe(final int pipePage) {
        super(Material.sponge);
        this.random = XURandom.getInstance();
        this.pipePage = pipePage;
        this.setBlockName("extrautils:pipes" + ((pipePage == 0) ? "" : ("." + pipePage)));
        this.setBlockTextureName("extrautils:pipes");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(0.1f);
        this.setStepSound(BlockTransferPipe.soundTypeStone);
    }
    
    public int getMobilityFlag() {
        return 0;
    }
    
    public int damageDropped(final int par1) {
        if (this.pipePage == 0 && (par1 <= 7 || par1 == 15)) {
            return 0;
        }
        return par1;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        BlockTransferPipe.pipes = par1IIconRegister.registerIcon("extrautils:pipes");
        BlockTransferPipe.pipes_oneway = par1IIconRegister.registerIcon("extrautils:pipes_1way");
        BlockTransferPipe.pipes_nozzle = par1IIconRegister.registerIcon("extrautils:pipes_nozzle");
        BlockTransferPipe.pipes_grouping = par1IIconRegister.registerIcon("extrautils:pipes_grouping");
        BlockTransferPipe.pipes_noninserting = par1IIconRegister.registerIcon("extrautils:pipes_noninserting");
        BlockTransferPipe.pipes_1way = par1IIconRegister.registerIcon("extrautils:pipes_1way2");
        for (int i = 0; i < 6; ++i) {
            BlockTransferPipe.pipes_diamond[i] = par1IIconRegister.registerIcon("extrautils:pipes_diamond" + i);
        }
        BlockTransferPipe.pipes_supply = par1IIconRegister.registerIcon("extrautils:pipes_supply");
        BlockTransferPipe.pipes_energy = par1IIconRegister.registerIcon("extrautils:pipes_energy");
        BlockTransferPipe.pipes_energy_extract = par1IIconRegister.registerIcon("extrautils:pipes_energy_extract");
        BlockTransferPipe.pipes_xover = par1IIconRegister.registerIcon("extrautils:pipes_crossover");
        BlockTransferPipe.pipes_modsorting = par1IIconRegister.registerIcon("extrautils:pipes_modgrouping");
        BlockTransferPipe.pipes_nozzle_energy = par1IIconRegister.registerIcon("extrautils:pipes_nozzle_energy");
        BlockTransferPipe.pipes_nozzle_energy_extract = par1IIconRegister.registerIcon("extrautils:pipes_nozzle_energy_extract");
        BlockTransferPipe.pipes_hyperrationing = par1IIconRegister.registerIcon("extrautils:pipes_hypersupply");
        super.registerBlockIcons(par1IIconRegister);
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        switch (this.pipePage) {
            case 0: {
                par3List.add(new ItemStack(par1, 1, 0));
                par3List.add(new ItemStack(par1, 1, 8));
                par3List.add(new ItemStack(par1, 1, 9));
                par3List.add(new ItemStack(par1, 1, 10));
                par3List.add(new ItemStack(par1, 1, 11));
                par3List.add(new ItemStack(par1, 1, 12));
                par3List.add(new ItemStack(par1, 1, 13));
                par3List.add(new ItemStack(par1, 1, 14));
                break;
            }
            case 1: {
                par3List.add(new ItemStack(par1, 1, 0));
                break;
            }
        }
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        int metadata = world.getBlockMetadata(x, y, z) + this.pipePage * 16;
        if (metadata == 9) {
            if (world.getTileEntity(x, y, z) != null) {
                par5EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 0, world, x, y, z);
            }
            return true;
        }
        if (XUHelper.isWrench(par5EntityPlayer.getCurrentEquippedItem())) {
            metadata = StdPipes.getNextPipeType((IBlockAccess)world, x, y, z, metadata);
            if (metadata < 16) {
                world.setBlock(x, y, z, (Block)ExtraUtils.transferPipe, metadata, 3);
            }
            else {
                world.setBlock(x, y, z, (Block)ExtraUtils.transferPipe2, metadata - 16, 3);
            }
            return true;
        }
        return false;
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        return this.getPipeModel(world, x, y, z, null);
    }
    
    public BoxModel getPipeModel(final IBlockAccess world, final int x, final int y, final int z, IPipe pipe_underlying) {
        if (pipe_underlying == null) {
            pipe_underlying = TNHelper.getPipe(world, x, y, z);
            if (pipe_underlying == null) {
                return new BoxModel();
            }
        }
        if (!(pipe_underlying instanceof IPipeCosmetic)) {
            return new BoxModel();
        }
        final IPipeCosmetic pipe = (IPipeCosmetic)pipe_underlying;
        final BoxModel boxes = new BoxModel();
        for (int i = 0; i < 6; ++i) {
            final ForgeDirection dir = ForgeDirection.getOrientation(i);
            if (TNHelper.getPipe(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != null) {
                if (TNHelper.doesPipeConnect(world, x, y, z, dir)) {
                    boxes.add(new Box(0.375f, 0.0f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(pipe.pipeTexture(dir, !TNHelper.canInput(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite()))).setLabel("pipe"));
                }
            }
            else {
                final TileEntity tile = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
                if (tile != null && pipe_underlying.shouldConnectToTile(world, x, y, z, dir)) {
                    boxes.add(new Box(0.375f, 0.1875f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(pipe.invPipeTexture(dir)).setLabel("pipe"));
                    boxes.add(new Box(0.3125f, 0.0f, 0.3125f, 0.6875f, 0.1875f, 0.6875f).rotateToSide(dir).setTexture(pipe.socketTexture(dir)).setLabel("nozzle"));
                }
            }
        }
        boxes.add(new Box(0.5f - pipe.baseSize(), 0.5f - pipe.baseSize(), 0.5f - pipe.baseSize(), 0.5f + pipe.baseSize(), 0.5f + pipe.baseSize(), 0.5f + pipe.baseSize()).setTexture(pipe.baseTexture()).setLabel("base"));
        return boxes;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final BoxModel boxes = new BoxModel();
        final IPipe pipe = this.getPipe(metadata);
        if (pipe != null) {
            for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                boxes.add(new Box(0.375f, 0.0f, 0.375f, 0.625f, 0.375f, 0.625f).rotateToSide(dir).setTexture(((IPipeCosmetic)pipe).invPipeTexture(dir)).setLabel("pipe"));
            }
            if (((IPipeCosmetic)this.getPipe(metadata)).baseSize() > 0.125f) {
                boxes.add(new Box(0.5f - ((IPipeCosmetic)pipe).baseSize(), 0.5f - ((IPipeCosmetic)pipe).baseSize(), 0.5f - ((IPipeCosmetic)pipe).baseSize(), 0.5f + ((IPipeCosmetic)pipe).baseSize(), 0.5f + ((IPipeCosmetic)pipe).baseSize(), 0.5f + ((IPipeCosmetic)pipe).baseSize()).setTexture(((IPipeCosmetic)this.getPipe(metadata)).baseTexture()).setLabel("base"));
            }
        }
        return boxes;
    }
    
    public void prepareForRender(final String label) {
    }
    
    public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5, final int par6) {
        final TileEntity tile = par1World.getTileEntity(par2, par3, par4);
        if (par1World.getBlock(par2, par3, par4) != FMPBase.getFMPBlockId() && tile instanceof TileEntityFilterPipe) {
            final TileEntityFilterPipe tileentity = (TileEntityFilterPipe)tile;
            if (tileentity.items != null) {
                for (int i = 0; i < 6; ++i) {
                    final ItemStack itemstack = tileentity.items.getStackInSlot(i);
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
                par1World.func_147453_f(par2, par3, par4, par5);
            }
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }
    
    public boolean hasTileEntity(final int metadata) {
        return metadata == 9;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityFilterPipe();
    }
    
    @Override
    public String getSortingName(final ItemStack par1ItemStack) {
        final ItemStack i2 = par1ItemStack.copy();
        i2.setItemDamage(0);
        return i2.getDisplayName() + "_" + par1ItemStack.getDisplayName();
    }
    
    @Override
    public IPipe getPipe(final int metadata) {
        return StdPipes.getPipeType(metadata + this.pipePage * 16);
    }
    
    static {
        BlockTransferPipe.pipes_nozzle = null;
        BlockTransferPipe.pipes_grouping = null;
        BlockTransferPipe.pipes = null;
        BlockTransferPipe.pipes_noninserting = null;
        BlockTransferPipe.pipes_xover = null;
        BlockTransferPipe.pipes_1way = null;
        BlockTransferPipe.pipes_diamond = new IIcon[6];
        BlockTransferPipe.pipes_supply = null;
        BlockTransferPipe.pipes_modsorting = null;
        BlockTransferPipe.pipes_energy = null;
        BlockTransferPipe.pipes_nozzle_energy = null;
        BlockTransferPipe.pipes_energy_extract = null;
        BlockTransferPipe.pipes_nozzle_energy_extract = null;
        BlockTransferPipe.pipes_hyperrationing = null;
    }
}


