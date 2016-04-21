// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.helper;

import gnu.trove.procedure.TIntByteProcedure;
import org.lwjgl.opengl.GL11;
import gnu.trove.map.hash.TIntByteHashMap;

public class GLHelper
{
    public static int state_level;
    public static final int max_state_level = 256;
    public static final TIntByteHashMap[] maps;
    
    public static void pushGLState() {
        ++GLHelper.state_level;
        if (GLHelper.maps[GLHelper.state_level] == null) {
            GLHelper.maps[GLHelper.state_level] = new TIntByteHashMap();
        }
        else {
            GLHelper.maps[GLHelper.state_level].clear();
        }
    }
    
    public static boolean enableGLState(final int state) {
        final boolean b = GL11.glIsEnabled(state);
        GLHelper.maps[GLHelper.state_level].putIfAbsent(state, (byte)(byte)(b ? 1 : 0));
        GL11.glEnable(state);
        return b;
    }
    
    public static boolean disableGLState(final int state) {
        final boolean b = GL11.glIsEnabled(state);
        GLHelper.maps[GLHelper.state_level].putIfAbsent(state, (byte)(byte)(b ? 1 : 0));
        GL11.glDisable(state);
        return b;
    }
    
    public static void popGLState() {
        GLHelper.maps[GLHelper.state_level].forEachEntry((TIntByteProcedure)new TIntByteProcedure() {
            public boolean execute(final int a, final byte b) {
                if (b == 1) {
                    GL11.glEnable(a);
                }
                else {
                    GL11.glDisable(a);
                }
                return true;
            }
        });
        GLHelper.maps[GLHelper.state_level].clear();
        --GLHelper.state_level;
    }
    
    static {
        GLHelper.state_level = -1;
        maps = new TIntByteHashMap[256];
    }
}


