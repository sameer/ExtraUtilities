// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.block.Block;
import com.rwtema.extrautils.block.BoxModel;
import net.minecraft.world.IBlockAccess;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import com.rwtema.extrautils.block.BlockMultiBlock;

public class BlockEnderMarkers extends BlockMultiBlock
{
    public static int[] dx;
    public static int[] dz;
    
    public BlockEnderMarkers() {
        super(Material.circuits);
        this.setBlockName("extrautils:endMarker");
        this.setBlockTextureName("extrautils:endMarker");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setStepSound(BlockEnderMarkers.soundTypeStone);
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(final World par1World, final int par2, final int par3, final int par4) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer p_149727_5_, final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random rand) {
        final int meta = world.getBlockMetadata(x, y, z);
        for (int i = 0; i < 4; ++i) {
            if ((meta & 1 << i) != 0x0) {
                for (int l = 0; l < 3; ++l) {
                    world.spawnParticle("reddust", x + 0.5 + BlockEnderMarkers.dx[i] * rand.nextDouble() * rand.nextDouble(), y + 0.5, z + 0.5 + BlockEnderMarkers.dz[i] * rand.nextDouble() * rand.nextDouble(), 0.501, 0.0, 1.0);
                }
            }
        }
    }
    
    public boolean hasTileEntity(final int metadata) {
        return true;
    }
    
    public TileEntity createTileEntity(final World world, final int metadata) {
        return new TileEntityEnderMarker();
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final BoxModel model = new BoxModel();
        model.addBoxI(7, 0, 7, 9, 13, 9).fillIcons(this, 0);
        for (final ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (world.isSideSolid(x + d.offsetX, y + d.offsetY, z + d.offsetZ, d.getOpposite(), false)) {
                model.rotateToSideTex(d);
                return model;
            }
        }
        return model;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final BoxModel model = new BoxModel();
        model.addBoxI(7, 0, 7, 9, 13, 9);
        return model;
    }
    
    static {
        BlockEnderMarkers.dx = new int[] { 0, 0, 1, -1 };
        BlockEnderMarkers.dz = new int[] { 1, -1, 0, 0 };
    }
}

