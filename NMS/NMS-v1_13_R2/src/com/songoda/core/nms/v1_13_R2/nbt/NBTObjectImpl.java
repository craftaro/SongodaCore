package com.songoda.core.nms.v1_13_R2.nbt;

import com.songoda.core.nms.nbt.NBTObject;
import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class NBTObjectImpl implements NBTObject {

    private final NBTTagCompound compound;
    private final String tag;

    public NBTObjectImpl(NBTTagCompound compound, String tag) {
        this.compound = compound;
        this.tag = tag;
    }

    public String asString() {
        return compound.getString(tag);
    }

    public boolean asBoolean() {
        return compound.getBoolean(tag);
    }

    public int asInt() {
        return compound.getInt(tag);
    }

    public double asDouble() {
        return compound.getDouble(tag);
    }

    public long asLong() {
        return compound.getLong(tag);
    }

    public short asShort() {
        return compound.getShort(tag);
    }

    public byte asByte() {
        return compound.getByte(tag);
    }

    public int[] asIntArray() {
        return compound.getIntArray(tag);
    }

}
