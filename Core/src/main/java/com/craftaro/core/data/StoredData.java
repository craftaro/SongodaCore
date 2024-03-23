package com.craftaro.core.data;

import com.craftaro.core.utils.ItemSerializer;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class StoredData {

    private Object object;

    public StoredData(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String asString() {
        if (object == null) return null;
        return object.toString();
    }

    public int asInt() {
        if (object == null) return 0;
        return Integer.parseInt(asString());
    }

    public double asDouble() {
        return Double.parseDouble(asString());
    }

    public long asLong() {
        String string = asString();
        if (string == null) return 0;
        return Long.parseLong(string);
    }

    public float asFloat() {
        return Float.parseFloat(asString());
    }

    public Instant asInstant() {
        return Instant.ofEpochMilli(asLong());
    }

    public boolean asBoolean() {
        if (object instanceof Integer)
            return (int) object == 1;
        return Boolean.parseBoolean(asString());
    }

    public void swap(Object object) {
        this.object = object;
    }

    public boolean isNull() {
        return object == null;
    }

    // get longblob
    public byte[] asBytes() {
        return (byte[]) object;
    }

    public UUID asUniqueID() {
        return UUID.fromString(asString());
    }

    public ItemStack asItemStack() {
        return ItemSerializer.deserializeItem(asBytes());
    }
}
