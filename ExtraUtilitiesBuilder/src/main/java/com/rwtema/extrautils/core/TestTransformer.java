// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core;

import cpw.mods.fml.common.FMLCommonHandler;
import java.util.Iterator;
import com.rwtema.extrautils.LogHelper;
import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class TestTransformer
{
    public static void performTest() {
        for (final String s : FMLDeobfuscatingRemapper.INSTANCE.getObfedClasses()) {
            LogHelper.info(s + FMLDeobfuscatingRemapper.INSTANCE.map(s), new Object[0]);
        }
    }
    
    static {
        performTest();
        FMLCommonHandler.instance().exitJava(0, true);
    }
}
