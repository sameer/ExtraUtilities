// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import java.util.Random;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

public class BlockTimer extends Block
{
    int powered_x;
    int powered_y;
    int powered_z;
    private boolean powered;
    private boolean changing;
    
    public BlockTimer() {
        super(Material.rock);
        this.powered_x = 0;
        this.powered_y = 0;
        this.powered_z = 0;
        this.powered = true;
        this.changing = false;
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:timer");
        this.setBlockTextureName("extrautils:timer");
        this.setHardness(1.0f);
    }
    
    public int getTickRate(final int metadata) {
        return 20;
    }
    
    public int getMobilityFlag() {
        return 2;
    }
    
    public int isProvidingWeakPower(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return (this.powered && par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1) ? 15 : 0;
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube() {
        return false;
    }
    
    public boolean canProvidePower() {
        return true;
    }
    
    public void onBlockAdded(final World world, final int x, final int y, final int z) {
        final int metadata = world.getBlockMetadata(x, y, z);
        world.scheduleBlockUpdate(x, y, z, (Block)this, this.getTickRate(metadata << 1) - 2);
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
        final int metadata = world.getBlockMetadata(x, y, z);
        if (metadata == 0) {
            this.changing = true;
            world.setBlockMetadataWithNotify(x, y, z, 1, 1);
            this.changing = false;
            world.scheduleBlockUpdate(x, y, z, (Block)this, 2);
        }
        else if (metadata == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 1);
            this.powered = false;
            final boolean p = world.getBlockPowerInput(x, y, z) > 0;
            this.powered = true;
            if (p) {
                this.changing = true;
                world.setBlockMetadataWithNotify(x, y, z, 2, 0);
                this.changing = false;
            }
            else {
                world.scheduleBlockUpdate(x, y, z, (Block)this, this.getTickRate(metadata) - 2);
            }
        }
    }
    
    public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block par5) {
        if (this.changing) {
            return;
        }
        final int metadata = world.getBlockMetadata(x, y, z);
        this.powered = false;
        final boolean p = world.getBlockPowerInput(x, y, z) > 0;
        this.powered = true;
        if (metadata == 0 && p) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 0);
        }
        else if (metadata == 2 && !p) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 0);
            world.scheduleBlockUpdate(x, y, z, (Block)this, this.getTickRate(metadata));
        }
    }
}
