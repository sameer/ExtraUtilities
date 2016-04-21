// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import java.util.Locale;
import com.rwtema.extrautils.crafting.RecipeUnstableCrafting;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import com.rwtema.extrautils.damgesource.DamageSourceDivByZero;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public class ItemUnstableIngot extends Item implements IItemMultiTransparency
{
    public static final int numTickstilDestruction = 200;
    private IIcon[] iconIngot;
    private IIcon[] iconNugget;
    
    public ItemUnstableIngot() {
        this.iconIngot = new IIcon[2];
        this.iconNugget = new IIcon[2];
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.maxStackSize = 1;
        this.setUnlocalizedName("extrautils:unstableingot");
        this.setHasSubtypes(true);
    }
    
    public int getItemStackLimit(final ItemStack item) {
        if (item != null) {
            if (item.getItem() == ExtraUtils.unstableIngot && item.getItemDamage() == 0 && item.hasTagCompound()) {
                if (!item.getTagCompound().hasKey("stable")) {
                    return 1;
                }
                if (item.getTagCompound().hasKey("time")) {
                    return 1;
                }
            }
            return 64;
        }
        return 1;
    }
    
    public static void explode(final EntityPlayer player) {
        stripPlayerOfIngots(player);
        if (ExtraUtils.unstableIngotExplosion) {
            player.worldObj.createExplosion((Entity)player, player.posX, player.posY, player.posZ, 1.0f, false);
            player.attackEntityFrom((DamageSource)DamageSourceDivByZero.divbyzero, 32767.0f);
        }
    }
    
    public static void stripPlayerOfIngots(final EntityPlayer player) {
        if (player != null && player.inventory != null) {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                final ItemStack item = player.inventory.getStackInSlot(i);
                if (item != null && item.getItem() == ExtraUtils.unstableIngot && item.hasTagCompound() && (item.getTagCompound().hasKey("crafting") || item.getTagCompound().hasKey("time"))) {
                    player.inventory.setInventorySlotContents(i, (ItemStack)null);
                }
            }
            final ItemStack item2 = player.inventory.getItemStack();
            if (item2 != null && item2.getItem() == ExtraUtils.unstableIngot && item2.getItemDamage() == 0 && item2.hasTagCompound() && (item2.getTagCompound().hasKey("crafting") || item2.getTagCompound().hasKey("time"))) {
                player.inventory.setItemStack((ItemStack)null);
            }
            player.inventory.markDirty();
            updatePlayer((Entity)player);
        }
    }
    
    public static void updatePlayer(final Entity player) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP)player);
        }
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }
    
    public static boolean isStable(final ItemStack item) {
        return item.getItemDamage() == 2 || (item.hasTagCompound() && item.getTagCompound().hasKey("stable"));
    }
    
    public static boolean isSuperStable(final ItemStack item) {
        return item.getItemDamage() == 2 || (item.hasTagCompound() && item.getTagCompound().hasKey("superstable"));
    }
    
    public void onUpdate(final ItemStack item, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {
        if (item.stackSize == 0) {
            return;
        }
        if (par2World.isRemote) {
            return;
        }
        if (!(par3Entity instanceof EntityPlayer)) {
            return;
        }
        final EntityPlayer player = (EntityPlayer)par3Entity;
        if (item.getItemDamage() > 0) {
            return;
        }
        if (item.hasTagCompound()) {
            final boolean deleteIngot = false;
            final boolean explode = false;
            if (item.getTagCompound().hasKey("creative") || isStable(item)) {
                return;
            }
            if (item.getTagCompound().hasKey("bug")) {
                item.getTagCompound().removeTag("bug");
                item.getTagCompound().setBoolean("bug_show", true);
                return;
            }
            if (item.getTagCompound().hasKey("crafting") && player.openContainer != null) {
                if (player.openContainer.getClass() != ContainerPlayer.class) {
                    this.addTimeStamp(item, par2World);
                    return;
                }
                stripPlayerOfIngots(player);
            }
            else {
                if (player.openContainer != null && player.openContainer.getClass() == ContainerPlayer.class) {
                    explode(player);
                }
                if (item.getTagCompound().hasKey("time") && item.getTagCompound().hasKey("dimension")) {
                    final float t = (200L - (par2World.getTotalWorldTime() - item.getTagCompound().getLong("time"))) / 20.0f;
                    if (par3Entity.worldObj.provider.dimensionId != item.getTagCompound().getInteger("dimension") | t < 0.0f) {
                        if (par3Entity.worldObj.provider.dimensionId == item.getTagCompound().getInteger("dimension")) {
                            explode(player);
                        }
                        else {
                            stripPlayerOfIngots(player);
                        }
                    }
                }
            }
        }
    }
    
    public void addTimeStamp(final ItemStack item, final World world) {
        final NBTTagCompound ts = new NBTTagCompound();
        if (ts.hasKey("crafting")) {
            ts.removeTag("crafting");
        }
        if (item.getItemDamage() > 0) {
            return;
        }
        ts.setInteger("dimension", world.provider.dimensionId);
        ts.setLong("time", world.getTotalWorldTime());
        item.setTagCompound(ts);
    }
    
    public void onCreated(final ItemStack par1ItemStack, final World par2World, final EntityPlayer par3EntityPlayer) {
        if (isStable(par1ItemStack)) {
            return;
        }
        if (par1ItemStack.getItemDamage() > 0) {
            return;
        }
        this.addTimeStamp(par1ItemStack, par2World);
        if (!par2World.isRemote) {
            updatePlayer((Entity)par3EntityPlayer);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        final NBTTagCompound tag = par1ItemStack.getTagCompound();
        if (tag != null && (tag == RecipeUnstableCrafting.nbt || tag.getBoolean("isNEI"))) {
            par1ItemStack = new ItemStack((Item)this, 1, 0);
        }
        if (par1ItemStack.getItemDamage() > 0) {
            return;
        }
        if (isStable(par1ItemStack)) {
            par3List.add("Stable");
            return;
        }
        if (par1ItemStack.hasTagCompound() && !par1ItemStack.getTagCompound().hasKey("crafting") && !par1ItemStack.getTagCompound().hasKey("creative") && !par1ItemStack.getTagCompound().hasKey("bug")) {
            if (par1ItemStack.getTagCompound().hasKey("dimension") && par2EntityPlayer.worldObj.provider.dimensionId == par1ItemStack.getTagCompound().getInteger("dimension")) {
                float t = (200L - (par2EntityPlayer.worldObj.getTotalWorldTime() - par1ItemStack.getTagCompound().getLong("time"))) / 20.0f;
                if (t < 0.0f) {
                    t = 0.0f;
                }
                par3List.add("Explodes in " + String.format(Locale.ENGLISH, "%.1f", t) + " seconds");
            }
            else if (par1ItemStack.getTagCompound().hasKey("bug_show")) {
                par3List.add("This ingot was created incorrectly");
                par3List.add("using getRecipeOutput() instead of getCraftingResult()");
                par3List.add("if this ingot was made legitimately please");
                par3List.add("report this to the mod developer.");
                par3List.add("(don't spam them though - check to see if");
                par3List.add("it hasn't already been reported)");
            }
        }
        else {
            par3List.add("ERROR: Divide by diamond");
            par3List.add("This ingot is highly unstable and will explode");
            par3List.add("after 10 seconds.");
            par3List.add("Will also explode if the crafting window is closed");
            par3List.add("or the ingot is thrown on the ground.");
            par3List.add("Additionally these ingots do not stack");
            par3List.add(" - Do not craft unless ready -");
            par3List.add("");
            par3List.add("Must be crafted in a vanilla crafting table.");
            if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("creative")) {
                par3List.add("");
                par3List.add("Creative Spawned - Stable");
            }
        }
    }
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return true;
    }
    
    public Entity createEntity(final World world, final Entity location, final ItemStack itemstack) {
        if (location instanceof EntityItem && itemstack.hasTagCompound() && (itemstack.getTagCompound().hasKey("crafting") || itemstack.getTagCompound().hasKey("time"))) {
            ((EntityItem)location).age = 1;
            location.setDead();
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("creative", true);
        ItemStack item = new ItemStack(par1, 1, 0);
        item.setTagCompound(tag);
        par3List.add(item);
        item = new ItemStack(par1, 1, 1);
        par3List.add(item);
        par3List.add(new ItemStack(par1, 1, 2));
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        final IIcon[] iconIngot = this.iconIngot;
        final int n = 0;
        final IIcon registerIcon = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5));
        iconIngot[n] = registerIcon;
        this.itemIcon = registerIcon;
        this.iconIngot[1] = par1IIconRegister.registerIcon(this.getUnlocalizedName().substring(5) + "1");
        this.iconNugget[0] = par1IIconRegister.registerIcon("extrautils:unstablenugget");
        this.iconNugget[1] = par1IIconRegister.registerIcon("extrautils:unstablenugget1");
    }
    
    public int numIcons(final ItemStack item) {
        return 2;
    }
    
    public IIcon getIconForTransparentRender(final ItemStack item, final int pass) {
        if (item.getItemDamage() == 1) {
            return this.iconNugget[pass];
        }
        return this.iconIngot[pass];
    }
    
    public float getIconTransparency(final ItemStack item, final int pass) {
        if (pass == 1) {
            return 0.5f;
        }
        return 1.0f;
    }
}


