// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import net.minecraft.util.MathHelper;
import com.rwtema.extrautils.modintegration.TConTextureResourcePackBedrockium;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import java.awt.image.BufferedImage;

public class TextureBedrockLava extends TextureDerived
{
    public TextureBedrockLava(final String p_i1282_1_, final String baseIcon) {
        super(p_i1282_1_, baseIcon, TextureMapType.BLOCK);
    }
    
    @Override
    public BufferedImage processImage(final BufferedImage image, final AnimationMetadataSection animationmetadatasection) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int[] pixels = new int[h * w];
        image.getRGB(0, 0, w, h, pixels, 0, w);
        double mean = 0.0;
        for (int i = 0; i < pixels.length; ++i) {
            pixels[i] = this.getLuminosity(pixels[i]);
            mean += pixels[i];
        }
        mean /= pixels.length;
        final BufferedImage bedrockImage = TConTextureResourcePackBedrockium.getBedrockImage();
        for (int j = 0; j < pixels.length; ++j) {
            final int x = j % w;
            final int y = (j - x) / w % w;
            final int sn = (j - x) / w / w;
            final int dx = x * bedrockImage.getWidth() / w;
            final int dy = y * bedrockImage.getHeight() / w;
            final int col = bedrockImage.getRGB(dx, dy);
            final double f = pixels[j] / mean;
            final int r = this.clamp(this.rgb.getRed(col) * f);
            final int g = this.clamp(this.rgb.getGreen(col) * f);
            final int b = this.clamp(this.rgb.getBlue(col) * f);
            pixels[j] = (0xFF000000 | r << 16 | g << 8 | b);
        }
        image.setRGB(0, 0, w, h, pixels, 0, w);
        return image;
    }
    
    private int clamp(final double v) {
        return MathHelper.clamp_int((int)v, 0, 255);
    }
}
