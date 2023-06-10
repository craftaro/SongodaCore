package com.craftaro.core.nms.v1_9_R1.nbt;

import com.craftaro.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {
    private final net.minecraft.server.v1_9_R1.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.server.v1_9_R1.ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());

        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        if (nmsItem == null) {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_9_R1.ItemStack.createStack(compound));
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
