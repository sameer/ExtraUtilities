// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraftforge.common.util.FakePlayer;
import java.util.List;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.tileentity.TileEntityHopper;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.Entity;
import net.minecraft.command.IEntitySelector;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemSword;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.Facing;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import java.util.Random;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

public class BlockPeacefulTable extends BlockMultiBlock
{
    private IIcon[] icons;
    
    public BlockPeacefulTable() {
        super(Material.wood);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.25f, 1.0f);
        this.setBlockName("extrautils:peaceful_table_top");
        this.setBlockTextureName("extrautils:peaceful_table_top");
        this.setTickRandomly(true);
        this.setHardness(1.0f);
        this.setResistance(10.0f).setStepSound(BlockPeacefulTable.soundTypeWood);
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        (this.icons = new IIcon[3])[0] = par1IIconRegister.registerIcon("extrautils:peaceful_table_bottom");
        this.icons[1] = par1IIconRegister.registerIcon("extrautils:peaceful_table_top");
        this.icons[2] = par1IIconRegister.registerIcon("extrautils:peaceful_table_side");
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random par5Random) {
        if (!world.isRemote && world instanceof WorldServer) {
            if (!ExtraUtils.peacefulTableInAllDifficulties && world.difficultySetting != EnumDifficulty.PEACEFUL) {
                return;
            }
            final BiomeGenBase.SpawnListEntry var7 = ((WorldServer)world).spawnRandomCreature(EnumCreatureType.monster, x, y, z);
            if (var7 != null) {
                IInventory swordInv = null;
                int swordSlot = -1;
                ItemStack sword = null;
                for (int j = 0; j < 6; ++j) {
                    final TileEntity tile = world.getTileEntity(x + Facing.offsetsXForSide[j], y + Facing.offsetsYForSide[j], z + Facing.offsetsZForSide[j]);
                    if (tile instanceof IInventory) {
                        final IInventory inv = (IInventory)tile;
                        for (int i = 0; i < inv.getSizeInventory(); ++i) {
                            final ItemStack item = inv.getStackInSlot(i);
                            if (item != null && item.getItem() instanceof ItemSword) {
                                swordInv = inv;
                                swordSlot = i;
                                sword = item;
                                break;
                            }
                        }
                        if (sword != null) {
                            break;
                        }
                    }
                }
                if (sword == null) {
                    return;
                }
                EntityLiving t;
                try {
                    t = var7.entityClass.getConstructor(World.class).newInstance(world);
                }
                catch (Exception var8) {
                    var8.printStackTrace();
                    return;
                }
                final List list1 = world.selectEntitiesWithinAABB((Class)EntityItem.class, AxisAlignedBB.getBoundingBox((double)(x - 2), (double)y, (double)(z - 2), (double)(x + 3), (double)(y + 4), (double)(z + 2)), IEntitySelector.selectAnything);
                t.setLocationAndAngles(x + 0.5, y + 1.25, z + 0.5, par5Random.nextFloat() * 360.0f, 0.0f);
                world.spawnEntityInWorld((Entity)t);
                final EnumDifficulty prev = world.difficultySetting;
                world.difficultySetting = EnumDifficulty.HARD;
                t.onSpawnWithEgg((IEntityLivingData)null);
                world.difficultySetting = prev;
                final FakePlayer fakePlayer = FakePlayerFactory.getMinecraft((WorldServer)world);
                fakePlayer.setCurrentItemOrArmor(0, sword.copy());
                final float h1 = t.getHealth();
                fakePlayer.attackTargetEntityWithCurrentItem((Entity)t);
                final float h2 = t.getHealth();
                if (t.isDead) {
                    if (fakePlayer.getCurrentEquippedItem() == null || fakePlayer.getCurrentEquippedItem().stackSize == 0) {
                        swordInv.setInventorySlotContents(swordSlot, (ItemStack)null);
                    }
                    else {
                        swordInv.setInventorySlotContents(swordSlot, fakePlayer.getCurrentEquippedItem());
                    }
                }
                else {
                    if (fakePlayer.getCurrentEquippedItem() == null || fakePlayer.getCurrentEquippedItem().stackSize == 0) {
                        swordInv.setInventorySlotContents(swordSlot, (ItemStack)null);
                    }
                    else {
                        if (h1 > h2) {
                            for (float h3 = h2; h3 > 0.0f; h3 -= h1 - h2) {
                                sword.hitEntity((EntityLivingBase)t, (EntityPlayer)fakePlayer);
                            }
                        }
                        if (sword.stackSize == 0) {
                            swordInv.setInventorySlotContents(swordSlot, (ItemStack)null);
                        }
                    }
                    t.onDeath(DamageSource.causePlayerDamage((EntityPlayer)fakePlayer));
                    t.motionX = 0.0;
                    t.motionY = 0.0;
                    t.motionZ = 0.0;
                }
                fakePlayer.setCurrentItemOrArmor(0, (ItemStack)null);
                t.setDead();
                final List list2 = world.getEntitiesWithinAABB((Class)EntityItem.class, AxisAlignedBB.getBoundingBox((double)(x - 2), (double)y, (double)(z - 2), (double)(x + 3), (double)(y + 4), (double)(z + 2)));
                for (final Object aList2 : list2) {
                    if (!list1.contains(aList2)) {
                        final int[] seq = XUHelper.rndSeq(6, world.rand);
                        for (int k = 0; k < 6; ++k) {
                            final IInventory inv2 = TileEntityHopper.func_145893_b(world, (double)(x + Facing.offsetsXForSide[seq[k]]), (double)(y + Facing.offsetsYForSide[seq[k]]), (double)(z + Facing.offsetsZForSide[seq[k]]));
                            if (inv2 != null && !((EntityItem)aList2).isDead) {
                                final ItemStack itemstack = ((EntityItem)aList2).getEntityItem().copy();
                                final ItemStack itemstack2 = XUHelper.invInsert(inv2, itemstack, Facing.oppositeSide[k]);
                                if (itemstack2 != null && itemstack2.stackSize != 0) {
                                    ((EntityItem)aList2).setEntityItemStack(itemstack2);
                                }
                                else {
                                    ((EntityItem)aList2).setDead();
                                }
                            }
                        }
                        ((EntityItem)aList2).setDead();
                    }
                }
            }
        }
    }
    
    public void onBlockAdded(final World world, final int x, final int y, final int z) {
        if (!world.isRemote) {
            world.scheduleBlockUpdate(x, y, z, (Block)this, 5 + world.rand.nextInt(100));
        }
    }
    
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity par5Entity) {
        if (par5Entity instanceof EntityXPOrb) {
            par5Entity.setDead();
        }
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int x) {
        final int i = Math.min(par1, 2);
        return this.icons[i];
    }
    
    public void prepareForRender(final String label) {
    }
    
    public BoxModel getWorldModel(final IBlockAccess world, final int x, final int y, final int z) {
        final BoxModel boxes = new BoxModel();
        final float h = 0.0625f;
        boxes.add(new Box(0.0f, 0.75f, 0.0f, 1.0f, 1.0f, 1.0f));
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.3125f, 0.75f, 0.3125f));
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.3125f, 0.75f, 0.3125f).rotateY(1));
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.3125f, 0.75f, 0.3125f).rotateY(2));
        boxes.add(new Box(0.0625f, 0.0f, 0.0625f, 0.3125f, 0.75f, 0.3125f).rotateY(3));
        return boxes;
    }
    
    public BoxModel getInventoryModel(final int metadata) {
        return this.getWorldModel(null, 0, 0, 0);
    }
}
