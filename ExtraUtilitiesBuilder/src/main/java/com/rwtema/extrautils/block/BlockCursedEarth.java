// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.block;

import net.minecraft.item.ItemStack;
import com.rwtema.extrautils.helper.XUHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityMobSpawner;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.world.IBlockAccess;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import java.lang.reflect.Method;
import net.minecraft.util.IIcon;
import com.rwtema.extrautils.item.IBlockLocalization;
import net.minecraft.block.Block;

public class BlockCursedEarth extends Block implements IBlockLocalization
{
    public static int powered;
    private IIcon cursedSide;
    private IIcon cursedTop;
    private IIcon blessedSide;
    private IIcon blessedTop;
    private IIcon dirt;
    private Method cache;
    
    public BlockCursedEarth() {
        super(Material.grass);
        this.cache = null;
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setStepSound(Block.soundTypeGravel);
        this.setCreativeTab((CreativeTabs)ExtraUtils.creativeTabExtraUtils);
        this.setBlockName("extrautils:cursedearthside");
        this.setBlockTextureName("extrautils:cursedearthside");
        this.setHardness(0.5f);
        this.blockResistance = 200.0f;
    }
    
    public boolean canSilkHarvest(final World world, final EntityPlayer player, final int x, final int y, final int z, final int metadata) {
        return true;
    }
    
    public Item getItemDropped(final int p_149650_1_, final Random p_149650_2_, final int p_149650_3_) {
        return Item.getItemFromBlock(Blocks.dirt);
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int par1, final int par2) {
        if (par1 == 0) {
            return this.dirt;
        }
        if (par2 >> 1 == 0) {
            if (par1 == 1) {
                return this.cursedTop;
            }
            return this.cursedSide;
        }
        else {
            if (par2 >> 1 != 1) {
                return this.dirt;
            }
            if (par1 == 1) {
                return this.blessedTop;
            }
            return this.blessedSide;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister par1IIconRegister) {
        this.dirt = par1IIconRegister.registerIcon("dirt");
        this.cursedSide = par1IIconRegister.registerIcon("extrautils:cursedearthside");
        this.cursedTop = par1IIconRegister.registerIcon("extrautils:cursedearthtop");
        this.blessedSide = par1IIconRegister.registerIcon("extrautils:blessedEarthSide");
        this.blessedTop = par1IIconRegister.registerIcon("extrautils:blessedEarthTop");
    }
    
    public void onEntityWalking(final World world, final int x, final int y, final int z, final Entity entity) {
        if (world.isRemote) {
            return;
        }
        if ((world.getBlockMetadata(x, y, z) & 0xE) == 0x2) {
            return;
        }
        if (entity.isEntityAlive()) {
            if (world.getBlockLightValue(x, y + 1, z) > 9) {
                if (!(entity instanceof EntityPlayer)) {
                    entity.attackEntityFrom(DamageSource.cactus, 1.0f);
                }
                if (world.rand.nextInt(24) == 0 && world.canBlockSeeTheSky(x, y + 1, z) && world.isAirBlock(x, y + 1, z)) {
                    world.setBlock(x, y + 1, z, (Block)Blocks.fire);
                    world.scheduleBlockUpdate(x, y, z, (Block)Blocks.fire, 1 + world.rand.nextInt(200));
                }
                for (int i = 0; i < 20; ++i) {
                    final int dx = x + world.rand.nextInt(9) - 4;
                    final int dy = y + world.rand.nextInt(5) - 3;
                    final int dz = z + world.rand.nextInt(9) - 4;
                    if (world.getBlock(dx, dy, dz) == this & (world.getBlockLightOpacity(dx, dy + 1, dz) > 2 | world.getBlockLightValue(dx, dy + 1, dz) > 9)) {
                        world.scheduleBlockUpdate(dx, dy, dz, (Block)this, 10 + world.rand.nextInt(600));
                    }
                }
            }
            else if (entity instanceof EntityMob) {
                ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 7200, 1));
                ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.damageBoost.id, 7200, 1));
                ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.regeneration.id, 20, 0));
                ((EntityMob)entity).addPotionEffect(new PotionEffect(Potion.resistance.id, 40, 0));
            }
            else if (entity instanceof EntityPlayer) {
                for (int i = 0; i < 3; ++i) {
                    final int var7 = x + world.rand.nextInt(9) - 4;
                    final int var8 = y + world.rand.nextInt(5) - 3;
                    final int var9 = z + world.rand.nextInt(9) - 4;
                    if (world.getBlock(var7, var8, var9) == this && world.getBlockLightValue(var7, var8 + 1, var9) < 9) {
                        world.scheduleBlockUpdate(var7, var8, var9, (Block)this, world.rand.nextInt(100));
                    }
                }
            }
        }
    }
    
    public int isProvidingWeakPower(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return BlockCursedEarth.powered;
    }
    
    public boolean renderAsNormalBlock() {
        return BlockCursedEarth.powered == 0;
    }
    
    public void resetTimer(final MobSpawnerBaseLogic logic) {
        if (this.cache == null) {
            this.cache = ReflectionHelper.findMethod((Class)MobSpawnerBaseLogic.class, (Object)logic, new String[] { "resetTimer", "func_98273_j" }, new Class[0]);
        }
        try {
            this.cache.invoke(logic, new Object[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int onBlockPlaced(final World world, final int x, final int y, final int z, final int p_149660_5_, final float p_149660_6_, final float p_149660_7_, final float p_149660_8_, final int p_149660_9_) {
        if (world.getTileEntity(x, y + 1, z) instanceof TileEntityMobSpawner) {
            world.scheduleBlockUpdate(x, y, z, (Block)this, 10);
        }
        return super.onBlockPlaced(world, x, y, z, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
    }
    
    public int getMobilityFlag() {
        return 2;
    }
    
    public void onNeighborBlockChange(final World p_149695_1_, final int x, final int y, final int z, final Block p_149695_5_) {
        if (p_149695_5_ == Blocks.mob_spawner) {
            p_149695_1_.scheduleBlockUpdate(x, y, z, (Block)this, 10);
        }
        super.onNeighborBlockChange(p_149695_1_, x, y, z, p_149695_5_);
    }
    
    public int damageDropped(final int p_149692_1_) {
        return p_149692_1_ & 0xE;
    }
    
    public void spawnLogic(final MobSpawnerBaseLogic logic) {
        if (logic.spawnDelay == -1) {
            this.resetTimer(logic);
        }
        if (logic.spawnDelay > 0) {
            logic.spawnDelay -= 100;
            if (logic.spawnDelay < 0) {
                logic.spawnDelay = 0;
            }
            return;
        }
        boolean flag = false;
        final NBTTagCompound tags = new NBTTagCompound();
        logic.writeToNBT(tags);
        final int spawnCount = tags.getShort("SpawnCount");
        final int maxNearbyEntities = tags.getShort("MaxNearbyEntities");
        final int spawnRange = tags.getShort("SpawnRange");
        for (int i = 0; i < spawnCount; ++i) {
            final Entity entity = EntityList.createEntityByName(logic.getEntityNameToSpawn(), logic.getSpawnerWorld());
            if (entity == null) {
                return;
            }
            final int j = logic.getSpawnerWorld().getEntitiesWithinAABB((Class)entity.getClass(), AxisAlignedBB.getBoundingBox((double)logic.getSpawnerX(), (double)logic.getSpawnerY(), (double)logic.getSpawnerZ(), (double)(logic.getSpawnerX() + 1), (double)(logic.getSpawnerY() + 1), (double)(logic.getSpawnerZ() + 1)).expand((double)(spawnRange * 2), 4.0, (double)(spawnRange * 2))).size();
            if (j >= maxNearbyEntities) {
                this.resetTimer(logic);
                return;
            }
            final double d2 = logic.getSpawnerX() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
            final double d3 = logic.getSpawnerY() + logic.getSpawnerWorld().rand.nextInt(3) - 1;
            final double d4 = logic.getSpawnerZ() + (logic.getSpawnerWorld().rand.nextDouble() - logic.getSpawnerWorld().rand.nextDouble()) * spawnRange;
            final EntityLiving entityliving = (entity instanceof EntityLiving) ? entity : null;
            entity.setLocationAndAngles(d2, d3, d4, logic.getSpawnerWorld().rand.nextFloat() * 360.0f, 0.0f);
            if (entityliving != null && this.SpawnMob(entityliving)) {
                logic.func_98265_a((Entity)entityliving);
                logic.getSpawnerWorld().playAuxSFX(2004, logic.getSpawnerX(), logic.getSpawnerY(), logic.getSpawnerZ(), 0);
                entityliving.spawnExplosionParticle();
                flag = true;
            }
        }
        if (flag) {
            this.resetTimer(logic);
        }
    }
    
    public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
        if (!world.isRemote && world instanceof WorldServer) {
            if (world.getTileEntity(x, y + 1, z) instanceof TileEntityMobSpawner) {
                final MobSpawnerBaseLogic logic = ((TileEntityMobSpawner)world.getTileEntity(x, y + 1, z)).func_145881_a();
                this.spawnLogic(logic);
                world.scheduleBlockUpdate(x, y, z, (Block)this, 100);
                return;
            }
            final boolean isWorldGen = (world.getBlockMetadata(x, y, z) & 0x1) == 0x1;
            final boolean blessed = (world.getBlockMetadata(x, y, z) & 0xE) == 0x2;
            if (blessed || world.getBlockLightValue(x, y + 1, z) < 9) {
                boolean flag = blessed || world.difficultySetting.getDifficultyId() > 0;
                if (flag && isWorldGen && world.getClosestPlayer((double)x, (double)y, (double)z, 10.0) == null) {
                    flag = false;
                }
                if (flag) {
                    final int var12 = world.getEntitiesWithinAABB((Class)EntityCreature.class, AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)).expand(7.0, 2.0, 7.0)).size();
                    flag = (rand.nextInt(blessed ? 8 : 4) >= var12);
                }
                if (flag) {
                    EnumCreatureType type = EnumCreatureType.monster;
                    if ((world.getBlockMetadata(x, y, z) & 0xE) == 0x2) {
                        type = EnumCreatureType.creature;
                    }
                    final BiomeGenBase.SpawnListEntry var13 = ((WorldServer)world).spawnRandomCreature(type, x, y, z);
                    if (var13 != null && !EntityFlying.class.isAssignableFrom(var13.entityClass)) {
                        EntityLiving t;
                        try {
                            t = var13.entityClass.getConstructor(World.class).newInstance(world);
                        }
                        catch (Exception var14) {
                            var14.printStackTrace();
                            return;
                        }
                        t.setLocationAndAngles(x + 0.5, (double)(y + 1), z + 0.5, rand.nextFloat() * 360.0f, 0.0f);
                        final int meta = world.getBlockMetadata(x, y, z);
                        world.setBlock(x, y, z, (Block)Blocks.grass, 0, 0);
                        if (this.SpawnMob(t)) {
                            world.spawnEntityInWorld((Entity)t);
                            t.playLivingSound();
                        }
                        world.setBlock(x, y, z, (Block)this, meta, 0);
                    }
                }
            }
            if (blessed) {
                return;
            }
            if (world.getBlockLightOpacity(x, y + 1, z) > 2 || world.getBlockLightValue(x, y + 1, z) > 9) {
                boolean nearbyFire = world.getBlock(x, y + 1, z) == Blocks.fire;
                if (nearbyFire) {
                    if (rand.nextInt(3) == 0) {
                        world.setBlock(x, y, z, Blocks.dirt);
                        return;
                    }
                }
                else if (world.isAirBlock(x, y + 1, z) && world.canBlockSeeTheSky(x, y + 1, z)) {
                    world.setBlock(x, y + 1, z, (Block)Blocks.fire);
                    nearbyFire = true;
                }
                if (!nearbyFire) {
                    for (int i = 0; i < 20; ++i) {
                        final int dx = x + rand.nextInt(9) - 4;
                        final int dy = y + rand.nextInt(5) - 3;
                        final int dz = z + rand.nextInt(9) - 4;
                        if (world.getBlock(dx, dy, dz) == Blocks.fire) {
                            nearbyFire = true;
                            break;
                        }
                    }
                }
                if (nearbyFire) {
                    for (int i = 0; i < 20; ++i) {
                        final int dx = x + rand.nextInt(9) - 4;
                        final int dy = y + rand.nextInt(5) - 3;
                        final int dz = z + rand.nextInt(9) - 4;
                        if ((world.getBlock(dx, dy, dz) == this & (world.getBlockLightOpacity(dx, dy + 1, dz) > 2 | world.getBlockLightValue(dx, dy + 1, dz) > 9)) && world.getBlock(dx, dy + 1, dz) != Blocks.fire) {
                            if (rand.nextInt(4) == 0 && world.isAirBlock(dx, dy + 1, dz)) {
                                world.setBlock(dx, dy + 1, dz, (Block)Blocks.fire);
                            }
                            else {
                                world.setBlock(dx, dy, dz, Blocks.dirt);
                            }
                        }
                    }
                }
            }
            else if (world.getBlockLightValue(x, y + 1, z) < 9) {
                for (int var15 = 0; var15 < 4; ++var15) {
                    final int var16 = x + rand.nextInt(3) - 1;
                    final int var17 = y + rand.nextInt(5) - 3;
                    final int var18 = z + rand.nextInt(3) - 1;
                    if ((world.getBlock(var16, var17, var18) == Blocks.dirt || world.getBlock(var16, var17, var18) == Blocks.grass) && world.getBlockLightOpacity(var16, var17 + 1, var18) <= 2 && world.getBlockLightValue(var16, var17 + 1, var18) < 9) {
                        world.setBlock(var16, var17, var18, (Block)this, world.getBlockMetadata(x, y, z), 3);
                    }
                }
            }
        }
    }
    
    public boolean SpawnMob(final EntityLiving t) {
        if (t.getCanSpawnHere()) {
            t.onSpawnWithEgg((IEntityLivingData)null);
            if (t instanceof EntityMob) {
                t.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 3600, 1));
                t.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 3600, 1));
            }
            else {
                t.addPotionEffect(new PotionEffect(Potion.regeneration.id, 3600, 1));
            }
            t.getEntityData().setLong("CursedEarth", 3600L);
            t.forceSpawn = true;
            t.func_110163_bv();
            return true;
        }
        return false;
    }
    
    public boolean isBlockSolid(final IBlockAccess par1IBlockAccess, final int par2, final int par3, final int par4, final int par5) {
        return true;
    }
    
    public boolean isSideSolid(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection side) {
        return true;
    }
    
    public boolean isFireSource(final World world, final int x, final int y, final int z, final ForgeDirection side) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World par1World, final int par2, final int par3, final int par4, final Random par5Random) {
        super.randomDisplayTick(par1World, par2, par3, par4, par5Random);
        if (par5Random.nextInt(2) == 0 && par1World.getBlockMetadata(par2, par3, par4) == 0) {
            par1World.spawnParticle("portal", (double)(par2 + par5Random.nextFloat()), (double)(par3 + 1.1f), (double)(par4 + par5Random.nextFloat()), 0.0, 0.05, 0.0);
        }
    }
    
    public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int p_149727_6_, final float p_149727_7_, final float p_149727_8_, final float p_149727_9_) {
        if (XUHelper.isThisPlayerACheatyBastardOfCheatBastardness(player) || XUHelper.deObf) {
            world.setBlock(x, y, z, (Block)this, 2, 3);
            return true;
        }
        return false;
    }
    
    public String getUnlocalizedName(final ItemStack par1ItemStack) {
        if (par1ItemStack.getItemDamage() == 0) {
            return this.getUnlocalizedName();
        }
        return this.getUnlocalizedName() + ".blessed";
    }
    
    static {
        BlockCursedEarth.powered = 0;
    }
}
