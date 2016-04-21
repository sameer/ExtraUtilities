// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.Entity;
import com.rwtema.extrautils.network.NetworkHandler;
import net.minecraft.entity.item.EntityItem;
import java.util.Collection;
import cofh.api.block.IDismantleable;
import com.rwtema.extrautils.EventHandlerEntityItemStealer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.IIcon;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;

public class ItemPrecisionShears extends ItemShears implements IItemMultiTransparency
{
    public static final Item[] toolsToMimic;
    public static final ItemStack[] toolStacks;
    public static final int[] COOLDOWN;
    public Random rand;
    private IIcon[] icons;
    
    public static int getCooldown(final ItemStack stack) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
        if (i < 0) {
            i = 0;
        }
        if (i >= ItemPrecisionShears.COOLDOWN.length) {
            i = ItemPrecisionShears.COOLDOWN.length - 1;
        }
        return ItemPrecisionShears.COOLDOWN[i];
    }
    
    public ItemPrecisionShears() {
        this.rand = XURandom.getInstance();
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:shears");
        this.setMaxStackSize(1);
        this.setMaxDamage(1024);
    }
    
    public int cofh_canEnchantApply(final ItemStack stack, final Enchantment ench) {
        return (ench.type == EnumEnchantmentType.digger) ? 1 : -1;
    }
    
    public int getItemEnchantability() {
        return Items.iron_pickaxe.getItemEnchantability();
    }
    
    public boolean isItemTool(final ItemStack p_77616_1_) {
        return p_77616_1_.stackSize == 1;
    }
    
    public boolean onBlockStartBreak(final ItemStack itemstack, final int x, final int y, final int z, final EntityPlayer player) {
        final World worldObj = player.worldObj;
        if (worldObj.isRemote) {
            return false;
        }
        final Block block = worldObj.getBlock(x, y, z);
        final int meta = worldObj.getBlockMetadata(x, y, z);
        worldObj.playAuxSFXAtEntity(player, 2001, x, y, z, Block.getIdFromBlock(block) + (worldObj.getBlockMetadata(x, y, z) << 12));
        final boolean flag1 = block.canHarvestBlock(player, meta);
        if (itemstack != null) {
            itemstack.func_150999_a(worldObj, block, x, y, z, player);
            if (itemstack.stackSize == 0) {
                player.destroyCurrentEquippedItem();
            }
        }
        List<EntityItem> extraDrops = null;
        List<EntityItem> baseCapturedDrops = null;
        EventHandlerEntityItemStealer.startCapture();
        if (block instanceof IDismantleable && ((IDismantleable)block).canDismantle(player, worldObj, x, y, z)) {
            ((IDismantleable)block).dismantleBlock(player, worldObj, x, y, z, false);
        }
        else {
            block.onBlockHarvested(worldObj, x, y, z, meta, player);
            if (block.removedByPlayer(worldObj, player, x, y, z, true)) {
                block.onBlockDestroyedByPlayer(worldObj, x, y, z, meta);
                if (flag1 || player.capabilities.isCreativeMode) {
                    extraDrops = EventHandlerEntityItemStealer.getCapturedEntities();
                    EventHandlerEntityItemStealer.startCapture();
                    block.harvestBlock(worldObj, player, x, y, z, meta);
                    baseCapturedDrops = EventHandlerEntityItemStealer.getCapturedEntities();
                }
            }
        }
        EventHandlerEntityItemStealer.stopCapture();
        boolean added = false;
        if (baseCapturedDrops == null) {
            baseCapturedDrops = EventHandlerEntityItemStealer.getCapturedEntities();
        }
        if (extraDrops != null) {
            baseCapturedDrops.addAll(extraDrops);
        }
        for (final EntityItem j : baseCapturedDrops) {
            if (player.inventory.addItemStackToInventory(j.getEntityItem())) {
                added = true;
                NetworkHandler.sendParticle(worldObj, "reddust", j.posX, j.posY, j.posZ, 0.5 + this.rand.nextDouble() * 0.15, 0.35, 0.65 + this.rand.nextDouble() * 0.3, false);
            }
            if (j.getEntityItem() != null && j.getEntityItem().stackSize > 0) {
                worldObj.spawnEntityInWorld((Entity)new EntityItem(j.worldObj, j.posX, j.posY, j.posZ, j.getEntityItem()));
            }
        }
        if (added) {
            for (int i = 0; i < 10; ++i) {
                NetworkHandler.sendParticle(worldObj, "reddust", x + this.rand.nextDouble(), y + this.rand.nextDouble(), z + this.rand.nextDouble(), 0.5 + this.rand.nextDouble() * 0.15, 0.35, 0.65 + this.rand.nextDouble() * 0.3, false);
            }
            ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
        }
        return true;
    }
    
    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int par7, final float par8, final float par9, final float par10) {
        if (!player.isSneaking()) {
            return false;
        }
        if (!this.check(par1ItemStack, world)) {
            return true;
        }
        if (world.isAirBlock(x, y, z)) {
            return false;
        }
        final Block block = world.getBlock(x, y, z);
        final int meta = world.getBlockMetadata(x, y, z);
        if (block.getBlockHardness(world, x, y, z) < 0.0f && (!(block instanceof IDismantleable) || !((IDismantleable)block).canDismantle(player, world, x, y, z))) {
            return false;
        }
        if (!block.canHarvestBlock(player, meta)) {
            return false;
        }
        player.swingItem();
        if (world.isRemote || !(player instanceof EntityPlayerMP)) {
            return true;
        }
        if (!this.check(par1ItemStack, world)) {
            return true;
        }
        if (!world.isAirBlock(x, y, z) && block.getBlockHardness(world, x, y, z) >= 0.0f) {
            ((EntityPlayerMP)player).theItemInWorldManager.tryHarvestBlock(x, y, z);
        }
        return true;
    }
    
    private void collectItems(final World world, final EntityPlayer player, final double x, final double y, final double z, final List before, final List after) {
        final Iterator iter = after.iterator();
        boolean added = false;
        while (iter.hasNext()) {
            final EntityItem j = iter.next();
            if (j.getClass() == EntityItem.class && !before.contains(j) && player.inventory.addItemStackToInventory(j.getEntityItem())) {
                NetworkHandler.sendParticle(world, "reddust", j.posX, j.posY, j.posZ, 0.5 + this.rand.nextDouble() * 0.15, 0.35, 0.65 + this.rand.nextDouble() * 0.3, false);
                added = true;
                if (j.getEntityItem() != null && j.getEntityItem().stackSize != 0) {
                    continue;
                }
                j.setDead();
            }
        }
        if (added) {
            for (int i = 0; i < 10; ++i) {
                NetworkHandler.sendParticle(world, "reddust", x + this.rand.nextDouble(), y + this.rand.nextDouble(), z + this.rand.nextDouble(), 0.5 + this.rand.nextDouble() * 0.15, 0.35, 0.65 + this.rand.nextDouble() * 0.3, false);
            }
            ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
        }
    }
    
    public boolean itemInteractionForEntity(final ItemStack itemstack, final EntityPlayer player, final EntityLivingBase entity) {
        final AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.posX, entity.posY, entity.posZ).offset(0.5, 0.5, 0.5).expand(3.0, 3.0, 3.0);
        final List items = player.worldObj.getEntitiesWithinAABB((Class)EntityItem.class, aabb);
        final boolean sheared = super.itemInteractionForEntity(itemstack, player, entity);
        if (sheared) {
            this.collectItems(player.worldObj, player, entity.posX - 0.5, entity.posY - 0.5, entity.posZ - 0.5, items, player.worldObj.getEntitiesWithinAABB((Class)EntityItem.class, aabb));
        }
        return sheared;
    }
    
    public boolean canHarvestBlock(final Block par1Block, final ItemStack item) {
        for (final Item tool : ItemPrecisionShears.toolsToMimic) {
            if (tool.canHarvestBlock(par1Block, new ItemStack(tool))) {
                return true;
            }
        }
        return false;
    }
    
    public float func_150893_a(final ItemStack stack, final Block block) {
        for (final ItemStack tool : ItemPrecisionShears.toolStacks) {
            if (ForgeHooks.isToolEffective(tool, block, 0)) {
                return 4.0f;
            }
        }
        return super.func_150893_a(stack, block);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        this.icons = new IIcon[2];
        final IIcon[] icons = this.icons;
        final int n = 0;
        final IIcon registerIcon = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5));
        icons[n] = registerIcon;
        this.itemIcon = registerIcon;
        this.icons[1] = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5) + "1");
    }
    
    @SideOnly(Side.CLIENT)
    public int numIcons(final ItemStack item) {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.worldObj != null && !this.check(item, Minecraft.getMinecraft().thePlayer.worldObj)) {
            return 1;
        }
        return 2;
    }
    
    public IIcon getIconForTransparentRender(final ItemStack item, final int pass) {
        return this.icons[pass];
    }
    
    public float getIconTransparency(final ItemStack item, final int pass) {
        if (pass == 1) {
            return 0.5f;
        }
        return 1.0f;
    }
    
    public boolean onBlockDestroyed(final ItemStack itemstack, final World par2World, final Block par3, final int par4, final int par5, final int par6, final EntityLivingBase par7EntityLivingBase) {
        itemstack.damageItem(1, par7EntityLivingBase);
        NBTTagCompound tag = itemstack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
        }
        tag.setInteger("dim", par2World.provider.dimensionId);
        tag.setLong("time", par2World.getTotalWorldTime());
        itemstack.setTagCompound(tag);
        return true;
    }
    
    public boolean check(final ItemStack itemstack, final World world) {
        if (!itemstack.hasTagCompound()) {
            return true;
        }
        if (!itemstack.getTagCompound().hasKey("dim") && !itemstack.getTagCompound().hasKey("time")) {
            return true;
        }
        final long totalWorldTime = world.getTotalWorldTime();
        final long time = itemstack.getTagCompound().getLong("time") + getCooldown(itemstack);
        if (itemstack.getTagCompound().getInteger("dim") != world.provider.dimensionId || time < totalWorldTime) {
            if (!world.isRemote) {
                itemstack.getTagCompound().removeTag("dim");
                itemstack.getTagCompound().removeTag("time");
                if (itemstack.getTagCompound().hasNoTags()) {
                    itemstack.setTagCompound((NBTTagCompound)null);
                }
            }
            return true;
        }
        return false;
    }
    
    public void onUpdate(final ItemStack itemstack, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {
        this.check(itemstack, par2World);
    }
    
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        if (itemstack.hasTagCompound()) {
            itemstack.getTagCompound().removeTag("dim");
            itemstack.getTagCompound().removeTag("time");
            if (itemstack.getTagCompound().hasNoTags()) {
                itemstack.setTagCompound((NBTTagCompound)null);
            }
        }
        return null;
    }
    
    static {
        toolsToMimic = new Item[] { Items.stone_pickaxe, Items.stone_axe, Items.stone_shovel, Items.stone_sword, Items.stone_hoe, Items.shears };
        toolStacks = new ItemStack[ItemPrecisionShears.toolsToMimic.length];
        for (int i = 0; i < ItemPrecisionShears.toolsToMimic.length; ++i) {
            ItemPrecisionShears.toolStacks[i] = new ItemStack(ItemPrecisionShears.toolsToMimic[i]);
        }
        COOLDOWN = new int[] { 20, 16, 12, 8, 4, 0 };
    }
}


