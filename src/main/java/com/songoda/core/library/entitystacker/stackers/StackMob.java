package com.songoda.core.library.entitystacker.stackers;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import uk.antiperson.stackmob.api.EntityManager;
import uk.antiperson.stackmob.api.StackedEntity;

public class StackMob extends Stacker {

    private final EntityManager plugin;

    public StackMob() {
        this.plugin = new EntityManager((uk.antiperson.stackmob.StackMob)
                Bukkit.getPluginManager().getPlugin("StackMob"));
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return plugin.isStackedEntity(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return plugin.getStackedEntity(entity).getSize();
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = plugin.getStackedEntity(entity);
        stackedEntity.setSize(stackedEntity.getSize() - amount);
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = plugin.getStackedEntity(entity);
        stackedEntity.setSize(stackedEntity.getSize() + amount);
    }
}
