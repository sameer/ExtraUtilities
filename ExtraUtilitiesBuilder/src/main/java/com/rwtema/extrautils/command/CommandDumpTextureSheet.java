// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.io.IOException;
import com.rwtema.extrautils.LogHelper;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.File;
import javax.imageio.ImageTypeSpecifier;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.CommandBase;

public class CommandDumpTextureSheet extends CommandBase
{
    public String getCommandName() {
        return "dumpTextureAtlas";
    }
    
    public String getCommandUsage(final ICommandSender p_71518_1_) {
        return "dumpTextureAtlas";
    }
    
    public boolean canCommandSenderUseCommand(final ICommandSender p_71519_1_) {
        return true;
    }
    
    public void processCommand(final ICommandSender p_71515_1_, final String[] p_71515_2_) {
        this.outputTexture(TextureMap.locationBlocksTexture, "blocks");
        this.outputTexture(TextureMap.locationItemsTexture, "items");
    }
    
    public void outputTexture(final ResourceLocation locationTexture, final String s) {
        final int terrainTextureId = Minecraft.getMinecraft().renderEngine.getTexture(locationTexture).getGlTextureId();
        if (terrainTextureId == 0) {
            return;
        }
        final int w = GL11.glGetTexLevelParameteri(3553, 0, 4096);
        final int h = GL11.glGetTexLevelParameteri(3553, 0, 4097);
        final int[] pixels = new int[w * h];
        final IntBuffer pixelBuf = ByteBuffer.allocateDirect(w * h * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GL11.glGetTexImage(3553, 0, 32993, 5121, pixelBuf);
        pixelBuf.limit(w * h);
        pixelBuf.get(pixels);
        final BufferedImage image = ImageTypeSpecifier.createFromBufferedImageType(2).createBufferedImage(w, h);
        image.setRGB(0, 0, w, h, pixels, 0, w);
        final File f = new File(new File(Minecraft.getMinecraft().mcDataDir, "xutexture"), s + ".png");
        try {
            if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
                return;
            }
            if (!f.exists() && !f.createNewFile()) {
                return;
            }
            ImageIO.write(image, "png", f);
        }
        catch (IOException e) {
            LogHelper.info("Unable to output " + s, new Object[0]);
            e.printStackTrace();
        }
    }
}
