// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemArmor;

public class ItemSonarGoggles extends ItemArmor
{
    private static final ResourceLocation texture;
    
    public ItemSonarGoggles() {
        super(ItemArmor.ArmorMaterial.IRON, 0, 0);
        this.setMaxDamage(1800);
        this.setUnlocalizedName("extrautils:sonar_goggles");
        this.setTextureName("extrautils:sonar_goggles");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setMaxStackSize(1);
    }
    
    public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
        return "extrautils:textures/sonar_lens.png";
    }
    
    @SideOnly(Side.CLIENT)
    public void renderHelmetOverlay(final ItemStack stack, final EntityPlayer player, final ScaledResolution resolution, final float partialTicks, final boolean hasScreen, final int mouseX, final int mouseY) {
        final double w = resolution.getScaledWidth_double();
        final double h = resolution.getScaledHeight_double();
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.2f);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        final Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertex(0.0, h, -90.0);
        tessellator.addVertex(w, h, -90.0);
        tessellator.addVertex(w, 0.0, -90.0);
        tessellator.addVertex(0.0, 0.0, -90.0);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(ItemSonarGoggles.texture);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0, h, -90.0, 0.0, 1.0);
        tessellator.addVertexWithUV(w, h, -90.0, 1.0, 1.0);
        tessellator.addVertexWithUV(w, 0.0, -90.0, 1.0, 0.0);
        tessellator.addVertexWithUV(0.0, 0.0, -90.0, 0.0, 0.0);
        tessellator.draw();
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        texture = new ResourceLocation("extrautils", "textures/goggle_overlay.png");
    }
}


