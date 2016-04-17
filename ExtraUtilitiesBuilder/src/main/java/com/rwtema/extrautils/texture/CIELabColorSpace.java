// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import java.awt.color.ColorSpace;

public class CIELabColorSpace extends ColorSpace
{
    private final float INV255 = 0.003921569f;
    private static final long serialVersionUID = 5027741380892134289L;
    private static final ColorSpace CIEXYZ;
    private static final double N = 0.13793103448275862;
    private static final double X0 = 0.95047;
    private static final double XI = 1.0521110608435826;
    private static final double Y0 = 1.0;
    private static final double YI = 1.0;
    private static final double Z0 = 1.08883;
    private static final double ZI = 0.9184170164304805;
    
    public static CIELabColorSpace getInstance() {
        return Holder.INSTANCE;
    }
    
    @Override
    public float[] fromCIEXYZ(final float[] colorvalue) {
        final double l = f(colorvalue[1] * 1.0);
        final double L = 116.0 * l - 16.0;
        final double a = 500.0 * (f(colorvalue[0] * 1.0521110608435826) - l);
        final double b = 200.0 * (l - f(colorvalue[2] * 0.9184170164304805));
        return new float[] { (float)L, (float)a, (float)b };
    }
    
    @Override
    public float[] fromRGB(final float[] rgbvalue) {
        final float[] xyz = CIELabColorSpace.CIEXYZ.fromRGB(rgbvalue);
        return this.fromCIEXYZ(xyz);
    }
    
    public float[] fromRGB(final int r, final int g, final int b) {
        return this.fromRGB(new float[] { r * 0.003921569f, g * 0.003921569f, b * 0.003921569f });
    }
    
    public float[] fromRGB(final int col) {
        return this.fromRGB(new float[] { ((col & 0xFF0000) >> 16) * 0.003921569f, ((col & 0xFF00) >> 8) * 0.003921569f, (col & 0xFF) * 0.003921569f });
    }
    
    @Override
    public float getMaxValue(final int component) {
        return 128.0f;
    }
    
    @Override
    public float getMinValue(final int component) {
        return (component == 0) ? 0.0f : -128.0f;
    }
    
    @Override
    public String getName(final int idx) {
        return String.valueOf("Lab".charAt(idx));
    }
    
    @Override
    public float[] toCIEXYZ(final float[] colorvalue) {
        final double i = (colorvalue[0] + 16.0) * 0.008620689655172414;
        final double X = fInv(i + colorvalue[1] * 0.002) * 0.95047;
        final double Y = fInv(i) * 1.0;
        final double Z = fInv(i - colorvalue[2] * 0.005) * 1.08883;
        return new float[] { (float)X, (float)Y, (float)Z };
    }
    
    @Override
    public float[] toRGB(final float[] colorvalue) {
        final float[] xyz = this.toCIEXYZ(colorvalue);
        return CIELabColorSpace.CIEXYZ.toRGB(xyz);
    }
    
    CIELabColorSpace() {
        super(1, 3);
    }
    
    private static double f(final double x) {
        if (x > 0.008856451679035631) {
            return Math.cbrt(x);
        }
        return 7.787037037037037 * x + 0.13793103448275862;
    }
    
    private static double fInv(final double x) {
        if (x > 0.20689655172413793) {
            return x * x * x;
        }
        return 0.12841854934601665 * (x - 0.13793103448275862);
    }
    
    private Object readResolve() {
        return getInstance();
    }
    
    static {
        CIEXYZ = ColorSpace.getInstance(1001);
    }
    
    private static class Holder
    {
        static final CIELabColorSpace INSTANCE;
        
        static {
            INSTANCE = new CIELabColorSpace();
        }
    }
}
