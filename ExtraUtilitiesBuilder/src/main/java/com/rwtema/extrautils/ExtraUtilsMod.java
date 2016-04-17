// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod;

@Mod(modid = "ExtraUtilities", name = "ExtraUtilities", dependencies = "required-after:Forge@[10.13.2.1291,);after:ForgeMultipart@[1.2.0.336,);after:Baubles;after:ThermalFoundation;after:EE3;before:TConstruct@[1.7.10-1.8.5,)")
public class ExtraUtilsMod
{
    public static final String modId = "ExtraUtilities";
    @SidedProxy(clientSide = "com.rwtema.extrautils.ExtraUtilsClient", serverSide = "com.rwtema.extrautils.ExtraUtilsProxy")
    public static ExtraUtilsProxy proxy;
    @Mod.Instance("ExtraUtilities")
    public static ExtraUtilsMod instance;
    public static ExtraUtils extraUtils;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        (ExtraUtilsMod.extraUtils = new ExtraUtils()).preInit(event);
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        ExtraUtilsMod.extraUtils.init(event);
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        ExtraUtilsMod.extraUtils.postInit(event);
    }
    
    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        ExtraUtilsMod.extraUtils.serverStarting(event);
    }
    
    @Mod.EventHandler
    public void serverStart(final FMLServerStartingEvent event) {
        ExtraUtilsMod.extraUtils.serverStart(event);
    }
    
    @Mod.EventHandler
    public void remap(final FMLMissingMappingsEvent event) {
        ExtraUtilsMod.extraUtils.remap(event);
    }
    
    @Mod.EventHandler
    public void loadComplete(final FMLLoadCompleteEvent event) {
        ExtraUtilsMod.extraUtils.loadComplete(event);
    }
}
