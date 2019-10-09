package com.songoda.core.hooks;

public class McMMOHook {

    static boolean canHook = false;

    static {
         try {
            // if this class exists, we're good to use WG classes
            Class.forName("com.sk89q.worldguard.protection.flags.Flag");
            canHook = true;
        } catch (ClassNotFoundException ex) {
        }
    }
    
    
}
