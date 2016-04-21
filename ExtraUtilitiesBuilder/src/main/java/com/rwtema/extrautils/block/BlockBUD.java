// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.tileentity.TileEntityBUD;
import net.minecraft.tileentity.TileEntity;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockBUD extends Block
{
    private IIcon[] icons;
    
    public BlockBUD() {
        super(Material.anvil);
        this.setBlockName("extrautils:budoff");
        this.setBlockTextureName("extrautils:budoff");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setResistance(10.0f).setStepSound(BlockBUD.soundTypeStone);
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer player, final int par6, final float par7, final float par8, final float par9) {
        if (!par1World.isRemote && XUHelper.isWrench(player.getCurrentEquippedItem())) {
            int metadata = par1World.getBlockMetadata(par2, par3, par4);
            if (metadata >= 3) {
                metadata = 3 + (metadata - 2) % 7;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, metadata, 3);
            }
        }
        return true;
    }
    
    public int damageDropped(final int par1) {
        return (par1 >= 3) ? 3 : 0;
    }
    
    public boolean hasTileEntity(final int metadata) {
        return metadata >= 3;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        if (metadata >= 3) {
            return new TileEntityBUD();
        }
        return null;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        (this.icons = new IIcon[6])[0] = par1IIconRegister.registerIcon("extrautils:budoff");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:budon");
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:advbudoff");
        this.icons[3] = par1IIconRegister.registerIcon("extrautils:advbudon");
        this.icons[4] = par1IIconRegister.registerIcon("extrautils:advbuddisabledoff");
        this.icons[5] = par1IIconRegister.registerIcon("extrautils:advbuddisabledon");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par2 >= 3) {
            return this.icons[2];
        }
        return this.icons[0];
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        final int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int i = (metadata >= 3) ? 2 : 0;
        if (metadata > 3 && metadata - 4 != Facing.oppositeSide[par5]) {
            i = 4;
        }
        if (par1IBlockAccess.getTileEntity(par2, par3, par4) instanceof TileEntityBUD) {
            return ((TileEntityBUD)par1IBlockAccess.getTileEntity(par2, par3, par4)).getPowered() ? this.icons[i + 1] : this.icons[i];
        }
        return this.icons[i + metadata % 2];
    }
    
    public int isProvidingStrongPower(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        final int metadata = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        if (metadata == 1) {
            return 15;
        }
        if (metadata < 3) {
            return 0;
        }
        if (metadata > 3 && metadata - 4 != par5) {
            return 0;
        }
        if (par1IBlockAccess.getTileEntity(par2, par3, par4) instanceof TileEntityBUD) {
            return ((TileEntityBUD)par1IBlockAccess.getTileEntity(par2, par3, par4)).getPowered() ? 15 : 0;
        }
        return 0;
    }
    
    public boolean canProvidePower() {
        return true;
    }
    
    public int isProvidingWeakPower(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return this.isProvidingStrongPower(par1IBlockAccess, par2, par3, par4, par5);
    }
    
    public boolean canConnectRedstone(final IBlockAccess iba, final int i, final int j, final int k, final int dir) {
        return true;
    }
    
    public int tickRate() {
        return 2;
    }
    
    public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final Block par5) {
        if (par5 != this & par5 != Blocks.unpowered_repeater) {
            final int data = par1World.getBlockMetadata(par2, par3, par4);
            if (data == 0) {
                par1World.scheduleBlockUpdate(par2, par3, par4, (Block)this, this.tickRate());
            }
        }
    }
    
    public void updateRedstone(final World par1World, final int par2, final int par3, final int par4) {
        par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, (Block)this);
        par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, (Block)this);
        par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, (Block)this);
        par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, (Block)this);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, (Block)this);
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, (Block)this);
    }
    
    public void updateTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        if (par1World.getBlockMetadata(par2, par3, par4) == 0) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1, 3);
            par1World.scheduleBlockUpdate(par2, par3, par4, (Block)this, this.tickRate());
        }
        else if (par1World.getBlockMetadata(par2, par3, par4) == 1) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 3);
            par1World.scheduleBlockUpdate(par2, par3, par4, (Block)this, this.tickRate() * 3);
        }
        else if (par1World.getBlockMetadata(par2, par3, par4) == 2) {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 3);
        }
        this.updateRedstone(par1World, par2, par3, par4);
    }
    
    public boolean isBlockSolid(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return true;
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        super.isSideSolid(world, x, y, z, side);
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 3));
    }
}

