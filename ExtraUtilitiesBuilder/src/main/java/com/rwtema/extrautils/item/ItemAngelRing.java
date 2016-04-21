// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.server.MinecraftServer;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;
import java.util.HashMap;
import net.minecraft.entity.EntityLivingBase;
import baubles.api.BaubleType;
import net.minecraft.nbt.NBTTagCompound;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import java.util.List;
import com.rwtema.extrautils.network.XUPacketBase;
import com.rwtema.extrautils.network.NetworkHandler;
import com.rwtema.extrautils.network.packets.PacketAngelRingNotifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import java.util.Map;
import baubles.api.IBauble;
import net.minecraft.item.Item;

public class ItemAngelRing extends Item implements IBauble
{
    public static boolean foundItem;
    public static Map<String, Integer> curFlyingPlayers;
    
    public ItemAngelRing() {
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:angelRing");
        this.setTextureName("extrautils:angelRing");
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
    }
    
    public static void addPlayer(final EntityPlayer player, final int i, final boolean override) {
        final String name = player.getGameProfile().getName();
        if (!ItemAngelRing.curFlyingPlayers.containsKey(name) || ItemAngelRing.curFlyingPlayers.get(name) == null || (override && ItemAngelRing.curFlyingPlayers.get(name) != i)) {
            ItemAngelRing.curFlyingPlayers.put(name, i);
            NetworkHandler.sendToAllPlayers(new PacketAngelRingNotifier(name, i));
        }
    }
    
    public static void removePlayer(final EntityPlayer player) {
        final String name = player.getGameProfile().getName();
        if (ItemAngelRing.curFlyingPlayers.containsKey(name)) {
            ItemAngelRing.curFlyingPlayers.remove(name);
            NetworkHandler.sendToAllPlayers(new PacketAngelRingNotifier(name, 0));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item p_150895_1_, final CreativeTabs p_150895_2_, final List p_150895_3_) {
        for (int i = 0; i < 5; ++i) {
            p_150895_3_.add(new ItemStack(p_150895_1_, 1, i));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        par3List.add(("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(par1ItemStack) + "." + par1ItemStack.getItemDamage() + ".name")).trim());
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
    
    public void onUpdate(final ItemStack itemstack, final World world, final Entity entity, final int slot, final boolean par5) {
        super.onUpdate(itemstack, world, entity, slot, par5);
        if (world.isRemote) {
            return;
        }
        if (!(entity instanceof EntityPlayerMP)) {
            return;
        }
        final NBTTagCompound nbt = XUHelper.getPersistantNBT(entity);
        nbt.setByte("XU|Flying", (byte)20);
        addPlayer((EntityPlayer)entity, itemstack.getItemDamage(), par5);
        if (!((EntityPlayerMP)entity).capabilities.allowFlying || !nbt.hasKey("XU|FlyingDim") || nbt.getInteger("XU|FlyingDim") != world.provider.dimensionId) {
            addPlayer((EntityPlayer)entity, itemstack.getItemDamage(), false);
            ((EntityPlayerMP)entity).capabilities.allowFlying = true;
            ((EntityPlayerMP)entity).sendPlayerAbilities();
        }
        nbt.setInteger("XU|FlyingDim", world.provider.dimensionId);
    }
    
    public BaubleType getBaubleType(final ItemStack itemstack) {
        return BaubleType.RING;
    }
    
    public void onWornTick(final ItemStack itemstack, final EntityLivingBase player) {
        this.onUpdate(itemstack, player.worldObj, (Entity)player, -1, true);
    }
    
    public void onEquipped(final ItemStack itemstack, final EntityLivingBase player) {
    }
    
    public void onUnequipped(final ItemStack itemstack, final EntityLivingBase player) {
    }
    
    public boolean canEquip(final ItemStack itemstack, final EntityLivingBase player) {
        return true;
    }
    
    public boolean canUnequip(final ItemStack itemstack, final EntityLivingBase player) {
        return true;
    }
    
    static {
        ItemAngelRing.foundItem = false;
        ItemAngelRing.curFlyingPlayers = new HashMap<String, Integer>();
        final EventHandlerRing handler = new EventHandlerRing();
        MinecraftForge.EVENT_BUS.register((Object)handler);
        FMLCommonHandler.instance().bus().register((Object)handler);
    }
    
    public static class EventHandlerRing
    {
        @SubscribeEvent
        public void playerLogin(final PlayerEvent.PlayerLoggedInEvent event) {
            for (final String name : MinecraftServer.getServer().getAllUsernames()) {
                if (ItemAngelRing.curFlyingPlayers.containsKey(name)) {
                    NetworkHandler.sendPacketToPlayer(new PacketAngelRingNotifier(name, ItemAngelRing.curFlyingPlayers.get(name)), event.player);
                }
                else {
                    NetworkHandler.sendPacketToPlayer(new PacketAngelRingNotifier(name, 0), event.player);
                }
            }
        }
        
        @SubscribeEvent
        public void entTick(final LivingEvent.LivingUpdateEvent event) {
            if (event.entity.worldObj.isRemote) {
                return;
            }
            ItemAngelRing.foundItem = true;
            if (!XUHelper.hasPersistantNBT(event.entity) || !XUHelper.getPersistantNBT(event.entity).hasKey("XU|Flying", 1)) {
                return;
            }
            Byte t = XUHelper.getPersistantNBT(event.entity).getByte("XU|Flying");
            t = (byte)(t - 1);
            if (t == 0) {
                XUHelper.getPersistantNBT(event.entity).removeTag("XU|Flying");
                if (event.entity instanceof EntityPlayerMP) {
                    final EntityPlayerMP entityPlayer = (EntityPlayerMP)event.entity;
                    ItemAngelRing.removePlayer((EntityPlayer)entityPlayer);
                    if (!entityPlayer.capabilities.isCreativeMode) {
                        entityPlayer.capabilities.allowFlying = false;
                        entityPlayer.capabilities.isFlying = false;
                        entityPlayer.sendPlayerAbilities();
                    }
                }
            }
            else {
                XUHelper.getPersistantNBT(event.entity).setByte("XU|Flying", (byte)t);
            }
        }
    }
}


