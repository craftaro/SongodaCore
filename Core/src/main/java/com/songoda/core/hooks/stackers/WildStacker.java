package com.songoda.core.hooks.stackers;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class WildStacker extends Stacker {

    final Plugin plugin;

    public WildStacker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "WildStacker";
    }

    @Override
    public boolean isEnabled() {
        return true;
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
        WildStackerAPI.getStackedItem(item).setStackAmount(amount, true);
    }

    @Override
    public int getItemAmount(Item item) {
        return WildStackerAPI.getItemAmount(item);
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return WildStackerAPI.getEntityAmount(entity) != 0;
    }

    @Override
    public int getSize(LivingEntity entity) {
        return WildStackerAPI.getEntityAmount(entity);
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = WildStackerAPI.getStackedEntity(entity);
        stackedEntity.setStackAmount(stackedEntity.getStackAmount() - amount, true);
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = WildStackerAPI.getStackedEntity(entity);
        stackedEntity.setStackAmount(stackedEntity.getStackAmount() + amount, true);
    }

    @Override
    public int minimumEntityStack(EntityType type) {
        int min = plugin.getConfig().getInt("entities.minimum-limits." + type.name(), -1);
        if (min == -1) {
            min = plugin.getConfig().getInt("entities.minimum-limits.all", -1);
        }
        return min == -1 ? 0 : min;
    }

}
