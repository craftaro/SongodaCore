package com.songoda.core.nms.world;

import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;

public interface WorldCore {

    SSpawner getSpawner(CreatureSpawner spawner);

    SSpawner getSpawner(Location location);
}
