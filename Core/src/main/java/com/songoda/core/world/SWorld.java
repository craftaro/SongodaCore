package com.songoda.core.world;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.NmsManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.List;

public class SWorld {

    protected final com.songoda.core.nms.world.SWorld sWorld;
    protected final World world;

    public SWorld(World world) {
        this.world = world;
        this.sWorld = NmsManager.getWorld().getWorld(world);
    }

    public Entity[] getEntitiesFromChunk(int x, int z) {
        Location location = new Location(null, 0.0D, 0.0D, 0.0D);
        return getLivingEntities().stream().filter((entity) -> {
            entity.getLocation(location);
            return location.getBlockX() >> 4 == x && location.getBlockZ() >> 4 == z;
        }).toArray(Entity[]::new);
    }

    public List<LivingEntity> getLivingEntities() {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_17))
            return world.getLivingEntities();
        return sWorld.getLivingEntities();
    }

    public World getWorld() {
        return world;
    }
}
