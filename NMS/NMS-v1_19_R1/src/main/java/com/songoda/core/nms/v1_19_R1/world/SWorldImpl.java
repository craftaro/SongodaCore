package com.songoda.core.nms.v1_19_R1.world;

import com.songoda.core.nms.world.SWorld;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class SWorldImpl implements SWorld {
    private final World world;

    public SWorldImpl(World world) {
        this.world = world;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> result = new ArrayList<>();

        ServerLevel worldServer = ((CraftWorld) world).getHandle();
        LevelEntityGetter<Entity> entities = worldServer.entityManager.getEntityGetter();

        entities.getAll().forEach((mcEnt) -> {
            org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();

            if (bukkitEntity instanceof LivingEntity && bukkitEntity.isValid()) {
                result.add((LivingEntity) bukkitEntity);
            }
        });

        return result;
    }
}
