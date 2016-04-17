// 
// Decompiled by Procyon v0.5.30
// 

package com.rwtema.extrautils;

import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper
{
    public static Logger logger;
    public static boolean isDeObf;
    
    public static void debug(final Object info, final Object... info2) {
        if (LogHelper.isDeObf) {
            String temp = "Debug: " + info;
            for (final Object t : info2) {
                temp = temp + " " + t;
            }
            LogHelper.logger.info(info);
        }
    }
    
    public static void info(final Object info, final Object... info2) {
        String temp = "" + info;
        for (final Object t : info2) {
            temp = temp + " " + t;
        }
        LogHelper.logger.info(info);
    }
    
    public static void fine(final Object info, final Object... info2) {
        String temp = "" + info;
        for (final Object t : info2) {
            temp = temp + " " + t;
        }
        LogHelper.logger.debug(temp);
    }
    
    public static void errorThrowable(final String message, final Throwable t) {
        LogHelper.logger.error(message, t);
    }
    
    public static void error(final Object info, final Object... info2) {
        String temp = "" + info;
        for (final Object t : info2) {
            temp = temp + " " + t;
        }
        LogHelper.logger.error(info);
    }
    
    static {
        LogHelper.logger = LogManager.getLogger("extrautils");
        LogHelper.isDeObf = false;
        try {
            World.class.getMethod("getBlock", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            LogHelper.isDeObf = true;
        }
        catch (Throwable ex) {
            LogHelper.isDeObf = false;
        }
    }
}
