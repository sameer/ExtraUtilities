// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderquarry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;

public class BlockDummy extends Block
{
    public BlockDummy(final Material par2Material) {
        super(par2Material);
        throw new RuntimeException("This block is a dummy and must never be assigned");
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z, final boolean willHarvest) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public boolean removedByPlayer(final World world, final EntityPlayer player, final int x, final int y, final int z) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public void breakBlock(final World par1World, final int par2, final int par3, final int par4, final Block par5, final int par6) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public void onBlockPreDestroy(final World par1World, final int par2, final int par3, final int par4, final int par5) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public void harvestBlock(final World par1World, final EntityPlayer par2EntityPlayer, final int par3, final int par4, final int par5, final int par6) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public void onBlockDestroyedByPlayer(final World par1World, final int par2, final int par3, final int par4, final int par5) {
        throw new RuntimeException("This block's methods must never be called");
    }
    
    public void onBlockHarvested(final World par1World, final int par2, final int par3, final int par4, final int par5, final EntityPlayer par6EntityPlayer) {
        throw new RuntimeException("This block's methods must never be called");
    }
}
