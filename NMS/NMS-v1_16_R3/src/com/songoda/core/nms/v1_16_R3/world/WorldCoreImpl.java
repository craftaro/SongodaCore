package com.songoda.core.nms.v1_16_R3.world;

import com.songoda.core.nms.v1_16_R3.world.spawner.SSpawnerImpl;
import com.songoda.core.nms.world.SSpawner;
import com.songoda.core.nms.world.WorldCore;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;

public class WorldCoreImpl implements WorldCore {

    @Override
    public SSpawner getSpawner(CreatureSpawner spawner) {
        return new SSpawnerImpl(spawner.getLocation());
    }

    @Override
    public SSpawner getSpawner(Location location) {
        return new SSpawnerImpl(location);
    }
}
