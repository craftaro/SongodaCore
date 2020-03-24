package com.songoda.core.compatibility;

import org.bukkit.inventory.EquipmentSlot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Get which hand is being used.
 *
 * @author Brianna
 * @since 2020-03-24
 */
public enum CompatibleHand {

    MAIN_HAND, OFF_HAND;

    private static Map<String, Method> methodCache = new HashMap<>();

    public static CompatibleHand getHand(Object event) {
        try {
            Class<?> clazz = event.getClass();
            String className = clazz.getName();
            Method method;
            if (methodCache.containsKey(className)) {
                method = methodCache.get(className);
            } else {
                method = clazz.getDeclaredMethod("getHand");
                methodCache.put(className, method);
            }
            EquipmentSlot slot = (EquipmentSlot) method.invoke(event);
            if (slot == EquipmentSlot.OFF_HAND)
                return OFF_HAND;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return MAIN_HAND;
        }
        return MAIN_HAND;
    }

    public static CompatibleHand getHand(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.HAND ? MAIN_HAND : OFF_HAND;
    }
}
