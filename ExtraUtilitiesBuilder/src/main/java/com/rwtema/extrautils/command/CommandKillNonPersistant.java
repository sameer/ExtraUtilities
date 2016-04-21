// 
// ExtraUtilities decompiled and fixed by Robotia https://github.com/Robotia
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
	    Entity e = (Entity)world.loadedEntityList.get(i);
	    if(!(e instanceof EntityLiving))continue;
	    EntityLiving el = (EntityLiving)e;
            if (!el.isNoDespawnRequired()) {
                ++this.numKills;
                (el).setDead();
            }
        }
    }
}


