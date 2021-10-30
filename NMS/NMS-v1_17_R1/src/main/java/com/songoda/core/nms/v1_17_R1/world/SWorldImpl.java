package com.songoda.core.nms.v1_17_R1.world;

import com.songoda.core.nms.world.SWorld;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SWorldImpl implements SWorld {
    private final World world;

    private static Field fieldG;

    static {
        try {
            fieldG = WorldServer.class.getDeclaredField("G");
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }

        fieldG.setAccessible(true);
    }

    public SWorldImpl(World world) {
        this.world = world;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<>();
        try {

            WorldServer worldServer = ((CraftWorld) world).getHandle();
            LevelEntityGetter<net.minecraft.world.entity.Entity> entities =
                    ((PersistentEntitySectionManager<Entity>) fieldG.get(worldServer)).d();

            entities.a().forEach((mcEnt) -> {
                org.bukkit.entity.Entity bukkitEntity = mcEnt.getBukkitEntity();
                if (bukkitEntity instanceof LivingEntity && bukkitEntity.isValid()) {
                    list.add((LivingEntity) bukkitEntity);
                }
            });
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}
