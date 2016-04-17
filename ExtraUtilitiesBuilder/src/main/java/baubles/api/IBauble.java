// 
// Decompiled by Procyon v0.5.30
// 

package baubles.api;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IBauble
{
    BaubleType getBaubleType(final ItemStack p0);
    
    void onWornTick(final ItemStack p0, final EntityLivingBase p1);
    
    void onEquipped(final ItemStack p0, final EntityLivingBase p1);
    
    void onUnequipped(final ItemStack p0, final EntityLivingBase p1);
    
    boolean canEquip(final ItemStack p0, final EntityLivingBase p1);
    
    boolean canUnequip(final ItemStack p0, final EntityLivingBase p1);
}
