package com.craftaro.core.world;

import com.craftaro.core.database.SerializedLocation;
import com.craftaro.core.nms.Nms;
import com.craftaro.core.nms.world.SpawnedEntity;
import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.core.hooks.EntityStackerManager;
import com.craftaro.core.utils.EntityUtils;
import org.bukkit.Location;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SSpawner {
    protected com.craftaro.core.nms.world.SSpawner sSpawner;
    protected Location location;

    public SSpawner(Location location) {
        this.location = location;
        this.sSpawner = Nms.getImplementations().getWorld().getSpawner(location);
    }

    public SSpawner(CreatureSpawner spawner) {
        this(spawner.getLocation());
    }

    public void initFromData(Map<String, Object> data) {
        location = SerializedLocation.of(data);
        sSpawner = Nms.getImplementations().getWorld().getSpawner(location);
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
    public int spawn(int amountToSpawn, String particle, Set<CompatibleMaterial> canSpawnOn, SpawnedEntity spawned,
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

        boolean useStackPlugin = EntityStackerManager.isEnabled();

        int spawnCountUsed = useStackPlugin ? 1 : amountToSpawn;

        int amountSpawned = 0;
        while (spawnCountUsed-- > 0) {
            EntityType type = types[ThreadLocalRandom.current().nextInt(types.length)];
            LivingEntity entity = sSpawner.spawnEntity(type, particle, spawned, canSpawnOn);

            if (entity != null) {
                // If this entity is indeed stackable then spawn a single stack with the desired stack size.
                if (useStackPlugin && amountToSpawn >= EntityStackerManager.getMinStackSize(type)) {
                    EntityStackerManager.add(entity, amountToSpawn);
                    amountSpawned = amountToSpawn;

                    break;
                }

                amountSpawned++;
            }
        }

        return amountSpawned;
    }

    public Location getLocation() {
        return location;
    }
}
