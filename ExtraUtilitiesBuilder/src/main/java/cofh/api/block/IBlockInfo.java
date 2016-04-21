// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.block;

import net.minecraft.util.IChatComponent;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public interface IBlockInfo
{
    void getBlockInfo(final IBlockAccess p0, final int p1, final int p2, final int p3, final ForgeDirection p4, final EntityPlayer p5, final List<IChatComponent> p6, final boolean p7);
}

