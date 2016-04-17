// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import net.minecraft.client.resources.IResource;
import java.io.IOException;
import cpw.mods.fml.client.FMLClientHandler;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourceManager;
import java.awt.image.DirectColorModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public abstract class TextureDerived extends TextureAtlasSprite
{
    private final String baseIcon;
    private final String basePath;
    private final TextureMapType type;
    public static DirectColorModel rgbBase;
    protected DirectColorModel rgb;
    
    public TextureDerived(final String p_i1282_1_, final String baseIcon, final TextureMapType type) {
        super(p_i1282_1_);
        this.rgb = TextureDerived.rgbBase;
        this.baseIcon = baseIcon;
        this.type = type;
        this.basePath = type.basePath;
    }
    
    public int getLuminosity(final int col) {
        return this.getLuminosity(this.rgb.getRed(col), this.rgb.getGreen(col), this.rgb.getBlue(col));
    }
    
    public int getLuminosity(final int r, final int g, final int b) {
        return (int)(r * 0.2126f + g * 0.7152f + b * 0.0722f);
    }
    
    public boolean hasCustomLoader(final IResourceManager manager, final ResourceLocation location) {
        return true;
    }
    
    public abstract BufferedImage processImage(final BufferedImage p0, final AnimationMetadataSection p1);
    
    public boolean load(final IResourceManager manager, final ResourceLocation oldlocation) {
        ResourceLocation location = new ResourceLocation(this.baseIcon);
        location = this.completeResourceLocation(location);
        try {
            final int mipmapLevels = Minecraft.getMinecraft().gameSettings.mipmapLevels;
            final int anisotropicFiltering = Minecraft.getMinecraft().gameSettings.anisotropicFiltering;
            final IResource iresource = manager.getResource(location);
            final BufferedImage[] abufferedimage = new BufferedImage[1 + mipmapLevels];
            abufferedimage[0] = ImageIO.read(iresource.getInputStream());
            final AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.getMetadata("animation");
            abufferedimage[0] = this.processImage(abufferedimage[0], animationmetadatasection);
            this.loadSprite(abufferedimage, animationmetadatasection, anisotropicFiltering > 1.0f);
        }
        catch (RuntimeException runtimeexception) {
            FMLClientHandler.instance().trackBrokenTexture(location, runtimeexception.getMessage());
            return true;
        }
        catch (IOException ioexception1) {
            FMLClientHandler.instance().trackMissingTexture(location);
            return true;
        }
        return false;
    }
    
    private ResourceLocation completeResourceLocation(final ResourceLocation p_147634_1_) {
        return new ResourceLocation(p_147634_1_.getResourceDomain(), String.format("%s/%s%s", this.basePath, p_147634_1_.getResourcePath(), ".png"));
    }
    
    static {
        TextureDerived.rgbBase = new DirectColorModel(32, 16711680, 65280, 255, -16777216);
    }
    
    public enum TextureMapType
    {
        BLOCK(0, "textures/blocks"), 
        ITEM(1, "textures/items");
        
        public final int textureMapType;
        public final String basePath;
        
        private TextureMapType(final int textureMapType, final String basePath) {
            this.textureMapType = textureMapType;
            this.basePath = basePath;
        }
    }
}
