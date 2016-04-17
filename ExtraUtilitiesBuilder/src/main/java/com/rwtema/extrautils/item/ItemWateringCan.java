// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.item;

import com.rwtema.extrautils.helper.XURandom;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.block.IGrowable;
import net.minecraftforge.common.IPlantable;
import net.minecraft.block.BlockSapling;
import net.minecraft.item.ItemBlock;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Facing;
import com.rwtema.extrautils.LogHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.monster.EntityEnderman;
import java.util.Collection;
import cpw.mods.fml.common.Loader;
import java.util.List;
import net.minecraft.item.EnumAction;
import net.minecraft.block.material.Material;
import net.minecraft.util.MovingObjectPosition;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.util.IIcon;
import java.util.Random;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.item.Item;

public class ItemWateringCan extends Item
{
    public static ArrayList<ItemStack> flowers;
    private static Random rand;
    IIcon busted;
    IIcon reinforced;
    public ThreadLocal<Boolean> watering;
    
    public ItemWateringCan() {
        this.busted = null;
        this.reinforced = null;
        this.watering = new ThreadLocal<Boolean>();
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setUnlocalizedName("extrautils:watering_can");
        this.setTextureName("extrautils:watering_can");
        this.setHasSubtypes(false);
        this.setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int par1) {
        if (par1 == 2) {
            return this.busted;
        }
        if (par1 == 3) {
            return this.reinforced;
        }
        return this.itemIcon;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister par1IIconRegister) {
        this.itemIcon = par1IIconRegister.registerIcon(this.getIconString());
        this.busted = par1IIconRegister.registerIcon("extrautils:watering_can_bust");
        this.reinforced = par1IIconRegister.registerIcon("extrautils:watering_can_reinforced");
    }
    
    public int getMaxItemUseDuration(final ItemStack par1ItemStack) {
        return 72000;
    }
    
    public boolean onItemUse(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final World par3World, final int par4, final int par5, final int par6, final int par7, final float par8, final float par9, final float par10) {
        if (par1ItemStack.getItemDamage() != 3 || !XUHelper.isPlayerFake(par2EntityPlayer)) {
            return false;
        }
        this.waterLocation(par3World, par4 + 0.5, par5 + 0.5, par6 + 0.5, par7, par1ItemStack, par2EntityPlayer);
        return true;
    }
    
    public ItemStack onItemRightClick(final ItemStack par1ItemStack, final World world, final EntityPlayer player) {
        if (par1ItemStack.getItemDamage() != 1) {
            if (XUHelper.isPlayerFake(player)) {
                if (par1ItemStack.getItemDamage() == 0) {
                    par1ItemStack.setItemDamage(2);
                }
                else {
                    this.onUsingTick(par1ItemStack, player, 0);
                }
            }
            else if (par1ItemStack.getItemDamage() == 2 && XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player)) {
                par1ItemStack.setItemDamage(4);
            }
            player.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
            return par1ItemStack;
        }
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
        if (movingobjectposition == null) {
            return par1ItemStack;
        }
        if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final int i = movingobjectposition.blockX;
            final int j = movingobjectposition.blockY;
            final int k = movingobjectposition.blockZ;
            if (world.getBlock(i, j, k).getMaterial() == Material.water) {
                if (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player)) {
                    par1ItemStack.setItemDamage(3);
                }
                else {
                    par1ItemStack.setItemDamage(0);
                }
                return par1ItemStack;
            }
        }
        return par1ItemStack;
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        if (par1ItemStack.getItemDamage() == 1) {
            return super.getUnlocalizedName() + ".empty";
        }
        if (par1ItemStack.getItemDamage() == 2) {
            return super.getUnlocalizedName() + ".busted";
        }
        if (par1ItemStack.getItemDamage() == 3) {
            return super.getUnlocalizedName() + ".reinforced";
        }
        return super.getUnlocalizedName();
    }
    
    public EnumAction getItemUseAction(final ItemStack par1ItemStack) {
        return EnumAction.none;
    }
    
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs, final List par3List) {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }
    
    public void initFlowers() {
        if (ItemWateringCan.flowers != null) {
            return;
        }
        ItemWateringCan.flowers = new ArrayList<ItemStack>();
        if (!Loader.isModLoaded("Forestry")) {
            return;
        }
        try {
            final Class flowerManager = Class.forName("forestry.api.apiculture.FlowerManager");
            final ArrayList<ItemStack> temp = (ArrayList<ItemStack>)flowerManager.getField("plainFlowers").get(null);
            ItemWateringCan.flowers.addAll(temp);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int count) {
        final MovingObjectPosition pos = this.getMovingObjectPositionFromPlayer(player.worldObj, player, true);
        if (pos != null) {
            this.waterLocation(player.worldObj, pos.hitVec.xCoord, pos.hitVec.yCoord, pos.hitVec.zCoord, pos.sideHit, stack, player);
        }
    }
    
    public void waterLocation(final World worldObj, final double hitX, final double hitY, final double hitZ, final int side, final ItemStack stack, final EntityPlayer play) {
        int range = 1;
        if (stack.getItemDamage() == 3) {
            range = 3;
        }
        if (stack.getItemDamage() == 4) {
            range = 5;
        }
        if (this.watering.get() == Boolean.TRUE) {
            return;
        }
        this.watering.set(Boolean.TRUE);
        this.waterLocation(worldObj, hitX, hitY, hitZ, side, stack, play, range);
        this.watering.set(Boolean.FALSE);
    }
    
    private void waterLocation(final World worldObj, final double hitX, final double hitY, final double hitZ, final int side, final ItemStack stack, final EntityPlayer play, final int range) {
        final List enderman = worldObj.getEntitiesWithinAABB((Class)EntityEnderman.class, AxisAlignedBB.getBoundingBox(hitX - range, hitY - range, hitZ - range, hitX + range, hitY + 6.0, hitZ + range));
        if (enderman != null) {
            for (final Object anEnderman : enderman) {
                ((EntityEnderman)anEnderman).attackEntityFrom(DamageSource.drown, 1.0f);
            }
        }
        final boolean cheat = stack.getItemDamage() == 4 && (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(play) || LogHelper.isDeObf || XUHelper.isPlayerFake(play));
        if (worldObj.isRemote) {
            final double dx = Facing.offsetsXForSide[side];
            final double dy = Facing.offsetsYForSide[side];
            final double dz = Facing.offsetsZForSide[side];
            final double x2 = hitX + dx * 0.1;
            final double y2 = hitY + dy * 0.1;
            final double z2 = hitZ + dz * 0.1;
            for (int i = 0; i < ((stack.getItemDamage() == 2) ? 1 : ((stack.getItemDamage() == 3) ? 100 : 10)); ++i) {
                worldObj.spawnParticle("splash", x2 + worldObj.rand.nextGaussian() * 0.6 * range, y2, z2 + worldObj.rand.nextGaussian() * 0.6 * range, 0.0, 0.0, 0.0);
            }
        }
        else {
            final List ents = worldObj.getEntitiesWithinAABB((Class)Entity.class, AxisAlignedBB.getBoundingBox(hitX - range, hitY - range, hitZ - range, hitX + range, hitY + range + 6.0, hitZ + range));
            if (ents != null) {
                for (final Object ent : ents) {
                    if (((Entity)ent).isBurning()) {
                        float p = 0.01f;
                        if (ent instanceof EntityPlayer) {
                            p = 0.333f;
                        }
                        ((Entity)ent).extinguish();
                        if (worldObj.rand.nextDouble() < p) {
                            if (stack.getItemDamage() < 3) {
                                stack.setItemDamage(1);
                            }
                            if (play != null) {
                                play.stopUsingItem();
                            }
                            return;
                        }
                        continue;
                    }
                }
            }
            final int blockX = (int)Math.floor(hitX);
            final int blockY = (int)Math.floor(hitY);
            final int blockZ = (int)Math.floor(hitZ);
            for (int x3 = blockX - range; x3 <= blockX + range; ++x3) {
                for (int y3 = blockY - range; y3 <= blockY + range; ++y3) {
                    for (int z3 = blockZ - range; z3 <= blockZ + range; ++z3) {
                        final Block id = worldObj.getBlock(x3, y3, z3);
                        if (!worldObj.isAirBlock(x3, y3, z3)) {
                            if (id == Blocks.fire) {
                                worldObj.setBlockToAir(x3, y3, z3);
                            }
                            if (id == Blocks.flowing_lava && worldObj.rand.nextInt(2) == 0) {
                                Blocks.flowing_lava.updateTick(worldObj, x3, y3, z3, worldObj.rand);
                            }
                            if (id == Blocks.farmland && worldObj.getBlockMetadata(x3, y3, z3) < 7) {
                                worldObj.setBlockMetadataWithNotify(x3, y3, z3, 7, 2);
                            }
                            int timer = -1;
                            if (id == Blocks.grass) {
                                timer = 20;
                                if (!cheat && worldObj.rand.nextInt(4500) == 0 && worldObj.isAirBlock(x3, y3 + 1, z3)) {
                                    this.initFlowers();
                                    if (ItemWateringCan.flowers.size() > 0 && worldObj.rand.nextInt(5) == 0) {
                                        final ItemStack flower = ItemWateringCan.flowers.get(worldObj.rand.nextInt(ItemWateringCan.flowers.size()));
                                        if (flower.getItem() instanceof ItemBlock && play != null) {
                                            ((ItemBlock)flower.getItem()).placeBlockAt(flower, play, worldObj, x3, y3 + 1, z3, 1, 0.5f, 1.0f, 0.5f, flower.getItem().getMetadata(flower.getItemDamage()));
                                        }
                                    }
                                    else {
                                        worldObj.getBiomeGenForCoords(x3, z3).plantFlower(worldObj, ItemWateringCan.rand, x3, y3 + 1, z3);
                                    }
                                }
                            }
                            else if (id == Blocks.mycelium) {
                                timer = 20;
                            }
                            else if (id == Blocks.wheat) {
                                timer = 40;
                            }
                            else if (id instanceof BlockSapling) {
                                timer = 50;
                            }
                            else if (id instanceof IPlantable || id instanceof IGrowable) {
                                timer = 40;
                            }
                            else if (id.getMaterial() == Material.grass) {
                                timer = 20;
                            }
                            if (stack.getItemDamage() == 2) {
                                timer *= 20;
                            }
                            timer /= range;
                            if (timer > 0 && id.getTickRandomly()) {
                                worldObj.scheduleBlockUpdate(x3, y3, z3, id, worldObj.rand.nextInt(timer));
                            }
                        }
                    }
                }
            }
            if (cheat) {
                for (int j = 0; j < 100; ++j) {
                    for (int x4 = blockX - range; x4 <= blockX + range; ++x4) {
                        for (int y4 = blockY - range; y4 <= blockY + range; ++y4) {
                            for (int z4 = blockZ - range; z4 <= blockZ + range; ++z4) {
                                final Block block = worldObj.getBlock(x4, y4, z4);
                                block.updateTick(worldObj, x4, y4, z4, worldObj.rand);
                                final TileEntity tile = worldObj.getTileEntity(x4, y4, z4);
                                if (tile != null && tile.canUpdate() && !tile.isInvalid()) {
                                    tile.updateEntity();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List, final boolean par4) {
        if (par1ItemStack.getItemDamage() == 2) {
            par3List.add("It appears that mechanical hands are not delicate enough");
            par3List.add("to use the watering can properly.");
        }
    }
    
    static {
        ItemWateringCan.flowers = null;
        ItemWateringCan.rand = XURandom.getInstance();
    }
}
