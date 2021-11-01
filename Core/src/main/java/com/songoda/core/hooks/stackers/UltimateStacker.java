package com.songoda.core.hooks.stackers;

import com.songoda.ultimatestacker.stackable.entity.EntityStack;
import com.songoda.ultimatestacker.utils.Methods;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class UltimateStacker extends Stacker {
    private final com.songoda.ultimatestacker.UltimateStacker plugin;

    public UltimateStacker() {
        this.plugin = com.songoda.ultimatestacker.UltimateStacker.getInstance();
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
        com.songoda.ultimatestacker.UltimateStacker.updateItemAmount(item, amount);
    }

    @Override
    public int getItemAmount(Item item) {
        return Methods.getActualItemAmount(item);
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return plugin.getEntityStackManager().isStackedAndLoaded(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return isStacked(entity) ? plugin.getEntityStackManager().getStack(entity).getAmount() : 0;
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        EntityStack stack = plugin.getEntityStackManager().getStack(entity);
        stack.takeEntities(amount);
        stack.updateStack();
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        plugin.getEntityStackManager().addStack(entity, amount);
    }

    @Override
    public int getMinStackSize(EntityType type) {
        return ((Plugin) plugin).getConfig().getInt("Entities.Min Stack Amount");
    }
}
