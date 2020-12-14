package com.songoda.core.nms.v1_16_R3.nbt;

import com.songoda.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NBTItemImpl extends NBTCompoundImpl implements NBTItem {

    private net.minecraft.server.v1_16_R3.ItemStack nmsItem;

    public NBTItemImpl(net.minecraft.server.v1_16_R3.ItemStack nmsItem) {
        super(nmsItem != null && nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound());
        this.nmsItem = nmsItem;
    }

    public ItemStack finish() {
        if (nmsItem == null) {
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(compound));
        } else {
            return CraftItemStack.asBukkitCopy(nmsItem);
        }
    }


    @Override
    public byte[] serialize(String... exclusions) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream(); BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(stream)) {
            bukkitStream.writeObject(nmsItem);
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deSerialize(byte[] serialized) {
        try (BukkitObjectInputStream stream = new BukkitObjectInputStream(
                new ByteArrayInputStream(serialized))) {
            nmsItem = (net.minecraft.server.v1_16_R3.ItemStack) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addExtras() {
    }
}
