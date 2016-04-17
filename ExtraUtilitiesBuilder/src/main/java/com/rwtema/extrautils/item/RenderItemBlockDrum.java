// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.block.Block;
import com.rwtema.extrautils.block.render.RenderBlockDrum;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class RenderItemBlockDrum implements IItemRenderer
{
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        switch (type) {
            case ENTITY: {
                return true;
            }
            case EQUIPPED: {
                return true;
            }
            case EQUIPPED_FIRST_PERSON: {
                return true;
            }
            case INVENTORY: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return true;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        if (!(item.getItem() instanceof ItemBlock)) {
            return;
        }
        final Block block = ((ItemBlock)item.getItem()).field_150939_a;
        if (block == null) {
            return;
        }
        final RenderBlocks renderer = (RenderBlocks)data[0];
        Entity holder = null;
        if (data.length > 1 && data[1] instanceof Entity) {
            holder = (Entity)data[1];
        }
        if (holder == null) {
            holder = (Entity)Minecraft.getMinecraft().thePlayer;
        }
        final Tessellator var4 = Tessellator.instance;
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
        switch (type) {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON: {
                GL11.glTranslatef(-1.0f, 0.5f, 0.0f);
                break;
            }
            default: {
                GL11.glTranslatef(-0.5f, -0.0f, -0.5f);
                break;
            }
        }
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glEnable(3008);
        RenderBlockDrum.drawInvBlock(block, item);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(0.5f, 0.0f, 0.5f);
    }
}
