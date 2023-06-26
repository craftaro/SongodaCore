package com.craftaro.core.world;

import com.craftaro.core.CraftaroCore;
import com.craftaro.core.utils.EntityUtils;
import com.cryptomorin.xseries.XMaterial;
import com.craftaro.core.nms.world.SpawnedEntity;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SSpawner {
    protected final com.craftaro.core.nms.world.SSpawner sSpawner;
    protected final Location location;

    public SSpawner(Location location) {
        this.location = location;
        this.sSpawner = CraftaroCore.getInstance().getNMSGetter().getWorld().getSpawner(location);
    }

    public SSpawner(CreatureSpawner spawner) {
        this(spawner.getLocation());
    }

    public int spawn(int amountToSpawn, EntityType... types) {
        return spawn(amountToSpawn, "EXPLOSION_NORMAL", null, null, types);
    }

    /**
     * Spawn the spawner.
     * <p>
     * If you want support for stackers you will need to load them
     * on your plugins enable.
     *
     * @return amount of entities spawned
     */
    public int spawn(int amountToSpawn, String particle, Set<XMaterial> canSpawnOn, SpawnedEntity spawned,
                     EntityType... types) {
        if (location.getWorld() == null) {
            return 0;
        }

        if (canSpawnOn == null) {
            canSpawnOn = new HashSet<>();
        }

        if (canSpawnOn.isEmpty()) {
            canSpawnOn.addAll(EntityUtils.getSpawnBlocks(types[0]));
        }

        // TODO: Add stacker plugin support. Refer to CraftaroCore v2.

        int spawnCountUsed = amountToSpawn;

        int amountSpawned = 0;
        while (spawnCountUsed-- > 0) {
            EntityType type = types[ThreadLocalRandom.current().nextInt(types.length)];
            LivingEntity entity = sSpawner.spawnEntity(type, particle, spawned, canSpawnOn);
            if (entity != null) {
                amountSpawned++;
            }
        }

        return amountSpawned;
    }

    public Location getLocation() {
        return location;
    }
}
