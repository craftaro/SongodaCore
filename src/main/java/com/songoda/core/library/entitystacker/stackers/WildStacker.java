package com.songoda.core.library.entitystacker.stackers;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;
import org.bukkit.entity.LivingEntity;

public class WildStacker extends Stacker {

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
