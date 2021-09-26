package com.songoda.core.nms.v1_17_R1.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final net.minecraft.world.item.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.world.item.ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());

        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        if (nmsItem == null) {
            return CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(compound));
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    @Override
    public void addExtras() {
    }
}
