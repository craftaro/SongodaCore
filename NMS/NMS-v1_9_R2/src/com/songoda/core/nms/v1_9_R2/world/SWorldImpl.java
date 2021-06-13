package com.songoda.core.nms.v1_9_R2.world;

import com.songoda.core.nms.world.SWorld;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class SWorldImpl implements SWorld {

    public SWorldImpl() {
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        return new ArrayList<>();
    }
}
