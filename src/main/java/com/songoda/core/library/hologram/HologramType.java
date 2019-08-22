package com.songoda.core.library.hologram;

import com.songoda.core.library.economy.economies.Economy;
import com.songoda.core.library.economy.economies.PlayerPointsEconomy;
import com.songoda.core.library.economy.economies.ReserveEconomy;
import com.songoda.core.library.economy.economies.VaultEconomy;
import com.songoda.core.library.hologram.holograms.Hologram;
import com.songoda.core.library.hologram.holograms.HolographicDisplaysHologram;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public enum HologramType {

    VAULT("HolographicDisplays", HolographicDisplaysHologram.class);

    public final String plugin;
    protected final Class managerClass;

    private HologramType(String plugin, Class managerClass) {
        this.plugin = plugin;
        this.managerClass = managerClass;
    }

    protected Hologram getInstance() {
        try {
            return (Hologram) managerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Unexpected Error while creating a new Hologram Manager for " + name(), ex);
        }
        return null;
    }
}
