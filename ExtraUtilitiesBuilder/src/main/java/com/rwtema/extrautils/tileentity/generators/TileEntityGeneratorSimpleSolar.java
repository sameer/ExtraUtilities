// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

import net.minecraft.util.MathHelper;

public class TileEntityGeneratorSimpleSolar extends TileEntityGenerator
{
    public int curLevel;
    
    public TileEntityGeneratorSimpleSolar() {
        this.curLevel = -1;
    }
    
    @Override
    public String getBlurb(final double coolDown, final double energy) {
        return "PowerLevel:\n" + energy;
    }
    
    @Override
    public int transferLimit() {
        return 400;
    }
    
    protected int genLevelForTime() {
        float f = this.worldObj.getCelestialAngleRadians(1.0f);
        if (f < 3.1415927f) {
            f += (0.0f - f) * 0.2f;
        }
        else {
            f += (6.2831855f - f) * 0.2f;
        }
        final int c = Math.min(40, Math.round(40.0f * MathHelper.cos(f)));
        return (c > 0) ? c : 0;
    }
    
    @Override
    public int getMaxCoolDown() {
        return 240;
    }
    
    @Override
    public double genLevel() {
        if (this.curLevel == -1 || this.worldObj.getTotalWorldTime() % 20L == 0L) {
            this.curLevel = this.genLevelForTime();
        }
        if (this.curLevel == 0) {
            return 0.0;
        }
        if (!this.worldObj.canBlockSeeTheSky(this.x(), this.y() + 1, this.z())) {
            return 0.0;
        }
        return this.curLevel;
    }
    
    @Override
    public boolean processInput() {
        this.coolDown = this.getMaxCoolDown();
        return true;
    }
    
    @Override
    public boolean shouldProcess() {
        return this.genLevel() > 0.0;
    }
}
