// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import java.util.ArrayList;
import com.rwtema.extrautils.item.ItemGoldenBag;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraft.world.World;
import com.rwtema.extrautils.item.ItemDivisionSigil;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import com.rwtema.extrautils.network.NetworkHandler;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.rwtema.extrautils.item.ItemSoul;
import net.minecraft.entity.SharedMonsterAttributes;
import com.rwtema.extrautils.item.ItemUnstableIngot;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.entity.EnumCreatureType;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.boss.EntityWither;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.entity.monster.EntitySilverfish;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraft.tileentity.TileEntity;
import java.util.Iterator;
import com.rwtema.extrautils.tileentity.IAntiMobTorch;
import net.minecraft.entity.Entity;
import java.util.List;

public class EventHandlerServer
{
    public static String debug;
    public static List<int[]> magnumTorchRegistry;
    private String silverName;
    
    public EventHandlerServer() {
        this.silverName = "nuggetSilver";
    }
    
    public static boolean isInRangeOfTorch(final Entity entity) {
        for (final int[] coord : EventHandlerServer.magnumTorchRegistry) {
            if (coord[0] == entity.worldObj.provider.dimensionId && entity.worldObj.blockExists(coord[1], coord[2], coord[3]) && entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]) instanceof IAntiMobTorch) {
                final TileEntity tile = entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
                final double dx = tile.xCoord + 0.5f - entity.posX;
                final double dy = tile.yCoord + 0.5f - entity.posY;
                final double dz = tile.zCoord + 0.5f - entity.posZ;
                if ((dx * dx + dz * dz) / ((IAntiMobTorch)tile).getHorizontalTorchRangeSquared() + dy * dy / ((IAntiMobTorch)tile).getVerticalTorchRangeSquared() <= 1.0) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @SubscribeEvent
    public void soulDropping(final LivingDropsEvent event) {
        int prob = 43046721;
        if (ExtraUtils.lawSwordEnabled && event.source.getEntity() instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)event.source.getEntity();
            if (player.getHeldItem() != null && player.getHeldItem().getItem() == ExtraUtils.lawSword) {
                prob /= 10;
            }
        }
        if (ExtraUtils.soul != null && event.entityLiving instanceof EntityMob && !event.entity.worldObj.isRemote && event.entity.worldObj.rand.nextInt(prob) == 0) {
            event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entity.posY, event.entityLiving.posZ, new ItemStack((Item)ExtraUtils.soul, 1, 3)));
        }
    }
    
    @SubscribeEvent
    public void silverFishDrop(final LivingDropsEvent event) {
        if (event.entityLiving instanceof EntitySilverfish && !event.entity.worldObj.isRemote && event.entity.worldObj.rand.nextInt(5) == 0 && event.recentlyHit && OreDictionary.getOres(this.silverName).size() > 0) {
            final ItemStack item = OreDictionary.getOres(this.silverName).get(0).copy();
            if (event.drops.size() > 0) {
                for (int i = 0; i < event.drops.size(); ++i) {
                    final ItemStack t = event.drops.get(i).getEntityItem();
                    if (t != null && (t.getItem() == item.getItem() || t.getItemDamage() == item.getItemDamage())) {
                        return;
                    }
                }
            }
            event.drops.add(new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entity.posY, event.entityLiving.posZ, item));
        }
    }
    
    @SubscribeEvent
    public void netherDrop(final LivingDropsEvent event) {
        if (ExtraUtils.divisionSigil != null && event.entityLiving instanceof EntityWither && event.source.getSourceOfDamage() instanceof EntityPlayer && event.entity.worldObj != null && event.entity.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            final EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
            NBTTagCompound t = new NBTTagCompound();
            if (player.getEntityData().hasKey("PlayerPersisted")) {
                t = player.getEntityData().getCompoundTag("PlayerPersisted");
            }
            else {
                player.getEntityData().setTag("PlayerPersisted", (NBTBase)t);
            }
            int kills = 0;
            if (t.hasKey("witherKills")) {
                kills = t.getInteger("witherKills");
            }
            ++kills;
            t.setInteger("witherKills", kills);
            if (kills == 1 || !t.hasKey("hasSigil") || event.entity.worldObj.rand.nextInt(10) == 0) {
                final ItemStack item = new ItemStack(ExtraUtils.divisionSigil);
                final EntityItem entityitem = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, item);
                entityitem.delayBeforeCanPickup = 10;
                event.drops.add(entityitem);
            }
        }
    }
    
    @SubscribeEvent
    public void decoratePiEasterEgg(final PopulateChunkEvent.Post event) {
        if (event.chunkX == 196349 && event.chunkZ == 22436 && event.world.provider.dimensionId == 0) {
            event.world.setBlock(3141592, 65, 358979, (Block)Blocks.chest);
            final TileEntity chest = event.world.getTileEntity(3141592, 65, 358979);
            if (chest instanceof IInventory) {
                ((IInventory)chest).setInventorySlotContents(0, new ItemStack(Items.pumpkin_pie));
            }
        }
    }
    
    @SubscribeEvent
    public void magnumTorchDenyTeleport(final EnderTeleportEvent event) {
        if (event.entityLiving instanceof EntityEnderman && !((EntityEnderman)event.entityLiving).isScreaming()) {
            for (final int[] coord : EventHandlerServer.magnumTorchRegistry) {
                if (coord[0] == event.entity.worldObj.provider.dimensionId && event.entity.worldObj.blockExists(coord[1], coord[2], coord[3]) && event.entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]) instanceof IAntiMobTorch) {
                    final TileEntity tile = event.entity.worldObj.getTileEntity(coord[1], coord[2], coord[3]);
                    final double dx = tile.xCoord + 0.5f - event.targetX;
                    final double dy = tile.yCoord + 0.5f - event.targetY;
                    final double dz = tile.zCoord + 0.5f - event.targetZ;
                    if ((dx * dx + dz * dz) / ((IAntiMobTorch)tile).getHorizontalTorchRangeSquared() + dy * dy / ((IAntiMobTorch)tile).getVerticalTorchRangeSquared() > 1.0) {
                        continue;
                    }
                    final double dx2 = tile.xCoord + 0.5f - event.entity.posX;
                    final double dy2 = tile.yCoord + 0.5f - event.entity.posY;
                    final double dz2 = tile.zCoord + 0.5f - event.entity.posZ;
                    if (dx * dx + dy * dy + dz * dz >= dx2 * dx2 + dy2 * dy2 + dz2 * dz2) {
                        continue;
                    }
                    event.setCanceled(true);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void magnumTorchDenySpawn(final LivingSpawnEvent.CheckSpawn event) {
        if (event.getResult() == Event.Result.ALLOW) {
            return;
        }
        if (event.entityLiving.isCreatureType(EnumCreatureType.monster, false) && isInRangeOfTorch(event.entity)) {
            event.setResult(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent
    public void rainInformer(final EntityJoinWorldEvent event) {
        if (event.world.isRemote || event.entity instanceof EntityPlayerMP) {}
    }
    
    @SubscribeEvent
    public void debugValueLoad(final ServerChatEvent event) {
        EventHandlerServer.debug = event.message;
    }
    
    @SubscribeEvent
    public void angelBlockDestroy(final PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && ExtraUtils.angelBlock != null && event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == ExtraUtils.angelBlock && event.entityPlayer.inventory.addItemStackToInventory(new ItemStack(ExtraUtils.angelBlock)) && !event.entityPlayer.worldObj.isRemote) {
            event.entityPlayer.worldObj.func_147480_a(event.x, event.y, event.z, false);
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void unstableIngotDestroyer(final EntityItemPickupEvent event) {
        if (ExtraUtils.unstableIngot != null && event.item.getEntityItem().getItem() == ExtraUtils.unstableIngot && event.item.getEntityItem().hasTagCompound() && (event.item.getEntityItem().getTagCompound().hasKey("crafting") || event.item.getEntityItem().getTagCompound().hasKey("time"))) {
            event.item.setDead();
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void unstableIngotExploder(final ItemTossEvent event) {
        if (ExtraUtils.unstableIngot != null && event.entityItem.getEntityItem().getItem() == ExtraUtils.unstableIngot && event.entityItem.getEntityItem().hasTagCompound() && event.entityItem.getEntityItem().getTagCompound().hasKey("time")) {
            ItemUnstableIngot.explode(event.player);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void updateSoulDrain(final LivingSpawnEvent event) {
        if (event.world.isRemote) {
            return;
        }
        if (!EntityPlayerMP.class.equals(event.getClass())) {
            return;
        }
        final IAttributeInstance a = event.entityLiving.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName());
        final AttributeModifier attr = a.getModifier(ItemSoul.uuid);
        if (attr == null || attr.getOperation() == 2) {
            return;
        }
        final double l = attr.getAmount() / 20.0;
        a.removeModifier(attr);
        a.applyModifier(new AttributeModifier(ItemSoul.uuid, "Missing Soul", l, 0));
    }
    
    @SubscribeEvent
    public void persistSoulDrain(final PlayerEvent.Clone event) {
        if (event.entityPlayer.worldObj.isRemote || ExtraUtils.permaSoulDrainOff) {
            return;
        }
        final EntityPlayer original = event.original;
        EntityPlayer clone = event.entityPlayer;
        AttributeModifier attr = original.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(ItemSoul.uuid);
        if (attr == null) {
            attr = clone.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).getModifier(ItemSoul.uuid);
            if (attr != null) {
                clone = event.original;
            }
        }
        if (attr != null) {
            try {
                clone.getAttributeMap().getAttributeInstanceByName(SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName()).applyModifier(attr);
            }
            catch (IllegalArgumentException ex) {}
        }
    }
    
    @SubscribeEvent
    public void updateEntity(final LivingEvent.LivingUpdateEvent event) {
        if (event.entity.worldObj.isRemote) {
            return;
        }
        if (event.entity.getEntityData().hasKey("CursedEarth")) {
            int c = event.entity.getEntityData().getInteger("CursedEarth");
            if (c == 0) {
                event.entity.setDead();
                NetworkHandler.sendParticle(event.entity.worldObj, "smoke", event.entity.posX, event.entity.posY + event.entity.height / 4.0f, event.entity.posZ, 0.0, 0.0, 0.0, false);
            }
            else {
                --c;
                event.entity.getEntityData().setInteger("CursedEarth", c);
            }
        }
    }
    
    @SubscribeEvent
    public void ActivationRitual(final LivingDeathEvent event) {
        if (ExtraUtils.divisionSigil == null && ExtraUtils.cursedEarth == null) {
            return;
        }
        if (!(event.source.getSourceOfDamage() instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)event.source.getSourceOfDamage();
        if (ExtraUtils.divisionSigil != null) {
            boolean flag = false;
            for (int j = 0; j < player.inventory.getSizeInventory(); ++j) {
                final ItemStack item = player.inventory.getStackInSlot(j);
                if (item != null && item.getItem() == ExtraUtils.divisionSigil) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return;
            }
        }
        final World world = event.entityLiving.worldObj;
        if (world.isRemote) {
            return;
        }
        int x = (int)event.entityLiving.posX;
        int y = (int)event.entityLiving.boundingBox.minY;
        int z = (int)event.entityLiving.posZ;
        boolean found = false;
        final long time = world.getWorldTime() % 24000L;
        if (time < 12000L || time > 24000L) {
            return;
        }
        for (int dx = -2; !found & dx <= 2; ++dx) {
            for (int dy = -2; !found & dy <= 0; ++dy) {
                for (int dz = -2; !found & dz <= 2; ++dz) {
                    if (world.getBlock(x + dx, y + dy, z + dz) == Blocks.enchanting_table) {
                        found = true;
                        x += dx;
                        y += dy;
                        z += dz;
                    }
                }
            }
        }
        if (!found) {
            return;
        }
        if (world.getBlock(x, y, z) != Blocks.enchanting_table) {
            return;
        }
        if (!ActivationRitual.redstoneCirclePresent(world, x, y, z)) {
            return;
        }
        if (!ActivationRitual.altarCanSeeMoon(world, x, y, z)) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar cannot see the moon");
            return;
        }
        if (!ActivationRitual.altarOnEarth(world, x, y, z)) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar and circle must be on natural earth");
            return;
        }
        if (!ActivationRitual.altarInDarkness(world, x, y, z)) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar is too brightly lit");
            return;
        }
        if (!ActivationRitual.naturalEarth(world, x, y, z)) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Altar requires more natural earth");
            return;
        }
        final int i = ActivationRitual.checkTime(world.getWorldTime());
        if (i == -1) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Too early");
            return;
        }
        if (i == 1) {
            PacketTempChat.sendChat(player, "Activation Ritual Failure: Too late");
            return;
        }
        ActivationRitual.startRitual(world, x, y, z, player);
        NetworkHandler.sendSoundEvent(world, 0, x + 0.5f, y + 0.5f, z + 0.5f);
        if (ExtraUtils.divisionSigil != null) {
            for (int k = 0; k < player.inventory.getSizeInventory(); ++k) {
                final ItemStack item2 = player.inventory.getStackInSlot(k);
                if (item2 != null && item2.getItem() == ExtraUtils.divisionSigil) {
                    NBTTagCompound tags;
                    if (item2.hasTagCompound()) {
                        tags = item2.getTagCompound();
                    }
                    else {
                        tags = new NBTTagCompound();
                    }
                    tags.setInteger("damage", ItemDivisionSigil.maxdamage);
                    item2.setTagCompound(tags);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void goldenLasso(final EntityInteractEvent event) {
        final ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();
        if (itemstack != null && ExtraUtils.goldenLasso != null && itemstack.getItem() == ExtraUtils.goldenLasso && event.target instanceof EntityLivingBase && itemstack.interactWithEntity(event.entityPlayer, (EntityLivingBase)event.target)) {
            if (itemstack.stackSize <= 0) {
                event.entityPlayer.destroyCurrentEquippedItem();
            }
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void captureGoldenBagDrop(final PlayerDropsEvent event) {
        if (ExtraUtils.goldenBag == null) {
            return;
        }
        if (event.entity.getEntityData().getCompoundTag("PlayerPersisted").hasKey("XU|GoldenBags")) {
            return;
        }
        int j = 0;
        final NBTTagCompound t = new NBTTagCompound();
        for (int i = 0; i < event.drops.size(); ++i) {
            final ItemStack item = event.drops.get(i).getEntityItem();
            if (item != null && item.getItem() == ExtraUtils.goldenBag && ItemGoldenBag.isMagic(item)) {
                final NBTTagCompound tags = new NBTTagCompound();
                item.writeToNBT(tags);
                t.setTag("item_" + j, (NBTBase)tags);
                ++j;
                event.drops.remove(i);
                --i;
            }
        }
        t.setInteger("no_items", j);
        NBTTagCompound e;
        if (event.entityPlayer.getEntityData().hasKey("PlayerPersisted")) {
            e = event.entityPlayer.getEntityData().getCompoundTag("PlayerPersisted");
        }
        else {
            e = new NBTTagCompound();
            event.entityPlayer.getEntityData().setTag("PlayerPersisted", (NBTBase)e);
        }
        e.setTag("XU|GoldenBags", (NBTBase)t);
    }
    
    @SubscribeEvent
    public void recreateGoldenBags(final EntityJoinWorldEvent event) {
        if (event.world.isRemote || ExtraUtils.goldenBag == null) {
            return;
        }
        if (event.entity instanceof EntityPlayer && event.entity.getEntityData().hasKey("PlayerPersisted") && event.entity.getEntityData().getCompoundTag("PlayerPersisted").hasKey("XU|GoldenBags")) {
            final NBTTagCompound tags = event.entity.getEntityData().getCompoundTag("PlayerPersisted").getCompoundTag("XU|GoldenBags");
            for (int n = tags.getInteger("no_items"), i = 0; i < n; ++i) {
                final ItemStack item = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("item_" + i));
                if (item != null) {
                    ((EntityPlayer)event.entity).inventory.addItemStackToInventory(ItemGoldenBag.clearMagic(item));
                }
            }
            event.entity.getEntityData().getCompoundTag("PlayerPersisted").removeTag("XU|GoldenBags");
        }
    }
    
    static {
        EventHandlerServer.debug = "";
        EventHandlerServer.magnumTorchRegistry = new ArrayList<int[]>();
    }
}

