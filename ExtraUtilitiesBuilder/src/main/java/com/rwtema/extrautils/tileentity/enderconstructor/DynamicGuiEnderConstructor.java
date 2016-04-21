// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.tileentity.enderconstructor;

import com.rwtema.extrautils.dynamicgui.DynamicContainer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import com.rwtema.extrautils.dynamicgui.DynamicGui;

@SideOnly(Side.CLIENT)
public class DynamicGuiEnderConstructor extends DynamicGui
{
    private static final ResourceLocation texBackground;
    private static final ResourceLocation texWidgets;
    
    public DynamicGuiEnderConstructor(final DynamicContainer container) {
        super(container);
    }
    
    @Override
    public ResourceLocation getBackground() {
        return DynamicGuiEnderConstructor.texBackground;
    }
    
    @Override
    public ResourceLocation getWidgets() {
        return DynamicGuiEnderConstructor.texWidgets;
    }
    
    static {
        texBackground = new ResourceLocation("extrautils", "textures/guiBase2.png");
        texWidgets = new ResourceLocation("extrautils", "textures/guiWidget2.png");
    }
}

