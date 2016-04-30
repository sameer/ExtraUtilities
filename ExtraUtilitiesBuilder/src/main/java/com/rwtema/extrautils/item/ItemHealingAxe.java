// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.item.ItemAxe;

public class ItemHealingAxe extends ItemAxe implements IItemMultiTransparency
{
    private IIcon[] icons;
    
    public ItemHealingAxe() {
        super(Item.ToolMaterial.EMERALD);
        this.maxStackSize = 1;
        this.setMaxDamage(1561);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:defoliageAxe");
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack par1ItemStack, final int pass) {
        return false;
    }
    
    public boolean onBlockDestroyed(final ItemStack par1ItemStack, final World par2World, final Block par3, final int par4, final int par5, final int par6, final EntityLivingBase par7EntityLivingBase) {
        return true;
    }
    
    public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity) {
        float k = 2.0f;
        player.addExhaustion(10.0f);
        if (entity instanceof EntityLiving) {
            if (entity.worldObj.isRemote) {
                for (int i = 0; i < 5; ++i) {
                    final int j = Potion.heal.getLiquidColor();
                    final float r = (j >> 16 & 0xFF) / 255.0f;
                    final float g = (j >> 8 & 0xFF) / 255.0f;
                    final float b = (j & 0xFF) / 255.0f;
                    entity.worldObj.spawnParticle("mobSpell", entity.posX + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width, entity.posY + entity.worldObj.rand.nextDouble() * entity.height - entity.yOffset, entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5) * entity.width, (double)r, (double)g, (double)b);
                }
            }
            if (k > 0.0f) {
                final EntityLiving entLivin = (EntityLiving)entity;
                k *= 2.0f;
                if (((EntityLiving)entity).isEntityUndead() && entity.isEntityAlive()) {
                    if (entity instanceof EntityZombie && ((EntityZombie)entity).isVillager()) {
                        if (!entity.worldObj.isRemote) {
                            entity.getDataWatcher().updateObject(14, (Object)(byte)1);
                            entity.worldObj.setEntityState(entity, (byte)16);
                        }
                        return true;
                    }
                    entLivin.attackEntityFrom(DamageSource.causePlayerDamage(player), k * 4.0f);
                    return true;
                }
                else if (entity.isEntityAlive() && ((EntityLiving)entity).getHealth() > 0.0f && entLivin.getHealth() < entLivin.getHealth()) {
                    if (!entLivin.worldObj.isRemote) {
                        ((EntityLiving)entity).heal(k);
                    }
                    return true;
                }
            }
        }
        return true;
    }
    
    public int getDamageVsEntity(final Entity par1Entity) {
        return 0;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }
    
    public void onUpdate(final ItemStack par1ItemStack, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {
        if (par5 && par3Entity instanceof EntityPlayer && par2World.getTotalWorldTime() % 40L == 0L) {
            ((EntityPlayer)par3Entity).getFoodStats().addStats(1, 0.2f);
        }
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
}


