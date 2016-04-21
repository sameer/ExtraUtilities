// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.block;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;

public interface IDismantleable
{
    ArrayList<ItemStack> dismantleBlock(final EntityPlayer p0, final World p1, final int p2, final int p3, final int p4, final boolean p5);
    
    boolean canDismantle(final EntityPlayer p0, final World p1, final int p2, final int p3, final int p4);
}

