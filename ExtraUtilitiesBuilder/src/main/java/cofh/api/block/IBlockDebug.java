// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public interface IBlockDebug
{
    void debugBlock(final IBlockAccess p0, final int p1, final int p2, final int p3, final ForgeDirection p4, final EntityPlayer p5);
}

