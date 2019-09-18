package com.songoda.core.hooks.stackers;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

public class WildStacker extends Stacker {

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
}
