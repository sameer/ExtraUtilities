// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;

public class BlockCardboardWalls extends BlockMultiBlock
{
    public BlockCardboardWalls() {
        super(Material.cloth);
        this.setBlockName("extrautils:cardboardwall");
        this.setBlockTextureName("extrautils:cardboard");
    }
    
    public void prepareForRender(final String label) {
    }
    
    public boolean isCardBoard(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return this == world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
    }
    
    public boolean isSide(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
        return world.isSideSolid(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.getOpposite(), false);
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final BoxModel model = new BoxModel();
        return model;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        final BoxModel box = new BoxModel();
        box.addBoxI(7, 0, 9, 7, 16, 9);
        return box;
    }
}


