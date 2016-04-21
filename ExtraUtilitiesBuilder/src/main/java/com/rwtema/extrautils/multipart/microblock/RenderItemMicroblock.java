// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.multipart.microblock;

import codechicken.lib.render.CCRenderPipeline;
import codechicken.lib.render.TextureUtils;
import codechicken.lib.render.CCRenderState;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemStack;
import codechicken.microblock.MicroMaterialRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class RenderItemMicroblock implements IItemRenderer
{
    MicroMaterialRegistry.IMicroMaterial wool;
    
    public RenderItemMicroblock() {
        this.wool = null;
    }
    
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return true;
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack stack, final Object... data) {
        if (!stack.hasTagCompound()) {
            return;
        }
        final String mat = stack.getTagCompound().getString("mat");
        final MicroMaterialRegistry.IMicroMaterial material = MicroMaterialRegistry.getMaterial(mat);
        if (material == null) {
            return;
        }
        GL11.glPushMatrix();
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glScaled(0.5, 0.5, 0.5);
        }
        if (type == IItemRenderer.ItemRenderType.INVENTORY || type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        }
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(3008);
        final CCRenderPipeline.PipelineBuilder builder = CCRenderState.pipeline.builder();
        CCRenderState.reset();
        TextureUtils.bindAtlas(0);
        CCRenderState.useNormals = true;
        CCRenderState.pullLightmap();
        CCRenderState.startDrawing();
        final IMicroBlock part = RegisterMicroBlocks.mParts.get(stack.getItemDamage());
        if (part != null) {
            part.renderItem(stack, material);
        }
        CCRenderState.draw();
        GL11.glPopMatrix();
    }
}


