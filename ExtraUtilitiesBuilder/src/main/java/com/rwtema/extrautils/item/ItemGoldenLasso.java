// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.StatCollector;
import net.minecraft.util.EnumChatFormatting;
import java.util.List;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.Item;

public class ItemGoldenLasso extends Item
{
    public ItemGoldenLasso() {
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.maxStackSize = 1;
        this.setHasSubtypes(true);
        this.setUnlocalizedName("extrautils:golden_lasso");
        this.setTextureName("extrautils:golden_lasso");
    }
    
    public boolean itemInteractionForEntity(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final EntityLivingBase par2EntityLiving) {
        if (par1ItemStack.hasTagCompound()) {
            if (par1ItemStack.getItemDamage() != 0) {
                return false;
            }
            par1ItemStack.setTagCompound((NBTTagCompound)null);
        }
        if (!(par2EntityLiving instanceof EntityCreature) && !(par2EntityLiving instanceof EntityAmbientCreature)) {
            return false;
        }
        if (par2EntityLiving instanceof EntityMob) {
            return false;
        }
        if (((EntityLiving)par2EntityLiving).getAttackTarget() != null) {
            return false;
        }
        final NBTTagCompound entityTags = new NBTTagCompound();
        entityTags.setBoolean("com.rwtema.extrautils.goldenlasso", true);
        if (!par2EntityLiving.writeMountToNBT(entityTags)) {
            return false;
        }
        if (!entityTags.hasKey("com.rwtema.extrautils.goldenlasso") | !entityTags.getBoolean("com.rwtema.extrautils.goldenlasso")) {
            return false;
        }
        String name = "";
        if (((EntityLiving)par2EntityLiving).hasCustomNameTag()) {
            name = ((EntityLiving)par2EntityLiving).getCustomNameTag();
        }
        if (!par2EntityLiving.worldObj.isRemote) {
            par2EntityLiving.setDead();
        }
        par1ItemStack.setTagCompound(entityTags);
        if (name.equals("")) {
            if (par2EntityLiving instanceof EntityVillager) {
                par1ItemStack.setItemDamage(2);
            }
            else {
                par1ItemStack.setItemDamage(1);
            }
        }
        else {
            par1ItemStack.setStackDisplayName(name);
            par1ItemStack.setItemDamage(2);
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return par1ItemStack.getItemDamage() != 0;
    }
    
    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World, int par4, int par5, int par6, final int par7, final float par8, final float par9, final float par10) {
        if (par1ItemStack.getItemDamage() == 0 | !par1ItemStack.hasTagCompound()) {
            par1ItemStack.setItemDamage(0);
            return false;
        }
        if (!par1ItemStack.getTagCompound().hasKey("id")) {
            par1ItemStack.setItemDamage(0);
            return false;
        }
        if (par3World.isRemote) {
            return true;
        }
        final Block i1 = par3World.getBlock(par4, par5, par6);
        par4 += Facing.offsetsXForSide[par7];
        par5 += Facing.offsetsYForSide[par7];
        par6 += Facing.offsetsZForSide[par7];
        double d0 = 0.0;
        if (par7 == 1 && i1 != null && i1.getRenderType() == 11) {
            d0 = 0.5;
        }
        final NBTTagCompound tags = par1ItemStack.getTagCompound();
        tags.setTag("Pos", (NBTBase)this.newDoubleNBTList(par4 + 0.5, par5 + d0, par6 + 0.5));
        tags.setTag("Motion", (NBTBase)this.newDoubleNBTList(0.0, 0.0, 0.0));
        tags.setFloat("FallDistance", 0.0f);
        tags.setInteger("Dimension", par3World.provider.dimensionId);
        final Entity entity = EntityList.createEntityFromNBT(tags, par3World);
        if (entity != null && entity instanceof EntityLiving && par1ItemStack.hasDisplayName()) {
            ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
        }
        par3World.spawnEntityInWorld(entity);
        par1ItemStack.setTagCompound((NBTTagCompound)null);
        par1ItemStack.setItemDamage(0);
        if (par2EntityPlayer.capabilities.isCreativeMode) {
            par2EntityPlayer.setCurrentItemOrArmor(0, new ItemStack(ExtraUtils.goldenLasso, 1, 0));
        }
        par2EntityPlayer.inventory.markDirty();
        return true;
    }
    
    protected NBTTagList newDoubleNBTList(final double... par1ArrayOfDouble) {
        final NBTTagList nbttaglist = new NBTTagList();
        for (final double d1 : par1ArrayOfDouble) {
            nbttaglist.appendTag((NBTBase)new NBTTagDouble(d1));
        }
        return nbttaglist;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("id")) {
            par3List.set(0, par3List.get(0).replaceFirst(EnumChatFormatting.ITALIC + par1ItemStack.getDisplayName() + EnumChatFormatting.RESET, this.getItemStackDisplayName(par1ItemStack)));
            final String animal_name = StatCollector.translateToLocal("entity." + par1ItemStack.getTagCompound().getString("id") + ".name");
            par3List.add(animal_name);
            if (par1ItemStack.hasDisplayName()) {
                if (par1ItemStack.getTagCompound().hasKey("spoiler")) {
                    par3List.add("*this " + animal_name.toLowerCase() + " has chosen a new name*");
                }
                else {
                    par3List.add(par1ItemStack.getDisplayName());
                }
            }
        }
    }
}


