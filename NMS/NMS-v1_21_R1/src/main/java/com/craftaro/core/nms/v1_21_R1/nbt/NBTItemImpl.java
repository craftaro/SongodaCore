package com.craftaro.core.nms.v1_21_R1.nbt;

import com.craftaro.core.nms.nbt.NBTItem;
import net.minecraft.world.item.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final ItemStack nmsItem;

    public NBTItemImpl(ItemStack nmsItem) {
        throw new UnsupportedOperationException("This constructor is not supported in this version of Minecraft.");
    }

    public org.bukkit.inventory.ItemStack finish() {
        throw new UnsupportedOperationException("This method is not supported in this version of Minecraft.");
    }
}
