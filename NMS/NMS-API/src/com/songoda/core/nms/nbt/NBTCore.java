package com.songoda.core.nms.nbt;

import org.bukkit.inventory.ItemStack;

public interface NBTCore {

    NBTItem of(ItemStack item);

    NBTCompound newCompound();

}
