package com.songoda.core.library.entitystacker;

import com.songoda.core.library.entitystacker.stackers.StackMob;
import com.songoda.core.library.entitystacker.stackers.Stacker;
import com.songoda.core.library.entitystacker.stackers.UltimateStacker;
import com.songoda.core.library.entitystacker.stackers.WildStacker;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public enum EntityStackerType {

    ULTIMATE_STACKER("UltimateStacker", UltimateStacker.class),
    WILD_STACKER("WildStacker", WildStacker.class),
    STACK_MOB("StackMob", StackMob.class);

    public final String plugin;
    protected final Class managerClass;

    private EntityStackerType(String plugin, Class managerClass) {
        this.plugin = plugin;
        this.managerClass = managerClass;
    }

    protected Stacker getInstance() {
        try {
            return (Stacker) managerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unexpected Error while creating a new Stacker Manager for " + name(), ex);
        }
        return null;
    }
}
