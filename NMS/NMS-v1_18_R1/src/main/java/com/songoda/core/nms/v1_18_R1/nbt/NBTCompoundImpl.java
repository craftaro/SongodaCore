package com.songoda.core.nms.v1_18_R1.nbt;

import com.songoda.core.nms.nbt.NBTCompound;
import com.songoda.core.nms.nbt.NBTObject;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

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
        compound.a(tag, s);
        return this;
    }

    @Override
    public NBTCompound set(String tag, boolean b) {
        compound.a(tag, b);
        return this;
    }

    @Override
    public NBTCompound set(String tag, int i) {
        compound.a(tag, i);
        return this;
    }

    @Override
    public NBTCompound set(String tag, double i) {
        compound.a(tag, i);
        return this;
    }

    @Override
    public NBTCompound set(String tag, long l) {
        compound.a(tag, l);
        return this;
    }

    @Override
    public NBTCompound set(String tag, short s) {
        compound.a(tag, s);
        return this;
    }

    @Override
    public NBTCompound set(String tag, byte b) {
        compound.a(tag, b);
        return this;
    }

    @Override
    public NBTCompound set(String tag, int[] i) {
        compound.a(tag, i);
        return this;
    }

    @Override
    public NBTCompound set(String tag, byte[] b) {
        compound.a(tag, b);
        return this;
    }

    @Override
    public NBTCompound set(String tag, UUID u) {
        compound.a(tag, u);
        return this;
    }

    @Override
    public NBTCompound remove(String tag) {
        compound.r(tag);
        return this;
    }

    @Override
    public boolean has(String tag) {
        return compound.e(tag);
    }

    @Override
    public NBTObject getNBTObject(String tag) {
        return new NBTObjectImpl(compound, tag);
    }

    @Override
    public String getString(String tag) {
        return getNBTObject(tag).asString();
    }

    @Override
    public boolean getBoolean(String tag) {
        return getNBTObject(tag).asBoolean();
    }

    @Override
    public int getInt(String tag) {
        return getNBTObject(tag).asInt();
    }

    @Override
    public double getDouble(String tag) {
        return getNBTObject(tag).asDouble();
    }

    @Override
    public long getLong(String tag) {
        return getNBTObject(tag).asLong();
    }

    @Override
    public short getShort(String tag) {
        return getNBTObject(tag).asShort();
    }

    @Override
    public byte getByte(String tag) {
        return getNBTObject(tag).asByte();
    }

    @Override
    public int[] getIntArray(String tag) {
        return getNBTObject(tag).asIntArray();
    }

    @Override
    public byte[] getByteArray(String tag) {
        return getNBTObject(tag).asByteArray();
    }

    @Override
    public NBTCompound getCompound(String tag) {
        if (has(tag)) {
            return getNBTObject(tag).asCompound();
        }

        NBTTagCompound newCompound = new NBTTagCompound();
        compound.a(tag, newCompound);
        return new NBTCompoundImpl(newCompound);
    }

    @Override
    public Set<String> getKeys() {
        return compound.d();
    }

    @Override
    public Set<String> getKeys(String tag) {
        return compound.p(tag).d();
    }

    @Override
    public byte[] serialize(String... exclusions) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream)) {
            addExtras();
            NBTTagCompound compound = this.compound.g();

            for (String exclusion : exclusions) {
                compound.r(exclusion);
            }

            NBTCompressedStreamTools.a(compound, (OutputStream) dataOutput);

            return outputStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void deSerialize(byte[] serialized) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
             ObjectInputStream dataInput = new ObjectInputStream(inputStream)) {
            compound = NBTCompressedStreamTools.a((InputStream) dataInput);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addExtras() {
        // None
    }

    @Override
    public String toString() {
        return compound.toString();
    }
}
