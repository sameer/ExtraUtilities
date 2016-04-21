// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.damgesource;

import net.minecraft.util.DamageSource;

public class DamageSourceDivByZero extends DamageSource
{
    public static DamageSourceDivByZero divbyzero;
    
    protected DamageSourceDivByZero() {
        super("divbyzero");
        this.setDamageBypassesArmor();
    }
    
    static {
        DamageSourceDivByZero.divbyzero = new DamageSourceDivByZero();
    }
}


