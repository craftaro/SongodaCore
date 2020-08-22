package com.songoda.core.nms.v1_9_R1.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {

    private net.minecraft.server.v1_9_R1.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.server.v1_9_R1.ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());
        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        if (nmsItem == null) {
            net.minecraft.server.v1_9_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(new ItemStack(Material.STONE));
            itemStack.setTag(compound);
            return CraftItemStack.asBukkitCopy(nmsItem);
        } else {
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
    }

    @Override
    public void addExtras() {
    }
}
