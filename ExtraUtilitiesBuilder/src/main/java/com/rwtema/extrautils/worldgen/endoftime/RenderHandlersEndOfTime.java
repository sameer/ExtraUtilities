// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen.endoftime;

import net.minecraft.util.Vec3;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.IRenderHandler;
import com.rwtema.extrautils.IClientCode;
import com.rwtema.extrautils.ExtraUtilsMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RenderHandlersEndOfTime
{
    @SideOnly(Side.CLIENT)
    public static NullRenderer nullRenderer;
    @SideOnly(Side.CLIENT)
    public static SkyRenderer skyRenderer;
    
    static {
        ExtraUtilsMod.proxy.exectuteClientCode(new IClientCode() {
            @SideOnly(Side.CLIENT)
            @Override
            public void exectuteClientCode() {
                RenderHandlersEndOfTime.skyRenderer = new SkyRenderer();
                RenderHandlersEndOfTime.nullRenderer = new NullRenderer();
            }
        });
    }
    
    @SideOnly(Side.CLIENT)
    public static class NullRenderer extends IRenderHandler
    {
        @SideOnly(Side.CLIENT)
        public void render(final float partialTicks, final WorldClient world, final Minecraft mc) {
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static class SkyRenderer extends IRenderHandler
    {
        int glSkyList;
        int glSkyList2;
        RenderGlobal renderGlobal;
        Field field_glSkyList;
        Field field_glSkyList2;
        
        public SkyRenderer() {
            this.renderGlobal = null;
            this.field_glSkyList = ReflectionHelper.findField((Class)RenderGlobal.class, new String[] { "glSkyList", "field_72771_w" });
            this.field_glSkyList2 = ReflectionHelper.findField((Class)RenderGlobal.class, new String[] { "glSkyList2", "field_72781_x" });
        }
        
        @SideOnly(Side.CLIENT)
        public void render(final float p_72714_1_, final WorldClient theWorld, final Minecraft mc) {
            Label_0064: {
                if (mc.renderGlobal != this.renderGlobal) {
                    this.renderGlobal = mc.renderGlobal;
                    Label_0349: {
                        try {
                            this.glSkyList = this.field_glSkyList.getInt(this.renderGlobal);
                            this.glSkyList2 = this.field_glSkyList2.getInt(this.renderGlobal);
                            break Label_0349;
                        }
                        catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                        break Label_0064;
                    }
                    final Vec3 vec3 = theWorld.getSkyColor((Entity)mc.renderViewEntity, p_72714_1_);
                    float f1 = (float)vec3.xCoord;
                    float f2 = (float)vec3.yCoord;
                    float f3 = (float)vec3.zCoord;
                    if (mc.gameSettings.anaglyph) {
                        final float f4 = (f1 * 30.0f + f2 * 59.0f + f3 * 11.0f) / 100.0f;
                        final float f5 = (f1 * 30.0f + f2 * 70.0f) / 100.0f;
                        final float f6 = (f1 * 30.0f + f3 * 70.0f) / 100.0f;
                        f1 = f4;
                        f2 = f5;
                        f3 = f6;
                    }
                    GL11.glColor3f(f1, f2, f3);
                    final Tessellator tessellator1 = Tessellator.instance;
                    GL11.glDepthMask(false);
                    GL11.glEnable(2912);
                    GL11.glColor3f(f1, f2, f3);
                    GL11.glCallList(this.glSkyList);
                    GL11.glDisable(2912);
                    GL11.glDisable(3008);
                    GL11.glEnable(3042);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    RenderHelper.disableStandardItemLighting();
                    final float[] afloat = theWorld.provider.calcSunriseSunsetColors(theWorld.getCelestialAngle(p_72714_1_), p_72714_1_);
                    if (afloat != null) {
                        GL11.glDisable(3553);
                        GL11.glShadeModel(7425);
                        GL11.glPushMatrix();
                        GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                        GL11.glRotatef((MathHelper.sin(theWorld.getCelestialAngleRadians(p_72714_1_)) < 0.0f) ? 180.0f : 0.0f, 0.0f, 0.0f, 1.0f);
                        GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                        float f6 = afloat[0];
                        float f7 = afloat[1];
                        float f8 = afloat[2];
                        if (mc.gameSettings.anaglyph) {
                            final float f9 = (f6 * 30.0f + f7 * 59.0f + f8 * 11.0f) / 100.0f;
                            final float f10 = (f6 * 30.0f + f7 * 70.0f) / 100.0f;
                            final float f11 = (f6 * 30.0f + f8 * 70.0f) / 100.0f;
                            f6 = f9;
                            f7 = f10;
                            f8 = f11;
                        }
                        tessellator1.startDrawing(6);
                        tessellator1.setColorRGBA_F(f6, f7, f8, afloat[3]);
                        tessellator1.addVertex(0.0, 100.0, 0.0);
                        final byte b0 = 16;
                        tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0f);
                        for (int j = 0; j <= b0; ++j) {
                            final float f11 = j * 3.1415927f * 2.0f / b0;
                            final float f12 = MathHelper.sin(f11);
                            final float f13 = MathHelper.cos(f11);
                            tessellator1.addVertex((double)(f12 * 120.0f), (double)(f13 * 120.0f), (double)(-f13 * 40.0f * afloat[3]));
                        }
                        tessellator1.draw();
                        GL11.glPopMatrix();
                        GL11.glShadeModel(7424);
                    }
                    GL11.glEnable(3553);
                    OpenGlHelper.glBlendFunc(770, 1, 1, 0);
                    GL11.glPushMatrix();
                    float f6 = 1.0f - theWorld.getRainStrength(p_72714_1_);
                    float f7 = 0.0f;
                    float f8 = 0.0f;
                    float f9 = 0.0f;
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, f6);
                    GL11.glTranslatef(f7, f8, f9);
                    GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
                    GL11.glRotatef(theWorld.getCelestialAngle(p_72714_1_) * 360.0f, 1.0f, 0.0f, 0.0f);
                    float f10 = 30.0f;
                    GL11.glDisable(3553);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDisable(3042);
                    GL11.glEnable(3008);
                    GL11.glEnable(2912);
                    GL11.glPopMatrix();
                    GL11.glDisable(3553);
                    GL11.glColor3f(0.0f, 0.0f, 0.0f);
                    final double d0 = mc.thePlayer.getPosition(p_72714_1_).yCoord - theWorld.getHorizon();
                    if (d0 < 0.0) {
                        GL11.glPushMatrix();
                        GL11.glTranslatef(0.0f, 12.0f, 0.0f);
                        GL11.glCallList(this.glSkyList2);
                        GL11.glPopMatrix();
                        f8 = 1.0f;
                        f9 = -(float)(d0 + 65.0);
                        f10 = -f8;
                        tessellator1.startDrawingQuads();
                        tessellator1.setColorRGBA_I(0, 255);
                        tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f9, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                        tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                        tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
                        tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
                        tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                        tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f9, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f9, (double)(-f8));
                        tessellator1.addVertex((double)(-f8), (double)f9, (double)(-f8));
                        tessellator1.addVertex((double)(-f8), (double)f9, (double)f8);
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)(-f8));
                        tessellator1.addVertex((double)(-f8), (double)f10, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f10, (double)f8);
                        tessellator1.addVertex((double)f8, (double)f10, (double)(-f8));
                        tessellator1.draw();
                    }
                    if (theWorld.provider.isSkyColored()) {
                        GL11.glColor3f(f1 * 0.2f + 0.04f, f2 * 0.2f + 0.04f, f3 * 0.6f + 0.1f);
                    }
                    else {
                        GL11.glColor3f(f1, f2, f3);
                    }
                    GL11.glPushMatrix();
                    GL11.glTranslatef(0.0f, -(float)(d0 - 16.0), 0.0f);
                    GL11.glCallList(this.glSkyList2);
                    GL11.glPopMatrix();
                    GL11.glEnable(3553);
                    GL11.glDepthMask(true);
                    return;
                }
            }
            GL11.glDisable(2912);
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(false);
            GL11.glDisable(3553);
            final Tessellator tessellator2 = Tessellator.instance;
            final Vec3 vec4 = theWorld.getSkyColor((Entity)mc.renderViewEntity, p_72714_1_);
            final float r = (float)vec4.xCoord;
            final float g = (float)vec4.yCoord;
            final float b2 = (float)vec4.zCoord;
            for (int i = 0; i < 6; ++i) {
                GL11.glPushMatrix();
                if (i == 1) {
                    GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 2) {
                    GL11.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 3) {
                    GL11.glRotatef(180.0f, 1.0f, 0.0f, 0.0f);
                }
                if (i == 4) {
                    GL11.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
                }
                if (i == 5) {
                    GL11.glRotatef(-90.0f, 0.0f, 0.0f, 1.0f);
                }
                tessellator2.startDrawingQuads();
                tessellator2.setColorOpaque_F(r, g, b2);
                tessellator2.addVertexWithUV(-100.0, -100.0, -100.0, 0.0, 0.0);
                tessellator2.addVertexWithUV(-100.0, -100.0, 100.0, 0.0, 16.0);
                tessellator2.addVertexWithUV(100.0, -100.0, 100.0, 16.0, 16.0);
                tessellator2.addVertexWithUV(100.0, -100.0, -100.0, 16.0, 0.0);
                tessellator2.draw();
                GL11.glPopMatrix();
            }
            GL11.glDepthMask(true);
            GL11.glEnable(3553);
            GL11.glEnable(3008);
        }
    }
}
