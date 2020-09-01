package com.songoda.core.nms.v1_16_R2.nbt;

import com.songoda.core.nms.nbt.NBTCompound;
import com.songoda.core.nms.nbt.NBTObject;
import net.minecraft.server.v1_16_R2.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R2.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.UUID;

public abstract class NBTCompoundImpl implements NBTCompound {

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
    public NBTCompound set(String tag, double i) {
        compound.setDouble(tag, i);
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
    public NBTCompound set(String tag, int[] i) {
        compound.setIntArray(tag, i);
        return this;
    }

    @Override
    public NBTCompound set(String tag, UUID u) {
        compound.a(tag, u);
        return this;
    }

    @Override
    public NBTCompound remove(String tag) {
        compound.remove(tag);
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

    @Override
    public byte[] serialize(String... exclusions) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream dataOutput = new ObjectOutputStream(outputStream)) {
            addExtras();
            NBTTagCompound compound = this.compound.clone();

            for (String exclusion : exclusions)
                compound.remove(exclusion);

            NBTCompressedStreamTools.a(compound, (OutputStream) dataOutput);

            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deSerialize(byte[] serialized) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(serialized);
             ObjectInputStream dataInput = new ObjectInputStream(inputStream)) {
            compound = NBTCompressedStreamTools.a((InputStream) dataInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
