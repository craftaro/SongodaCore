package com.craftaro.core.nms.nbt;

import org.bukkit.entity.Entity;

public interface NBTCore {
    NBTEntity of(Entity entity);

    NBTEntity newEntity();
}
