package com.songoda.core.nms.v1_18_R1.nbt;

import com.songoda.core.nms.nbt.NBTCompound;
import com.songoda.core.nms.nbt.NBTObject;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

public class NBTObjectImpl implements NBTObject {
    private final NBTTagCompound compound;
    private final String tag;

    public NBTObjectImpl(NBTTagCompound compound, String tag) {
        this.compound = compound;
        this.tag = tag;
    }

    @Override
    public String asString() {
        return compound.l(tag);
    }

    @Override
    public boolean asBoolean() {
        return compound.q(tag);
    }

    @Override
    public int asInt() {
        return compound.h(tag);
    }

    @Override
    public double asDouble() {
        return compound.k(tag);
    }

    @Override
    public long asLong() {
        return compound.i(tag);
    }

    @Override
    public short asShort() {
        return compound.g(tag);
    }

    @Override
    public byte asByte() {
        return compound.f(tag);
    }

    @Override
    public int[] asIntArray() {
        return compound.n(tag);
    }

    @Override
    public byte[] asByteArray() {
        return compound.m(tag);
    }

    @Override
    public NBTCompound asCompound() {
        return new NBTCompoundImpl(compound.p(tag));
    }

    @Override
    public Set<String> getKeys() {
        return compound.d();
    }
}
