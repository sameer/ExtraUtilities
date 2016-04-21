// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.texture.IIconRegister;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.item.ItemSword;

public class ItemEthericSword extends ItemSword implements IItemMultiTransparency
{
    private double weaponDamage;
    private IIcon[] icons;
    
    public ItemEthericSword() {
        super(Item.ToolMaterial.IRON);
        this.maxStackSize = 1;
        this.setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses());
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:ethericsword");
        this.weaponDamage = 8.0;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return false;
    }
    
    public float func_150893_a(final ItemStack par1ItemStack, final Block par2Block) {
        if (par2Block == Blocks.web) {
            return 15.0f;
        }
        final Material var3 = par2Block.getMaterial();
        return (var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.plants) ? 1.0f : 1.5f;
    }
    
    public boolean hitEntity(final ItemStack par1ItemStack, final EntityLivingBase par2EntityLiving, final EntityLivingBase par3EntityLiving) {
        return true;
    }
    
    public boolean onBlockDestroyed(final ItemStack par1ItemStack, final World par2World, final Block par3, final int par4, final int par5, final int par6, final EntityLivingBase par7EntityLiving) {
        if (par3.getBlockHardness(par2World, par4, par5, par6) != 0.0) {
            par1ItemStack.damageItem(1, par7EntityLiving);
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    
    public EnumAction getItemUseAction(final ItemStack par1ItemStack) {
        return EnumAction.block;
    }
    
    public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
        return 72000;
    }
    
    public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World par2World, final EntityPlayer player) {
        if (ExtraUtils.lawSwordEnabled && XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player)) {
            return ItemLawSword.newSword();
        }
        player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        return par1ItemStack;
    }
    
    public boolean canHarvestBlock(final Block par1Block, final ItemStack item) {
        return par1Block == Blocks.web;
    }
    
    public boolean getIsRepairable(final ItemStack par1ItemStack, final ItemStack par2ItemStack) {
        return false;
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
    
    public int numIcons(final ItemStack item) {
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
    
    public Multimap getItemAttributeModifiers() {
        final Multimap multimap = super.getItemAttributeModifiers();
        multimap.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemEthericSword.field_111210_e, "Weapon modifier", this.weaponDamage, 0));
        return multimap;
    }
}

