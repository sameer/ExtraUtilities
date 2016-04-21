// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.block.Block;

public class BlockEnderQuarry extends Block
{
    int[] tiletype;
    IIcon[] top;
    IIcon[] bottom;
    IIcon[] side;
    
    public BlockEnderQuarry() {
        super(Material.rock);
        this.tiletype = new int[] { 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
        this.top = new IIcon[3];
        this.bottom = new IIcon[3];
        this.side = new IIcon[3];
        this.setBlockName("extrautils:enderQuarry");
        this.setBlockTextureName("extrautils:enderQuarry");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(1.0f);
        this.setStepSound(BlockEnderQuarry.soundTypeStone);
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityEnderQuarry();
    }
    
    public void onNeighborBlockChange(final World par1World, final int par2, final int par3, final int par4, final Block par5) {
        final TileEntity tile;
        if ((tile = par1World.getTileEntity(par2, par3, par4)) instanceof TileEntityEnderQuarry) {
            ((TileEntityEnderQuarry)tile).detectInventories();
        }
    }
    
    public boolean onBlockActivated(final World par1World, final int par2, final int par3, final int par4, final EntityPlayer par5EntityPlayer, final int par6, final float par7, final float par8, final float par9) {
        if (par1World.isRemote) {
            return true;
        }
        final TileEntity tile;
        if ((tile = par1World.getTileEntity(par2, par3, par4)) instanceof TileEntityEnderQuarry) {
            ((TileEntityEnderQuarry)tile).startFencing(par5EntityPlayer);
            if (par5EntityPlayer.getHeldItem() == null && par5EntityPlayer.capabilities.isCreativeMode && par5EntityPlayer.isSneaking() && ((TileEntityEnderQuarry)tile).started) {
                par5EntityPlayer.addChatComponentMessage((IChatComponent)new ChatComponentText("Overclock Mode Activated"));
                ((TileEntityEnderQuarry)tile).debug();
            }
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.top[0] = (this.top[2] = par1IIconRegister.registerIcon("extrautils:enderQuarry_top"));
        this.top[1] = par1IIconRegister.registerIcon("extrautils:enderQuarry_top_active");
        final IIcon[] bottom = this.bottom;
        final int n = 0;
        final IIcon[] bottom2 = this.bottom;
        final int n2 = 1;
        final IIcon[] bottom3 = this.bottom;
        final int n3 = 2;
        final IIcon registerIcon = par1IIconRegister.registerIcon("extrautils:enderQuarry_bottom");
        bottom3[n3] = registerIcon;
        bottom[n] = (bottom2[n2] = registerIcon);
        this.side[0] = (this.blockIcon = par1IIconRegister.registerIcon("extrautils:enderQuarry"));
        this.side[1] = par1IIconRegister.registerIcon("extrautils:enderQuarry_active");
        this.side[2] = par1IIconRegister.registerIcon("extrautils:enderQuarry_finished");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, int par2) {
        if (par2 > 2 || par2 < 0) {
            par2 = 0;
        }
        if (par1 == 0) {
            return this.bottom[par2];
        }
        if (par1 == 1) {
            return this.top[par2];
        }
        return this.side[par2];
    }
}


