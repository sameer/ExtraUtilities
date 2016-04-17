// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.helper;

public class SplineHelper
{
    public static double[] splineParams(final double p0, final double p1, final double d0, final double d1) {
        return new double[] { 2.0 * p0 - 2.0 * p1 + d0 + d1, -3.0 * p0 + 3.0 * p1 - 2.0 * d0 - d1, d0, p0 };
    }
    
    public static double evalSpline(final double t, final double[] p) {
        return ((p[0] * t + p[1]) * t + p[2]) * t + p[3];
    }
}
