package com.songoda.core.nms.nbt;

import java.util.List;

public interface NBTObject {

    String asString();

    boolean asBoolean();

    int asInt();

    long asLong();

    short asShort();

    byte asByte();
}
