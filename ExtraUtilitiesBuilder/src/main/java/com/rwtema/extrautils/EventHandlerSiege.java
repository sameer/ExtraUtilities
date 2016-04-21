// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import java.util.Collection;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.event.world.WorldEvent;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.item.Item;
import java.util.HashSet;
import net.minecraftforge.oredict.OreDictionary;
import java.util.Iterator;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.Items;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.ArrayList;
import net.minecraftforge.common.DimensionManager;
import com.rwtema.extrautils.item.ItemDivisionSigil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.server.MinecraftServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.List;

public class EventHandlerSiege
{
    public static final int numKills = 100;
    public static final int[] ddx;
    public static final int[] ddz;
    public static List<String> SiegeParticipants;
    public static List<BiomeGenBase.SpawnListEntry> mobSpawns;
    public static ItemStack[] earthItems;
    public static ItemStack[] fireItems;
    private static Random rand;
    
    public static void endSiege(final World world, final boolean announce) {
        if (world.isRemote) {
            return;
        }
        for (int i = 0; i < world.loadedEntityList.size(); ++i) {
            if (world.loadedEntityList.get(i) instanceof EntityMob && ((Entity)world.loadedEntityList.get(i)).getEntityData().hasKey("Siege")) {
                world.removeEntity((Entity)world.loadedEntityList.get(i));
            }
        }
        if (announce) {
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentText("The Siege has ended in 'The End'"));
        }
    }
    
    public static void upgradeSigil(final EntityPlayer player) {
        for (int j = 0; j < player.inventory.mainInventory.length; ++j) {
            if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == ExtraUtils.divisionSigil) {
                if (player.inventory.mainInventory[j].hasTagCompound() && player.inventory.mainInventory[j].getTagCompound().hasKey("damage")) {
                    player.inventory.mainInventory[j] = ItemDivisionSigil.newStableSigil();
                }
                return;
            }
        }
    }
    
    public static void beginSiege(final World world) {
        if (world.isRemote) {
            return;
        }
        if (world.provider.dimensionId != 1) {
            return;
        }
        for (int i = 0; i < world.loadedEntityList.size(); ++i) {
            if (world.loadedEntityList.get(i) instanceof EntityMob) {
                world.removeEntity((Entity)world.loadedEntityList.get(i));
            }
            else if (world.loadedEntityList.get(i) instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer)world.loadedEntityList.get(i);
                EventHandlerSiege.SiegeParticipants.add(player.getGameProfile().getName());
                player.getEntityData().setInteger("SiegeKills", 0);
            }
        }
        if (EventHandlerSiege.SiegeParticipants.size() != 0) {
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg((IChatComponent)new ChatComponentText("The Siege has begun in 'The End'"));
        }
        else {
            endSiege(world, false);
        }
    }
    
    public static boolean hasSigil(final EntityPlayer player) {
        for (int j = 0; j < player.inventory.mainInventory.length; ++j) {
            if (player.inventory.mainInventory[j] != null && player.inventory.mainInventory[j].getItem() == ExtraUtils.divisionSigil && player.inventory.mainInventory[j].hasTagCompound() && player.inventory.mainInventory[j].getTagCompound().hasKey("damage")) {
                return true;
            }
        }
        return false;
    }
    
    public static void checkPlayers() {
        final World worldObj = (World)DimensionManager.getWorld(1);
        if (worldObj == null || worldObj.isRemote) {
            EventHandlerSiege.SiegeParticipants.clear();
            return;
        }
        if (EventHandlerSiege.SiegeParticipants.size() > 0) {
            for (int i = 0; i < EventHandlerSiege.SiegeParticipants.size(); ++i) {
                if (worldObj.getPlayerEntityByName((String)EventHandlerSiege.SiegeParticipants.get(i)) == null) {
                    EventHandlerSiege.SiegeParticipants.remove(EventHandlerSiege.SiegeParticipants.get(i));
                    --i;
                }
            }
            if (EventHandlerSiege.SiegeParticipants.size() == 0) {
                endSiege(worldObj, true);
            }
        }
    }
    
    public static int[] getStrength(final World world, final int x, final int y, final int z) {
        final List<ChunkPos> rs = new ArrayList<ChunkPos>();
        final List<ChunkPos> st = new ArrayList<ChunkPos>();
        rs.add(new ChunkPos(x, y, z));
        st.add(new ChunkPos(x, y, z));
        int maxDist = 0;
        final Block rsId = (Block)Blocks.redstone_wire;
        final Block stId = Blocks.tripwire;
        int k2 = 0;
        for (int i = 0; i < rs.size(); ++i) {
            for (int j = 0; j < 4; ++j) {
                final ChunkPos nPos = new ChunkPos(rs.get(i).x + EventHandlerSiege.ddx[j], y, rs.get(i).z + EventHandlerSiege.ddz[j]);
                final int m = mDist(nPos.x - x, nPos.z - z);
                if (m < 16 && world.getBlock(nPos.x, y, nPos.z) == rsId && !rs.contains(nPos)) {
                    rs.add(nPos);
                    if (world.getBlockMetadata(nPos.x, y, nPos.z) != 0) {
                        ++k2;
                    }
                    if (m > maxDist) {
                        maxDist = m;
                    }
                }
            }
        }
        rs.remove(new ChunkPos(x, y, z));
        int l = 0;
        for (int i2 = 0; i2 < st.size(); ++i2) {
            for (int j2 = 0; j2 < 4; ++j2) {
                final ChunkPos nPos2 = new ChunkPos(st.get(i2).x + EventHandlerSiege.ddx[j2], y, st.get(i2).z + EventHandlerSiege.ddz[j2]);
                final int m2 = mDist(nPos2.x - x, nPos2.z - z);
                if (m2 < 16) {
                    if (world.getBlock(nPos2.x, y, nPos2.z) == stId && !st.contains(nPos2)) {
                        st.add(nPos2);
                        if (m2 > maxDist) {
                            maxDist = m2;
                        }
                    }
                    else if (i2 != 0 && world.getBlock(nPos2.x, y, nPos2.z) == rsId && rs.contains(nPos2)) {
                        ++l;
                    }
                }
            }
        }
        return new int[] { l, maxDist * maxDist * 4 };
    }
    
    public static int mDist(int x, int z) {
        if (x < 0) {
            x *= -1;
        }
        if (z < 0) {
            z *= -1;
        }
        return (x > z) ? x : z;
    }
    
    public static int checkChestList(final IInventory chest, final ItemStack[] items, final boolean destroy) {
        final boolean[] check = new boolean[items.length];
        int s = 0;
        for (int i = 0; i < chest.getSizeInventory() && s < items.length; ++i) {
            if (chest.getStackInSlot(i) != null) {
                for (int j = 0; j < items.length && (!destroy || chest.getStackInSlot(i) != null); ++j) {
                    if (!check[j] && XUHelper.canItemsStack(items[j], chest.getStackInSlot(i), false, true)) {
                        if (destroy) {
                            chest.setInventorySlotContents(i, (ItemStack)null);
                        }
                        check[j] = true;
                        ++s;
                        break;
                    }
                }
            }
        }
        return s;
    }
    
    public static int checkChestEarth(final IInventory chest, final boolean destroy) {
        if (chest == null) {
            return 0;
        }
        if (EventHandlerSiege.earthItems == null) {
            EventHandlerSiege.earthItems = new ItemStack[] { new ItemStack(Blocks.coal_ore), new ItemStack(Blocks.gold_ore), new ItemStack(Blocks.iron_ore), new ItemStack(Blocks.lapis_ore), new ItemStack(Blocks.diamond_ore), new ItemStack(Blocks.emerald_ore), new ItemStack(Blocks.redstone_ore), new ItemStack((Block)Blocks.grass), new ItemStack(Blocks.dirt), new ItemStack(Blocks.clay), new ItemStack((Block)Blocks.sand), new ItemStack(Blocks.gravel), new ItemStack(Blocks.obsidian) };
        }
        return checkChestList(chest, EventHandlerSiege.earthItems, destroy);
    }
    
    public static int checkChestFire(final IInventory chest, final boolean destroy) {
        if (chest == null) {
            return 0;
        }
        if (EventHandlerSiege.fireItems == null) {
            EventHandlerSiege.fireItems = new ItemStack[] { new ItemStack(Items.coal, 1, 1), new ItemStack(Blocks.stone), new ItemStack(Items.brick), new ItemStack(Items.cooked_fished), new ItemStack(Blocks.glass), new ItemStack(Items.gold_ingot), new ItemStack(Items.iron_ingot), new ItemStack(Items.baked_potato), new ItemStack(Items.netherbrick), new ItemStack(Items.dye, 1, 2), new ItemStack(Blocks.hardened_clay), new ItemStack(Items.cooked_porkchop), new ItemStack(Items.cooked_beef), new ItemStack(Items.cooked_chicken) };
        }
        return checkChestList(chest, EventHandlerSiege.fireItems, destroy);
    }
    
    public static int checkChestWater(final IInventory chest, final boolean destroy) {
        if (chest == null) {
            return 0;
        }
        final List<PotionEffect> numEffects = new ArrayList<PotionEffect>();
        for (int i = 0; i < chest.getSizeInventory() && numEffects.size() < 12; ++i) {
            if (chest.getStackInSlot(i) != null && chest.getStackInSlot(i).getItem() == Items.potionitem) {
                final List temp = Items.potionitem.getEffects(chest.getStackInSlot(i));
                if (temp != null) {
                    for (final Object aTemp : temp) {
                        if (!numEffects.contains(aTemp)) {
                            numEffects.add((PotionEffect)aTemp);
                            if (!destroy) {
                                continue;
                            }
                            chest.setInventorySlotContents(i, (ItemStack)null);
                        }
                    }
                }
            }
        }
        return numEffects.size();
    }
    
    public static int checkChestAir(final IInventory chest, final boolean destroy) {
        if (chest == null) {
            return 0;
        }
        int s = 0;
        final List<ItemStack> pt = (List<ItemStack>)OreDictionary.getOres("record");
        final HashSet<Item> items = new HashSet<Item>();
        for (int i = 0; i < chest.getSizeInventory() && s < 12; ++i) {
            if (chest.getStackInSlot(i) != null) {
                final ItemStack item = chest.getStackInSlot(i);
                if (!items.contains(item.getItem())) {
                    boolean flag = false;
                    for (final ItemStack ore : pt) {
                        if (OreDictionary.itemMatches(item, ore, false)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        if (destroy) {
                            chest.setInventorySlotContents(i, (ItemStack)null);
                        }
                        items.add(item.getItem());
                        ++s;
                    }
                }
            }
        }
        return s;
    }
    
    @SubscribeEvent
    public void Siege(final EntityJoinWorldEvent event) {
        if (event.world.isRemote) {
            return;
        }
        checkPlayers();
        if (event.entity instanceof EntityPlayer) {
            if (event.world.provider.dimensionId != 1) {
                if (event.entity.getEntityData().hasKey("SiegeKills")) {
                    event.entity.getEntityData().removeTag("SiegeKills");
                    EventHandlerSiege.SiegeParticipants.remove(((EntityPlayer)event.entity).getGameProfile().getName());
                }
            }
            else if (event.entity.getEntityData().hasKey("SiegeKills") && !EventHandlerSiege.SiegeParticipants.contains(((EntityPlayer)event.entity).getGameProfile().getName())) {
                EventHandlerSiege.SiegeParticipants.add(((EntityPlayer)event.entity).getGameProfile().getName());
            }
        }
    }
    
    public double sq(final double x, final double y, final double z) {
        return x * x + z * z + y * y;
    }
    
    @SubscribeEvent
    public void golemDeath(final LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity.worldObj.provider.dimensionId == 1 && event.entity instanceof EntityIronGolem && event.source.getSourceOfDamage() instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
            if (!hasSigil(player)) {
                return;
            }
            final List t = event.entity.worldObj.loadedTileEntityList;
            for (final Object aT : t) {
                if (aT.getClass().equals(TileEntityBeacon.class)) {
                    final TileEntityBeacon beacon = (TileEntityBeacon)aT;
                    final int x = beacon.xCoord;
                    final int y = beacon.yCoord;
                    final int z = beacon.zCoord;
                    if (this.sq(event.entity.posX - x - 0.5, event.entity.posY - y - 0.5, event.entity.posZ - z - 0.5) >= 300.0) {
                        continue;
                    }
                    final int[] s = getStrength(event.entity.worldObj, x, y, z);
                    final World world = beacon.getWorldObj();
                    if (s[0] != 64) {
                        continue;
                    }
                    final int debug = 1;
                    boolean flag = true;
                    if (checkChestFire(TileEntityHopper.func_145893_b(world, (double)x, (double)y, (double)(z - 5)), false) < debug) {
                        flag = false;
                    }
                    if (flag && checkChestEarth(TileEntityHopper.func_145893_b(world, (double)x, (double)y, (double)(z + 5)), false) < debug) {
                        flag = false;
                    }
                    if (flag && checkChestAir(TileEntityHopper.func_145893_b(world, (double)(x - 5), (double)y, (double)z), false) < debug) {
                        flag = false;
                    }
                    if (flag && checkChestWater(TileEntityHopper.func_145893_b(world, (double)(x + 5), (double)y, (double)z), false) < debug) {
                        flag = false;
                    }
                    if (flag) {
                        world.func_147480_a(x, y, z, false);
                        for (int j = 0; j < 4; ++j) {
                            world.func_147480_a(x + EventHandlerSiege.ddx[j] * 5, y, z + EventHandlerSiege.ddz[j] * 5, false);
                        }
                        world.func_147480_a(x, y, z, false);
                        world.createExplosion((Entity)null, (double)x, (double)y, (double)z, 6.0f, true);
                        for (int j = 0; j < 4; ++j) {
                            world.createExplosion((Entity)null, (double)(x + EventHandlerSiege.ddx[j] * 5), (double)y, (double)(z + EventHandlerSiege.ddz[j] * 5), 3.0f, true);
                        }
                        beginSiege(world);
                        return;
                    }
                    continue;
                }
            }
        }
    }
    
    @SubscribeEvent
    public void SiegeDeath(final LivingDeathEvent event) {
        if (EventHandlerSiege.SiegeParticipants.isEmpty()) {
            return;
        }
        if (event.entityLiving.worldObj.isRemote || event.entityLiving.worldObj.provider.dimensionId != 1) {
            return;
        }
        if (event.entityLiving instanceof EntityPlayer) {
            checkPlayers();
        }
        if (event.entityLiving instanceof EntityMob && event.source.getSourceOfDamage() instanceof EntityPlayer && event.entityLiving.getEntityData().hasKey("Siege")) {
            final EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
            if (player != null && EventHandlerSiege.SiegeParticipants.contains(player.getGameProfile().getName()) && hasSigil(player)) {
                if (player.getEntityData().hasKey("SiegeKills")) {
                    player.getEntityData().setInteger("SiegeKills", player.getEntityData().getInteger("SiegeKills") + 1);
                }
                else {
                    player.getEntityData().setInteger("SiegeKills", 1);
                }
                final int kills = player.getEntityData().getInteger("SiegeKills");
                if (kills > 100) {
                    upgradeSigil(player);
                    player.getEntityData().removeTag("SiegeKills");
                    EventHandlerSiege.SiegeParticipants.remove(player.getGameProfile().getName());
                    PacketTempChat.sendChat(player, "Your Sigil has stabilized");
                }
                else {
                    PacketTempChat.sendChat(player, "Kills: " + player.getEntityData().getInteger("SiegeKills"));
                }
            }
        }
        else if (!(event.entityLiving instanceof EntityPlayer) || EventHandlerSiege.SiegeParticipants.contains(((EntityPlayer)event.entityLiving).getGameProfile().getName())) {}
    }
    
    @SubscribeEvent
    public void SiegePotentialSpawns(final WorldEvent.PotentialSpawns event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 1 && event.type == EnumCreatureType.monster) {
            checkPlayers();
            if (EventHandlerSiege.SiegeParticipants.isEmpty()) {
                event.list.removeAll(EventHandlerSiege.mobSpawns);
            }
            else if (event.list.size() < EventHandlerSiege.mobSpawns.size()) {
                event.list.addAll(EventHandlerSiege.mobSpawns);
            }
        }
    }
    
    @SubscribeEvent
    public void Siege(final LivingEvent.LivingUpdateEvent event) {
        if (event.entity.worldObj.isRemote) {
            return;
        }
        if (EventHandlerSiege.SiegeParticipants.isEmpty()) {
            if (event.entityLiving.getEntityData().hasKey("Siege")) {
                event.entity.setDead();
                endSiege(event.entity.worldObj, true);
            }
            return;
        }
        if (event.entityLiving.worldObj.rand.nextInt(1000) == 0) {
            checkPlayers();
        }
        if (event.entityLiving.worldObj.provider.dimensionId != 1) {
            return;
        }
        if (event.entityLiving instanceof EntityMob && ((EntityMob)event.entityLiving).getAttackTarget() == null && event.entityLiving.getEntityData().hasKey("Siege")) {
            final int i = EventHandlerSiege.rand.nextInt(EventHandlerSiege.SiegeParticipants.size());
            final EntityPlayer player = event.entityLiving.worldObj.getPlayerEntityByName((String)EventHandlerSiege.SiegeParticipants.get(i));
            if (player != null) {
                ((EntityMob)event.entityLiving).setAttackTarget((EntityLivingBase)player);
                ((EntityMob)event.entityLiving).setTarget((Entity)player);
            }
            else {
                EventHandlerSiege.SiegeParticipants.remove(i);
            }
        }
        if (event.entityLiving instanceof EntityPlayer) {
            final EntityPlayer player2 = (EntityPlayer)event.entityLiving;
            if (player2.motionY == 0.0 && player2.fallDistance == 0.0f && !player2.onGround && !player2.isOnLadder() && !player2.isInWater() && player2.ridingEntity == null) {
                player2.attackEntityFrom(DamageSource.generic, 0.5f);
            }
        }
    }
    
    @SubscribeEvent
    public void SiegeCheckSpawn(final LivingSpawnEvent.CheckSpawn event) {
        if (EventHandlerSiege.SiegeParticipants.isEmpty()) {
            return;
        }
        if (event.entity.worldObj.isRemote) {
            return;
        }
        if (event.world.provider.dimensionId != 1) {
            return;
        }
        if (event.entityLiving instanceof EntityMob && event.entityLiving.worldObj.checkNoEntityCollision(event.entityLiving.boundingBox) && event.entityLiving.worldObj.getCollidingBoundingBoxes((Entity)event.entityLiving, event.entityLiving.boundingBox).isEmpty() && !event.entityLiving.worldObj.isAnyLiquid(event.entityLiving.boundingBox)) {
            event.entityLiving.getEntityData().setBoolean("Siege", true);
            event.entityLiving.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 7200, 2));
            event.setResult(Event.Result.ALLOW);
        }
    }
    
    static {
        ddx = new int[] { -1, 0, 1, 0 };
        ddz = new int[] { 0, -1, 0, 1 };
        EventHandlerSiege.SiegeParticipants = new ArrayList<String>();
        (EventHandlerSiege.mobSpawns = new ArrayList<BiomeGenBase.SpawnListEntry>()).add(new BiomeGenBase.SpawnListEntry((Class)EntitySpider.class, 200, 3, 3));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityCaveSpider.class, 40, 4, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityZombie.class, 80, 4, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntitySkeleton.class, 200, 4, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityCreeper.class, 200, 4, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityBlaze.class, 80, 2, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityPigZombie.class, 40, 4, 4));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityWitch.class, 40, 1, 3));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntitySilverfish.class, 40, 3, 3));
        EventHandlerSiege.mobSpawns.add(new BiomeGenBase.SpawnListEntry((Class)EntityGiantZombie.class, 5, 1, 1));
        EventHandlerSiege.rand = XURandom.getInstance();
    }
}


