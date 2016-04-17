// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.transfernodes.multiparts;

import java.util.Arrays;
import codechicken.lib.vec.Cuboid6;
import codechicken.multipart.NormalOcclusionTest;
import com.rwtema.extrautils.tileentity.transfernodes.pipes.IPipe;
import codechicken.multipart.TMultiPart;
import codechicken.microblock.ISidedHollowConnect;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.JCuboidPart;

public class DummyPipePart extends JCuboidPart implements JNormalOcclusion, ISidedHollowConnect
{
    public int dir;
    public float h;
    
    public DummyPipePart(final int dir, final float h) {
        this.dir = dir;
        this.h = h;
    }
    
    public boolean occlusionTest(final TMultiPart npart) {
        return npart instanceof IPipe || NormalOcclusionTest.apply((JNormalOcclusion)this, npart);
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return Arrays.asList(this.getBounds());
    }
    
    public Cuboid6 getBounds() {
        switch (this.dir) {
            case 0: {
                return new Cuboid6(0.375, 0.0, 0.375, 0.625, (double)this.h, 0.625);
            }
            case 1: {
                return new Cuboid6(0.375, (double)(1.0f - this.h), 0.375, 0.625, 1.0, 0.625);
            }
            case 2: {
                return new Cuboid6(0.375, 0.375, 0.0, 0.625, 0.625, (double)this.h);
            }
            case 3: {
                return new Cuboid6(0.375, 0.375, (double)(1.0f - this.h), 0.625, 0.625, 1.0);
            }
            case 4: {
                return new Cuboid6(0.0, 0.375, 0.375, (double)this.h, 0.625, 0.625);
            }
            case 5: {
                return new Cuboid6((double)(1.0f - this.h), 0.375, 0.375, 1.0, 0.625, 0.625);
            }
            default: {
                return new Cuboid6(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            }
        }
    }
    
    public String getType() {
        return "dummyPipe";
    }
    
    public int getHollowSize(final int side) {
        return 2;
    }
}
