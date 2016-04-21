// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.multipart.MultipartGenerator;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import codechicken.microblock.MicroblockRender;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.lib.vec.Vector3;
import net.minecraft.block.Block;
import net.minecraft.util.Facing;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.TMultiPart;
import java.util.List;
import java.util.ArrayList;
import codechicken.lib.vec.Cuboid6;

public class PartFence extends PartConnecting implements IFence
{
    public static final Cuboid6[] partCuboids;
    public static final Cuboid6[] renderCuboids1;
    public static final Cuboid6[] renderCuboids2;
    public static final String type = "extrautils:fence";
    
    public PartFence() {
    }
    
    public PartFence(final int material) {
        super(material);
    }
    
    @Override
    public String getType() {
        return "extrautils:fence";
    }
    
    @Override
    public Cuboid6 getBounds() {
        return PartFence.partCuboids[0];
    }
    
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
        final List<Cuboid6> t = new ArrayList<Cuboid6>();
        if (this.isEthereal()) {
            return t;
        }
        t.add(PartFence.partCuboids[1].copy());
        for (int i = 2; i < 6; ++i) {
            if ((this.connectionMask & 1 << i) != 0x0) {
                t.add(PartFence.partCuboids[i].copy());
            }
        }
        return t;
    }
    
    @Override
    public boolean shouldConnect(final int x, final int y, final int z, final int direction) {
        final Block l = this.world().getBlock(x, y, z);
        if (this.world().getTileEntity(x, y, z) instanceof IFence && this.tile().canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[direction])) {
            return ((TileMultipart)this.world().getTileEntity(x, y, z)).canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[Facing.oppositeSide[direction]]);
        }
        return l.getMaterial().isOpaque() && l.renderAsNormalBlock() && this.tile().canAddPart((TMultiPart)PartFenceDummyArm.dummyArms[direction]);
    }
    
    @Override
    public void reloadShape() {
        final int prevMask = this.connectionMask;
        this.connectionMask = 0;
        for (int i = 2; i < 6; ++i) {
            if (this.shouldConnect(this.x() + Facing.offsetsXForSide[i], this.y() + Facing.offsetsYForSide[i], this.z() + Facing.offsetsZForSide[i], i)) {
                this.connectionMask |= 1 << i;
            }
        }
        if (prevMask != this.connectionMask) {
            this.tile().notifyPartChange((TMultiPart)this);
            this.tile().markRender();
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderStatic(final Vector3 pos, final int pass) {
        this.reloadShape();
        if (this.mat == null) {
            this.mat = MicroMaterialRegistry.getMaterial(this.material);
        }
        if (this.mat != null && this.mat.canRenderInPass(pass)) {
            MicroblockRender.renderCuboid(new Vector3((double)this.x(), (double)this.y(), (double)this.z()), this.mat, pass, this.getRenderBounds(), 0);
            for (int i = 0; i < 6; ++i) {
                if ((this.connectionMask & 1 << i) != 0x0) {
                    MicroblockRender.renderCuboid(new Vector3((double)this.x(), (double)this.y(), (double)this.z()), this.mat, pass, PartFence.renderCuboids1[i], 1 << Facing.oppositeSide[i] | 1 << i);
                    MicroblockRender.renderCuboid(new Vector3((double)this.x(), (double)this.y(), (double)this.z()), this.mat, pass, PartFence.renderCuboids2[i], 1 << Facing.oppositeSide[i] | 1 << i);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public float getStrength(final MovingObjectPosition hit, final EntityPlayer player) {
        return this.getMaterial().getStrength(player);
    }
    
    public int getMetadata() {
        return 1;
    }
    
    public TMultiPart newPart(final boolean client) {
        return (TMultiPart)new PartFence();
    }
    
    public TMultiPart placePart(final ItemStack stack, final EntityPlayer player, final World world, final BlockCoord pos, final int side, final Vector3 arg5, final int materialID) {
        return (TMultiPart)new PartFence(materialID);
    }
    
    public void registerPassThroughs() {
        MultipartGenerator.registerPassThroughInterface(IFence.class.getName());
    }
    
    public void renderItem(final ItemStack item, final MicroMaterialRegistry.IMicroMaterial material) {
        MicroblockRender.renderCuboid(new Vector3(0.0, 0.0, 0.0), material, -1, PartFence.partCuboids[0], 0);
        MicroblockRender.renderCuboid(new Vector3(0.0, 0.0, 0.0), material, -1, PartFence.renderCuboids1[2], 0);
        MicroblockRender.renderCuboid(new Vector3(0.0, 0.0, 0.0), material, -1, PartFence.renderCuboids1[3], 0);
        MicroblockRender.renderCuboid(new Vector3(0.0, 0.0, 0.0), material, -1, PartFence.renderCuboids2[2], 0);
        MicroblockRender.renderCuboid(new Vector3(0.0, 0.0, 0.0), material, -1, PartFence.renderCuboids2[3], 0);
    }
    
    static {
        partCuboids = new Cuboid6[] { new Cuboid6(0.375, 0.0, 0.375, 0.625, 1.0, 0.625), new Cuboid6(0.375, 0.0, 0.375, 0.625, 1.5, 0.625), new Cuboid6(0.375, 0.0, 0.0, 0.625, 1.5, 0.375), new Cuboid6(0.375, 0.0, 0.625, 0.625, 1.5, 1.0), new Cuboid6(0.0, 0.0, 0.375, 0.375, 1.5, 0.625), new Cuboid6(0.625, 0.0, 0.375, 1.0, 1.5, 0.625) };
        renderCuboids1 = new Cuboid6[] { null, null, new Cuboid6(0.4375, 0.75, 0.0, 0.5625, 0.9375, 0.375), new Cuboid6(0.4375, 0.75, 0.625, 0.5625, 0.9375, 1.0), new Cuboid6(0.0, 0.75, 0.4375, 0.375, 0.9375, 0.5625), new Cuboid6(0.625, 0.75, 0.4375, 1.0, 0.9375, 0.5625) };
        renderCuboids2 = new Cuboid6[] { null, null, new Cuboid6(0.4375, 0.375, 0.0, 0.5625, 0.5625, 0.375), new Cuboid6(0.4375, 0.375, 0.625, 0.5625, 0.5625, 1.0), new Cuboid6(0.0, 0.375, 0.4375, 0.375, 0.5625, 0.5625), new Cuboid6(0.625, 0.375, 0.4375, 1.0, 0.5625, 0.5625) };
    }
}

