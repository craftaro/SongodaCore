package com.craftaro.core.database;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SerializedLocation {

    private String world;
    private double x;
    private double y;
    private double z;
    private float pitch = 0;
    private float yaw = 0;

    public SerializedLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        if (location.getPitch() != 0 && location.getYaw() != 0) {
            this.pitch = location.getPitch();
            this.yaw = location.getYaw();
        }
    }

    public static Location of(Map<String, Object> map) {
        return new Location(Bukkit.getWorld((String) map.get("world")),
                (double) map.get("x"),
                (double) map.get("y"),
                (double) map.get("z"),
                Double.valueOf((double)map.getOrDefault("yaw", 0.0)).floatValue(),
                Double.valueOf((double)map.getOrDefault("pitch", 0.0)).floatValue());
    }

    public static Map<String, Object> of(Location location) {
        Map<String, Object> map = new HashMap<>();
        map.put("world", location.getWorld().getName());
        map.put("x", location.getX());
        map.put("y", location.getY());
        map.put("z", location.getZ());
        if (location.getPitch() != 0 && location.getYaw() != 0) {
            map.put("pitch", location.getPitch());
            map.put("yaw", location.getYaw());
        }
        return map;
    }

    public Location asLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("world", world);
        map.put("x", x);
        map.put("y", y);
        map.put("z", z);
        if (pitch != 0 && yaw != 0) {
            map.put("pitch", pitch);
            map.put("yaw", yaw);
        }
        return map;
    }
}
