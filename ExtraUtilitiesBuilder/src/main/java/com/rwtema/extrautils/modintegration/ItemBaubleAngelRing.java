// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.modintegration;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import baubles.api.IBauble;
import com.rwtema.extrautils.item.ItemAngelRing;

public class ItemBaubleAngelRing extends ItemAngelRing implements IBauble
{
    @Override
    public BaubleType getBaubleType(final ItemStack itemstack) {
        return super.getBaubleType(null);
    }
    
    @Override
    public void onWornTick(final ItemStack itemstack, final EntityLivingBase player) {
        super.onUpdate(itemstack, player.worldObj, (Entity)player, 0, false);
    }
    
    @Override
    public void onEquipped(final ItemStack itemstack, final EntityLivingBase player) {
    }
    
    @Override
    public void onUnequipped(final ItemStack itemstack, final EntityLivingBase player) {
    }
    
    @Override
    public boolean canEquip(final ItemStack itemstack, final EntityLivingBase player) {
        return false;
    }
    
    @Override
    public boolean canUnequip(final ItemStack itemstack, final EntityLivingBase player) {
        return false;
    }
}
