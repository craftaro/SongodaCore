package com.craftaro.core.nms.v1_19_R3.nbt;

import com.craftaro.core.nms.nbt.NBTItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final ItemStack nmsItem;

    public NBTItemImpl(ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new CompoundTag());

        this.nmsItem = nmsItem;
    }

    public org.bukkit.inventory.ItemStack finish() {
        if (this.nmsItem == null) {
            return CraftItemStack.asBukkitCopy(ItemStack.of(this.compound));
        }

        return CraftItemStack.asBukkitCopy(this.nmsItem);
    }
}
