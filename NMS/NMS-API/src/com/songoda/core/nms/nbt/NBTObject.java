package com.songoda.core.nms.nbt;

public interface NBTObject {

    String asString();

    boolean asBoolean();

    int asInt();

    double asDouble();

    long asLong();

    short asShort();

    byte asByte();

    int[] asIntArray();
}
