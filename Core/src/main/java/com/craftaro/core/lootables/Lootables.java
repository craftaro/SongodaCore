package com.craftaro.core.lootables;

import com.craftaro.core.lootables.loot.LootManager;

public class Lootables {
    private final String lootablesDir;

    private final LootManager lootManager;

    public Lootables(String lootablesDir) {
        this.lootablesDir = lootablesDir;
        this.lootManager = new LootManager(this);
    }

    public String getLootablesDir() {
        return lootablesDir;
    }

    public LootManager getLootManager() {
        return lootManager;
    }
}
