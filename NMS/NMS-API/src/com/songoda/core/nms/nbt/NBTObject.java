package com.songoda.core.nms.nbt;

import java.util.Set;

public interface NBTObject {

    String asString();

    boolean asBoolean();

    int asInt();

    double asDouble();

    long asLong();

    short asShort();

    byte asByte();

    int[] asIntArray();

    Set<String> getKeys();

    NBTCompound getCompound(String tag);
}
