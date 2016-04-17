// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import com.rwtema.extrautils.network.packets.PacketTempChat;
import net.minecraft.util.ChatComponentText;
import com.rwtema.extrautils.ExtraUtilsMod;
import net.minecraft.world.World;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.FluidStack;
import com.rwtema.extrautils.item.filters.Matcher;
import com.rwtema.extrautils.item.filters.AdvancedNodeUpgrades;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraftforge.fluids.FluidTank;
import com.rwtema.extrautils.tileentity.transfernodes.nodebuffer.INodeBuffer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import cofh.api.item.ISpecialFilterFluid;
import cofh.api.item.ISpecialFilterItem;
import com.rwtema.extrautils.ICreativeTabSorting;
import net.minecraft.item.Item;

public class ItemNodeUpgrade extends Item implements ICreativeTabSorting, ISpecialFilterItem, ISpecialFilterFluid
{
    private static final int numUpgrades = 11;
    private IIcon[] icons;
    
    public ItemNodeUpgrade() {
        this.icons = new IIcon[11];
        this.setHasSubtypes(true);
        this.setUnlocalizedName("extrautils:nodeUpgrade");
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
    }
    
    public static boolean hasKey(final ItemStack filter, final String key) {
        if (filter != null) {
            final NBTTagCompound tags = filter.getTagCompound();
            if (tags != null && tags.hasKey(key)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean getPolarity(final ItemStack filter) {
        return hasKey(filter, "Inverted");
    }
    
    public static boolean getFuzzyMetadata(final ItemStack filter) {
        return hasKey(filter, "FuzzyMeta");
    }
    
    public static boolean getFuzzyNBT(final ItemStack filter) {
        return hasKey(filter, "FuzzyNBT");
    }
    
    public static boolean matchesFilterBuffer(final INodeBuffer item, final ItemStack filter) {
        if (item == null) {
            return false;
        }
        final Object buffer = item.getBuffer();
        if (buffer == null) {
            return false;
        }
        if (buffer instanceof ItemStack) {
            return matchesFilterItem((ItemStack)buffer, filter);
        }
        return !(buffer instanceof FluidTank) || matchesFilterLiquid(((FluidTank)buffer).getFluid(), filter);
    }
    
    public static boolean isFilter(final ItemStack filter) {
        if (filter == null) {
            return false;
        }
        if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade) {
            return filter.getItemDamage() == 1 || filter.getItemDamage() == 10;
        }
        return filter.getItem() instanceof ISpecialFilterItem;
    }
    
    public static boolean matchesFilterItem(final ItemStack item, final ItemStack filter) {
        if (item == null || item.getItem() == null || filter == null) {
            return false;
        }
        if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade) {
            if (filter.getItemDamage() == 1) {
                final boolean polarity = !getPolarity(filter);
                final boolean fuzzyMeta = getFuzzyMetadata(filter);
                final boolean fuzzyNBT = getFuzzyNBT(filter);
                final NBTTagCompound tags = filter.getTagCompound();
                if (tags != null) {
                    for (int i = 0; i < 9; ++i) {
                        if (tags.hasKey("items_" + i)) {
                            final ItemStack f = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("items_" + i));
                            if (f != null) {
                                if (XUHelper.canItemsStack(f, item, fuzzyMeta, true, fuzzyNBT)) {
                                    return polarity;
                                }
                                if (isFilter(f) && matchesFilterItem(item, f)) {
                                    return polarity;
                                }
                            }
                        }
                    }
                }
                return !polarity;
            }
            if (filter.getItemDamage() == 10) {
                final Matcher matcher = AdvancedNodeUpgrades.getMatcher(filter);
                return matcher.matchItem(item) != getPolarity(filter);
            }
        }
        if (filter.getItem() instanceof ISpecialFilterItem) {
            return ((ISpecialFilterItem)filter.getItem()).matchesItem(filter, item);
        }
        return XUHelper.canItemsStack(item, filter, false, true);
    }
    
    public static boolean matchesFilterLiquid(final FluidStack fluid, final ItemStack filter) {
        if (fluid == null || filter == null) {
            return false;
        }
        if (ExtraUtils.nodeUpgrade != null && filter.getItem() == ExtraUtils.nodeUpgrade) {
            if (filter.getItemDamage() == 1) {
                final boolean polarity = !getPolarity(filter);
                final NBTTagCompound tags = filter.getTagCompound();
                if (tags != null) {
                    for (int i = 0; i < 9; ++i) {
                        if (tags.hasKey("items_" + i)) {
                            final ItemStack f = ItemStack.loadItemStackFromNBT(tags.getCompoundTag("items_" + i));
                            if (f != null) {
                                if (fluid.isFluidEqual(f)) {
                                    return polarity;
                                }
                                if (isFilter(f) && matchesFilterLiquid(fluid, f)) {
                                    return polarity;
                                }
                            }
                        }
                    }
                }
                return !polarity;
            }
            if (filter.getItemDamage() == 10) {
                final Matcher matcher = AdvancedNodeUpgrades.getMatcher(filter);
                return matcher.matchFluid(fluid) != getPolarity(filter);
            }
        }
        if (filter.getItem() instanceof ISpecialFilterFluid) {
            ((ISpecialFilterFluid)filter.getItem()).matchesFluid(filter, fluid);
        }
        return fluid.isFluidEqual(filter);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        this.icons[0] = par1IIconRegister.registerIcon("extrautils:nodeUpgrade");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:filter");
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeMining");
        this.icons[3] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeStack");
        this.icons[4] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeCreative");
        this.icons[5] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeEnder");
        this.icons[6] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeEnderReceiver");
        this.icons[7] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeDepth");
        this.icons[8] = par1IIconRegister.registerIcon("extrautils:nodeUpgradeBreadth");
        this.icons[9] = par1IIconRegister.registerIcon("extrautils:nodeUpgradePatience");
        this.icons[10] = par1IIconRegister.registerIcon("extrautils:filter2");
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        return this.icons[par1 % 11];
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        for (int i = 0; i < 11; ++i) {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack item, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        if (item.getItemDamage() == 1) {
            if (item.getTagCompound() != null) {
                if (getPolarity(item)) {
                    par3List.add("Inverted");
                }
                if (getFuzzyMetadata(item)) {
                    par3List.add("Fuzzy - Ignores Metadata");
                }
                if (getFuzzyNBT(item)) {
                    par3List.add("Fuzzy - Ignores NBT");
                }
                for (int i = 0; i < 9; ++i) {
                    if (item.getTagCompound().hasKey("items_" + i)) {
                        final ItemStack temp = ItemStack.loadItemStackFromNBT(item.getTagCompound().getCompoundTag("items_" + i));
                        final List tempList = temp.getTooltip(par2EntityPlayer, false);
                        for (int j = 0; j < tempList.size(); ++j) {
                            if (j == 0) {
                                par3List.add("  " + tempList.get(j));
                            }
                            else {
                                par3List.add("     " + tempList.get(j));
                            }
                        }
                        tempList.clear();
                    }
                }
            }
            else {
                par3List.add(EnumChatFormatting.ITALIC + "Right click to select items to filter" + EnumChatFormatting.RESET);
                par3List.add(EnumChatFormatting.ITALIC + "Filters can be placed within other filters to create advanced behaviours" + EnumChatFormatting.RESET);
                par3List.add(EnumChatFormatting.ITALIC + "Craft with" + EnumChatFormatting.RESET);
            }
        }
        if (item.getItemDamage() == 10) {
            final Matcher matcher = AdvancedNodeUpgrades.getMatcher(item);
            par3List.add("Filter Program: " + matcher.getLocalizedName());
            if (getPolarity(item)) {
                par3List.add("Inverted");
            }
            else if (matcher == AdvancedNodeUpgrades.nullMatcher) {
                par3List.add(EnumChatFormatting.ITALIC + "Right-click to change Filter Program" + EnumChatFormatting.RESET);
                par3List.add(EnumChatFormatting.ITALIC + "Craft with a redstone torch to Invert" + EnumChatFormatting.RESET);
                par3List.add(EnumChatFormatting.ITALIC + "Can be placed in normal filters to create advanced behaviours" + EnumChatFormatting.RESET);
            }
        }
        if (item.getItemDamage() == 5 || item.getItemDamage() == 6) {
            par3List.set(0, par3List.get(0).replaceFirst(EnumChatFormatting.ITALIC + item.getDisplayName() + EnumChatFormatting.RESET, this.getItemStackDisplayName(item)));
            if (!item.hasDisplayName()) {
                par3List.add("Unspecified Frequency: You must name this upgrade in an anvil to choose a frequency");
                par3List.add("You cannot use this upgrade until it has a frequency");
            }
            else {
                par3List.add("Frequency: " + item.getDisplayName());
                final String s = XUHelper.getPlayerOwner(item);
                if (s.equals("")) {
                    par3List.add("Public Spectrum");
                }
                else {
                    par3List.add("Private Spectrum - " + s);
                }
            }
        }
    }
    
    public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World, final EntityPlayer par3EntityPlayer) {
        if (!par2World.isRemote) {
            switch (par1ItemStack.getItemDamage()) {
                case 1: {
                    par3EntityPlayer.openGui((Object)ExtraUtilsMod.instance, 1, par2World, par3EntityPlayer.inventory.currentItem, 0, 0);
                    break;
                }
                case 5:
                case 6: {
                    if (XUHelper.getPlayerOwner(par1ItemStack).equals("")) {
                        PacketTempChat.sendChat(par3EntityPlayer, (IChatComponent)new ChatComponentText("Spectrum set to private"));
                        XUHelper.setPlayerOwner(par1ItemStack, par3EntityPlayer.getGameProfile().getName());
                        break;
                    }
                    PacketTempChat.sendChat(par3EntityPlayer, (IChatComponent)new ChatComponentText("Spectrum set to public"));
                    XUHelper.setPlayerOwner(par1ItemStack, "");
                    break;
                }
                case 10: {
                    final Matcher matcher = AdvancedNodeUpgrades.nextEntry(par1ItemStack, !par3EntityPlayer.isSneaking());
                    PacketTempChat.sendChat(par3EntityPlayer, new ChatComponentText("Filter Program: ").appendSibling((IChatComponent)new ChatComponentTranslation(matcher.unlocalizedName, new Object[0])));
                    break;
                }
            }
        }
        return par1ItemStack;
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        return super.getUnlocalizedName(par1ItemStack) + "." + par1ItemStack.getItemDamage();
    }
    
    public String getSortingName(final ItemStack item) {
        if (item.getItemDamage() == 1) {
            return item.getDisplayName();
        }
        final ItemStack i = item.copy();
        i.setItemDamage(-1);
        return i.getDisplayName() + item.getDisplayName();
    }
    
    public boolean matchesItem(final ItemStack filter, final ItemStack item) {
        return matchesFilterItem(item, filter);
    }
    
    public boolean matchesFluid(final ItemStack filter, final FluidStack fluidstack) {
        return matchesFilterLiquid(fluidstack, filter);
    }
}
