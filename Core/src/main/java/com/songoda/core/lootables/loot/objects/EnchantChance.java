package com.songoda.core.lootables.loot.objects;

import org.bukkit.enchantments.Enchantment;

public class EnchantChance {
    private final Enchantment enchantment;
    private final int level;
    private final double chanceOverride;

    public EnchantChance(Enchantment enchantment, int level, double chanceOverride) {
        this.enchantment = enchantment;
        this.level = level;
        this.chanceOverride = chanceOverride;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public double getChanceOverride() {
        return chanceOverride;
    }
}
