package com.songoda.core.nms.v1_19_R1.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final ItemStack nmsItem;

    public NBTItemImpl(ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag());

        this.nmsItem = nmsItem;
    }

    public org.bukkit.inventory.ItemStack finish() {
        if (nmsItem == null) {
            return CraftItemStack.asBukkitCopy(ItemStack.of(compound));
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
