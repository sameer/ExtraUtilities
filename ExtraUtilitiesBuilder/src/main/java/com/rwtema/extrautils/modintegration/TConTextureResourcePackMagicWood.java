// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import net.minecraft.util.MathHelper;
import java.awt.image.BufferedImage;

public class TConTextureResourcePackMagicWood extends TConTextureResourcePackBase
{
    static int[][] offsets;
    
    public TConTextureResourcePackMagicWood(final String name) {
        super(name);
    }
    
    @Override
    public BufferedImage modifyImage(final BufferedImage image) {
        final int w = image.getWidth();
        final int h = image.getHeight();
        image.getType();
        final int[][] pixels = new int[w][h];
        final boolean[][] base = new boolean[w][h];
        int mean = 0;
        int div = 0;
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < h; ++y) {
                final int c = image.getRGB(x, y);
                pixels[x][y] = this.brightness(c);
                final boolean nottrans = c != 0 && TConTextureResourcePackMagicWood.rgb.getAlpha(c) > 64;
                if (nottrans) {
                    base[x][y] = true;
                    mean += pixels[x][y];
                    ++div;
                }
            }
        }
        mean = mean / div * 2 / 4;
        int n;
        if (w >= 256) {
            n = 5;
        }
        else if (w >= 128) {
            n = 4;
        }
        else if (w >= 64) {
            n = 3;
        }
        else if (w >= 32) {
            n = 2;
        }
        else {
            n = 1;
        }
        final boolean[][] baseSilhouette = this.contract(base, n);
        final boolean[][] interior1 = this.contract(baseSilhouette, n);
        final boolean[][] baseCorners = this.multI(this.mult(this.expand(this.getCorners(baseSilhouette), n), baseSilhouette), interior1);
        final boolean[][] baseCornersShift = this.orwise(this.orwise(this.shift(baseCorners, 0, -1), this.shift(baseCorners, -1, 0)), this.shift(baseCorners, -1, -1));
        final boolean[][] interior2 = this.contract(interior1, 2 * n);
        final boolean[][] interior3 = this.contract(interior2, n);
        final boolean[][] interior4 = this.contract(interior3, n);
        final boolean[][] interiorCorners = this.multI(this.mult(this.expand(this.getCorners(interior2), n), interior2), interior3);
        final boolean[][] interiorCornersShift = this.orwise(this.orwise(this.shift(interiorCorners, -1, 0), this.shift(interiorCorners, 0, -1)), this.shift(interiorCorners, -1, -1));
        final int trans = 0;
        final int gold = -398001;
        final int gold_highlight = -117;
        final int wood = -6455217;
        final int darkwood = -10071758;
        final int[][] outpixels = new int[w][w];
        for (int x2 = 0; x2 < w; ++x2) {
            for (int y2 = 0; y2 < h; ++y2) {
                if (!baseSilhouette[x2][y2]) {
                    if (base[x2][y2]) {
                        outpixels[x2][y2] = this.multPixel(darkwood, pixels[x2][y2] / 2);
                    }
                    else {
                        outpixels[x2][y2] = trans;
                    }
                }
                else if (!interior1[x2][y2]) {
                    if (baseCorners[x2][y2]) {
                        if (baseCornersShift[x2][y2]) {
                            outpixels[x2][y2] = this.multPixel(gold, Math.max(pixels[x2][y2], mean));
                        }
                        else {
                            outpixels[x2][y2] = this.multPixel(gold_highlight, Math.max(pixels[x2][y2], mean) + 5);
                        }
                    }
                    else {
                        outpixels[x2][y2] = this.multPixel(darkwood, pixels[x2][y2]);
                    }
                }
                else if (!interior2[x2][y2] || interior3[x2][y2]) {
                    if (interior3[x2][y2] && !interior4[x2][y2]) {
                        outpixels[x2][y2] = this.multPixel(wood, pixels[x2][y2] * 3 / 4);
                    }
                    else {
                        outpixels[x2][y2] = this.multPixel(wood, pixels[x2][y2]);
                    }
                }
                else if (interiorCorners[x2][y2]) {
                    if (interiorCornersShift[x2][y2]) {
                        outpixels[x2][y2] = this.multPixel(gold, Math.max(pixels[x2][y2], mean));
                    }
                    else {
                        outpixels[x2][y2] = this.multPixel(gold_highlight, Math.max(pixels[x2][y2], mean) + 5);
                    }
                }
                else {
                    outpixels[x2][y2] = this.multPixel(darkwood, pixels[x2][y2]);
                }
                image.setRGB(x2, y2, outpixels[x2][y2]);
            }
        }
        return image;
    }
    
    private boolean[][] orwise(final boolean[][] a, final boolean[][] b) {
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                final boolean[] array = a[i];
                final int n = j;
                array[n] |= b[i][j];
            }
        }
        return a;
    }
    
    private boolean[][] mult(final boolean[][] a, final boolean[][] b) {
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                final boolean[] array = a[i];
                final int n = j;
                array[n] &= b[i][j];
            }
        }
        return a;
    }
    
    private boolean[][] multI(final boolean[][] a, final boolean[][] b) {
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                final boolean[] array = a[i];
                final int n = j;
                array[n] &= !b[i][j];
            }
        }
        return a;
    }
    
    private boolean[][] expand(final boolean[][] base, final int n) {
        boolean[][] output = this.expand(base);
        for (int i = 0; i < n - 1; ++i) {
            output = this.expand(output);
        }
        return output;
    }
    
    private boolean[][] contract(final boolean[][] base, final int n) {
        boolean[][] output = this.contract(base);
        for (int i = 0; i < n - 1; ++i) {
            output = this.contract(output);
        }
        return output;
    }
    
    public int multPixel(final int col, final int b) {
        return 0xFF000000 | this.clamp(TConTextureResourcePackMagicWood.rgb.getRed(col) * b / 255) << 16 | this.clamp(TConTextureResourcePackMagicWood.rgb.getGreen(col) * b / 255) << 8 | this.clamp(TConTextureResourcePackMagicWood.rgb.getBlue(col) * b / 255);
    }
    
    private int clamp(final int i) {
        return MathHelper.clamp_int(i, 0, 255);
    }
    
    public boolean get(final boolean[][] img, final int x, final int y) {
        return x >= 0 && y >= 0 && x < img.length && y < img[x].length && img[x][y];
    }
    
    public boolean[][] shift(final boolean[][] img, final int dx, final int dy) {
        final int w = img.length;
        final boolean[][] img2 = new boolean[w][w];
        for (int x = Math.max(-dx, 0); x < Math.min(w, w + dx); ++x) {
            System.arraycopy(img[x + dx], Math.max(-dy, 0) + dy, img2[x], Math.max(-dy, 0), Math.min(w, w + dy) - Math.max(-dy, 0));
        }
        return img2;
    }
    
    public boolean[][] getCorners(final boolean[][] img) {
        final int w = img.length;
        final boolean[][] img2 = new boolean[w][w];
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < w; ++y) {
                if (img[x][y]) {
                    int an = -1;
                    int n = 0;
                    for (final int[] offset : TConTextureResourcePackMagicWood.offsets) {
                        if (this.get(img, x + offset[0], y + offset[1])) {
                            if (an == -1) {
                                an = n;
                            }
                            n = 0;
                        }
                        else if (++n == 5) {
                            break;
                        }
                    }
                    if (an != -1) {
                        n += an;
                    }
                    if (n >= 5) {
                        img2[x][y] = true;
                    }
                }
            }
        }
        return img2;
    }
    
    public boolean[][] contract(final boolean[][] img) {
        final int w = img.length;
        final boolean[][] img2 = new boolean[w][w];
        for (int x = 0; x < w; ++x) {
            System.arraycopy(img[x], 0, img2[x], 0, w);
        }
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < w; ++y) {
                if (img[x][y]) {
                    if (x == 0 || y == 0 || x == w - 1 || y == w - 1) {
                        img2[x][y] = false;
                    }
                }
                else {
                    if (x > 0) {
                        img2[x - 1][y] = false;
                    }
                    if (y > 0) {
                        img2[x][y - 1] = false;
                    }
                    if (x < w - 1) {
                        img2[x + 1][y] = false;
                    }
                    if (y < w - 1) {
                        img2[x][y + 1] = false;
                    }
                }
            }
        }
        return img2;
    }
    
    public boolean[][] expand(final boolean[][] img) {
        final int w = img.length;
        final boolean[][] img2 = new boolean[w][w];
        for (int x = 0; x < w; ++x) {
            System.arraycopy(img[x], 0, img2[x], 0, w);
        }
        for (int x = 0; x < w; ++x) {
            for (int y = 0; y < w; ++y) {
                if (img[x][y]) {
                    for (final int[] offset : TConTextureResourcePackMagicWood.offsets) {
                        final int dx = x + offset[0];
                        final int dy = y + offset[1];
                        if (dx >= 0 && dy >= 0 && dx < w && dy < w) {
                            img2[dx][dy] = true;
                        }
                    }
                }
            }
        }
        return img2;
    }
    
    static {
        TConTextureResourcePackMagicWood.offsets = new int[][] { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 } };
    }
}

