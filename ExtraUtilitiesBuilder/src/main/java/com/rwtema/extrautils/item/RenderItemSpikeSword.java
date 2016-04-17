// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import com.rwtema.extrautils.tileentity.RenderTileEntitySpike;
import com.rwtema.extrautils.helper.GLHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import com.rwtema.extrautils.ExtraUtilsProxy;
import com.rwtema.extrautils.ExtraUtilsClient;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemSpikeSword implements IItemRenderer
{
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return true;
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        GL11.glPushMatrix();
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            GL11.glScaled(0.5, 0.5, 0.5);
        }
        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == IItemRenderer.ItemRenderType.EQUIPPED) {
            GL11.glTranslated(0.5, 0.5, 0.5);
        }
        ExtraUtilsClient.renderBlockSpike.renderInventoryBlock(((ItemBlockSpike)item.getItem()).spike, 0, ExtraUtilsProxy.spikeBlockID, (RenderBlocks)data[0]);
        if (item.hasEffect(0)) {
            GL11.glTranslated(0.0, 0.0, -1.0);
            GL11.glScaled(1.01, 1.01, 1.01);
            GL11.glTranslated(-0.5, -0.5, -0.5);
            GLHelper.pushGLState();
            GLHelper.enableGLState(3008);
            RenderTileEntitySpike.hashCode = System.identityHashCode(item);
            RenderTileEntitySpike.drawEnchantedSpike(0);
            GLHelper.popGLState();
        }
        GL11.glPopMatrix();
    }
}
