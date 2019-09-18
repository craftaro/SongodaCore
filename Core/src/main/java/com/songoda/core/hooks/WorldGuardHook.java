package com.songoda.core.hooks;

import com.songoda.core.hooks.worldguard.WorldGuardFlagHandler;
import org.bukkit.Chunk;
import org.bukkit.Location;

public class WorldGuardHook {

    static boolean canHook = false;

    static {
         try {
            // if this class exists, we're good to use WG classes
            Class.forName("com.sk89q.worldguard.protection.flags.Flag");
            canHook = true;
        } catch (ClassNotFoundException ex) {
        }
    }

    /**
     * Attempt to register a worldGuard flag (ALLOW/DENY) <br />
     * Note: This must be called before WorldGuard loads, or it will fail.
     * 
     * @param flag name of the flag to set
     * @param state default value of the flag
     */
    public static void addHook(String flag, boolean state) {
        if(canHook) {
            WorldGuardFlagHandler.addHook(flag, state);
        }
    }

    /**
     * Check to see if WorldGuard is installed and hooked
     *
     * @return true if and only if WorldGuard exists and addHook() has been
     * called and added successfully
     */
    public static boolean isEnabled() {
        return canHook && WorldGuardFlagHandler.isEnabled();
    }

    /**
     * Checks this location to see what this flag is set to
     *
     * @param l location to check
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Location l, String flag) {
        return canHook ? WorldGuardFlagHandler.getBooleanFlag(l, flag) : null;
    }

    /**
     * Query all regions that are in or intersect this chunk
     *
     * @param c chunk to check for regions in
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Chunk c, String flag) {
        return canHook ? WorldGuardFlagHandler.getBooleanFlag(c, flag) : null;
    }
}
