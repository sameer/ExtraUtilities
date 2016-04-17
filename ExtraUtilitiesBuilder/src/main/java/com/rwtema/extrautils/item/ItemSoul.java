// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.entity.Entity;
import com.google.common.collect.Multimap;
import com.google.common.collect.HashMultimap;
import net.minecraft.util.DamageSource;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import java.util.UUID;
import net.minecraft.item.Item;

public class ItemSoul extends Item
{
    public static final UUID uuid;
    
    public ItemSoul() {
        this.setUnlocalizedName("extrautils:mini-soul");
        this.setTextureName("extrautils:mini-soul");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs p_150895_2_, final List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 3));
    }
    
    public ItemStack onItemRightClick(final ItemStack item, final World par3World, final EntityPlayer player) {
        if (par3World.isRemote) {
            return item;
        }
        if (!EntityPlayerMP.class.equals(player.getClass())) {
            return item;
        }
        if (player.capabilities.isCreativeMode && item.getItemDamage() == 3) {
            final AttributeModifier mod = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(ItemSoul.uuid);
            if (mod != null) {
                player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).removeModifier(mod);
            }
            --item.stackSize;
            return item;
        }
        if (item.getItemDamage() != 0 && item.getItemDamage() != 3) {
            return item;
        }
        if (item.hasTagCompound()) {
            final NBTTagCompound tag = item.getTagCompound();
            if (tag.hasKey("owner_id") && player.getGameProfile().getId() != null) {
                if (!player.getGameProfile().getId().toString().equals(tag.getString("owner_id"))) {
                    return item;
                }
            }
            else if (tag.hasKey("owner") && !player.getCommandSenderName().equals(tag.getString("owner"))) {
                return item;
            }
        }
        double l = 0.0;
        final IAttributeInstance a = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
        final AttributeModifier attr = a.getModifier(ItemSoul.uuid);
        if (attr != null) {
            l = attr.getAmount();
        }
        if (l > -0.1) {
            return item;
        }
        l += 0.1;
        a.removeModifier(attr);
        a.applyModifier(new AttributeModifier(ItemSoul.uuid, "Missing Soul", l, 2));
        player.addChatComponentMessage((IChatComponent)new ChatComponentText("You feel strangely refreshed (+10% Max Health)"));
        --item.stackSize;
        return item;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return par1ItemStack.getItemDamage() == 3;
    }
    
    public void onCreated(final ItemStack par1ItemStack, final World par2World, final EntityPlayer player) {
        super.onCreated(par1ItemStack, par2World, player);
        par1ItemStack.setItemDamage(1);
        if (!par2World.isRemote && XUHelper.isPlayerFake(player)) {
            return;
        }
        NBTTagCompound tag = par1ItemStack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setString("owner", player.getCommandSenderName());
        if (player.getGameProfile().getId() != null) {
            tag.setString("owner_id", player.getGameProfile().getId().toString());
        }
        par1ItemStack.setTagCompound(tag);
        if (!par2World.isRemote) {
            player.attackEntityFrom(DamageSource.magic, 0.0f);
        }
        double l = 0.0;
        final IAttributeInstance a = player.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
        final AttributeModifier attr = a.getModifier(ItemSoul.uuid);
        if (attr != null) {
            l = attr.getAmount();
        }
        l -= 0.1;
        final double c = Math.min(Math.min(a.getBaseValue() * (1.0 + l), a.getAttributeValue()), 20.0 * (1.0 + l));
        if (c >= 6.0) {
            par1ItemStack.setItemDamage(0);
            if (!par2World.isRemote) {
                final Multimap multimap = (Multimap)HashMultimap.create();
                multimap.put((Object)SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemSoul.uuid, "Missing Soul", l, 2));
                player.getAttributeMap().applyAttributeModifiers(multimap);
                player.addChatComponentMessage((IChatComponent)new ChatComponentText("You feel diminished (-10% Max Health)"));
            }
        }
        player.inventory.markDirty();
    }
    
    public static void updatePlayer(final Entity player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
        }
    }
    
    public void onUpdate(final ItemStack item, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {
        super.onUpdate(item, par2World, par3Entity, par4, par5);
        if (item.getItemDamage() == 2 && par3Entity instanceof EntityPlayerMP) {
            this.onCreated(item, par2World, (EntityPlayer)par3Entity);
            updatePlayer(par3Entity);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack item, final EntityPlayer player, final List par3List, final boolean par4) {
        super.addInformation(item, player, par3List, par4);
        final NBTTagCompound tag = item.getTagCompound();
        if (item.getItemDamage() == 3) {
            par3List.add("Soul of a forgotten deity");
        }
        if (item.getItemDamage() == 1) {
            par3List.add("Soul is too weak and has been spread too thin");
        }
        if (tag == null) {
            return;
        }
        if (tag.hasKey("owner")) {
            par3List.add("Owner: " + tag.getString("owner"));
        }
    }
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return true;
    }
    
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        if (itemstack.getItemDamage() == 2) {
            location.setDead();
        }
        return null;
    }
    
    static {
        uuid = UUID.fromString("12345678-9182-3532-aaaa-aaabacadabaa");
    }
}
