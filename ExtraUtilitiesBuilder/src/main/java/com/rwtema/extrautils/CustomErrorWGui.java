// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiErrorScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.client.CustomModLoadingErrorDisplayException;

@SideOnly(Side.CLIENT)
public class CustomErrorWGui extends CustomModLoadingErrorDisplayException
{
    String cause;
    String[] message;
    
    public CustomErrorWGui(final String cause, final String... message) {
        this.cause = cause;
        this.message = message;
    }
    
    public void initGui(final GuiErrorScreen errorScreen, final FontRenderer fontRenderer) {
    }
    
    public void drawScreen(final GuiErrorScreen errorScreen, final FontRenderer fontRenderer, final int mouseRelX, final int mouseRelY, final float tickTime) {
        errorScreen.drawDefaultBackground();
        final List t = new ArrayList();
        for (final String m : this.message) {
            if (m != null) {
                t.addAll(fontRenderer.listFormattedStringToWidth(m, errorScreen.width));
            }
        }
        int offset = Math.max(85 - t.size() * 10, 10);
        errorScreen.drawCenteredString(fontRenderer, this.cause, errorScreen.width / 2, offset, 16777215);
        offset += 10;
        for (final Object aT : t) {
            errorScreen.drawCenteredString(fontRenderer, (String)aT, errorScreen.width / 2, offset, 16777215);
            offset += 10;
        }
    }
}
