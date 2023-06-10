package com.craftaro.core.nms.v1_13_R1.nbt;

import com.craftaro.core.nms.nbt.NBTCore;
import com.craftaro.core.nms.nbt.NBTEntity;
import com.craftaro.core.nms.nbt.NBTItem;
import net.minecraft.server.v1_13_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class NBTCoreImpl implements NBTCore {
    @Deprecated
    @Override
    public NBTItem of(ItemStack item) {
        return new NBTItemImpl(CraftItemStack.asNMSCopy(item));
    }

    @Deprecated
    @Override
    public NBTItem newItem() {
        return new NBTItemImpl(null);
    }

    @Override
    public NBTEntity of(Entity entity) {
        net.minecraft.server.v1_13_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nmsEntity.save(nbt);

        return new NBTEntityImpl(nbt, nmsEntity);
    }

    @Override
    public NBTEntity newEntity() {
        return new NBTEntityImpl(new NBTTagCompound(), null);
    }
}
