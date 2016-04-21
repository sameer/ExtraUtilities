// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.modintegration;

import java.awt.image.BufferedImage;

public class TConTextureResourcePackUnstableIngot extends TConTextureResourcePackBase
{
    public TConTextureResourcePackUnstableIngot(final String name) {
        super(name);
    }
    
    @Override
    public BufferedImage modifyImage(final BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean[][] trans = new boolean[width][height];
        final boolean[][] edge = new boolean[width][height];
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    edge[x][y] = true;
                }
                final int c = image.getRGB(x, y);
                if (c == 0 || TConTextureResourcePackUnstableIngot.rgb.getAlpha(c) < 64) {
                    trans[x][y] = true;
                    if (x > 0) {
                        edge[x - 1][y] = true;
                    }
                    if (y > 0) {
                        edge[x][y - 1] = true;
                    }
                    if (x < width - 1) {
                        edge[x + 1][y] = true;
                    }
                    if (y < height - 1) {
                        edge[x][y + 1] = true;
                    }
                }
            }
        }
        final int white = -1;
        for (int x2 = 0; x2 < width; ++x2) {
            for (int y2 = 0; y2 < height; ++y2) {
                if (!trans[x2][y2]) {
                    final int c2 = image.getRGB(x2, y2);
                    int lum = this.brightness(TConTextureResourcePackUnstableIngot.rgb.getRed(c2), TConTextureResourcePackUnstableIngot.rgb.getGreen(c2), TConTextureResourcePackUnstableIngot.rgb.getBlue(c2));
                    if (edge[x2][y2]) {
                        final int alpha = 255;
                        lum = 256 + (x2 * 16 / width + y2 * 16 / height - 16) * 6;
                        if (lum >= 256) {
                            lum = 255 - (lum - 256);
                        }
                        final int col = alpha << 24 | lum << 16 | lum << 8 | lum;
                        image.setRGB(x2, y2, col);
                    }
                    else {
                        image.setRGB(x2, y2, 0);
                    }
                }
            }
        }
        return image;
    }
}


