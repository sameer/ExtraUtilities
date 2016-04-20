// 
// Decompiled by Procyon v0.5.30
// Very Pretty Blocks m9
// 

package cofh.api.block;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.world.IBlockAccess;

public interface IBlockAppearance
{
    Block getVisualBlock(final IBlockAccess p0, final int p1, final int p2, final int p3, final ForgeDirection p4);
    
    int getVisualMeta(final IBlockAccess p0, final int p1, final int p2, final int p3, final ForgeDirection p4);
    
    boolean supportsVisualConnections();
}
