// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.dynamicgui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureManager;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTBase;

public abstract class WidgetTextData extends WidgetText implements IWidget
{
    public Object[] curData;
    
    public WidgetTextData(final int x, final int y, final int align, final int color) {
        super(x, y, align, color, null);
        this.curData = null;
    }
    
    public WidgetTextData(final int x, final int y, final int w, final int h, final int align, final int color) {
        super(x, y, w, h, align, color, null);
        this.curData = null;
    }
    
    public WidgetTextData(final int x, final int y, final int w) {
        super(x, y, null, w);
        this.curData = null;
    }
    
    public static Object getNBTBaseData(final NBTBase nbt) {
        if (nbt == null) {
            return null;
        }
        switch (nbt.getId()) {
            case 1: {
                return ((NBTTagByte)nbt).func_150290_f();
            }
            case 2: {
                return ((NBTTagShort)nbt).func_150289_e();
            }
            case 3: {
                return ((NBTTagInt)nbt).func_150287_d();
            }
            case 4: {
                return ((NBTTagLong)nbt).func_150291_c();
            }
            case 5: {
                return ((NBTTagFloat)nbt).func_150288_h();
            }
            case 6: {
                return ((NBTTagDouble)nbt).func_150286_g();
            }
            case 8: {
                return ((NBTTagString)nbt).func_150285_a_();
            }
            case 10: {
                return nbt;
            }
            default: {
                return null;
            }
        }
    }
    
    public static NBTBase getNBTBase(final Object o) {
        if (o instanceof Integer) {
            return (NBTBase)new NBTTagInt((Integer)o);
        }
        if (o instanceof Short) {
            return (NBTBase)new NBTTagShort((Short)o);
        }
        if (o instanceof Long) {
            return (NBTBase)new NBTTagLong((Long)o);
        }
        if (o instanceof String) {
            return (NBTBase)new NBTTagString((String)o);
        }
        if (o instanceof Double) {
            return (NBTBase)new NBTTagDouble((Double)o);
        }
        if (o instanceof Float) {
            return (NBTBase)new NBTTagFloat((Float)o);
        }
        if (o instanceof NBTTagCompound) {
            return (NBTBase)o;
        }
        if (o instanceof Byte) {
            return (NBTBase)new NBTTagByte((Byte)o);
        }
        LogHelper.debug("Can't find type for " + o, new Object[0]);
        throw null;
    }
    
    public abstract int getNumParams();
    
    public abstract Object[] getData();
    
    public abstract String getConstructedText();
    
    @Override
    public NBTTagCompound getDescriptionPacket(boolean changesOnly) {
        final Object[] newData = this.getData();
        if (this.curData == null) {
            this.curData = new Object[this.getNumParams()];
            changesOnly = false;
        }
        final NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < this.curData.length; ++i) {
            if (newData[i] != null && (!changesOnly || this.curData[i] == null || !this.curData[i].toString().equals(newData[i].toString()))) {
                final NBTBase nbtBase = getNBTBase(newData[i]);
                if (nbtBase != null) {
                    tag.setTag(Integer.toString(i), nbtBase);
                }
            }
            this.curData[i] = newData[i];
        }
        if (tag.hasNoTags()) {
            return null;
        }
        return tag;
    }
    
    @Override
    public void handleDescriptionPacket(final NBTTagCompound packet) {
        if (this.curData == null) {
            this.curData = new Object[this.getNumParams()];
        }
        for (int n = this.getNumParams(), i = 0; i < n; ++i) {
            final NBTBase base = packet.getTag(Integer.toString(i));
            if (base != null) {
                this.curData[i] = getNBTBaseData(base);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void renderBackground(final TextureManager manager, final DynamicGui gui, final int guiLeft, final int guiTop) {
        if (this.curData == null) {
            return;
        }
        final int x = this.getX() + (1 - this.align) * (this.getW() - gui.getFontRenderer().getStringWidth(this.getMsgClient())) / 2;
        gui.getFontRenderer().drawSplitString(this.getConstructedText(), guiLeft + x, guiTop + this.getY(), this.getW(), 4210752);
        manager.bindTexture(gui.getWidgets());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
