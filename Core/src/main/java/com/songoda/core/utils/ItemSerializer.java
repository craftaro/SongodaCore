package com.songoda.core.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Class based off of https://gist.github.com/graywolf336/8153678
 *
 * @deprecated Will be moved into a more appropriate package and refactored.
 */
@Deprecated
public class ItemSerializer {
    /**
     * Returns true if the old SnakeYAML Base64Coder still exists.
     */
    private static boolean hasLegacyBase64() {
        try {
            Class.forName("org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * Encodes using the legacy Base64Coder when available,
     * otherwise falls back to java.util.Base64.
     */
    private static String encodeBase64(byte[] data) throws Exception {
        if (hasLegacyBase64()) {
            Class<?> clazz = Class.forName(
                    "org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder");

            Method method = clazz.getMethod("encodeLines", byte[].class);

            return (String) method.invoke(null, data);
        }

        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Decodes using the legacy Base64Coder when available,
     * otherwise falls back to java.util.Base64.
     */
    private static byte[] decodeBase64(String data) throws Exception {
        if (hasLegacyBase64()) {
            Class<?> clazz = Class.forName(
                    "org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder");

            Method method = clazz.getMethod("decodeLines", String.class);

            return (byte[]) method.invoke(null, data);
        }

        return Base64.getMimeDecoder().decode(data);
    }

    /**
     * Serialize a List<ItemStack> into Base64.
     */
    public static String toBase64(List<ItemStack> items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(items.size());

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();

            return encodeBase64(outputStream.toByteArray());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Deserialize a List<ItemStack> from Base64.
     */
    public static List<ItemStack> fromBase64(String data) {
        try {
            ByteArrayInputStream inputStream =
                    new ByteArrayInputStream(decodeBase64(data));

            BukkitObjectInputStream dataInput =
                    new BukkitObjectInputStream(inputStream);

            int length = dataInput.readInt();
            List<ItemStack> items = new ArrayList<>();

            for (int i = 0; i < length; ++i) {
                items.add((ItemStack) dataInput.readObject());
            }

            dataInput.close();

            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    /**
     * Deserialize a byte array into an ItemStack.
     *
     * @param data Data to deserialize.
     * @return Deserialized ItemStack.
     */
    public static ItemStack deserializeItem(byte[] data) {
        ItemStack item = null;

        try (BukkitObjectInputStream stream =
                     new BukkitObjectInputStream(new ByteArrayInputStream(data))) {

            item = (ItemStack) stream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return item;
    }

    /**
     * Serialize an ItemStack into a byte array.
     *
     * @param item Item to serialize.
     * @return Serialized data.
     */
    public static byte[] serializeItem(ItemStack item) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(stream)) {

            bukkitStream.writeObject(item);

            return stream.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
