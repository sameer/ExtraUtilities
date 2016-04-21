// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.worldgen.Underdark;

import net.minecraft.util.Vec3;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.chunk.IChunkProvider;
import com.rwtema.extrautils.ExtraUtils;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.WorldProvider;

public class WorldProviderUnderdark extends WorldProvider
{
    private float[] colorsSunriseSunset;
    
    public WorldProviderUnderdark() {
        this.colorsSunriseSunset = new float[4];
    }
    
    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManager(this.worldObj);
        this.dimensionId = ExtraUtils.underdarkDimID;
        this.hasNoSky = true;
    }
    
    public long getSeed() {
        return super.getSeed() + (ChunkProviderUnderdark.denyDecor ? 1 : 0);
    }
    
    public IChunkProvider createChunkGenerator() {
        return (IChunkProvider)new ChunkProviderUnderdark(this.worldObj, this.worldObj.getSeed(), false);
    }
    
    public int getAverageGroundLevel() {
        return 81;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(final int par1, final int par2) {
        return true;
    }
    
    public String getDimensionName() {
        return "Underdark";
    }
    
    public boolean renderStars() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(final World world, final float f) {
        return 0.0f;
    }
    
    public boolean renderClouds() {
        return false;
    }
    
    public boolean renderVoidFog() {
        return true;
    }
    
    public boolean renderEndSky() {
        return false;
    }
    
    public float setSunSize() {
        return 0.0f;
    }
    
    public float setMoonSize() {
        return 0.0f;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor() {
        return 1.0;
    }
    
    public boolean canRespawnHere() {
        return false;
    }
    
    public boolean isSurfaceWorld() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return 256.0f;
    }
    
    public boolean canCoordinateBeSpawn(final int par1, final int par2) {
        return false;
    }
    
    public ChunkCoordinates getEntrancePortalLocation() {
        return new ChunkCoordinates(50, 5, 0);
    }
    
    protected void generateLightBrightnessTable() {
        final float f = 0.0f;
        for (int i = 0; i <= 15; ++i) {
            final float p = i / 15.0f;
            final float f2 = 1.0f - p;
            this.lightBrightnessTable[i] = p / (f2 * 3.0f + 1.0f);
            if (this.lightBrightnessTable[i] < 0.2f) {
                final float[] lightBrightnessTable = this.lightBrightnessTable;
                final int n = i;
                lightBrightnessTable[n] *= this.lightBrightnessTable[i] / 0.2f;
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public String getWelcomeMessage() {
        if (this instanceof WorldProviderUnderdark) {
            return "Entering the 'Deep Dark'";
        }
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(final float par1, final float par2) {
        return null;
    }
    
    public float calculateCelestialAngle(final long par1, final float par3) {
        final int j = 18000;
        float f1 = (j + par3) / 24000.0f - 0.25f;
        if (f1 < 0.0f) {
            ++f1;
        }
        if (f1 > 1.0f) {
            --f1;
        }
        final float f2 = f1;
        f1 = 1.0f - (float)((Math.cos(f1 * 3.141592653589793) + 1.0) / 2.0);
        f1 = f2 + (f1 - f2) / 3.0f;
        return f1;
    }
    
    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(final float par1, final float par2) {
        return Vec3.createVectorHelper(9.999999974752427E-7, 9.999999974752427E-7, 9.999999974752427E-7);
    }
}

