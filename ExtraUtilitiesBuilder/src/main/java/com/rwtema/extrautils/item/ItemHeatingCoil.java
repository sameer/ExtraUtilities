// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import com.rwtema.extrautils.helper.XUHelper;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.IFuelHandler;
import cofh.api.energy.ItemEnergyContainer;

public class ItemHeatingCoil extends ItemEnergyContainer implements IFuelHandler
{
    public static final int rate = 25;
    public static final int energyAmmountForOneHeat = 15000;
    public static final int uses = 100;
    
    public ItemHeatingCoil() {
        super(1500000, 1500000, 75000);
        GameRegistry.registerFuelHandler((IFuelHandler)this);
        this.setMaxStackSize(1);
        this.setTextureName("extrautils:heatingElement");
        this.setUnlocalizedName("extrautils:heatingElement");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2) {
        final int i = 30;
        final float f13 = Minecraft.getSystemTime() % 6000L / 3000.0f * 3.141592f * 2.0f;
        final float t = 0.9f + 0.1f * MathHelper.cos(f13);
        final double v = 1.0 - this.getDurabilityForDisplay(par1ItemStack);
        int r = i + (int)(v * (255 - i) * t);
        if (r > 255) {
            r = 255;
        }
        final int g = i + (int)(v * (64 - i) * t);
        return r << 16 | g << 8 | i;
    }
    
    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        par3List.add(XUHelper.addThousandsCommas(this.getEnergyStored(par1ItemStack)) + " / " + XUHelper.addThousandsCommas(this.getMaxEnergyStored(par1ItemStack)));
    }
    
    public double getDurabilityForDisplay(final ItemStack stack) {
        return 1.0 - this.getEnergyStored(stack) / this.capacity;
    }
    
    public ItemStack getContainerItem(final ItemStack itemStack) {
        final ItemStack newItem = itemStack.copy();
        newItem.stackSize = 1;
        this.extractEnergy(newItem, 15000, false);
        return newItem;
    }
    
    public boolean hasContainerItem(final ItemStack stack) {
        return true;
    }
    
    public int getBurnTime(final ItemStack fuel) {
        if (fuel == null || fuel.getItem() != this) {
            return 0;
        }
        return this.extractEnergy(fuel, 15000, true) / 50;
    }
}

