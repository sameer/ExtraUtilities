// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ChunkPos
{
    public int x;
    public int y;
    public int z;
    
    public ChunkPos(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public ChunkPos(final Vec3 p_i45364_1_) {
        this(MathHelper.floor_double(p_i45364_1_.xCoord), MathHelper.floor_double(p_i45364_1_.yCoord), MathHelper.floor_double(p_i45364_1_.zCoord));
    }
    
    @Override
    public boolean equals(final Object par1Obj) {
        if (!(par1Obj instanceof ChunkPos)) {
            return false;
        }
        final ChunkPos pos = (ChunkPos)par1Obj;
        return pos.x == this.x && pos.y == this.y && pos.z == this.z;
    }
    
    @Override
    public int hashCode() {
        return this.x * 8976890 + this.y * 981131 + this.z;
    }
}

