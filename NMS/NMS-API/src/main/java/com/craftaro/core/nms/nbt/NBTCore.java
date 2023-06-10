package com.craftaro.core.nms.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface NBTCore {
    /**
     * @deprecated Use {@link de.tr7zw.nbtapi.NBTItem} instead.
     */
    @Deprecated
    NBTItem of(ItemStack item);

    /**
     * @deprecated Use {@link de.tr7zw.nbtapi.NBTItem} instead.
     */
    @Deprecated
    NBTItem newItem();

    NBTEntity of(Entity entity);

    NBTEntity newEntity();
}
