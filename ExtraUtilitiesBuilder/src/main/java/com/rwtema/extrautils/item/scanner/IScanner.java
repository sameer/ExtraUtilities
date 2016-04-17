// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item.scanner;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import java.util.List;

public interface IScanner
{
    Class<?> getTargetClass();
    
    void addData(final Object p0, final List<String> p1, final ForgeDirection p2, final EntityPlayer p3);
    
    int getPriority();
}
