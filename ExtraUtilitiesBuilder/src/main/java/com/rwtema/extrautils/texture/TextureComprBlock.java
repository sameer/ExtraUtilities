// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.texture;

import net.minecraft.client.resources.IResource;
import java.io.IOException;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

@SideOnly(Side.CLIENT)
public class TextureComprBlock extends TextureAtlasSprite
{
    private int n;
    private ResourceLocation textureLocation;
    
    public TextureComprBlock(final String par1Str, final String base, final int n) {
        super(par1Str);
        this.n = n;
        this.textureLocation = new ResourceLocation("minecraft", "textures/blocks/" + base + ".png");
    }
    
    public boolean load(final IResourceManager manager, final ResourceLocation location) {
        final int mp = Minecraft.getMinecraft().gameSettings.mipmapLevels;
        try {
            final IResource iresource = manager.getResource(location);
            final BufferedImage[] abufferedimage = new BufferedImage[1 + Minecraft.getMinecraft().gameSettings.mipmapLevels];
            abufferedimage[0] = ImageIO.read(iresource.getInputStream());
            final TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");
            final AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
            this.loadSprite(abufferedimage, animationmetadatasection, Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0f);
        }
        catch (IOException e) {
            try {
                final IResource iresource2 = manager.getResource(this.textureLocation);
                final BufferedImage[] abufferedimage2 = new BufferedImage[1 + Minecraft.getMinecraft().gameSettings.mipmapLevels];
                abufferedimage2[0] = ImageIO.read(iresource2.getInputStream());
                final TextureMetadataSection texturemetadatasection2 = (TextureMetadataSection)iresource2.getMetadata("texture");
                final AnimationMetadataSection animationmetadatasection2 = (AnimationMetadataSection)iresource2.getMetadata("animation");
                this.loadSprite(abufferedimage2, animationmetadatasection2, Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1.0f);
            }
            catch (IOException e2) {
                e.printStackTrace();
                return true;
            }
            final float nh = this.n / 8.5f;
            final float br = 1.0f - nh;
            for (int j = 0; j < this.framesTextureData.size(); ++j) {
                final int[] image = new int[((int[][])this.framesTextureData.get(j))[0].length];
                for (int i = 0; i < image.length; ++i) {
                    final int x = i % this.width;
                    final int y = i / this.height;
                    final int l = ((int[][])this.framesTextureData.get(j))[0][i];
                    float r = (-l >> 16 & 0xFF) / 255.0f;
                    float g = (-l >> 8 & 0xFF) / 255.0f;
                    float b = (-l & 0xFF) / 255.0f;
                    final float dx = 2 * x / (this.width - 1) - 1.0f;
                    final float dy = 2 * y / (this.height - 1) - 1.0f;
                    float db = Math.max(Math.abs(dx), Math.abs(dy));
                    db = Math.max(db, (float)Math.sqrt(dx * dx + dy * dy) / 1.4f);
                    float d = 1.0f - db + 1.0f - nh;
                    final float rb = 1.0f - (2 + this.n) / 32.0f;
                    float k = 1.0f;
                    if (db > rb) {
                        k = 0.7f + 0.1f * (db - rb) / (1.0f - rb);
                    }
                    d *= k * k;
                    if (d > 1.0f) {
                        d = 1.0f;
                    }
                    else if (d < 0.0f) {
                        d = 0.0f;
                    }
                    r = 1.0f - (1.0f - r) * br * d;
                    g = 1.0f - (1.0f - g) * br * d;
                    b = 1.0f - (1.0f - b) * br * d;
                    image[i] = -((int)(r * 255.0f) << 16 | (int)(g * 255.0f) << 8 | (int)(b * 255.0f));
                }
                final int[][] aint = new int[1 + mp][];
                aint[0] = image;
                this.framesTextureData.set(j, aint);
            }
        }
        return false;
    }
    
    public boolean hasCustomLoader(final IResourceManager manager, final ResourceLocation location) {
        return true;
    }
}


