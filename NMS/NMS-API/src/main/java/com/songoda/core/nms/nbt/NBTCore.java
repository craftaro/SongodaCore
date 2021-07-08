package com.songoda.core.nms.nbt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface NBTCore {

    NBTItem of(ItemStack item);

    NBTItem newItem();

    NBTEntity of(Entity entity);

    NBTEntity newEntity();
}
