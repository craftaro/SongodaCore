package com.songoda.core.hooks.stackers;

import dev.rosewood.rosestacker.api.RoseStackerAPI;
import dev.rosewood.rosestacker.stack.StackedEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class RoseStacker extends Stacker {
    final Plugin plugin;

    public RoseStacker(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "RoseStacker";
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
        if (RoseStackerAPI.getInstance().isItemStacked(item))
            RoseStackerAPI.getInstance().getStackedItem(item).setStackSize(amount);
        else item.getItemStack().setAmount(amount);
    }

    @Override
    public int getItemAmount(Item item) {
        if (RoseStackerAPI.getInstance().isItemStacked(item))
            return RoseStackerAPI.getInstance().getStackedItem(item).getStackSize();
        return item.getItemStack().getAmount();
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return RoseStackerAPI.getInstance().isEntityStacked(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return RoseStackerAPI.getInstance().getStackedEntity(entity).getStackSize();
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = RoseStackerAPI.getInstance().getStackedEntity(entity);
        for (int times = 0; amount < times; times++) {
            stackedEntity.decreaseStackSize(); // last in, first out
        }
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        StackedEntity stackedEntity = RoseStackerAPI.getInstance().getStackedEntity(entity);
        for (int times = 0; amount < times; times++) {
            stackedEntity.increaseStackSize(stackedEntity.getEntity()); // copy the default entity
        }
    }

    @Override
    public int getMinStackSize(EntityType type) {
        return RoseStackerAPI.getInstance().getEntityStackSettings(type).getMinStackSize();
    }
}
