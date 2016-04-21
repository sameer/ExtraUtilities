// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.IIcon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class RenderItemUnstable implements IItemRenderer
{
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return true;
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        if (!(item.getItem() instanceof IItemMultiTransparency)) {
            return;
        }
        final IItemMultiTransparency itemTrans = (IItemMultiTransparency)item.getItem();
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5f, -0.25f, 0.0f);
            GL11.glDisable(2884);
            GL11.glTranslatef(1.0f, 0.0f, 0.0f);
            GL11.glScalef(-1.0f, 1.0f, 1.0f);
        }
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(16.0f, 16.0f, 1.0f);
        }
        final Tessellator tessellator = Tessellator.instance;
        float t = 0.0f;
        if (item.hasTagCompound()) {
            final NBTTagCompound tg = item.getTagCompound();
            if (tg.hasKey("time") && tg.hasKey("dimension")) {
                t = (Minecraft.getMinecraft().thePlayer.worldObj.getTotalWorldTime() - tg.getLong("time")) / 200.0f;
                if (t > 1.0f) {
                    t = 1.0f;
                }
            }
        }
        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        if (t > 0.85 && Minecraft.getSystemTime() % 200L < 100L) {
            r = 1.0f;
            g = 1.0f - t * 0.7f / 3.0f;
            b = 0.0f;
        }
        else {
            r = 1.0f;
            g = 1.0f - t * 0.7f;
            b = 1.0f - t;
        }
        GL11.glColor3f(r, g, b);
        for (int i = 0; i < itemTrans.numIcons(item); ++i) {
            final IIcon icon = itemTrans.getIconForTransparentRender(item, i);
            final float trans = itemTrans.getIconTransparency(item, i);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glEnable(3008);
            if (trans < 1.0f) {
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glDisable(3008);
                GL11.glShadeModel(7425);
                GL11.glColor4f(r, g, b, trans);
            }
            else {
                GL11.glColor4f(r, g, b, 1.0f);
            }
            if (type != IItemRenderer.ItemRenderType.INVENTORY) {
                GL11.glEnable(32826);
                ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 0.0625f);
                GL11.glDisable(32826);
            }
            else {
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
                tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());
                tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
                tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
                tessellator.draw();
            }
            if (trans < 1.0f) {
                GL11.glShadeModel(7424);
                GL11.glEnable(3008);
                GL11.glDisable(3042);
            }
        }
        if (type == IItemRenderer.ItemRenderType.INVENTORY) {
            GL11.glScalef(0.0625f, 0.0625f, 1.0f);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glScalef(-1.0f, 1.0f, 1.0f);
            GL11.glTranslatef(1.0f, 0.0f, 0.0f);
            GL11.glTranslatef(0.5f, 0.25f, 0.0f);
            GL11.glEnable(2884);
        }
    }
    
    private int f(final float t, final long a, final int k) {
        final int b = (int)((2.0 + Math.cos(Minecraft.getSystemTime() % a / a * 2.0f * 3.141592653589793) / 3.0) * (t * t) * k);
        if (b < 0) {
            return 0;
        }
        if (b > 8) {
            return 8;
        }
        return 0;
    }
}


