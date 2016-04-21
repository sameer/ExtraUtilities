// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.vec.Cuboid6;

public class PartWallDummy extends PartFenceDummyArm
{
    public static final PartWallDummy[] dummyArms;
    
    public PartWallDummy(final int dir) {
        this.boxes.add(PartWall.renderCuboids1[dir]);
    }
    
    @Override
    public Cuboid6 getBounds() {
        return PartWall.partCuboids[this.dir];
    }
    
    static {
        dummyArms = new PartWallDummy[] { null, null, new PartWallDummy(2), new PartWallDummy(3), new PartWallDummy(4), new PartWallDummy(5) };
    }
}

