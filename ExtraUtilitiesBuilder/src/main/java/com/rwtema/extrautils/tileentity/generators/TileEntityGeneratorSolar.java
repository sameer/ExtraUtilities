// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.generators;

public class TileEntityGeneratorSolar extends TileEntityGeneratorSimpleSolar
{
    public int curLevel;
    
    public TileEntityGeneratorSolar() {
        this.curLevel = -1;
    }
    
    @Override
    public String getBlurb(final double coolDown, final double energy) {
        if (this.isPowered()) {
            if (coolDown == 0.0) {
                return "Transmitting: Deactivate redstone signal to resume charging";
            }
            return "Time Remaining until transmittion starts:\n" + TileEntityGenerator.getCoolDownString(coolDown);
        }
        else {
            if (coolDown == 0.0) {
                return "\n\n\nCharging: Apply redstone signal to transmit energy";
            }
            return "PowerLevel:\n" + energy + "\n\nCharging: Apply redstone signal to transmit energy";
        }
    }
    
    @Override
    public int transferLimit() {
        return 20 + (int)(this.genLevel() / 2.0);
    }
    
    @Override
    public int getMaxCoolDown() {
        return 200;
    }
    
    @Override
    public double genLevel() {
        if (this.curLevel == -1 || this.worldObj.getTotalWorldTime() % 20L == 0L) {
            this.curLevel = this.genLevelForTime();
        }
        if (this.curLevel == 0) {
            return 0.0;
        }
        if (!this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord + 1, this.zCoord)) {
            return 0.0;
        }
        if (this.isPowered()) {
            return 0.0;
        }
        return this.curLevel;
    }
    
    @Override
    public boolean processInput() {
        if (this.genLevel() > 0.0) {
            this.coolDown = this.getMaxCoolDown();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean shouldTransmit() {
        return this.coolDown == 0.0;
    }
}

