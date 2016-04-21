// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;
import codechicken.lib.vec.Cuboid6;
import java.util.ArrayList;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.JCuboidPart;

public class PartFenceDummyArm extends JCuboidPart implements JNormalOcclusion
{
    public static final PartFenceDummyArm[] dummyArms;
    protected final ArrayList<Cuboid6> boxes;
    public int dir;
    
    public PartFenceDummyArm() {
        this.boxes = new ArrayList<Cuboid6>();
    }
    
    public PartFenceDummyArm(final int dir) {
        this.boxes = new ArrayList<Cuboid6>();
        this.dir = dir;
        this.boxes.add(PartFence.renderCuboids1[dir]);
        this.boxes.add(PartFence.renderCuboids2[dir]);
    }
    
    public Cuboid6 getBounds() {
        return PartFence.partCuboids[this.dir];
    }
    
    public String getType() {
        return "extrautils:fence_dummy_part_should_never_actually_be_created_(if_it_is,_it_is_bug)";
    }
    
    public boolean occlusionTest(final TMultiPart npart) {
        return NormalOcclusionTest.apply((JNormalOcclusion)this, npart);
    }
    
    public Iterable<Cuboid6> getOcclusionBoxes() {
        return this.boxes;
    }
    
    static {
        dummyArms = new PartFenceDummyArm[] { null, null, new PartFenceDummyArm(2), new PartFenceDummyArm(3), new PartFenceDummyArm(4), new PartFenceDummyArm(5) };
    }
}


