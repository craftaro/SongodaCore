package com.songoda.core.nms.v1_13_R2.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {

    private net.minecraft.server.v1_13_R2.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.server.v1_13_R2.ItemStack nmsItem) {
        super(nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());
        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
