package com.songoda.core.nms.v1_11_R1.nbt;

import com.songoda.core.nms.nbt.NBTCompound;
import com.songoda.core.nms.nbt.NBTObject;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

public class NBTCompoundImpl implements NBTCompound {

    protected NBTTagCompound compound;

    protected NBTCompoundImpl(NBTTagCompound compound) {
        this.compound = compound;
    }

    public NBTCompoundImpl() {
        this.compound = new NBTTagCompound();
    }

    @Override
    public NBTCompound set(String tag, String s) {
        compound.setString(tag, s);
        return this;
    }

    @Override
    public NBTCompound set(String tag, boolean b) {
        compound.setBoolean(tag, b);
        return this;
    }

    @Override
    public NBTCompound set(String tag, int i) {
        compound.setInt(tag, i);
        return this;
    }

    @Override
    public NBTCompound set(String tag, long l) {
        compound.setLong(tag, l);
        return this;
    }

    @Override
    public NBTCompound set(String tag, short s) {
        compound.setShort(tag, s);
        return this;
    }

    @Override
    public NBTCompound set(String tag, byte b) {
        compound.setByte(tag, b);
        return this;
    }

    @Override
    public boolean has(String tag) {
        return compound.hasKey(tag);
    }

    @Override
    public NBTObject getNBTObject(String tag) {
        return new NBTObjectImpl(compound, tag);
    }

}
