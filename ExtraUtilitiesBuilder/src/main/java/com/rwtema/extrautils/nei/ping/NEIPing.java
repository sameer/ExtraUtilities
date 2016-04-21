// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.nei.ping;

import codechicken.nei.api.API;
import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketNEIPing;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.LayoutManager;
import codechicken.nei.NEIClientConfig;
import net.minecraft.client.gui.inventory.GuiContainer;
import codechicken.nei.guihook.IContainerInputHandler;

public class NEIPing implements IContainerInputHandler
{
    public static final String KEY_IDENTIFIER = "gui.xu_ping";
    
    public boolean keyTyped(final GuiContainer guiContainer, final char c, final int i) {
        final int keyBinding = NEIClientConfig.getKeyBinding("gui.xu_ping");
        if (i != keyBinding) {
            return false;
        }
        final LayoutManager layout = LayoutManager.instance();
        if (layout == null || LayoutManager.itemPanel == null || NEIClientConfig.isHidden()) {
            return false;
        }
        final ItemStack stackMouseOver = GuiContainerManager.getStackMouseOver(guiContainer);
        if (stackMouseOver == null || stackMouseOver.getItem() == null) {
            return false;
        }
        NetworkHandler.sendPacketToServer(new PacketNEIPing(stackMouseOver));
        return true;
    }
    
    public static void init() {
        API.addKeyBind("gui.xu_ping", 20);
        GuiContainerManager.addInputHandler((IContainerInputHandler)new NEIPing());
    }
    
    public void onKeyTyped(final GuiContainer guiContainer, final char c, final int i) {
    }
    
    public boolean lastKeyTyped(final GuiContainer guiContainer, final char c, final int i) {
        return false;
    }
    
    public boolean mouseClicked(final GuiContainer guiContainer, final int i, final int i1, final int i2) {
        return false;
    }
    
    public void onMouseClicked(final GuiContainer guiContainer, final int i, final int i1, final int i2) {
    }
    
    public void onMouseUp(final GuiContainer guiContainer, final int i, final int i1, final int i2) {
    }
    
    public boolean mouseScrolled(final GuiContainer guiContainer, final int i, final int i1, final int i2) {
        return false;
    }
    
    public void onMouseScrolled(final GuiContainer guiContainer, final int i, final int i1, final int i2) {
    }
    
    public void onMouseDragged(final GuiContainer guiContainer, final int i, final int i1, final int i2, final long l) {
    }
}

