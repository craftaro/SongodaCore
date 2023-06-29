package com.craftaro.core.hooks.stackers;

import com.craftaro.ultimatestacker.api.UltimateStackerAPI;
import com.craftaro.ultimatestacker.api.stack.entity.EntityStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class UltimateStacker extends Stacker {
    private final Plugin plugin;

    public UltimateStacker() {
        this.plugin = Bukkit.getPluginManager().getPlugin("UltimateStacker");
    }

    @Override
    public String getName() {
        return "UltimateStacker";
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public boolean supportsItemStacking() {
        return true;
    }

    @Override
    public boolean supportsEntityStacking() {
        return true;
    }

    @Override
    public void setItemAmount(Item item, int amount) {
        UltimateStackerAPI.getStackedItemManager().getStackedItem(item, true).setAmount(amount);
    }

    @Override
    public int getItemAmount(Item item) {
        return UltimateStackerAPI.getStackedItemManager().getActualItemAmount(item);
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return UltimateStackerAPI.getEntityStackManager().isStackedEntity(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return isStacked(entity) ? UltimateStackerAPI.getEntityStackManager().getStackedEntity(entity).getAmount() : 0;
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        EntityStack stack = UltimateStackerAPI.getEntityStackManager().getStackedEntity(entity);
        stack.take(amount);
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        EntityStack stack = UltimateStackerAPI.getEntityStackManager().getStackedEntity(entity);
        stack.add(amount);
    }

    @Override
    public int getMinStackSize(EntityType type) {
        return ((Plugin) plugin).getConfig().getInt("Entities.Min Stack Amount", 1);
    }
}
