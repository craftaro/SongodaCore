package com.craftaro.core.nms.world;

import org.bukkit.entity.LivingEntity;

public interface SpawnedEntity {
    boolean onSpawn(LivingEntity entity);
}
