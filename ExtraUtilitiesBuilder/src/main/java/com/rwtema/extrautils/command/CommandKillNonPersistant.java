// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.command;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class CommandKillNonPersistant extends CommandKillEntities
{
    public CommandKillNonPersistant(final String type, final Class<? extends Entity> entityclass, final boolean except) {
        super("despawns", null, true);
    }
    
    @Override
    public void killEntities(final World world) {
        for (int i = 0; i < world.loadedEntityList.size(); ++i) {
            if (!(  ( (Entity)world.loadedEntityList.get(i) ).isNoDespawnRequired())  ) {
                ++this.numKills;
                ((Entity)world.loadedEntityList.get(i)).setDead();
            }
        }
    }
}

