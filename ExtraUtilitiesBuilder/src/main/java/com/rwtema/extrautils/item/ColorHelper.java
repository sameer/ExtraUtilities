// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

public class ColorHelper
{
    public static int colorFromHue(final long h) {
        final float x = h % 60L / 60.0f;
        final int k = (int)(h % 360L) / 60;
        float r = 0.0f;
        float g = 0.0f;
        float b = 0.0f;
        switch (k) {
            case 0: {
                r = 1.0f;
                g = x;
                break;
            }
            case 1: {
                r = 1.0f - x;
                g = 1.0f;
                break;
            }
            case 2: {
                g = 1.0f;
                b = x;
                break;
            }
            case 3: {
                b = 1.0f;
                g = 1.0f - x;
                break;
            }
            case 4: {
                b = 1.0f;
                r = x;
                break;
            }
            case 5: {
                r = 1.0f;
                b = 1.0f - x;
                break;
            }
        }
        return (int)(r * 255.0f) << 16 | (int)(g * 255.0f) << 8 | (int)(b * 255.0f);
    }
}
