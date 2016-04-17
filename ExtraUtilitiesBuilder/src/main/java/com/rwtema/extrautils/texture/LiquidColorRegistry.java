// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.texture;

import java.util.HashMap;
import net.minecraftforge.fluids.FluidStack;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import net.minecraft.client.resources.IResource;
import javax.imageio.ImageIO;
import net.minecraft.util.ResourceLocation;
import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.IIcon;
import java.util.Map;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.IResourceManagerReloadListener;

@SideOnly(Side.CLIENT)
public class LiquidColorRegistry implements IResourceManagerReloadListener
{
    public static final int emptyColor = 16777215;
    public static final int defaultColor = 16777215;
    public static Map<IIcon, Integer> m;
    private static IResourceManager resourcemanager;
    
    public static void reset() {
        LiquidColorRegistry.m.clear();
    }
    
    public static int getIconColor(final IIcon icon, final int defaultColor) {
        if (icon == null) {
            return defaultColor;
        }
        if (LiquidColorRegistry.m.containsKey(icon)) {
            final Integer col = LiquidColorRegistry.m.get(icon);
            if (col == null) {
                return defaultColor;
            }
            return col;
        }
        else {
            final String t = icon.getIconName();
            Integer col2;
            try {
                col2 = readIconCol(t);
            }
            catch (IOException e) {
                col2 = null;
            }
            if (col2 == null) {
                LiquidColorRegistry.m.put(icon, null);
                return defaultColor;
            }
            final float r = (col2 >> 16 & 0xFF) / 255.0f * ((defaultColor >> 16 & 0xFF) / 255.0f);
            final float g = (col2 >> 8 & 0xFF) / 255.0f * ((defaultColor >> 8 & 0xFF) / 255.0f);
            final float b = (col2 & 0xFF) / 255.0f * ((defaultColor & 0xFF) / 255.0f);
            col2 = ((int)(r * 255.0f) << 16 | (int)(g * 255.0f) << 8 | (int)(b * 255.0f));
            LiquidColorRegistry.m.put(icon, col2);
            return col2;
        }
    }
    
    public static Integer readIconCol(final String t) throws IOException {
        String s1 = "minecraft";
        String s2 = t;
        final int i = t.indexOf(58);
        if (i >= 0) {
            s2 = t.substring(i + 1, t.length());
            if (i > 1) {
                s1 = t.substring(0, i);
            }
        }
        s1 = s1.toLowerCase();
        s2 = "textures/blocks/" + s2 + ".png";
        final IResource resource = LiquidColorRegistry.resourcemanager.getResource(new ResourceLocation(s1, s2));
        final InputStream inputstream = resource.getInputStream();
        final BufferedImage bufferedimage = ImageIO.read(inputstream);
        final int height = bufferedimage.getHeight();
        final int width = bufferedimage.getWidth();
        final int[] aint = new int[height * width];
        bufferedimage.getRGB(0, 0, width, height, aint, 0, width);
        if (aint.length == 0) {
            return null;
        }
        final float[] lab = new float[3];
        final CIELabColorSpace colorSpace = CIELabColorSpace.getInstance();
        for (final int l : aint) {
            final float[] f = colorSpace.fromRGB(l);
            for (int j = 0; j < 3; ++j) {
                final float[] array = lab;
                final int n = j;
                array[n] += f[j];
            }
        }
        for (int k = 0; k < 3; ++k) {
            final float[] array2 = lab;
            final int n2 = k;
            array2[n2] /= aint.length;
        }
        final float[] col = colorSpace.toRGB(lab);
        return 0xFF000000 | ((int)(col[0] * 255.0f) << 16 | (int)(col[1] * 255.0f) << 8 | (int)(col[2] * 255.0f));
    }
    
    public static int getFluidColor(final FluidStack fluid) {
        if (fluid == null || fluid.getFluid() == null) {
            return 16777215;
        }
        if (fluid.getFluid().getIcon(fluid) == null) {
            return 16777215;
        }
        return getIconColor(fluid.getFluid().getIcon(fluid), fluid.getFluid().getColor(fluid));
    }
    
    public void onResourceManagerReload(final IResourceManager resourcemanager) {
        reset();
        LiquidColorRegistry.resourcemanager = resourcemanager;
    }
    
    static {
        LiquidColorRegistry.m = new HashMap<IIcon, Integer>();
    }
}
