// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.worldgen.endoftime;

import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.chunk.IChunkProvider;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.biome.WorldChunkManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.IRenderHandler;
import com.rwtema.extrautils.ExtraUtilsMod;
import com.rwtema.extrautils.IClientCode;
import net.minecraft.world.WorldProvider;

public class WorldProviderEndOfTime extends WorldProvider implements IClientCode
{
    public static final float celestialAngle = 0.5f;
    public static final int skyColor = 1052736;
    
    public WorldProviderEndOfTime() {
        ExtraUtilsMod.proxy.exectuteClientCode(this);
    }
    
    @SideOnly(Side.CLIENT)
    public void exectuteClientCode() {
        this.setCloudRenderer((IRenderHandler)RenderHandlersEndOfTime.nullRenderer);
        this.setSkyRenderer((IRenderHandler)RenderHandlersEndOfTime.skyRenderer);
    }
    
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManager(this.worldObj);
        this.dimensionId = ExtraUtils.endoftimeDimID;
    }
    
    public IChunkProvider createChunkGenerator() {
        return (IChunkProvider)new ChunkProviderEndOfTime(this.worldObj, this.worldObj.getSeed());
    }
    
    public boolean canCoordinateBeSpawn(final int p_76566_1_, final int p_76566_2_) {
        return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
    }
    
    public ChunkCoordinates getEntrancePortalLocation() {
        final WorldInfo worldInfo = this.worldObj.getWorldInfo();
        if (worldInfo.getSpawnY() != 64) {
            worldInfo.setSpawnPosition(worldInfo.getSpawnX(), 64, worldInfo.getSpawnZ());
        }
        return new ChunkCoordinates(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
    }
    
    public int getAverageGroundLevel() {
        return 64;
    }
    
    public String getDimensionName() {
        return "The Last Millenium";
    }
    
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor() {
        return 1.0;
    }
    
    public boolean canRespawnHere() {
        return true;
    }
    
    public boolean isSurfaceWorld() {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 256.0f;
    }
    
    protected void generateLightBrightnessTable() {
        final float f = 0.0f;
        for (int i = 0; i <= 15; ++i) {
            final float f2 = 1.0f - i / 15.0f;
            this.lightBrightnessTable[i] = (1.0f - f2) / (f2 * 3.0f + 1.0f) * (1.0f - f) + f;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public String getWelcomeMessage() {
        return "Travelling to the End of Time";
    }
    
    public ChunkCoordinates getSpawnPoint() {
        return this.getEntrancePortalLocation();
    }
    
    public ChunkCoordinates getRandomizedSpawnPoint() {
        final ChunkCoordinates chunkCoordinates = this.getEntrancePortalLocation();
        chunkCoordinates.posY = this.worldObj.getTopSolidOrLiquidBlock(chunkCoordinates.posX, chunkCoordinates.posZ);
        return chunkCoordinates;
    }
    
    public float calculateCelestialAngle(final long p_76563_1_, final float p_76563_3_) {
        return 0.5f;
    }
    
    public float getCurrentMoonPhaseFactor() {
        return 0.0f;
    }
    
    public double getHorizon() {
        return 3333.0;
    }
    
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(final Entity p_72833_1_, final float p_72833_2_) {
        final int color = 65538;
        float r = (color >> 16 & 0xFF) / 255.0f;
        float g = (color >> 8 & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        final float f7 = this.worldObj.getRainStrength(p_72833_2_);
        if (f7 > 0.0f) {
            final float f8 = (r * 0.3f + g * 0.59f + b * 0.11f) * 0.6f;
            final float f9 = 1.0f - f7 * 0.75f;
            r = r * f9 + f8 * (1.0f - f9);
            g = g * f9 + f8 * (1.0f - f9);
            b = b * f9 + f8 * (1.0f - f9);
        }
        final float f8 = this.worldObj.getWeightedThunderStrength(p_72833_2_);
        if (f8 > 0.0f) {
            final float f9 = (r * 0.3f + g * 0.59f + b * 0.11f) * 0.2f;
            final float f10 = 1.0f - f8 * 0.75f;
            r = r * f10 + f9 * (1.0f - f10);
            g = g * f10 + f9 * (1.0f - f10);
            b = b * f10 + f9 * (1.0f - f10);
        }
        if (this.worldObj.lastLightningBolt > 0) {
            float f9 = this.worldObj.lastLightningBolt - p_72833_2_;
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            f9 *= 0.45f;
            r = r * (1.0f - f9) + 0.8f * f9;
            g = g * (1.0f - f9) + 0.8f * f9;
            b = b * (1.0f - f9) + 1.0f * f9;
        }
        return Vec3.createVectorHelper((double)r, (double)g, (double)b);
    }
    
    public int getMoonPhase(final long p_76559_1_) {
        return 4;
    }
}


