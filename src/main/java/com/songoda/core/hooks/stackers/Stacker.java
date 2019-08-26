package com.songoda.core.hooks.stackers;

import org.bukkit.entity.LivingEntity;

public abstract class Stacker {

    public abstract boolean isStacked(LivingEntity entity);

    public abstract int getSize(LivingEntity entity);

    public void removeOne(LivingEntity entity) {
        remove(entity, 1);
    }

    public abstract void remove(LivingEntity entity, int amount);

    public void addOne(LivingEntity entity) {
        add(entity, 1);
    }

    public abstract void add(LivingEntity entity, int amount);
}
