// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core.config;

import net.minecraftforge.common.config.Configuration;
import java.util.HashSet;

public class Config
{
    public static HashSet<Config> configs;
    public String name;
    public String comment;
    public boolean shouldReload;
    
    public boolean reloadData() {
        return false;
    }
    
    public void load(final Configuration config) {
    }
    
    static {
        Config.configs = new HashSet<Config>();
    }
}
