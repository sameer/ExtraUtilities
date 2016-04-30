// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
// 

package com.rwtema.extrautils.particle;

import net.minecraft.world.World;

public enum Particles
{
    BUBBLE, 
    SUSPENDED, 
    DEPTHSUSPEND, 
    TOWNAURA, 
    CRIT, 
    MAGICCRIT, 
    SMOKE, 
    MOBSPELL, 
    MOBSPELLAMBIENT, 
    SPELL, 
    INSTANTSPELL, 
    WITCHMAGIC, 
    NOTE, 
    PORTAL, 
    ENCHANTMENTTABLE, 
    EXPLODE, 
    FLAME, 
    LAVA, 
    FOOTSTEP, 
    SPLASH, 
    LARGESMOKE, 
    CLOUD, 
    REDDUST, 
    SNOWBALLPOOF, 
    DRIPWATER, 
    DRIPLAVA, 
    SNOWSHOVEL, 
    SLIME, 
    HEART, 
    ANGRYVILLAGER, 
    HAPPYVILLAGER;
    
    public final String id;
    
    private Particles() {
        this.id = this.name().toLowerCase();
    }
    
    public void spawn(final World world, final double x, final double y, final double z) {
        this.spawn(world, x, y, z, 0, 0, 0);
    }
    
    public void spawn(final World world, final double x, final double y, final double z, final int r, final int g, final int b) {
        world.spawnParticle(this.id, x, y, z, (double)r, (double)g, (double)b);
    }
}


