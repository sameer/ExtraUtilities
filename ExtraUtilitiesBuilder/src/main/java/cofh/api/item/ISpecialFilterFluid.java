// 
// Decompiled by Procyon v0.5.30
// 

package cofh.api.item;

import net.minecraftforge.fluids.FluidStack;
import net.minecraft.item.ItemStack;

public interface ISpecialFilterFluid
{
    boolean matchesFluid(final ItemStack p0, final FluidStack p1);
}
