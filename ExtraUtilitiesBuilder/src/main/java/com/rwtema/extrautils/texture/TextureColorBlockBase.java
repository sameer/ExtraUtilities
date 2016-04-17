// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import net.minecraft.client.resources.IResource;
import java.io.IOException;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@SideOnly(Side.CLIENT)
public class TextureColorBlockBase extends TextureAtlasSprite
{
    private String name;
    private final String directory;
    public float scale;
    
    public TextureColorBlockBase(final String par1Str) {
        this(par1Str, "blocks");
    }
    
    public TextureColorBlockBase(final String par1Str, final String directory) {
        super("extrautils:bw_(" + par1Str + ")");
        this.name = par1Str;
        this.directory = directory;
        this.scale = 1.6666666f;
    }
    
    public boolean load(final IResourceManager manager, final ResourceLocation location) {
        String s1 = "minecraft";
        String s2 = this.name;
        final int ind = this.name.indexOf(58);
        if (ind >= 0) {
            s2 = this.name.substring(ind + 1, this.name.length());
            if (ind > 1) {
                s1 = this.name.substring(0, ind);
            }
        }
        final int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;
        s1 = s1.toLowerCase();
        s2 = "textures/" + this.directory + "/" + s2 + ".png";
        try {
            final IResource iresource = manager.getResource(new ResourceLocation(s1, s2));
            final BufferedImage[] abufferedimage = new BufferedImage[1 + mp];
            abufferedimage[0] = ImageIO.read(iresource.getInputStream());
            final AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
            this.loadSprite(abufferedimage, animationmetadatasection, Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0f);
        }
        catch (IOException e) {
            return true;
        }
        for (int j = 0; j < this.framesTextureData.size(); ++j) {
            final int[] image = new int[((int[][])this.framesTextureData.get(j))[0].length];
            float min_m = 1.0f;
            float max_m = 0.0f;
            for (int i = 0; i < image.length; ++i) {
                final int l = ((int[][])this.framesTextureData.get(j))[0][i];
                if (l < 0) {
                    float r = (-l >> 16 & 0xFF) / 255.0f;
                    float g = (-l >> 8 & 0xFF) / 255.0f;
                    float b = (-l & 0xFF) / 255.0f;
                    r = 1.0f - r;
                    g = 1.0f - g;
                    b = 1.0f - b;
                    final float m = r * 0.2126f + g * 0.7152f + b * 0.0722f;
                    if (m > max_m) {
                        max_m = m;
                    }
                    if (m < min_m) {
                        min_m = m;
                    }
                }
            }
            if (min_m == 1.0f && max_m == 0.0f) {
                return false;
            }
            if (max_m == min_m) {
                max_m = min_m + 0.001f;
            }
            final float new_max_m = Math.min(max_m * this.scale, 1.0f);
            final float new_min_m = min_m / max_m * new_max_m;
            for (int k = 0; k < image.length; ++k) {
                final int l2 = ((int[][])this.framesTextureData.get(j))[0][k];
                if (l2 < 0) {
                    float r2 = (-l2 >> 16 & 0xFF) / 255.0f;
                    float g2 = (-l2 >> 8 & 0xFF) / 255.0f;
                    float b2 = (-l2 & 0xFF) / 255.0f;
                    r2 = 1.0f - r2;
                    g2 = 1.0f - g2;
                    b2 = 1.0f - b2;
                    float m2 = r2 * 0.2126f + g2 * 0.7152f + b2 * 0.0722f;
                    final float dm = (m2 - min_m) / (max_m - min_m);
                    m2 = new_min_m + dm * (new_max_m - new_min_m);
                    g2 = (r2 = (b2 = Math.max(Math.min(0.975f, m2), 0.025f)));
                    r2 = 1.0f - r2;
                    g2 = 1.0f - g2;
                    b2 = 1.0f - b2;
                    image[k] = -((int)(r2 * 255.0f) << 16 | (int)(g2 * 255.0f) << 8 | (int)(b2 * 255.0f));
                }
            }
            final int[][] aint = new int[1 + mp][];
            aint[0] = image;
            this.framesTextureData.set(j, aint);
        }
        return false;
    }
    
    public boolean hasCustomLoader(final IResourceManager manager, final ResourceLocation location) {
        return true;
    }
}
