// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.damgesource;

import net.minecraft.util.DamageSource;

public class DamageSourceDarkness extends DamageSource
{
    public static DamageSourceDarkness darkness;
    
    protected DamageSourceDarkness() {
        super("darkness");
        this.setDamageBypassesArmor();
    }
    
    static {
        DamageSourceDarkness.darkness = new DamageSourceDarkness();
    }
}

