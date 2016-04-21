// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S23PacketBlockChange;
import com.google.common.base.Throwables;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import com.rwtema.extrautils.helper.XUHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemGlove extends ItemSword
{
    public static final int INVALID_METADATA = 32767;
    static Item.ToolMaterial materialWool;
    IIcon glove1;
    IIcon glove2;
    static int genericDmg;
    public static BaseAttribute woolProtection;
    UUID freezeUUID;
    
    public ItemGlove() {
        super(ItemGlove.materialWool);
        this.freezeUUID = UUID.fromString("EC21E5A7-1E80-4913-b55C-6ABD8EC8EA90");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:glove");
        this.setTextureName("extrautils:glove");
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
    }
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return stack != null && stack.getItemDamage() == 32767;
    }
    
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        if (itemstack.getItemDamage() == 32767) {
            location.setDead();
        }
        return null;
    }
    
    public void registerIcons(final IIconRegister register) {
        this.glove1 = register.registerIcon("extrautils:glove_1");
        this.glove2 = register.registerIcon("extrautils:glove_2");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(final int p_77618_1_, final int p_77618_2_) {
        return (p_77618_2_ == 0) ? this.glove1 : this.glove2;
    }
    
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(final ItemStack p_82790_1_, final int pass) {
        final int dmg = p_82790_1_.getItemDamage();
        return XUHelper.dyeCols[getColIndex(pass, dmg)];
    }
    
    public static int getColIndex(final int pass, int dmg) {
        if (isInvalidDamage(dmg)) {
            dmg = ItemGlove.genericDmg;
        }
        if (pass == 0) {
            return dmg & 0xF;
        }
        return dmg >> 4 & 0xF;
    }
    
    public static boolean isInvalidDamage(final int dmg) {
        return dmg < 0 || dmg > 255;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public int getRenderPasses(final int metadata) {
        return 2;
    }
    
    public String getUnlocalizedName(final ItemStack p_77667_1_) {
        if (isInvalidDamage(p_77667_1_.getItemDamage())) {
            return super.getUnlocalizedName(p_77667_1_) + ".english";
        }
        return super.getUnlocalizedName(p_77667_1_);
    }
    
    @SubscribeEvent
    public void repair(final AnvilUpdateEvent event) {
    }
    
    @SubscribeEvent
    public void attack(final PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            return;
        }
        final EntityPlayer player = event.entityPlayer;
        if (event.world == null || event.world.isRemote) {
            return;
        }
        if (player == null || !(player instanceof EntityPlayerMP)) {
            return;
        }
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || heldItem.getItem() != this) {
            return;
        }
        if (heldItem.getItemDamage() == 32767) {
            heldItem.stackSize = 0;
            return;
        }
        final int i = player.inventory.currentItem;
        if (i >= 9 || i < 0) {
            return;
        }
        event.setCanceled(true);
        final ItemStack item = heldItem.copy();
        heldItem.setItemDamage(32767);
        player.inventory.setInventorySlotContents(i, (ItemStack)null);
        final int x = event.x;
        final int y = event.y;
        final int z = event.z;
        final int side = event.face;
        try {
            final PlayerInteractEvent e = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.LEFT_CLICK_BLOCK, x, y, z, side, player.worldObj);
            final boolean result = !e.isCanceled() && event.useBlock != Event.Result.DENY;
            if (result) {
                final Block block = event.world.getBlock(x, y, z);
                block.onBlockClicked(event.world, x, y, z, player);
            }
        }
        catch (Exception err) {
            for (int j = 0; j < player.inventory.getSizeInventory(); ++j) {
                final ItemStack stackInSlot = player.inventory.getStackInSlot(i);
                if (stackInSlot != null) {
                    if (stackInSlot.getItem() == this) {
                        if (stackInSlot.getItemDamage() == 32767) {
                            player.inventory.setInventorySlotContents(j, (ItemStack)null);
                        }
                    }
                }
            }
            if (player.inventory.getStackInSlot(i) == null) {
                player.inventory.setInventorySlotContents(i, item);
            }
            else if (!player.inventory.addItemStackToInventory(item)) {
                player.dropPlayerItemWithRandomChoice(item, false);
            }
            throw Throwables.propagate((Throwable)err);
        }
        ((EntityPlayerMP)player).playerNetServerHandler.sendPacket((Packet)new S23PacketBlockChange(x, y, z, event.world));
        final ItemStack newItem = player.inventory.getStackInSlot(i);
        player.inventory.setInventorySlotContents(i, item);
        if (newItem != null && !player.inventory.addItemStackToInventory(newItem.copy())) {
            player.dropPlayerItemWithRandomChoice(newItem.copy(), false);
        }
        ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
    }
    
    public boolean onItemUseFirst(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        if (player == null) {
            return false;
        }
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null || heldItem.getItem() != this) {
            return false;
        }
        if (ExtraUtilsMod.proxy.isAltSneaking()) {
            if (world.isRemote) {
                ExtraUtilsMod.proxy.sendAltUsePacket(x, y, z, side, stack, hitX, hitY, hitZ);
                return true;
            }
            return false;
        }
        else {
            if (heldItem.getItemDamage() == 32767) {
                heldItem.stackSize = 0;
                return true;
            }
            final int i = player.inventory.currentItem;
            if (i >= 9 || i < 0) {
                return false;
            }
            final ItemStack item = heldItem.copy();
            heldItem.setItemDamage(32767);
            player.inventory.setInventorySlotContents(i, (ItemStack)null);
            try {
                final PlayerInteractEvent event = ForgeEventFactory.onPlayerInteract(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, side, player.worldObj);
                final boolean result = !event.isCanceled() && event.useBlock != Event.Result.DENY;
                if (result) {
                    final Block block = world.getBlock(x, y, z);
                    block.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
                }
            }
            catch (Exception err) {
                for (int j = 0; j < player.inventory.getSizeInventory(); ++j) {
                    final ItemStack stackInSlot = player.inventory.getStackInSlot(i);
                    if (stackInSlot != null) {
                        if (stackInSlot.getItem() == this) {
                            if (stackInSlot.getItemDamage() == 32767) {
                                player.inventory.setInventorySlotContents(j, (ItemStack)null);
                            }
                        }
                    }
                }
                if (player.inventory.getStackInSlot(i) == null) {
                    player.inventory.setInventorySlotContents(i, item);
                }
                else if (!player.inventory.addItemStackToInventory(item)) {
                    player.dropPlayerItemWithRandomChoice(item, false);
                }
                throw Throwables.propagate((Throwable)err);
            }
            final ItemStack newItem = player.inventory.getStackInSlot(i);
            player.inventory.setInventorySlotContents(i, item);
            if (newItem != null && !player.inventory.addItemStackToInventory(newItem.copy())) {
                player.dropPlayerItemWithRandomChoice(newItem.copy(), false);
            }
            if (player.worldObj.isRemote) {
                ExtraUtilsMod.proxy.sendUsePacket(x, y, z, side, item, hitX, hitY, hitZ);
            }
            else {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
                }
                player.openContainer.detectAndSendChanges();
            }
            return true;
        }
    }
    
    public boolean doesSneakBypassUse(final World world, final int x, final int y, final int z, final EntityPlayer player) {
        return true;
    }
    
    public Multimap getAttributeModifiers(final ItemStack stack) {
        final Multimap multimap = (Multimap)HashMultimap.create();
        multimap.put((Object)ItemGlove.woolProtection.getAttributeUnlocalizedName(), (Object)new AttributeModifier(this.freezeUUID, "Weapon modifier", 0.001, 0));
        return multimap;
    }
    
    public EnumAction getItemUseAction(final ItemStack p_77661_1_) {
        return EnumAction.none;
    }
    
    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World p_77659_2_, final EntityPlayer p_77659_3_) {
        return p_77659_1_;
    }
    
    public boolean hitEntity(final ItemStack p_77644_1_, final EntityLivingBase p_77644_2_, final EntityLivingBase p_77644_3_) {
        return true;
    }
    
    public boolean onBlockDestroyed(final ItemStack p_150894_1_, final World p_150894_2_, final Block p_150894_3_, final int p_150894_4_, final int p_150894_5_, final int p_150894_6_, final EntityLivingBase p_150894_7_) {
        return true;
    }
    
    public int getMaxItemUseDuration(final ItemStack p_77626_1_) {
        return 0;
    }
    
    public int getItemEnchantability() {
        return 2;
    }
    
    public boolean isItemTool(final ItemStack p_77616_1_) {
        return true;
    }
    
    public boolean getIsRepairable(final ItemStack p_82789_1_, final ItemStack p_82789_2_) {
        return false;
    }
    
    static {
        (ItemGlove.materialWool = EnumHelper.addToolMaterial("wool", 0, 0, 0.0f, 0.0f, 0)).setRepairItem(new ItemStack(Blocks.wool));
        ItemGlove.genericDmg = 0;
        ItemGlove.woolProtection = (BaseAttribute)new RangedAttribute("extrautils.freezeProtection", 0.0, -1.7976931348623157E308, Double.MAX_VALUE);
    }
}

