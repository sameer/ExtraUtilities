// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart;

import net.minecraft.tileentity.TileEntity;
import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.CommonMicroblock;
import codechicken.lib.render.uv.MultiIconTransformation;
import com.rwtema.extrautils.block.render.FakeRenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import com.rwtema.extrautils.block.render.RenderBlockConnectedTextures;
import codechicken.lib.render.Vertex5;
import codechicken.microblock.HollowMicroblockClient;
import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import java.util.Iterator;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.TileMultipart;
import codechicken.lib.vec.BlockCoord;
import com.rwtema.extrautils.helper.XUHelperClient;
import codechicken.multipart.TMultiPart;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import com.rwtema.extrautils.block.BlockDecoration;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.block.IconConnectedTexture;
import net.minecraft.world.World;
import codechicken.microblock.BlockMicroMaterial;

public class ConnectedTextureMicroMaterial extends BlockMicroMaterial
{
    public static final double[] u;
    public static final double[] v;
    public boolean isGlass;
    public boolean[] isConnected;
    public boolean hasConnected;
    private int pass;
    private World world;
    int[] cols;
    @SideOnly(Side.CLIENT)
    IconConnectedTexture resetIcons;
    
    public ConnectedTextureMicroMaterial(final Block block, final int meta) {
        super(block, meta);
        this.isGlass = true;
        this.cols = new int[] { 269488383, -1, 269549567, 285151487, -15724289, -15663105, -61185 };
        if (block instanceof BlockDecoration) {
            this.isGlass = !((BlockDecoration)block).opaque[this.meta()];
            this.isConnected = ((BlockDecoration)block).ctexture[this.meta()];
            for (final boolean b : this.isConnected) {
                if (b) {
                    this.hasConnected = true;
                    break;
                }
            }
        }
        else {
            this.isGlass = !block.isOpaqueCube();
            this.isConnected = new boolean[6];
            this.hasConnected = false;
        }
    }
    
    public int getLightValue() {
        if (this.block() instanceof BlockDecoration) {
            return ((BlockDecoration)this.block()).light[this.meta()];
        }
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    public TMultiPart getPart(final Vector3 pos, final Cuboid6 bounds) {
        final World world = XUHelperClient.clientWorld();
        final TileMultipart t = TileMultipart.getOrConvertTile(world, new BlockCoord(pos));
        if (t == null) {
            return null;
        }
        for (final TMultiPart part : t.jPartList()) {
            if (part instanceof JNormalOcclusion) {
                for (final Cuboid6 bound : ((JNormalOcclusion)part).getOcclusionBoxes()) {
                    if (bound.intersects(bounds)) {
                        return part;
                    }
                }
            }
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void renderMicroFace(final Vector3 pos, final int pass, final Cuboid6 b) {
        if (!CCRenderState.model.getClass().equals(BlockRenderer.BlockFace.class)) {
            super.renderMicroFace(pos, pass, b);
            return;
        }
        final Cuboid6 bounds = b.copy();
        final Cuboid6 renderBounds = bounds.copy();
        final TMultiPart part = this.getPart(pos, bounds);
        if (pass >= 0) {
            final boolean isHollow = part instanceof HollowMicroblockClient;
            final int s = ((BlockRenderer.BlockFace)CCRenderState.model).side;
            if (isHollow) {
                for (final Cuboid6 b2 : ((JNormalOcclusion)part).getOcclusionBoxes()) {
                    bounds.enclose(b2);
                }
            }
            if (this.isGlass) {
                this.glassChange(bounds);
            }
        }
        if (!this.hasConnected || !this.renderConnected(pos, pass, bounds, renderBounds)) {
            super.renderMicroFace(pos, pass, bounds);
        }
        if (this.resetIcons != null) {
            this.resetIcons.resetType();
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void glassChange(final Cuboid6 c) {
        final BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
        final int side = face.side;
        final double x1 = c.min.x;
        final double x2 = c.max.x;
        final double y1 = c.min.y;
        final double y2 = c.max.y;
        final double z1 = c.min.z;
        final double z2 = c.max.z;
        double u1 = 0.0;
        double u2 = 0.0;
        double v1 = 0.0;
        double v2 = 0.0;
        switch (side) {
            case 0: {
                u1 = x1;
                u2 = x2;
                v1 = z1;
                v2 = z2;
                break;
            }
            case 1: {
                u1 = x1;
                u2 = x2;
                v1 = z1;
                v2 = z2;
                break;
            }
            case 2: {
                u1 = 1.0 - x2;
                u2 = 1.0 - x1;
                v1 = 1.0 - y2;
                v2 = 1.0 - y1;
                break;
            }
            case 3: {
                u1 = x1;
                u2 = x2;
                v1 = 1.0 - y2;
                v2 = 1.0 - y1;
                break;
            }
            case 4: {
                u1 = z1;
                v1 = 1.0 - y2;
                u2 = z2;
                v2 = 1.0 - y1;
                break;
            }
            case 5: {
                u1 = 1.0 - z2;
                u2 = 1.0 - z1;
                v1 = 1.0 - y2;
                v2 = 1.0 - y1;
                break;
            }
            default: {
                return;
            }
        }
        if (v1 == v2 || u1 == u2) {
            return;
        }
        for (final Vertex5 v3 : face.verts) {
            v3.uv.u = (v3.uv.u - u1) / (u2 - u1);
            v3.uv.v = (v3.uv.v - v1) / (v2 - v1);
        }
        face.lcComputed = false;
        face.computeLightCoords();
    }
    
    public boolean isInt(final double t) {
        return t == (int)t;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean renderConnected(final Vector3 pos, final int pass, final Cuboid6 bounds, final Cuboid6 renderBounds) {
        this.pass = pass;
        if (pass < 0) {
            return false;
        }
        if (!this.isInt(pos.x) || !this.isInt(pos.y) || !this.isInt(pos.z)) {
            return false;
        }
        if (!CCRenderState.model.getClass().equals(BlockRenderer.BlockFace.class)) {
            return false;
        }
        if (!this.isFlush(bounds)) {
            return false;
        }
        int s = this.getSideFromBounds(bounds);
        final BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
        final int side = face.side;
        if (!this.isConnected[side]) {
            return false;
        }
        if (s == -1) {
            s = side;
        }
        final IIcon icon = this.icont().icons[side];
        if (!(icon instanceof IconConnectedTexture)) {
            return false;
        }
        this.world = XUHelperClient.clientPlayer().getEntityWorld();
        if (s == side) {
            final int c = this.getColour(pass);
            if (this.isGlass) {
                final double h = 0.001;
                switch (s) {
                    case 0: {
                        this.renderHalfSide(this.block(), 0.5, h, 0.5, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                    case 1: {
                        this.renderHalfSide(this.block(), 0.5, 1.0 - h, 0.5, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                    case 2: {
                        this.renderHalfSide(this.block(), 0.5, 0.5, h, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                    case 3: {
                        this.renderHalfSide(this.block(), 0.5, 0.5, 1.0 - h, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                    case 4: {
                        this.renderHalfSide(this.block(), h, 0.5, 0.5, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                    case 5: {
                        this.renderHalfSide(this.block(), 1.0 - h, 0.5, 0.5, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                        break;
                    }
                }
            }
            else {
                final FakeRenderBlocks fr = RenderBlockConnectedTextures.fakeRender;
                fr.setWorld((IBlockAccess)this.world);
                fr.curBlock = this.block();
                fr.curMeta = this.meta();
                switch (s) {
                    case 0: {
                        this.renderSide(this.block(), 0.5, 0.0, 0.5, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                    case 1: {
                        this.renderSide(this.block(), 0.5, 1.0, 0.5, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                    case 2: {
                        this.renderSide(this.block(), 0.5, 0.5, 0.0, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                    case 3: {
                        this.renderSide(this.block(), 0.5, 0.5, 1.0, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                    case 4: {
                        this.renderSide(this.block(), 0.0, 0.5, 0.5, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                    case 5: {
                        this.renderSide(this.block(), 1.0, 0.5, 0.5, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, side, pos, c, this.icont(), renderBounds, bounds);
                        break;
                    }
                }
            }
            return true;
        }
        if (side == Facing.oppositeSide[s]) {
            final double h2 = this.sideSize(bounds);
            switch (Facing.oppositeSide[s]) {
                case 0: {
                    this.renderHalfSide(this.block(), 0.5, h2, 0.5, 1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
                case 1: {
                    this.renderHalfSide(this.block(), 0.5, h2, 0.5, -1, 0, 0, 0, 0, -1, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
                case 2: {
                    this.renderHalfSide(this.block(), 0.5, 0.5, h2, 1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
                case 3: {
                    this.renderHalfSide(this.block(), 0.5, 0.5, h2, -1, 0, 0, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
                case 4: {
                    this.renderHalfSide(this.block(), h2, 0.5, 0.5, 0, 0, -1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
                case 5: {
                    this.renderHalfSide(this.block(), h2, 0.5, 0.5, 0, 0, 1, 0, 1, 0, (IconConnectedTexture)icon, bounds, side, pos, renderBounds);
                    break;
                }
            }
            return true;
        }
        if (this.isGlass) {
            final double d = renderBounds.getSide(side);
            return (d == 0.0 || d == 1.0) && this.hasMatchingPart(bounds, (int)pos.x + Facing.offsetsXForSide[side], (int)pos.y + Facing.offsetsYForSide[side], (int)pos.z + Facing.offsetsZForSide[side]);
        }
        return false;
    }
    
    public double dist(final Vector3 a, final Vector3 b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y) + (a.z - b.z) * (a.z - b.z));
    }
    
    public boolean isFlush(final Cuboid6 bounds) {
        int i = 0;
        if (bounds.max.y != 1.0) {
            ++i;
        }
        if (bounds.min.y != 0.0) {
            ++i;
        }
        if (bounds.max.z != 1.0) {
            ++i;
        }
        if (bounds.min.z != 0.0) {
            ++i;
        }
        if (bounds.max.x != 1.0) {
            ++i;
        }
        if (bounds.min.x != 0.0) {
            ++i;
        }
        return i <= 1;
    }
    
    public double sideSize(final Cuboid6 bounds) {
        if (bounds.max.y != 1.0) {
            return bounds.max.y;
        }
        if (bounds.min.y != 0.0) {
            return bounds.min.y;
        }
        if (bounds.max.z != 1.0) {
            return bounds.max.z;
        }
        if (bounds.min.z != 0.0) {
            return bounds.min.z;
        }
        if (bounds.max.x != 1.0) {
            return bounds.max.x;
        }
        if (bounds.min.x != 0.0) {
            return bounds.min.x;
        }
        return 0.0;
    }
    
    public int getSideFromBounds(final Cuboid6 bounds) {
        if (bounds.max.y != 1.0) {
            return 0;
        }
        if (bounds.min.y != 0.0) {
            return 1;
        }
        if (bounds.max.z != 1.0) {
            return 2;
        }
        if (bounds.min.z != 0.0) {
            return 3;
        }
        if (bounds.max.x != 1.0) {
            return 4;
        }
        if (bounds.min.x != 0.0) {
            return 5;
        }
        return -1;
    }
    
    public boolean isTransparent() {
        return this.isGlass;
    }
    
    @SideOnly(Side.CLIENT)
    public void renderSide(final Block block, final double ox, final double oy, final double oz, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final IconConnectedTexture icon, final int side, final Vector3 pos, final int colour, final MultiIconTransformation icont, final Cuboid6 renderBounds, Cuboid6 bounds) {
        final int[] tex = new int[4];
        boolean isDifferent = false;
        for (int j = 0; j < 4; ++j) {
            RenderBlockConnectedTextures.fakeRender.isOpaque = !this.isGlass;
            tex[j] = RenderBlockConnectedTextures.fakeRender.getType(block, side, (int)pos.x, (int)pos.y, (int)pos.z, ax * (int)ConnectedTextureMicroMaterial.u[j], ay * (int)ConnectedTextureMicroMaterial.u[j], az * (int)ConnectedTextureMicroMaterial.u[j], bx * (int)ConnectedTextureMicroMaterial.v[j], by * (int)ConnectedTextureMicroMaterial.v[j], bz * (int)ConnectedTextureMicroMaterial.v[j], (int)(ox * 2.0 - 1.0), (int)(oy * 2.0 - 1.0), (int)(oz * 2.0 - 1.0));
            if (tex[j] != tex[0]) {
                isDifferent = true;
            }
        }
        final BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
        face.lcComputed = false;
        if (isDifferent) {
            for (int i = 0; i < 4; ++i) {
                final double cx = ox + ax * ConnectedTextureMicroMaterial.u[i] / 4.0 + bx * ConnectedTextureMicroMaterial.v[i] / 4.0;
                final double cy = oy + ay * ConnectedTextureMicroMaterial.u[i] / 4.0 + by * ConnectedTextureMicroMaterial.v[i] / 4.0;
                final double cz = oz + az * ConnectedTextureMicroMaterial.u[i] / 4.0 + bz * ConnectedTextureMicroMaterial.v[i] / 4.0;
                Cuboid6 b = new Cuboid6(cx, cy, cz, cx, cy, cz);
                b.setSide(side, bounds.getSide(side));
                b.setSide(Facing.oppositeSide[side], bounds.getSide(Facing.oppositeSide[side]));
                for (int k = 0; k < 4; ++k) {
                    this.expandToInclude(b, new Vector3(cx + ConnectedTextureMicroMaterial.u[k] * ax * 0.25 + ConnectedTextureMicroMaterial.v[k] * bx * 0.25, cy + ConnectedTextureMicroMaterial.u[k] * ay * 0.25 + ConnectedTextureMicroMaterial.v[k] * by * 0.25, cz + ConnectedTextureMicroMaterial.u[k] * az * 0.25 + ConnectedTextureMicroMaterial.v[k] * bz * 0.25));
                }
                b = this.shrinkToEnclose(b.copy(), renderBounds);
                if (!this.isEmpty(b)) {
                    if (this.isGlass) {
                        for (int l = 0; l < 4; ++l) {
                            face.lightCoords[l].computeO(face.verts[l].vec, side);
                        }
                        face.lcComputed = true;
                    }
                    else {
                        face.lcComputed = false;
                    }
                    for (final IIcon ic : this.icont().icons) {
                        if (ic instanceof IconConnectedTexture) {
                            ((IconConnectedTexture)ic).setType(tex[i]);
                        }
                    }
                    face.loadCuboidFace(b, side);
                    super.renderMicroFace(pos, this.pass, b);
                    for (final IIcon ic : this.icont().icons) {
                        if (ic instanceof IconConnectedTexture) {
                            ((IconConnectedTexture)ic).resetType();
                        }
                    }
                }
            }
        }
        else {
            bounds = this.shrinkToEnclose(bounds.copy(), renderBounds);
            if (this.isEmpty(bounds)) {
                return;
            }
            face.loadCuboidFace(bounds, side);
            for (final IIcon ic2 : this.icont().icons) {
                if (ic2 instanceof IconConnectedTexture) {
                    ((IconConnectedTexture)ic2).setType(tex[0]);
                }
            }
            super.renderMicroFace(pos, this.pass, bounds);
            for (final IIcon ic2 : this.icont().icons) {
                if (ic2 instanceof IconConnectedTexture) {
                    ((IconConnectedTexture)ic2).resetType();
                }
            }
        }
    }
    
    private double interp(final double v) {
        return v / 16.0;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasMatchingPart(final Cuboid6 part, final int x, final int y, final int z) {
        final TileEntity tile_base = this.world.getTileEntity(x, y, z);
        if (tile_base != null && tile_base instanceof TileMultipart) {
            final scala.collection.Iterator<TMultiPart> parts = (scala.collection.Iterator<TMultiPart>)((TileMultipart)tile_base).partList().toIterator();
            while (parts.hasNext()) {
                final TMultiPart p = (TMultiPart)parts.next();
                if (p instanceof CommonMicroblock) {
                    final CommonMicroblock mat = (CommonMicroblock)p;
                    if (!this.equalCubes(mat.getBounds(), part)) {
                        continue;
                    }
                    final int material = mat.getMaterial();
                    if (MicroMaterialRegistry.getMaterial(material) == this) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    public boolean equalCubes(final Cuboid6 a, final Cuboid6 b) {
        return this.getSideFromBounds(a) == this.getSideFromBounds(b) && this.sideSize(a) == this.sideSize(b);
    }
    
    @SideOnly(Side.CLIENT)
    public int getHalfType(final Block block, final int side, final int x, final int y, final int z, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final Cuboid6 part) {
        final boolean a = this.hasMatchingPart(part, x + ax, y + ay, z + az);
        final boolean b = this.hasMatchingPart(part, x + bx, y + by, z + bz);
        if (a) {
            if (!b) {
                return 2;
            }
            if (this.hasMatchingPart(part, x + ax + bx, y + ay + by, z + az + bz)) {
                return 3;
            }
            return 4;
        }
        else {
            if (b) {
                return 1;
            }
            return 0;
        }
    }
    
    public Cuboid6 getBounds(final Cuboid6 b, final int side) {
        return b;
    }
    
    public Cuboid6 expandToInclude(final Cuboid6 a, final Vector3 v) {
        if (a.min.x > v.x) {
            a.min.x = v.x;
        }
        if (a.min.y > v.y) {
            a.min.y = v.y;
        }
        if (a.min.z > v.z) {
            a.min.z = v.z;
        }
        if (a.max.y < v.y) {
            a.max.y = v.y;
        }
        if (a.max.x < v.x) {
            a.max.x = v.x;
        }
        if (a.max.z < v.z) {
            a.max.z = v.z;
        }
        return a;
    }
    
    public boolean isEmpty(final Cuboid6 a) {
        return a.min.x >= a.max.x || a.min.y >= a.max.y || a.min.z >= a.max.z;
    }
    
    public Cuboid6 shrinkToEnclose(final Cuboid6 a, final Cuboid6 c) {
        if (a.min.x < c.min.x) {
            a.min.x = c.min.x;
        }
        if (a.min.y < c.min.y) {
            a.min.y = c.min.y;
        }
        if (a.min.z < c.min.z) {
            a.min.z = c.min.z;
        }
        if (a.max.x > c.max.x) {
            a.max.x = c.max.x;
        }
        if (a.max.y > c.max.y) {
            a.max.y = c.max.y;
        }
        if (a.max.z > c.max.z) {
            a.max.z = c.max.z;
        }
        return a;
    }
    
    @SideOnly(Side.CLIENT)
    public void renderHalfSide(final Block block, final double ox, final double oy, final double oz, final int ax, final int ay, final int az, final int bx, final int by, final int bz, final IconConnectedTexture icon, Cuboid6 bounds, final int side, final Vector3 pos, final Cuboid6 renderBounds) {
        final int[] tex = new int[4];
        boolean isDifferent = false;
        int s = Facing.oppositeSide[this.getSideFromBounds(bounds)];
        for (int j = 0; j < 4; ++j) {
            RenderBlockConnectedTextures.fakeRender.isOpaque = !this.isGlass;
            tex[j] = this.getHalfType(block, side, (int)pos.x, (int)pos.y, (int)pos.z, ax * (int)ConnectedTextureMicroMaterial.u[j], ay * (int)ConnectedTextureMicroMaterial.u[j], az * (int)ConnectedTextureMicroMaterial.u[j], bx * (int)ConnectedTextureMicroMaterial.v[j], by * (int)ConnectedTextureMicroMaterial.v[j], bz * (int)ConnectedTextureMicroMaterial.v[j], bounds);
            if (tex[j] != tex[0]) {
                isDifferent = true;
            }
        }
        final BlockRenderer.BlockFace face = (BlockRenderer.BlockFace)CCRenderState.model;
        face.lcComputed = false;
        if (isDifferent) {
            s = s;
            for (int i = 0; i < 4; ++i) {
                final double cx = ox + ax * ConnectedTextureMicroMaterial.u[i] / 4.0 + bx * ConnectedTextureMicroMaterial.v[i] / 4.0;
                final double cy = oy + ay * ConnectedTextureMicroMaterial.u[i] / 4.0 + by * ConnectedTextureMicroMaterial.v[i] / 4.0;
                final double cz = oz + az * ConnectedTextureMicroMaterial.u[i] / 4.0 + bz * ConnectedTextureMicroMaterial.v[i] / 4.0;
                Cuboid6 b = new Cuboid6(cx, cy, cz, cx, cy, cz);
                b.setSide(face.side, bounds.getSide(face.side));
                b.setSide(Facing.oppositeSide[face.side], bounds.getSide(Facing.oppositeSide[face.side]));
                for (int k = 0; k < 4; ++k) {
                    this.expandToInclude(b, new Vector3(cx + ConnectedTextureMicroMaterial.u[k] * ax * 0.25 + ConnectedTextureMicroMaterial.v[k] * bx * 0.25, cy + ConnectedTextureMicroMaterial.u[k] * ay * 0.25 + ConnectedTextureMicroMaterial.v[k] * by * 0.25, cz + ConnectedTextureMicroMaterial.u[k] * az * 0.25 + ConnectedTextureMicroMaterial.v[k] * bz * 0.25));
                }
                b = this.shrinkToEnclose(b.copy(), renderBounds);
                if (!this.isEmpty(b)) {
                    if (this.isGlass) {
                        for (int l = 0; l < 4; ++l) {
                            face.lightCoords[l].computeO(face.verts[l].vec, side);
                        }
                        face.lcComputed = true;
                    }
                    else {
                        face.lcComputed = false;
                    }
                    for (final IIcon ic : this.icont().icons) {
                        if (ic instanceof IconConnectedTexture) {
                            ((IconConnectedTexture)ic).setType(tex[i]);
                        }
                    }
                    face.loadCuboidFace(b, face.side);
                    super.renderMicroFace(pos, this.pass, b);
                    for (final IIcon ic : this.icont().icons) {
                        if (ic instanceof IconConnectedTexture) {
                            ((IconConnectedTexture)ic).resetType();
                        }
                    }
                }
            }
        }
        else {
            bounds = this.shrinkToEnclose(bounds.copy(), renderBounds);
            if (this.isEmpty(bounds)) {
                return;
            }
            face.loadCuboidFace(bounds, side);
            for (final IIcon ic2 : this.icont().icons) {
                if (ic2 instanceof IconConnectedTexture) {
                    ((IconConnectedTexture)ic2).setType(tex[0]);
                }
            }
            super.renderMicroFace(pos, this.pass, bounds);
            for (final IIcon ic2 : this.icont().icons) {
                if (ic2 instanceof IconConnectedTexture) {
                    ((IconConnectedTexture)ic2).resetType();
                }
            }
        }
    }
    
    static {
        u = new double[] { -1.0, 1.0, 1.0, -1.0 };
        v = new double[] { 1.0, 1.0, -1.0, -1.0 };
    }
}


