// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import net.minecraft.util.EntityDamageSource;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import com.rwtema.extrautils.crafting.LawSwordCraftHandler;
import net.minecraftforge.common.util.EnumHelper;
import com.rwtema.extrautils.ExtraUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Multimap;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class ItemLawSword extends ItemSword
{
    public static Item.ToolMaterial material;
    public static BaseAttribute godSlayingDamage;
    public static BaseAttribute armorPiercingDamage;
    
    public ItemLawSword() {
        super(ItemLawSword.material);
        this.maxStackSize = 1;
        this.setUnlocalizedName("extrautils:lawSword");
    }
    
    public ItemStack onItemRightClick(final ItemStack p_77659_1_, final World p_77659_2_, final EntityPlayer p_77659_3_) {
        p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
        return p_77659_1_;
    }
    
    public boolean hitEntity(final ItemStack par1ItemStack, final EntityLivingBase par2EntityLiving, final EntityLivingBase par3EntityLiving) {
        return true;
    }
    
    public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity) {
        if (entity.canAttackWithItem()) {
            final Multimap<String, AttributeModifier> multimap = (Multimap<String, AttributeModifier>)stack.getAttributeModifiers();
            Collection<AttributeModifier> gsd = (Collection<AttributeModifier>)multimap.get((Object)ItemLawSword.godSlayingDamage.getAttributeUnlocalizedName());
            if (gsd != null) {
                for (final AttributeModifier t : gsd) {
                    this.attackEntity(player, entity, t.getAmount(), (DamageSource)new DamageSourceEvil(player, true));
                }
            }
            gsd = (Collection<AttributeModifier>)multimap.get((Object)ItemLawSword.armorPiercingDamage.getAttributeUnlocalizedName());
            if (gsd != null) {
                for (final AttributeModifier gs : gsd) {
                    this.attackEntity(player, entity, gs.getAmount(), (DamageSource)new DamageSourceEvil(player, false));
                }
            }
        }
        return false;
    }
    
    public void attackEntity(final EntityPlayer player, final Entity entity, double f, final DamageSource damageSource) {
        float f2 = 0.0f;
        if (entity instanceof EntityLivingBase) {
            f2 = EnchantmentHelper.getEnchantmentModifierLiving((EntityLivingBase)player, (EntityLivingBase)entity);
        }
        if (f > 0.0 || f2 > 0.0f) {
            final boolean flag = player.fallDistance > 0.0f && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(Potion.blindness) && player.ridingEntity == null && entity instanceof EntityLivingBase;
            if (flag && f > 0.0) {
                f *= 1.5;
            }
            f += f2;
            final boolean flag2 = entity.attackEntityFrom(damageSource, (float)f);
            if (flag2) {
                if (flag) {
                    player.onCriticalHit(entity);
                }
                if (f2 > 0.0f) {
                    player.onEnchantmentCritical(entity);
                }
                player.setLastAttacker(entity);
                if (entity instanceof EntityLivingBase) {
                    EnchantmentHelper.func_151384_a((EntityLivingBase)entity, (Entity)player);
                }
                EnchantmentHelper.func_151385_b((EntityLivingBase)player, entity);
            }
        }
    }
    
    public Multimap getAttributeModifiers(final ItemStack stack) {
        final Multimap multimap = super.getItemAttributeModifiers();
        multimap.put((Object)SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemLawSword.field_111210_e, "Weapon modifier", 4.0, 0));
        multimap.put((Object)ItemLawSword.godSlayingDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemLawSword.field_111210_e, "Weapon modifier", 2.0, 0));
        multimap.put((Object)ItemLawSword.armorPiercingDamage.getAttributeUnlocalizedName(), (Object)new AttributeModifier(ItemLawSword.field_111210_e, "Weapon modifier", 4.0, 0));
        return multimap;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack p_77624_1_, final EntityPlayer p_77624_2_, final List p_77624_3_, final boolean p_77624_4_) {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
        p_77624_3_.add(("" + StatCollector.translateToLocal(this.getUnlocalizedNameInefficiently(p_77624_1_) + ".tooltip")).trim());
    }
    
    public static ItemStack newSword() {
        return new ItemStack(ExtraUtils.lawSword);
    }
    
    static {
        ItemLawSword.material = EnumHelper.addToolMaterial("OpeOpeNoMi", 3, 4096, 8.0f, 3.0f, 10);
        LawSwordCraftHandler.init();
        ItemLawSword.godSlayingDamage = (BaseAttribute)new RangedAttribute("extrautils.godSlayingAttackDamage", 2.0, 0.0, Double.MAX_VALUE);
        ItemLawSword.armorPiercingDamage = (BaseAttribute)new RangedAttribute("extrautils.armorPiercingAttackDamage", 2.0, 0.0, Double.MAX_VALUE);
    }
    
    public static class DamageSourceEvil extends EntityDamageSource
    {
        public DamageSourceEvil(final EntityPlayer player, final boolean creative) {
            super("player", (Entity)player);
            this.setDamageBypassesArmor();
            this.setDamageIsAbsolute();
            if (creative) {
                this.setDamageAllowedInCreativeMode();
            }
        }
    }
}

