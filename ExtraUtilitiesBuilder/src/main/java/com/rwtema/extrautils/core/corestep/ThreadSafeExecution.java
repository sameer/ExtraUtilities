// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils.core.corestep;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import cpw.mods.fml.common.gameevent.TickEvent;
import com.rwtema.extrautils.ExtraUtilsMod;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;

public class ThreadSafeExecution
{
    public static final ArrayList<IDelayCallable> clientCallable;
    public static final ArrayList<IDelayCallable> serverCallable;
    
    public static void assignCode(final Side side, final IDelayCallable delayCallable) {
        if (side.isClient()) {
            if (ExtraUtilsMod.proxy.isClientSideAvailable()) {
                synchronized (ThreadSafeExecution.clientCallable) {
                    ThreadSafeExecution.clientCallable.add(delayCallable);
                }
            }
        }
        else {
            synchronized (ThreadSafeExecution.serverCallable) {
                ThreadSafeExecution.serverCallable.add(delayCallable);
            }
        }
    }
    
    @SubscribeEvent
    public void server(final TickEvent.ServerTickEvent server) {
        synchronized (ThreadSafeExecution.serverCallable) {
            for (final IDelayCallable iDelayCallable : ThreadSafeExecution.serverCallable) {
                try {
                    iDelayCallable.call();
                }
                catch (Exception e) {
                    new RuntimeException("Network code failed on Server: " + e.toString(), e).printStackTrace();
                }
            }
            ThreadSafeExecution.serverCallable.clear();
        }
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void client(final TickEvent.ClientTickEvent server) {
        synchronized (ThreadSafeExecution.clientCallable) {
            for (final IDelayCallable iDelayCallable : ThreadSafeExecution.clientCallable) {
                try {
                    iDelayCallable.call();
                }
                catch (Exception e) {
                    new RuntimeException("Network code failed on Client: " + e.toString(), e).printStackTrace();
                }
            }
            ThreadSafeExecution.clientCallable.clear();
        }
    }
    
    static {
        FMLCommonHandler.instance().bus().register((Object)new ThreadSafeExecution());
        clientCallable = new ArrayList<IDelayCallable>();
        serverCallable = new ArrayList<IDelayCallable>();
    }
}
