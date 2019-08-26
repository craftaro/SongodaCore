package com.songoda.core.hooks.stackers;

import com.songoda.ultimatestacker.entity.EntityStack;
import org.bukkit.entity.LivingEntity;

public class UltimateStacker extends Stacker {

    private final com.songoda.ultimatestacker.UltimateStacker plugin;

    public UltimateStacker() {
        this.plugin = com.songoda.ultimatestacker.UltimateStacker.getInstance();
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return plugin.getEntityStackManager().isStacked(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return isStacked(entity) ? plugin.getEntityStackManager().getStack(entity).getAmount() : 0;
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        EntityStack stack = plugin.getEntityStackManager().getStack(entity);
        stack.setAmount(stack.getAmount() - amount);
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        plugin.getEntityStackManager().getStack(entity).addAmount(amount);
    }
}
