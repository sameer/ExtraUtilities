// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import com.rwtema.extrautils.tileentity.TileEntityAntiMobTorch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

public class BlockChandelier extends Block
{
    public static int range;
    public static int[] dx;
    public static int[] dy;
    public static int[] dz;
    
    public BlockChandelier() {
        super(Material.circuits);
        this.setLightLevel(1.0f);
        this.setLightOpacity(0);
        this.setBlockName("extrautils:chandelier");
        this.setBlockTextureName("extrautils:chandelier");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setHardness(0.1f);
    }
    
    public boolean isOpaqueCube() {
        return false;
    }
    
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public int getRenderType() {
        return 1;
    }
    
    public boolean canPlaceBlockAt(final World par1World, final int par2, final int par3, final int par4) {
        if (par1World.isSideSolid(par2, par3 + 1, par4, ForgeDirection.DOWN, true)) {
            return true;
        }
        final Block id = par1World.getBlock(par2, par3 + 1, par4);
        return id == Blocks.fence || id == Blocks.nether_brick_fence || id == Blocks.glass || id == Blocks.cobblestone_wall;
    }
    
    private boolean dropTorchIfCantStay(final World par1World, final int par2, final int par3, final int par4) {
        if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
            if (par1World.getBlock(par2, par3, par4) == this) {
                this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                par1World.setBlockToAir(par2, par3, par4);
            }
            return false;
        }
        return true;
    }
    
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block par5) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            this.dropBlockAsItem(world, x, y, z, 0, 0);
            world.setBlockToAir(x, y, z);
        }
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityAntiMobTorch();
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
        final double i = rand.nextInt(5);
        final double t = (2.0 + i * 3.0) / 16.0;
        if (rand.nextBoolean()) {
            world.spawnParticle("smoke", x + t, y + Math.abs(i - 2.0) * 2.0 / 16.0, z + t, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", x + t, y + Math.abs(i - 2.0) * 2.0 / 16.0, z + t, 0.0, 0.0, 0.0);
        }
        else {
            world.spawnParticle("smoke", x + t, y + Math.abs(i - 2.0) * 2.0 / 16.0, z + 1 - t, 0.0, 0.0, 0.0);
            world.spawnParticle("flame", x + t, y + Math.abs(i - 2.0) * 2.0 / 16.0, z + 1 - t, 0.0, 0.0, 0.0);
        }
    }
    
    static {
        BlockChandelier.range = 1;
        BlockChandelier.dx = new int[] { -1, 1, 0, 0, 0, 0 };
        BlockChandelier.dy = new int[] { 0, 0, 0, 0, -1, 1 };
        BlockChandelier.dz = new int[] { 0, 0, -1, 1, 0, 0 };
    }
}
