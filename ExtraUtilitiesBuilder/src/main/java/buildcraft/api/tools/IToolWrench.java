// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package buildcraft.api.tools;

import net.minecraft.entity.player.EntityPlayer;

public interface IToolWrench
{
    boolean canWrench(final EntityPlayer p0, final int p1, final int p2, final int p3);
    
    void wrenchUsed(final EntityPlayer p0, final int p1, final int p2, final int p3);
}


