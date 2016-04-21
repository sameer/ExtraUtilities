// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.block.Block;
import java.util.Collection;
import java.util.Iterator;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;
import java.util.ArrayList;

public class BoxModel extends ArrayList<Box>
{
    public int invModelRotate;
    public String label;
    
    public BoxModel() {
        this.invModelRotate = 90;
        this.label = "";
    }
    
    public BoxModel(final Box newBox) {
        this.invModelRotate = 90;
        this.label = "";
        this.add(newBox);
    }
    
    public BoxModel(final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        this.invModelRotate = 90;
        this.label = "";
        this.add(new Box(par1, par3, par5, par7, par9, par11));
    }
    
    public static BoxModel newStandardBlock() {
        final Box t = new Box(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        t.renderAsNormalBlock = true;
        return new BoxModel(t);
    }
    
    public static BoxModel hollowBox(final float minX, final float minY, final float minZ, final float holeMinX, final float holeMinZ, final float holeMaxX, final float holeMaxZ, final float maxX, final float maxY, final float maxZ) {
        final BoxModel t = new BoxModel();
        t.add(new Box(minX, minY, minZ, holeMinX, maxY, maxZ));
        t.add(new Box(holeMinX, minY, minZ, holeMaxX, maxY, holeMinZ));
        t.add(new Box(holeMinX, minY, holeMaxZ, holeMaxX, maxY, maxZ));
        t.add(new Box(holeMaxX, minY, minZ, maxX, maxY, maxZ));
        return t;
    }
    
    public static Box boundingBox(final List<Box> models) {
        if (models == null) {
            return null;
        }
        if (models.size() == 0) {
            return null;
        }
        final Box bounds = models.get(0).copy();
        for (int i = 1; i < models.size(); ++i) {
            bounds.setBounds(Math.min(bounds.minX, models.get(i).minX), Math.min(bounds.minY, models.get(i).minY), Math.min(bounds.minZ, models.get(i).minZ), Math.max(bounds.maxX, models.get(i).maxX), Math.max(bounds.maxY, models.get(i).maxY), Math.max(bounds.maxZ, models.get(i).maxZ));
        }
        return bounds;
    }
    
    public Box addBoxI(final int par1, final int par3, final int par5, final int par7, final int par9, final int par11) {
        return this.addBox("", par1 / 16.0f, par3 / 16.0f, par5 / 16.0f, par7 / 16.0f, par9 / 16.0f, par11 / 16.0f);
    }
    
    public Box addBox(final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        return this.addBox("", par1, par3, par5, par7, par9, par11);
    }
    
    public Box addBox(final String l, final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        final Box b = new Box(l, par1, par3, par5, par7, par9, par11);
        this.add(b);
        return b;
    }
    
    public BoxModel rotateToSide(final ForgeDirection dir) {
        for (final Box box : this) {
            box.rotateToSide(dir);
        }
        return this;
    }
    
    public BoxModel rotateY(final int numRotations) {
        for (final Box box : this) {
            box.rotateY(numRotations);
        }
        return this;
    }
    
    public BoxModel offset(final float x, final float y, final float z) {
        for (final Box box : this) {
            box.offset(x, y, z);
        }
        return this;
    }
    
    public BoxModel setColor(final int col) {
        for (final Box box : this) {
            box.setColor(col);
        }
        return this;
    }
    
    public BoxModel addYRotations() {
        this.addAll(this.copy().rotateY(1));
        this.addAll(this.copy().rotateY(2));
        return this;
    }
    
    public Box boundingBox() {
        return boundingBox(this);
    }
    
    public BoxModel copy() {
        final BoxModel newModel = new BoxModel();
        for (int i = 0; i < this.size(); ++i) {
            newModel.add(this.get(i).copy());
        }
        return newModel;
    }
    
    public BoxModel fillIcons(final Block block, final int meta) {
        if (ExtraUtilsMod.proxy.isClientSideAvailable()) {
            for (final Box b : this) {
                b.fillIcons(block, meta);
            }
        }
        return this;
    }
    
    public BoxModel rotateToSideTex(final ForgeDirection dir) {
        for (final Box b : this) {
            b.rotateToSideTex(dir);
        }
        return this;
    }
}

