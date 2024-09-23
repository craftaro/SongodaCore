package com.craftaro.core.nms.v1_8_R3.nbt;

import com.craftaro.core.nms.nbt.NBTCore;
import com.craftaro.core.nms.nbt.NBTEntity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class NBTCoreImpl implements NBTCore {
    @Override
    public NBTEntity of(Entity entity) {
        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        NBTTagCompound nbt = new NBTTagCompound();
        nmsEntity.e(nbt); // Method name changed in 1.11

        return new NBTEntityImpl(nbt, nmsEntity);
    }

    @Override
    public NBTEntity newEntity() {
        return new NBTEntityImpl(new NBTTagCompound(), null);
    }
}
