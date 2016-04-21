// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import java.nio.IntBuffer;

public class Texture
{
    private int id;
    public final int w;
    public final int h;
    private final IntBuffer pixelBuf;
    
    public Texture(final int w, final int h, final int fillColour, final int minFilter, final int maxFilter, final int textureWrap) {
        this.id = GL11.glGenTextures();
        this.w = w;
        this.h = h;
        this.pixelBuf = allocateDirectIntBuffer(w * h);
        this.fillRect(0, 0, w, h, fillColour);
        this.pixelBuf.position(0);
        this.bind();
        GL11.glTexImage2D(3553, 0, 32856, w, h, 0, 32993, 5121, this.pixelBuf);
        this.setTexParameters(minFilter, maxFilter, textureWrap);
    }
    
    public static IntBuffer allocateDirectIntBuffer(final int size) {
        return ByteBuffer.allocateDirect(size * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
    }
    
    public Texture(final int w, final int h, final int fillColour) {
        this(w, h, fillColour, 9729, 9728, 33071);
    }
    
    public static int getTextureWidth() {
        return GL11.glGetTexLevelParameteri(3553, 0, 4096);
    }
    
    public static int getTextureHeight() {
        return GL11.glGetTexLevelParameteri(3553, 0, 4097);
    }
    
    public Texture(final int id) {
        this.id = id;
        this.bind();
        this.w = getTextureWidth();
        this.h = getTextureHeight();
        this.pixelBuf = allocateDirectIntBuffer(this.w * this.h);
        this.getPixelsFromExistingTexture();
    }
    
    public synchronized void close() {
        if (this.id != 0) {
            try {
                GL11.glDeleteTextures(this.id);
            }
            catch (NullPointerException e) {
                this.log("MwTexture.close: null pointer exception (texture %d)", this.id);
            }
            this.id = 0;
        }
    }
    
    public synchronized void fillRect(final int x, final int y, final int w, final int h, final int colour) {
        final int offset = y * this.w + x;
        for (int j = 0; j < h; ++j) {
            this.pixelBuf.position(offset + j * this.w);
            for (int i = 0; i < w; ++i) {
                this.pixelBuf.put(colour);
            }
        }
    }
    
    public synchronized void getRGB(final int x, final int y, final int w, final int h, final int[] pixels, final int offset, final int scanSize) {
        final int bufOffset = y * this.w + x;
        for (int i = 0; i < h; ++i) {
            this.pixelBuf.position(bufOffset + i * this.w);
            this.pixelBuf.get(pixels, offset + i * scanSize, w);
        }
    }
    
    public void bind() {
        GL11.glBindTexture(3553, this.id);
    }
    
    public void setTexParameters(final int minFilter, final int maxFilter, final int textureWrap) {
        this.bind();
        GL11.glTexParameteri(3553, 10242, textureWrap);
        GL11.glTexParameteri(3553, 10243, textureWrap);
        GL11.glTexParameteri(3553, 10241, minFilter);
        GL11.glTexParameteri(3553, 10240, maxFilter);
    }
    
    private synchronized void getPixelsFromExistingTexture() {
        try {
            this.bind();
            this.pixelBuf.clear();
            GL11.glGetTexImage(3553, 0, 32993, 5121, this.pixelBuf);
            this.pixelBuf.limit(this.w * this.h);
        }
        catch (NullPointerException e) {
            this.log("MwTexture.getPixels: null pointer exception (texture %d)", this.id);
        }
    }
    
    private void log(final String s, final int id) {
    }
}

