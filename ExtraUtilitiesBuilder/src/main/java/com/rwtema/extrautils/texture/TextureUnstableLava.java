// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.texture;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import java.awt.image.BufferedImage;

public class TextureUnstableLava extends TextureDerived
{
    public TextureUnstableLava(final String p_i1282_1_, final String baseIcon) {
        super(p_i1282_1_, baseIcon, TextureMapType.BLOCK);
    }
    
    @Override
    public BufferedImage processImage(final BufferedImage image, final AnimationMetadataSection animationmetadatasection) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        final int[] aint = new int[h * w];
        final int[] c = new int[256];
        image.getRGB(0, 0, w, h, aint, 0, w);
        int n1 = 0;
        for (int i = 0; i < aint.length; ++i) {
            if (this.rgb.getAlpha(aint[i]) > 10) {
                aint[i] = this.getLuminosity(aint[i]);
                n1 = Math.max(n1, aint[i]);
            }
            else {
                aint[i] = 255;
            }
        }
        final int v = h / w;
        for (int j = 0; j < aint.length; ++j) {
            final int x = j % w;
            final int y = (j - x) / w % w;
            final int sn = (j - x) / w / w;
            final int p = 1;
            int lum = 256 + (x * 16 / w + y * 16 / w - 16) % 32 * 1 * 2;
            for (int t = 0; (lum >= 256 || lum < 240) && t < 100; lum = 480 - lum) {
                ++t;
                if (lum >= 256) {
                    lum = 511 - lum;
                }
                if (lum < 240) {}
            }
            final int col = aint[j];
            int l = col + n1;
            l = 255 - (255 - l) * 2;
            if (l < 0) {
                l = 0;
            }
            l = 192 + (l >> 2);
            if (XURandom.getInstance().nextInt(3) != 0) {
                l -= XURandom.getInstance().nextInt(4);
            }
            l = l * lum / 255;
            if (l > 255) {
                l = 255;
            }
            if (l < 128) {
                l = 128;
            }
            aint[j] = (0xFF000000 | l * 65793);
        }
        image.setRGB(0, 0, w, h, aint, 0, w);
        return image;
    }
}


