package com.songoda.core.nms.v1_16_R3.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final net.minecraft.server.v1_16_R3.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.server.v1_16_R3.ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());

        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        if (nmsItem == null) {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(compound));
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public void addExtras() {
    }
}
