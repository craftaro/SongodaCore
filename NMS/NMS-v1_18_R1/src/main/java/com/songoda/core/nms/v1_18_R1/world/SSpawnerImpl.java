package com.songoda.core.nms.v1_18_R1.world;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.compatibility.CompatibleParticleHandler;
import com.songoda.core.nms.world.SSpawner;
import com.songoda.core.nms.world.SpawnedEntity;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.level.MobSpawnerData;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class SSpawnerImpl implements SSpawner {
    private final Location spawnerLocation;

    public SSpawnerImpl(Location location) {
        this.spawnerLocation = location;
    }

    @Override
    public LivingEntity spawnEntity(EntityType type, Location spawnerLocation) {
        return spawnEntity(type, "EXPLOSION_NORMAL", null, null);
    }

    @Override
    public LivingEntity spawnEntity(EntityType type, String particleType, SpawnedEntity spawned,
                                    Set<CompatibleMaterial> canSpawnOn) {
        MobSpawnerData data = new MobSpawnerData();
        NBTTagCompound compound = data.a();

        String name = type.name().toLowerCase().replace("snowman", "snow_golem")
                .replace("mushroom_cow", "mooshroom");
        compound.a("id", "minecraft:" + name);

        short spawnRange = 4;
        for (int i = 0; i < 50; i++) {
            assert spawnerLocation.getWorld() != null;
            WorldServer world = ((CraftWorld) spawnerLocation.getWorld()).getHandle();

            Random random = world.r_();
            double x = spawnerLocation.getX() + (random.nextDouble() - random.nextDouble()) * (double) spawnRange + 0.5D;
            double y = spawnerLocation.getY() + random.nextInt(3) - 1;
            double z = spawnerLocation.getZ() + (random.nextDouble() - random.nextDouble()) * (double) spawnRange + 0.5D;

            Optional<Entity> optionalEntity = EntityTypes.a(compound, world);
            if (optionalEntity.isEmpty()) continue;

            Entity entity = optionalEntity.get();
            entity.e(x, y, z);

            BlockPosition position = entity.cW();
            DifficultyDamageScaler damageScaler = world.d_(position);

            if (!(entity instanceof EntityInsentient entityInsentient)) {
                continue;
            }

            Location spot = new Location(spawnerLocation.getWorld(), x, y, z);

            if (!canSpawn(world, entityInsentient, spot, canSpawnOn)) {
                continue;
            }

            entityInsentient.a(world, damageScaler, EnumMobSpawn.c, null, null);

            LivingEntity craftEntity = (LivingEntity) entity.getBukkitEntity();

            if (spawned != null && !spawned.onSpawn(craftEntity)) {
                return null;
            }

            if (particleType != null) {
                float xx = (float) (0 + (Math.random() * 1));
                float yy = (float) (0 + (Math.random() * 2));
                float zz = (float) (0 + (Math.random() * 1));

                CompatibleParticleHandler.spawnParticles(CompatibleParticleHandler.ParticleType.getParticle(particleType),
                        spot, 5, xx, yy, zz, 0);
            }

            world.addFreshEntity(entity, CreatureSpawnEvent.SpawnReason.SPAWNER);

            spot.setYaw(random.nextFloat() * 360.0F);
            craftEntity.teleport(spot);

            return craftEntity;
        }

        return null;
    }

    private boolean canSpawn(WorldServer world, EntityInsentient entityInsentient, Location location,
                             Set<CompatibleMaterial> canSpawnOn) {
        if (!world.a(entityInsentient, entityInsentient.cw())) {
            return false;
        }

        CompatibleMaterial spawnedIn = CompatibleMaterial.getMaterial(location.getBlock());
        CompatibleMaterial spawnedOn = CompatibleMaterial.getMaterial(location.getBlock().getRelative(BlockFace.DOWN));

        if (spawnedIn == null || spawnedOn == null) {
            return false;
        }

        if (!spawnedIn.isAir() &&
                spawnedIn != CompatibleMaterial.WATER &&
                !spawnedIn.name().contains("PRESSURE") &&
                !spawnedIn.name().contains("SLAB")) {
            return false;
        }

        for (CompatibleMaterial material : canSpawnOn) {
            if (material == null) continue;

            if (spawnedOn.equals(material) || material.isAir()) {
                return true;
            }
        }

        return false;
    }
}
