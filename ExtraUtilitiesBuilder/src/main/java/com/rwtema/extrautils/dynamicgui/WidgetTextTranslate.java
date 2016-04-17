// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import net.minecraft.util.StatCollector;

public class WidgetTextTranslate extends WidgetText
{
    public WidgetTextTranslate(final int x, final int y, final int w, final int h, final int align, final int color, final String msg) {
        super(x, y, w, h, align, color, msg);
    }
    
    public WidgetTextTranslate(final int i, final int j, final String invName, final int playerInvWidth) {
        super(i, j, invName, playerInvWidth);
    }
    
    @Override
    public String getMsgClient() {
        return StatCollector.translateToLocal(this.msg);
    }
}
