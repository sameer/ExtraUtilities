// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.OpenGlHelper;
import com.rwtema.extrautils.helper.GLHelper;
import net.minecraft.util.Facing;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

@SideOnly(Side.CLIENT)
public class RenderTileEntitySpike extends TileEntitySpecialRenderer
{
    private static final ResourceLocation RES_ITEM_GLINT;
    public static int hashCode;
    static double[][] pointCoords;
    static double[][] base1Coords;
    static double[][] base2Coords;
    static double[][] base3Coords;
    static double[][] base4Coords;
    
    public static void render(final double[] v1, final double[] v2, final double[] v3, final double[] v4, final boolean isPoint) {
        final Tessellator tes = Tessellator.instance;
        final float f8 = 0.125f;
        GL11.glPushMatrix();
        GL11.glScalef(f8, f8, f8);
        float f9 = (Minecraft.getSystemTime() + RenderTileEntitySpike.hashCode) % 3000L / 3000.0f * 8.0f;
        GL11.glTranslatef(f9, 0.0f, 0.0f);
        GL11.glRotatef(-50.0f, 0.0f, 0.0f, 1.0f);
        renderFace(v1, v2, v3, v4, tes, isPoint);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glScalef(f8, f8, f8);
        f9 = (Minecraft.getSystemTime() + RenderTileEntitySpike.hashCode) % 4873L / 4873.0f * 8.0f;
        GL11.glTranslatef(-f9, 0.0f, 0.0f);
        GL11.glRotatef(10.0f, 0.0f, 0.0f, 1.0f);
        renderFace(v1, v2, v3, v4, tes, isPoint);
        GL11.glPopMatrix();
    }
    
    private static void renderFace(final double[] v1, final double[] v2, final double[] v3, final double[] v4, final Tessellator tes, final boolean isPoint) {
        tes.startDrawingQuads();
        tes.setBrightness(255);
        if (isPoint) {
            tes.addVertexWithUV(v1[0], v1[1], v1[2], 0.0, 0.5);
            tes.addVertexWithUV(v2[0], v2[1], v2[2], 0.0, 0.5);
        }
        else {
            tes.addVertexWithUV(v1[0], v1[1], v1[2], 0.0, 0.0);
            tes.addVertexWithUV(v2[0], v2[1], v2[2], 0.0, 1.0);
        }
        tes.addVertexWithUV(v3[0], v3[1], v3[2], 1.0, 1.0);
        tes.addVertexWithUV(v4[0], v4[1], v4[2], 1.0, 0.0);
        tes.draw();
    }
    
    public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float f) {
        RenderTileEntitySpike.hashCode = 0;
        final int side = Facing.oppositeSide[tileentity.getBlockMetadata() % 6];
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glScaled(1.01, 1.01, 1.01);
        GL11.glTranslated(-0.5, -0.5, -0.5);
        drawEnchantedSpike(side);
        GL11.glPopMatrix();
        RenderTileEntitySpike.hashCode = 0;
    }
    
    public static void drawEnchantedSpike(final int side) {
        final TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        GLHelper.pushGLState();
        GL11.glDepthMask(true);
        GLHelper.disableGLState(2884);
        GL11.glDepthFunc(515);
        GLHelper.disableGLState(2896);
        texturemanager.bindTexture(RenderTileEntitySpike.RES_ITEM_GLINT);
        GLHelper.enableGLState(3042);
        GL11.glBlendFunc(774, 774);
        OpenGlHelper.glBlendFunc(768, 1, 1, 0);
        final float f7 = 0.76f;
        GL11.glColor4f(0.5f * f7, 0.25f * f7, 0.8f * f7, 1.0f);
        GL11.glMatrixMode(5890);
        GL11.glPushMatrix();
        render(RenderTileEntitySpike.base1Coords[side], RenderTileEntitySpike.base2Coords[side], RenderTileEntitySpike.base3Coords[side], RenderTileEntitySpike.base4Coords[side], false);
        RenderTileEntitySpike.hashCode += 123;
        render(RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.base2Coords[side], RenderTileEntitySpike.base1Coords[side], true);
        RenderTileEntitySpike.hashCode += 123;
        render(RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.base1Coords[side], RenderTileEntitySpike.base4Coords[side], true);
        RenderTileEntitySpike.hashCode += 123;
        render(RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.base2Coords[side], RenderTileEntitySpike.base3Coords[side], true);
        RenderTileEntitySpike.hashCode += 123;
        render(RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.pointCoords[side], RenderTileEntitySpike.base3Coords[side], RenderTileEntitySpike.base4Coords[side], true);
        RenderTileEntitySpike.hashCode = 0;
        GL11.glPopMatrix();
        GLHelper.popGLState();
        GL11.glMatrixMode(5888);
        GL11.glDepthFunc(515);
        GL11.glDepthMask(true);
        GL11.glDepthFunc(515);
    }
    
    static {
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
        RenderTileEntitySpike.hashCode = 0;
        RenderTileEntitySpike.pointCoords = new double[][] { { 0.5, 1.0, 0.5 }, { 0.5, 0.0, 0.5 }, { 0.5, 0.5, 1.0 }, { 0.5, 0.5, 0.0 }, { 1.0, 0.5, 0.5 }, { 0.0, 0.5, 0.5 } };
        RenderTileEntitySpike.base1Coords = new double[][] { { 0.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 }, { 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 } };
        RenderTileEntitySpike.base2Coords = new double[][] { { 0.0, 0.0, 1.0 }, { 0.0, 1.0, 1.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 1.0, 1.0 }, { 0.0, 0.0, 1.0 }, { 1.0, 0.0, 1.0 } };
        RenderTileEntitySpike.base3Coords = new double[][] { { 1.0, 0.0, 1.0 }, { 1.0, 1.0, 1.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 1.0, 1.0 }, { 0.0, 1.0, 1.0 }, { 1.0, 1.0, 1.0 } };
        RenderTileEntitySpike.base4Coords = new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 1.0, 0.0, 0.0 }, { 1.0, 0.0, 1.0 }, { 0.0, 1.0, 0.0 }, { 1.0, 1.0, 0.0 } };
    }
}

