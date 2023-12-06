package com.craftaro.core.nms.v1_20_R3.nbt;

import com.craftaro.core.nms.nbt.NBTCore;
import com.craftaro.core.nms.nbt.NBTEntity;
import com.craftaro.core.nms.nbt.NBTItem;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
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
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        CompoundTag nbt = new CompoundTag();
        nmsEntity.saveWithoutId(nbt);

        return new NBTEntityImpl(nbt, nmsEntity);
    }

    @Override
    public NBTEntity newEntity() {
        return new NBTEntityImpl(new CompoundTag(), null);
    }
}
