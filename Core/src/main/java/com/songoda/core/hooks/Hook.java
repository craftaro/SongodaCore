package com.songoda.core.hooks;

public interface Hook {
    /**
     * Get the name of the plugin being used
     */
    String getName();

    /**
     * Check to see if the economy plugin being used is active
     *
     * @return true if the plugin is loaded and active
     */
    boolean isEnabled();
}
