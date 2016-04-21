// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen.endoftime;

import java.util.Iterator;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraft.world.World;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.Facing;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldServer;
import net.minecraft.village.MerchantRecipe;
import java.util.LinkedList;
import com.rwtema.extrautils.worldgen.TeleporterBase;

public class TeleporterEndOfTime extends TeleporterBase
{
    public static final LinkedList<MerchantRecipe> lastVillagerTrades;
    
    public TeleporterEndOfTime(final WorldServer p_i1963_1_) {
        super(p_i1963_1_);
    }
    
    public void placeInPortal(final Entity entity, final double x, double y, final double z, final float r) {
        if (!this.placeInExistingPortal(entity, x, y, z, r)) {
            if (this.worldServerInstance.provider.dimensionId != ExtraUtils.endoftimeDimID) {
                y = this.worldServerInstance.getTopSolidOrLiquidBlock((int)x, (int)z);
                entity.setLocationAndAngles(x, y, z, entity.rotationYaw, 0.0f);
            }
            else {
                final ChunkCoordinates entrancePortalLocation = this.worldServerInstance.getEntrancePortalLocation();
                if (!this.placeInExistingPortal(entity, entrancePortalLocation.posX, y, entrancePortalLocation.posZ, r)) {
                    this.makePortal(entity);
                }
            }
        }
    }
    
    public boolean placeInExistingPortal(final Entity entity, final double x, final double y, final double z, final float r) {
        TileEntity destPortal = null;
        for (int s = 0; s <= 5 && destPortal == null; ++s) {
            for (int dx = -s; dx <= s; ++dx) {
                for (int dz = -s; dz <= s; ++dz) {
                    if (destPortal == null) {
                        destPortal = this.findPortalInChunk(x + dx * 16, z + dz * 16);
                    }
                }
            }
        }
        if (destPortal != null) {
            entity.setLocationAndAngles(destPortal.xCoord + 0.5, (double)(destPortal.yCoord + 1), destPortal.zCoord + 0.5, entity.rotationYaw, entity.rotationPitch);
            final double motionX = 0.0;
            entity.motionZ = motionX;
            entity.motionY = motionX;
            entity.motionX = motionX;
            return true;
        }
        return false;
    }
    
    public boolean makePortal(final Entity entity) {
        final ChunkCoordinates dest = this.worldServerInstance.getEntrancePortalLocation();
        final int x = dest.posX;
        int y = dest.posY;
        final int z = dest.posZ;
        if (y < 64) {
            y = 64;
            this.worldServerInstance.getWorldInfo().setSpawnPosition(x, 64, z);
        }
        final int r = 8;
        final boolean d = ExtraUtils.decorative1 != null;
        final Block bricks = d ? ExtraUtils.decorative1 : Blocks.stonebrick;
        final int bricksMeta1 = d ? 4 : 0;
        final int bricksMeta2 = d ? 0 : 0;
        final int bricksMeta3 = d ? 10 : 0;
        final Block darkBricks = d ? ExtraUtils.decorative1 : Blocks.obsidian;
        final int darkBricksMeta = d ? 2 : 0;
        for (int dx = -r; dx <= r; ++dx) {
            for (int dz = -r; dz <= r; ++dz) {
                for (int dy = -2; dy < 7; ++dy) {
                    this.worldServerInstance.setBlock(x + dx, y + dy, z + dz, Blocks.air, 0, 2);
                }
                final int min_r = Math.min(Math.abs(dx), Math.abs(dz));
                final int max_r = Math.max(Math.abs(dx), Math.abs(dz));
                if (max_r <= r - 1) {
                    this.worldServerInstance.setBlock(x + dx, y - 1, z + dz, Blocks.bedrock, 0, 2);
                }
                if (dx == 0 && dz == 0) {
                    this.worldServerInstance.setBlock(x, y, z, ExtraUtils.portal, 3, 2);
                }
                else if (max_r == r && min_r > 1) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 0, 2);
                    this.worldServerInstance.setBlock(x + dx, y + 1, z + dz, bricks, bricksMeta1, 2);
                    this.worldServerInstance.setBlock(x + dx, y + 2, z + dz, Blocks.fence, 0, 2);
                }
                else if (max_r == r) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 3, 2);
                }
                else if (max_r == 1) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta2, 2);
                }
                else if (min_r == 1) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta2, 2);
                }
                else if (max_r == r - 1 && dx != 0 && dz != 0) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 0, 2);
                    this.worldServerInstance.setBlock(x + dx, y + 1, z + dz, (Block)Blocks.stone_slab, 5, 2);
                }
                else if (max_r == 5) {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, Blocks.stonebrick, 3, 2);
                }
                else {
                    this.worldServerInstance.setBlock(x + dx, y, z + dz, bricks, bricksMeta3, 2);
                }
            }
        }
        final int lx = x + 3;
        final int lz = z + 3;
        final int lh = 6;
        this.worldServerInstance.setBlock(lx, y + 1, lz, darkBricks, darkBricksMeta, 2);
        for (int i = 2; i < lh; ++i) {
            this.worldServerInstance.setBlock(lx, y + i, lz, Blocks.nether_brick_fence, 0, 2);
        }
        if (ExtraUtils.colorBlockRedstone != null) {
            this.worldServerInstance.setBlock(lx, y + lh, lz, (Block)ExtraUtils.colorBlockRedstone, 15, 2);
        }
        else {
            this.worldServerInstance.setBlock(lx, y + lh, lz, Blocks.redstone_block, 0, 2);
        }
        for (int i = 2; i < 6; ++i) {
            this.worldServerInstance.setBlock(lx + Facing.offsetsXForSide[i], y + lh, lz + Facing.offsetsZForSide[i], Blocks.nether_brick_fence, 0, 2);
        }
        this.worldServerInstance.setBlock(lx, y + lh + 2, lz, (Block)Blocks.stone_slab, 6, 2);
        this.worldServerInstance.setBlock(lx, y + lh + 1, lz, Blocks.lit_redstone_lamp, 0, 3);
        this.worldServerInstance.setBlock(x + r - 2, y + 1, z - r + 2, (Block)Blocks.cauldron, 3, 2);
        final EntityVillager villager = new EntityVillager((World)this.worldServerInstance);
        villager.setLocationAndAngles(lx - 0.5, (double)(y + 2), lz - 0.5, 0.0f, 0.0f);
        villager.setProfession(0);
        final MerchantRecipeList list = villager.getRecipes((EntityPlayer)FakePlayerFactory.getMinecraft(this.worldServerInstance));
        list.clear();
        if (!TeleporterEndOfTime.lastVillagerTrades.isEmpty()) {
            for (final MerchantRecipe merchantRecipe : TeleporterEndOfTime.lastVillagerTrades) {
                list.addToListWithCheck(merchantRecipe);
            }
        }
        villager.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0);
        villager.setCustomNameTag("The Last Villager");
        villager.forceSpawn = true;
        villager.func_110163_bv();
        this.worldServerInstance.spawnEntityInWorld((Entity)villager);
        entity.setLocationAndAngles(x + 0.5, (double)(y + 1), z + 0.5, entity.rotationYaw, entity.rotationPitch);
        final double motionX = 0.0;
        entity.motionZ = motionX;
        entity.motionY = motionX;
        entity.motionX = motionX;
        return true;
    }
    
    public void removeStalePortalLocations(final long p_85189_1_) {
    }
    
    static {
        lastVillagerTrades = new LinkedList<MerchantRecipe>();
    }
}

