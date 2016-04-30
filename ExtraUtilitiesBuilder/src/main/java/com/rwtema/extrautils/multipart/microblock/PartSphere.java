// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.render.BlockRenderer;
import java.util.Iterator;
import codechicken.lib.render.CCRenderState;
import com.rwtema.extrautils.LogHelper;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.lib.vec.BlockCoord;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import codechicken.multipart.TMultiPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import codechicken.lib.render.Vertex5;
import java.util.ArrayList;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Cuboid6;

public class PartSphere extends PartMicroBlock
{
    final double r = 0.375;
    public static Cuboid6 bounds;
    public static final Vector3 DOWN;
    public static final Vector3 UP;
    public static final Vector3 NORTH;
    public static final Vector3 SOUTH;
    public static final Vector3 EAST;
    public static final Vector3 WEST;
    public BlockFaceUniformLighting face;
    public static ArrayList<Vertex5[]> faces;
    public static ArrayList<Vertex5[]> faces_inv;
    double h;
    double h2;
    
    public PartSphere(final int materialID) {
        super(materialID);
        this.face = new BlockFaceUniformLighting();
        this.h = 1.35;
        this.h2 = Math.sqrt(1.0 - this.h * this.h / 4.0);
    }
    
    public PartSphere() {
        this.face = new BlockFaceUniformLighting();
        this.h = 1.35;
        this.h2 = Math.sqrt(1.0 - this.h * this.h / 4.0);
    }
    
    public Cuboid6 getBounds() {
        return new Cuboid6(0.125, 0.125, 0.125, 0.875, 0.875, 0.875);
    }
    
    public String getType() {
        return "extrautils:sphere";
    }
    
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
        if (this.isEthereal()) {
            return new ArrayList<Cuboid6>();
        }
        return Arrays.asList(this.getBounds());
    }
    
    @Override
    public boolean hideCreativeTab() {
        return false;
    }
    
    @Override
    public void reloadShape() {
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void render(final Vector3 pos, final int pass) {
        this.renderSphere(pos.copy().add(0.5), pass, this.mat);
    }
    
    public int getMetadata() {
        return 3;
    }
    
    public TMultiPart newPart(final boolean client) {
        return (TMultiPart)new PartSphere();
    }
    
    public TMultiPart placePart(final ItemStack stack, final EntityPlayer player, final World world, final BlockCoord pos, final int side, final Vector3 arg5, final int materialID) {
        return (TMultiPart)new PartSphere(materialID);
    }
    
    public void registerPassThroughs() {
    }
    
    @SideOnly(Side.CLIENT)
    public void renderItem(final ItemStack item, final MicroMaterialRegistry.IMicroMaterial material) {
        final Vector3 pos = new Vector3(0.5, 0.5, 0.5);
        if (PartSphere.faces_inv == null) {
            this.calcSphere(0.5, 0.5, PartSphere.faces_inv = new ArrayList<Vertex5[]>());
            LogHelper.debug("Calculated faces " + PartSphere.faces_inv.size(), new Object[0]);
        }
        CCRenderState.setModel((CCRenderState.IVertexSource)this.face);
        for (final Vertex5[] f : PartSphere.faces_inv) {
            this.face.lcComputed = false;
            this.face.verts[0].set(f[0]);
            this.face.verts[1].set(f[1]);
            this.face.verts[2].set(f[2]);
            this.face.verts[3].set(f[3]);
            this.face.side = f[0].uv.tex;
            material.renderMicroFace(pos, -1, this.getBounds());
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void renderSphere(final Vector3 pos, final int pass, final MicroMaterialRegistry.IMicroMaterial m) {
        if (PartSphere.faces == null) {
            this.calcSphere(0.25, 0.25, PartSphere.faces = new ArrayList<Vertex5[]>());
            LogHelper.debug("Calculated " + PartSphere.faces.size(), new Object[0]);
        }
        CCRenderState.setModel((CCRenderState.IVertexSource)this.face);
        for (final Vertex5[] f : PartSphere.faces) {
            this.face.verts[0].set(f[0]);
            this.face.verts[1].set(f[1]);
            this.face.verts[2].set(f[2]);
            this.face.verts[3].set(f[3]);
            this.face.side = f[0].uv.tex;
            m.renderMicroFace(pos, pass, this.getBounds());
        }
    }
    
    public void calcSphere(final double d, final double d2, final ArrayList<Vertex5[]> faces) {
        this.renderCurvedSide2(PartSphere.DOWN, PartSphere.NORTH, PartSphere.WEST, 0, d, d2, faces);
        this.renderCurvedSide2(PartSphere.UP, PartSphere.NORTH, PartSphere.EAST, 1, d, d2, faces);
        this.renderCurvedSide(PartSphere.NORTH, PartSphere.EAST, PartSphere.UP, 2, d, d2, faces);
        this.renderCurvedSide(PartSphere.SOUTH, PartSphere.WEST, PartSphere.UP, 3, d, d2, faces);
        this.renderCurvedSide(PartSphere.WEST, PartSphere.NORTH, PartSphere.UP, 4, d, d2, faces);
        this.renderCurvedSide(PartSphere.EAST, PartSphere.SOUTH, PartSphere.UP, 5, d, d2, faces);
    }
    
    @Override
    public int getLightValue() {
        return 15;
    }
    
    public void renderCurvedSide(final Vector3 forward, final Vector3 left, final Vector3 up, final int side, final double d, final double d2, final ArrayList<Vertex5[]> faces) {
        for (double u = 0.0; u < 1.0; u += d2) {
            for (double v = 0.0; v < 1.0; v += d) {
                final Vertex5[] verts = { new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5() };
                this.calcVec1(verts[0], forward, left, up, u + d2, v, side);
                this.calcVec1(verts[1], forward, left, up, u + d2, v + d, side);
                this.calcVec1(verts[2], forward, left, up, u, v + d, side);
                this.calcVec1(verts[3], forward, left, up, u, v, side);
                faces.add(verts);
            }
        }
    }
    
    public void calcVec1(final Vertex5 vert, final Vector3 forward, final Vector3 left, final Vector3 up, final double u, final double v, final int side) {
        final double a = u - 0.5;
        final double dy = (v - 0.5) * this.h;
        final double dx = Math.sin(a * 3.141592653589793 / 2.0) * Math.sqrt(1.0 - dy * dy);
        final double dz = Math.sqrt(1.0 - dx * dx - dy * dy);
        vert.vec.set(left.copy().multiply(dx).add(up.copy().multiply(dy)).add(forward.copy().multiply(dz)).multiply(0.375));
        vert.uv.set(u, 1.0 - v, side);
    }
    
    public void renderCurvedSide2(final Vector3 forward, final Vector3 left, final Vector3 up, final int side, final double d, final double d2, final ArrayList<Vertex5[]> faces) {
        for (double t = 0.0; t < 1.0; t += d2 / 4.0) {
            for (double dr = 0.0; dr < 1.0; dr += d * 2.0) {
                final Vertex5[] verts = { new Vertex5(), new Vertex5(), new Vertex5(), new Vertex5() };
                this.calcVec2(verts[0], forward, left, up, t, dr, side);
                this.calcVec2(verts[1], forward, left, up, t, dr + d * 2.0, side);
                this.calcVec2(verts[2], forward, left, up, t + d2 / 4.0, dr + d * 2.0, side);
                this.calcVec2(verts[3], forward, left, up, t + d2 / 4.0, dr, side);
                faces.add(verts);
            }
        }
    }
    
    public void calcVec2(final Vertex5 vert, final Vector3 forward, final Vector3 left, final Vector3 up, final double t, final double dr, final int side) {
        final double du = Math.cos(t * 3.141592653589793 * 2.0) * dr;
        final double dv = Math.sin(t * 3.141592653589793 * 2.0) * dr;
        double d = (du == 0.0 || dv == 0.0) ? 0.0 : Math.min(Math.abs(du / dv), Math.abs(dv / du));
        d = Math.sqrt(1.0 + d * d);
        final double dx = du * this.h2;
        final double dy = dv * this.h2;
        final double dz = Math.sqrt(1.0 - dx * dx - dy * dy);
        vert.vec.set(left.copy().multiply(dx).add(up.copy().multiply(dy)).add(forward.copy().multiply(dz)).multiply(0.375));
        vert.uv.set((1.0 + d * du) / 2.0, (1.0 + d * dv) / 2.0, side);
    }
    
    static {
        DOWN = new Vector3(0.0, -1.0, 0.0);
        UP = new Vector3(0.0, 1.0, 0.0);
        NORTH = new Vector3(0.0, 0.0, -1.0);
        SOUTH = new Vector3(0.0, 0.0, 1.0);
        EAST = new Vector3(-1.0, 0.0, 0.0);
        WEST = new Vector3(1.0, 0.0, 0.0);
        PartSphere.faces = null;
        PartSphere.faces_inv = null;
    }
    
    public class BlockFaceUniformLighting extends BlockRenderer.BlockFace
    {
        public void prepareVertex() {
            CCRenderState.side = 1;
        }
        
        public BlockRenderer.BlockFace computeLightCoords() {
            if (!this.lcComputed) {
                for (int i = 0; i < 4; ++i) {
                    this.lightCoords[i].set(0, 1.0f, 0.0f, 0.0f, 0.0f);
                }
                this.lcComputed = true;
            }
            return this;
        }
    }
}


