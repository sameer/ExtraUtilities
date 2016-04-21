// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.block;

import net.minecraftforge.common.util.ForgeDirection;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.block.Block;
import net.minecraft.util.IIcon;

public class Box
{
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;
    public int offsetx;
    public int offsety;
    public int offsetz;
    public String label;
    public IIcon texture;
    public IIcon[] textureSide;
    public boolean invisible;
    public boolean renderAsNormalBlock;
    public boolean[] invisibleSide;
    public int uvRotateEast;
    public int uvRotateWest;
    public int uvRotateSouth;
    public int uvRotateNorth;
    public int uvRotateTop;
    public int uvRotateBottom;
    public int color;
    public int[] rotAdd;
    
    public Box(final String l, final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        this.offsetx = 0;
        this.offsety = 0;
        this.offsetz = 0;
        this.label = "";
        this.texture = null;
        this.textureSide = new IIcon[6];
        this.invisible = false;
        this.renderAsNormalBlock = false;
        this.invisibleSide = new boolean[6];
        this.uvRotateEast = 0;
        this.uvRotateWest = 0;
        this.uvRotateSouth = 0;
        this.uvRotateNorth = 0;
        this.uvRotateTop = 0;
        this.uvRotateBottom = 0;
        this.color = 16777215;
        this.rotAdd = new int[] { 0, 1, 2, 3 };
        this.label = l;
        this.minX = Math.min(par1, par7);
        this.minY = Math.min(par3, par9);
        this.minZ = Math.min(par5, par11);
        this.maxX = Math.max(par1, par7);
        this.maxY = Math.max(par3, par9);
        this.maxZ = Math.max(par5, par11);
    }
    
    public Box(final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        this("", par1, par3, par5, par7, par9, par11);
    }
    
    public Box setTextures(final IIcon[] icons) {
        for (int i = 0; i < 6 && i < icons.length; ++i) {
            this.textureSide[i] = icons[i];
        }
        return this;
    }
    
    public Box setTextureSides(final Object... tex) {
        this.textureSide = new IIcon[6];
        int s = 0;
        for (final Object aTex : tex) {
            if (aTex instanceof Integer) {
                s = (Integer)aTex;
            }
            else if (aTex instanceof IIcon && s < 6 && s >= 0) {
                this.textureSide[s] = (IIcon)aTex;
                ++s;
            }
        }
        return this;
    }
    
    public Box setColor(final int col) {
        this.color = col;
        return this;
    }
    
    public Box setAllSideInvisible() {
        for (int i = 0; i < 6; ++i) {
            this.invisibleSide[i] = true;
        }
        return this;
    }
    
    public Box setSideInvisible(final Object... tex) {
        int s = 0;
        for (final Object aTex : tex) {
            if (aTex instanceof Integer) {
                s = (Integer)aTex;
                this.invisibleSide[s] = true;
            }
            else if (aTex instanceof Boolean) {
                this.invisibleSide[s] = (boolean)aTex;
                ++s;
            }
        }
        return this;
    }
    
    public Box setTexture(final IIcon l) {
        this.texture = l;
        return this;
    }
    
    public Box setLabel(final String l) {
        this.label = l;
        return this;
    }
    
    public Box copy() {
        return new Box(this.label, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
    
    public void setBounds(final float par1, final float par3, final float par5, final float par7, final float par9, final float par11) {
        this.minX = Math.min(par1, par7);
        this.minY = Math.min(par3, par9);
        this.minZ = Math.min(par5, par11);
        this.maxX = Math.max(par1, par7);
        this.maxY = Math.max(par3, par9);
        this.maxZ = Math.max(par5, par11);
    }
    
    public Box offset(final float x, final float y, final float z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }
    
    public Box rotateY(int numRotations) {
        if (numRotations == 0) {
            return this;
        }
        if (numRotations < 0) {
            numRotations += 4;
        }
        numRotations &= 0x3;
        for (int i = 0; i < numRotations; ++i) {
            final Box prev = this.copy();
            this.minZ = prev.minX;
            this.maxZ = prev.maxX;
            this.minX = 1.0f - prev.maxZ;
            this.maxX = 1.0f - prev.minZ;
            final IIcon temp = this.textureSide[2];
            this.textureSide[2] = this.textureSide[4];
            this.textureSide[4] = this.textureSide[3];
            this.textureSide[3] = this.textureSide[5];
            this.textureSide[5] = temp;
        }
        this.uvRotateTop = (this.uvRotateTop + this.rotAdd[numRotations]) % 2;
        this.uvRotateBottom = (this.uvRotateBottom + this.rotAdd[numRotations]) % 2;
        return this;
    }
    
    public Box fillIcons(final Block b, final int meta) {
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @Override
            public void exectuteClientCode() {
                for (int side = 0; side < 6; ++side) {
                    Box.this.textureSide[side] = b.getIcon(side, meta);
                }
            }
        });
        return this;
    }
    
    public Box swapIcons(final int a, final int b) {
        final IIcon temp = this.textureSide[a];
        this.textureSide[a] = this.textureSide[b];
        this.textureSide[b] = temp;
        return this;
    }
    
    public Box rotateToSideTex(final ForgeDirection dir) {
        final Box prev = this.copy();
        this.rotateToSide(dir);
        switch (dir) {
            case UP: {
                this.swapIcons(0, 1);
                this.uvRotateEast = 3;
                this.uvRotateWest = 3;
                this.uvRotateSouth = 3;
                this.uvRotateNorth = 3;
                break;
            }
            case NORTH: {
                this.swapIcons(1, 3);
                this.swapIcons(0, 2);
                this.uvRotateSouth = 2;
                this.uvRotateNorth = 1;
                this.uvRotateTop = 3;
                this.uvRotateBottom = 3;
                break;
            }
            case SOUTH: {
                this.swapIcons(1, 2);
                this.swapIcons(0, 3);
                this.uvRotateSouth = 1;
                this.uvRotateNorth = 2;
                break;
            }
            case WEST: {
                this.swapIcons(1, 5);
                this.swapIcons(0, 4);
                this.uvRotateEast = 2;
                this.uvRotateWest = 1;
                this.uvRotateTop = 1;
                this.uvRotateBottom = 2;
                break;
            }
            case EAST: {
                this.swapIcons(1, 4);
                this.swapIcons(0, 5);
                this.uvRotateEast = 1;
                this.uvRotateWest = 2;
                this.uvRotateTop = 2;
                this.uvRotateBottom = 1;
                break;
            }
        }
        return this;
    }
    
    public Box rotateToSide(final ForgeDirection dir) {
        final Box prev = this.copy();
        switch (dir) {
            case UP: {
                this.minY = 1.0f - prev.maxY;
                this.maxY = 1.0f - prev.minY;
                break;
            }
            case NORTH: {
                this.minZ = prev.minY;
                this.maxZ = prev.maxY;
                this.minY = prev.minX;
                this.maxY = prev.maxX;
                this.minX = prev.minZ;
                this.maxX = prev.maxZ;
                break;
            }
            case SOUTH: {
                this.minZ = 1.0f - prev.maxY;
                this.maxZ = 1.0f - prev.minY;
                this.minY = prev.minX;
                this.maxY = prev.maxX;
                this.minX = 1.0f - prev.maxZ;
                this.maxX = 1.0f - prev.minZ;
                break;
            }
            case WEST: {
                this.minX = prev.minY;
                this.maxX = prev.maxY;
                this.minY = prev.minX;
                this.maxY = prev.maxX;
                this.minZ = 1.0f - prev.maxZ;
                this.maxZ = 1.0f - prev.minZ;
                break;
            }
            case EAST: {
                this.minX = 1.0f - prev.maxY;
                this.maxX = 1.0f - prev.minY;
                this.minY = prev.minX;
                this.maxY = prev.maxX;
                break;
            }
        }
        return this;
    }
}



