// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import java.util.List;
import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public abstract class BlockMultiBlockSelection extends BlockMultiBlock
{
    public Box boundsOveride;
    
    public BlockMultiBlockSelection(final Material xMaterial) {
        super(xMaterial);
        this.boundsOveride = null;
    }
    
    public MovingObjectPosition collisionRayTrace(final World world, final int x, final int y, final int z, final Vec3 start, final Vec3 end) {
        MovingObjectPosition result = null;
        for (final Box box : this.getWorldModel((IBlockAccess)world, x, y, z)) {
            this.boundsOveride = box;
            final MovingObjectPosition r = super.collisionRayTrace(world, x, y, z, start, end);
            if (r != null && (result == null || start.distanceTo(r.hitVec) < start.distanceTo(result.hitVec))) {
                result = r;
            }
        }
        this.boundsOveride = null;
        return result;
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess par1IBlockAccess, final int x, final int y, final int z) {
        Box bounds;
        if (this.boundsOveride != null) {
            bounds = this.boundsOveride;
        }
        else {
            bounds = BoxModel.boundingBox(this.getWorldModel(par1IBlockAccess, x, y, z));
        }
        if (bounds != null) {
            this.setBlockBounds(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        }
    }
}


