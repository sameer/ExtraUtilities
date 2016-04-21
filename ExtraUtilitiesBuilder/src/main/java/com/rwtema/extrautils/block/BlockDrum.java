// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import java.util.Random;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import java.util.List;
import net.minecraft.item.Item;
import com.rwtema.extrautils.item.ItemBlockDrum;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.stats.StatList;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.ExtraUtilsProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import java.util.Locale;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.FluidContainerRegistry;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.tileentity.TileEntityDrum;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockDrum extends Block
{
    public static IIcon drum_side;
    public static IIcon drum_top;
    public static IIcon drum_side2;
    private static IIcon drum_top2;
    
    public BlockDrum() {
        super(Material.rock);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.5f);
        this.setBlockName("extrautils:drum");
        this.setBlockBounds(0.07499999f, 0.0f, 0.07499999f, 0.925f, 1.0f, 0.925f);
    }
    
    public boolean hasComparatorInputOverride() {
        return true;
    }
    
    public int getComparatorInputOverride(final World world, final int x, final int y, final int z, final int side) {
        if (!(world.getTileEntity(x, y, z) instanceof TileEntityDrum)) {
            return 0;
        }
        final TileEntityDrum drum = (TileEntityDrum)world.getTileEntity(x, y, z);
        final FluidTankInfo tank = drum.getTankInfo(ForgeDirection.UP)[0];
        if (tank == null || tank.fluid == null || tank.fluid.amount == 0) {
            return 0;
        }
        double t = tank.fluid.amount * 14.0 / tank.capacity;
        if (t < 0.0) {
            t = 0.0;
        }
        if (t > 15.0) {
            t = 14.0;
        }
        return (int)Math.floor(t) + 1;
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        if (par1World.getTileEntity(par2, par3, par4) instanceof TileEntityDrum) {
            final TileEntityDrum drum = (TileEntityDrum)par1World.getTileEntity(par2, par3, par4);
            final FluidTankInfo tank = drum.getTankInfo(ForgeDirection.UP)[0];
            final ItemStack item = par5EntityPlayer.getCurrentEquippedItem();
            if (item != null) {
                if (item.getItem() == Items.stick || (par5EntityPlayer.isSneaking() && XUHelper.isWrench(item))) {
                    this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                    par1World.setBlockToAir(par2, par3, par4);
                    return true;
                }
                if (FluidContainerRegistry.isEmptyContainer(item)) {
                    final ItemStack filled = FluidContainerRegistry.fillFluidContainer(tank.fluid, item);
                    if (filled != null) {
                        final int a = FluidContainerRegistry.getFluidForFilledItem(filled).amount;
                        if (par5EntityPlayer.capabilities.isCreativeMode) {
                            drum.drain(ForgeDirection.DOWN, a, true);
                        }
                        else if (item.stackSize == 1) {
                            par5EntityPlayer.setCurrentItemOrArmor(0, filled);
                            drum.drain(ForgeDirection.DOWN, a, true);
                        }
                        else if (par5EntityPlayer.inventory.addItemStackToInventory(filled)) {
                            final ItemStack itemStack = item;
                            --itemStack.stackSize;
                            drum.drain(ForgeDirection.DOWN, a, true);
                            if (par5EntityPlayer instanceof EntityPlayerMP) {
                                ((EntityPlayerMP)par5EntityPlayer).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)par5EntityPlayer);
                            }
                        }
                        if (drum.getTankInfo(ForgeDirection.DOWN)[0].fluid == null) {
                            par1World.markBlockForUpdate(par2, par3, par4);
                        }
                        return true;
                    }
                }
                else if (FluidContainerRegistry.isFilledContainer(item)) {
                    final FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
                    if (drum.fill(ForgeDirection.UP, fluid, false) == fluid.amount) {
                        if (par5EntityPlayer.capabilities.isCreativeMode) {
                            drum.fill(ForgeDirection.UP, fluid, true);
                        }
                        else {
                            ItemStack c = null;
                            if (item.getItem().hasContainerItem(item)) {
                                c = item.getItem().getContainerItem(item);
                            }
                            if (c == null || item.stackSize == 1 || par5EntityPlayer.inventory.addItemStackToInventory(c)) {
                                drum.fill(ForgeDirection.UP, fluid, true);
                                if (item.stackSize == 1) {
                                    par5EntityPlayer.setCurrentItemOrArmor(0, c);
                                }
                                else if (item.stackSize > 1) {
                                    final ItemStack itemStack2 = item;
                                    --itemStack2.stackSize;
                                }
                            }
                        }
                        return true;
                    }
                }
            }
            final FluidStack fluid2 = tank.fluid;
            String s;
            if (fluid2 != null) {
                s = XUHelper.getFluidName(fluid2) + ": " + String.format(Locale.ENGLISH, "%,d", fluid2.amount) + " / " + String.format(Locale.ENGLISH, "%,d", tank.capacity);
            }
            else {
                s = "Empty: 0 / " + String.format(Locale.ENGLISH, "%,d", tank.capacity);
            }
            PacketTempChat.sendChat(par5EntityPlayer, s);
            return true;
        }
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        return (par1 <= 1) ? ((par2 == 1) ? BlockDrum.drum_top2 : BlockDrum.drum_top) : ((par2 == 1) ? BlockDrum.drum_side2 : BlockDrum.drum_side);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        BlockDrum.drum_side = par1IIconRegister.registerIcon("extrautils:drum_side");
        BlockDrum.drum_side2 = par1IIconRegister.registerIcon("extrautils:drum_side2");
        BlockDrum.drum_top = par1IIconRegister.registerIcon("extrautils:drum_top");
        BlockDrum.drum_top2 = par1IIconRegister.registerIcon("extrautils:drum_top2");
    }
    
    public int getRenderType() {
        return ExtraUtilsProxy.drumRendererID;
    }
    
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(final IBlockAccess world, final int x, final int y, final int z) {
        if (world.getTileEntity(x, y, z) instanceof TileEntityDrum) {
            return ((TileEntityDrum)world.getTileEntity(x, y, z)).getColor();
        }
        return 16777215;
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityDrum(metadata);
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        if (player.capabilities.isCreativeMode || !this.canHarvestBlock(player, world.getBlockMetadata(x, y, z))) {
            return super.removedByPlayer(world, player, x, y, z, willHarvest);
        }
        final ArrayList<ItemStack> items = this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        if (world.setBlockToAir(x, y, z)) {
            if (!world.isRemote) {
                for (final ItemStack item : items) {
                    this.dropBlockAsItem(world, x, y, z, item);
                }
            }
            return true;
        }
        return false;
    }
    
    public void harvestBlock(final World par1World, final EntityPlayer par2EntityPlayer, final int par3, final int par4, final int par5, final int par6) {
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[getIdFromBlock((Block)this)], 1);
        par2EntityPlayer.addExhaustion(0.025f);
    }
    
    public void onBlockPlacedBy(final World par1World, final int par2, final int par3, final int par4, final EntityLivingBase par5EntityLivingBase, final ItemStack par6ItemStack) {
        if (par6ItemStack.hasTagCompound() && par6ItemStack.getItem() instanceof ItemBlockDrum) {
            final TileEntity drum = par1World.getTileEntity(par2, par3, par4);
            if (drum != null && drum instanceof TileEntityDrum) {
                final FluidStack fluid = ((ItemBlockDrum)par6ItemStack.getItem()).drain(par6ItemStack, Integer.MAX_VALUE, false);
                ((TileEntityDrum)drum).setCapacityFromMetadata(par6ItemStack.getItemDamage());
                if (fluid != null) {
                    ((TileEntityDrum)drum).fill(ForgeDirection.UP, fluid, true);
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        if (par2CreativeTabs != null) {
            return;
        }
        FluidRegistry.getRegisteredFluidIDs().keySet().iterator();
        for (final Fluid fluid1 : FluidRegistry.getRegisteredFluids().values()) {
            final ItemStack drum = new ItemStack(par1, 1, 0);
            final ItemStack drum2 = new ItemStack(par1, 1, 1);
            final FluidStack fluid2 = FluidRegistry.getFluidStack(fluid1.getName(), TileEntityDrum.getCapacityFromMetadata(1));
            if (fluid2 != null) {
                ((ItemBlockDrum)par1).fill(drum, fluid2, true);
                par3List.add(drum);
                ((ItemBlockDrum)par1).fill(drum2, fluid2, true);
                par3List.add(drum2);
            }
        }
    }
    
    public ItemStack getPickBlock(final MovingObjectPosition target, final World world, final int x, final int y, final int z) {
        return this.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0).get(0);
    }
    
    public int damageDropped(final int meta) {
        return meta;
    }
    
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        final ItemStack item = new ItemStack((Block)this, 1, this.damageDropped(metadata));
        if (world.getTileEntity(x, y, z) instanceof TileEntityDrum && item.getItem() instanceof ItemBlockDrum) {
            final FluidStack fluid = ((TileEntityDrum)world.getTileEntity(x, y, z)).getTankInfo(ForgeDirection.UP)[0].fluid;
            if (fluid != null) {
                ((ItemBlockDrum)item.getItem()).fill(item, fluid, true);
            }
        }
        ret.add(item);
        return ret;
    }
    
    public void updateTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        if (!par1World.isRemote) {
            final TileEntity drum = par1World.getTileEntity(par2, par3, par4);
            if (drum instanceof TileEntityDrum) {
                ((TileEntityDrum)drum).ticked();
            }
        }
    }
}

