package com.songoda.core.compatibility.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Get which hand is being used.
 */
public enum CompatibleHand {
    MAIN_HAND, OFF_HAND;

    private static final Map<String, Method> methodCache = new HashMap<>();

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

            if (slot == EquipmentSlot.OFF_HAND) {
                return OFF_HAND;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
        }

        return MAIN_HAND;
    }

    public static CompatibleHand getHand(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.HAND ? MAIN_HAND : OFF_HAND;
    }

    /**
     * Use up whatever item the player is holding in their main hand
     *
     * @param player player to grab item from
     */
    public void takeItem(Player player) {
        takeItem(player, 1);
    }

    /**
     * Use up whatever item the player is holding in their main hand
     *
     * @param player player to grab item from
     * @param amount number of items to use up
     */
    public void takeItem(Player player, int amount) {
        ItemStack item = this == CompatibleHand.MAIN_HAND
                ? player.getInventory().getItemInHand() : player.getInventory().getItemInOffHand();

        int result = item.getAmount() - amount;
        item.setAmount(result);

        if (this == CompatibleHand.MAIN_HAND) {
            player.setItemInHand(result > 0 ? item : null);
            return;
        }

        player.getInventory().setItemInOffHand(result > 0 ? item : null);
    }

    /**
     * Get item in the selected hand
     *
     * @param player the player to get the item from
     *
     * @return the item
     */
    public ItemStack getItem(Player player) {
        if (this == MAIN_HAND) {
            return player.getItemInHand();
        }

        return player.getInventory().getItemInOffHand();
    }

    /**
     * Set the item in the selected hand
     *
     * @param player the player to set the item of
     * @param item   the item to set
     */
    public void setItem(Player player, ItemStack item) {
        if (this == MAIN_HAND) {
            player.setItemInHand(item);
            return;
        }

        player.getInventory().setItemInOffHand(item);
    }
}
