package com.songoda.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityUtils {

    private static Class<?> clazzEntityInsentient, clazzEntity, clazzCraftEntity;

    private static Field aware, fromMobSpawner;

    private static Method methodGetHandle;

    static {
        try {
            String ver = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            clazzEntityInsentient = Class.forName("net.minecraft.server." + ver + ".EntityInsentient");
            clazzEntity = Class.forName("net.minecraft.server." + ver + ".Entity");
            clazzCraftEntity = Class.forName("org.bukkit.craftbukkit." + ver + ".entity.CraftEntity");
            methodGetHandle = clazzCraftEntity.getDeclaredMethod("getHandle");
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            aware = clazzEntityInsentient.getField("aware");
        } catch (NoSuchFieldException e) {
            try {
                fromMobSpawner = clazzEntity.getField("fromMobSpawner");
            } catch (NoSuchFieldException ee) {
                ee.printStackTrace();
            }
        }
    }

    public static void setUnaware(LivingEntity entity) {
        try {
            setUnaware(methodGetHandle.invoke(clazzCraftEntity.cast(entity)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void setUnaware(Object entity) {
        try {
            if (aware != null)
                aware.setBoolean(entity, false);
            else
                fromMobSpawner.setBoolean(entity, true);
        } catch (IllegalAccessException ee) {
            ee.printStackTrace();
        }
    }

    public static boolean isAware(LivingEntity entity) {
        try {
            return isAware(methodGetHandle.invoke(clazzCraftEntity.cast(entity)));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAware(Object entity) {
        try {
            if (aware != null)
                return aware.getBoolean(entity);
            else
                return fromMobSpawner.getBoolean(entity);
        } catch (IllegalAccessException ee) {
            ee.printStackTrace();
        }
        return false;
    }

}
