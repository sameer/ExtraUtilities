// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.passive.EntitySheep;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraftforge.client.event.RenderPlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class RenderItemGlove implements IItemRenderer
{
    public static final RenderItemGlove INSTANCE;
    float renderTickTime;
    private static final ResourceLocation glove1;
    private static final ResourceLocation glove2;
    
    @SubscribeEvent
    public void getFrame(final TickEvent.RenderTickEvent event) {
        this.renderTickTime = event.renderTickTime;
    }
    
    @SubscribeEvent
    public void tickCol(final TickEvent.ClientTickEvent event) {
        ItemGlove.genericDmg = (0x50 | (ItemGlove.genericDmg + 1 & 0xF));
    }
    
    @SubscribeEvent
    public void renderEquippedGlove(final RenderPlayerEvent.Specials.Post event) {
        if (event.entityPlayer == null) {
            return;
        }
        final ItemStack heldItem = event.entityPlayer.getHeldItem();
        if (heldItem == null || heldItem.getItem() != ExtraUtils.glove) {
            return;
        }
        final int dmg = heldItem.getItemDamage();
        final RenderPlayer renderplayer = event.renderer;
        float[] col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(1, dmg)];
        GL11.glColor3f(col[0], col[1], col[2]);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderItemGlove.glove1);
        renderplayer.modelBipedMain.bipedRightArm.render(0.0625f);
        col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(0, dmg)];
        GL11.glColor3f(col[0], col[1], col[2]);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderItemGlove.glove2);
        renderplayer.modelBipedMain.bipedRightArm.render(0.0625f);
    }
    
    public boolean handleRenderType(final ItemStack item, final IItemRenderer.ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON || type == IItemRenderer.ItemRenderType.EQUIPPED;
    }
    
    public boolean shouldUseRenderHelper(final IItemRenderer.ItemRenderType type, final ItemStack item, final IItemRenderer.ItemRendererHelper helper) {
        return type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON && helper == IItemRenderer.ItemRendererHelper.EQUIPPED_BLOCK;
    }
    
    public void renderItem(final IItemRenderer.ItemRenderType type, final ItemStack item, final Object... data) {
        if (type == IItemRenderer.ItemRenderType.EQUIPPED) {
            return;
        }
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        GL11.glPushMatrix();
        GL11.glEnable(32826);
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        final float p_78440_1_ = this.renderTickTime;
        final float f1 = 1.0f;
        final float f2 = 0.8f;
        final float f3 = 2.5f;
        GL11.glScalef(f3, f3, f3);
        float f4 = player.getSwingProgress(p_78440_1_);
        float f5 = MathHelper.sin(f4 * f4 * 3.1415927f);
        float f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f);
        GL11.glRotatef(f6 * 80.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(f6 * 20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(f5 * 20.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(-0.7f * f2, 0.65f * f2 + (1.0f - f1) * 0.6f, 0.9f * f2);
        f4 = player.getSwingProgress(p_78440_1_);
        f5 = MathHelper.sin(f4 * 3.1415927f);
        f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f);
        GL11.glTranslatef(f6 * 0.4f, -MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f * 2.0f) * 0.2f, f5 * 0.2f);
        f4 = player.getSwingProgress(p_78440_1_);
        f5 = MathHelper.sin(f4 * 3.1415927f);
        f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f);
        GL11.glTranslatef(-f6 * 0.3f, MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f * 2.0f) * 0.4f, -f5 * 0.4f);
        GL11.glTranslatef(0.8f * f2, -0.75f * f2 - (1.0f - f1) * 0.6f, -0.9f * f2);
        GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
        GL11.glEnable(32826);
        f4 = player.getSwingProgress(p_78440_1_);
        f5 = MathHelper.sin(f4 * f4 * 3.1415927f);
        f6 = MathHelper.sin(MathHelper.sqrt_float(f4) * 3.1415927f);
        GL11.glRotatef(f6 * 70.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-f5 * 20.0f, 0.0f, 0.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());
        GL11.glTranslatef(-1.0f, 3.6f, 3.5f);
        GL11.glRotatef(120.0f, 0.0f, 0.0f, 1.0f);
        GL11.glRotatef(200.0f, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glScalef(1.0f, 1.0f, 1.0f);
        GL11.glTranslatef(5.6f, 0.0f, 0.0f);
        final RenderPlayer renderplayer = (RenderPlayer)RenderManager.instance.getEntityRenderObject((Entity)player);
        final float f7 = 1.0f;
        GL11.glScalef(f7, f7, f7);
        renderplayer.renderFirstPersonArm((EntityPlayer)player);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        renderplayer.modelBipedMain.onGround = 0.0f;
        renderplayer.modelBipedMain.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, (Entity)player);
        renderplayer.modelBipedMain.bipedRightArm.render(0.0625f);
        final int dmg = item.getItemDamage();
        float[] col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(1, dmg)];
        GL11.glColor3f(col[0], col[1], col[2]);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderItemGlove.glove1);
        renderplayer.modelBipedMain.bipedRightArm.render(0.0625f);
        col = EntitySheep.fleeceColorTable[ItemGlove.getColIndex(0, dmg)];
        GL11.glColor3f(col[0], col[1], col[2]);
        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderItemGlove.glove2);
        renderplayer.modelBipedMain.bipedRightArm.render(0.0625f);
        GL11.glPopMatrix();
    }
    
    static {
        INSTANCE = new RenderItemGlove();
        FMLCommonHandler.instance().bus().register((Object)RenderItemGlove.INSTANCE);
        MinecraftForge.EVENT_BUS.register((Object)RenderItemGlove.INSTANCE);
        glove1 = new ResourceLocation("extrautils", "textures/glove0.png");
        glove2 = new ResourceLocation("extrautils", "textures/glove1.png");
    }
}

