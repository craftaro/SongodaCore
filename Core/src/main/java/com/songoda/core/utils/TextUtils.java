package com.songoda.core.utils;

import org.bukkit.ChatColor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {

    public static String formatText(String text) {
        return formatText(text, false);
    }

    public static String formatText(String text, boolean capitalize) {
        if (text == null || text.equals(""))
            return "";
        if (capitalize)
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> formatText(List<String> list) {
        return list.stream().map(TextUtils::formatText).collect(Collectors.toList());
    }

    public static List<String> formatText(String... list) {
        return Arrays.stream(list).map(TextUtils::formatText).collect(Collectors.toList());
    }

    public static List<String> wrap(String line) {
        return wrap(null, line);
    }

    public static List<String> wrap(String color, String line) {
        if (color != null)
            color = "&" + color;
        else
            color = "";

        List<String> lore = new ArrayList<>();
        int lastIndex = 0;
        for (int n = 0; n < line.length(); n++) {
            if (n - lastIndex < 25)
                continue;

            if (line.charAt(n) == ' ') {
                lore.add(TextUtils.formatText(color + TextUtils.formatText(line.substring(lastIndex, n))));
                lastIndex = n;
            }
        }

        if (lastIndex - line.length() < 25)
            lore.add(TextUtils.formatText(color + TextUtils.formatText(line.substring(lastIndex))));
        return lore;
    }


    /**
     * Convert a string to an invisible colored string that's lore-safe <br />
     * (Safe to use as lore) <br />
     * Note: Do not use semi-colons in this string, or they will be lost when decoding!
     *
     * @param s string to convert
     * @return encoded string
     */
    public static String convertToInvisibleLoreString(String s) {
        if (s == null || s.equals(""))
            return "";
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray())
            hidden.append(ChatColor.COLOR_CHAR).append(';').append(ChatColor.COLOR_CHAR).append(c);
        return hidden.toString();
    }

    /**
     * Convert a string to an invisible colored string <br />
     * (Not safe to use as lore) <br />
     * Note: Do not use semi-colons in this string, or they will be lost when decoding!
     *
     * @param s string to convert
     * @return encoded string
     */
    public static String convertToInvisibleString(String s) {
        if (s == null || s.equals(""))
            return "";
        StringBuilder hidden = new StringBuilder();
        for (char c : s.toCharArray()) hidden.append(ChatColor.COLOR_CHAR).append(c);
        return hidden.toString();
    }

    /**
     * Removes color markers used to encode strings as invisible text
     *
     * @param s encoded string
     * @return string with color markers removed
     */
    public static String convertFromInvisibleString(String s) {
        if (s == null || s.equals("")) {
            return "";
        }
        return s.replaceAll(ChatColor.COLOR_CHAR + ";" + ChatColor.COLOR_CHAR + "|" + ChatColor.COLOR_CHAR, "");
    }

    protected static final List<Charset> supportedCharsets = new ArrayList();

    static {
        supportedCharsets.add(StandardCharsets.UTF_8); // UTF-8 BOM: EF BB BF
        supportedCharsets.add(StandardCharsets.ISO_8859_1); // also starts with EF BB BF
        //supportedCharsets.add(StandardCharsets.UTF_16LE); // FF FE
        //supportedCharsets.add(StandardCharsets.UTF_16BE); // FE FF
        //supportedCharsets.add(StandardCharsets.UTF_16);
        try {
            supportedCharsets.add(Charset.forName("windows-1253"));
            supportedCharsets.add(Charset.forName("ISO-8859-7"));
        } catch (Exception e) {
        } // UnsupportedCharsetException technically can be thrown, but can also be ignored
        supportedCharsets.add(StandardCharsets.US_ASCII);
    }

    public static Charset detectCharset(File f, Charset def) {
        byte[] buffer = new byte[2048];
        int read = -1;
        // read the first 2kb of the file and test the file's encoding
        try (FileInputStream input = new FileInputStream(f)) {
            read = input.read(buffer);
        } catch (Exception ex) {
            return null;
        }
        return read != -1 ? detectCharset(buffer, read, def) : def;
    }

    public static Charset detectCharset(BufferedInputStream reader, Charset def) {
        byte[] buffer = new byte[2048];
        int read;
        try {
            reader.mark(2048);
            read = reader.read(buffer);
            reader.reset();
        } catch (Exception ex) {
            return null;
        }
        return read != -1 ? detectCharset(buffer, read, def) : def;
    }

    public static Charset detectCharset(byte[] data, int len, Charset def) {
        // check the file header
        if (len > 4) {
            if (data[0] == (byte) 0xFF && data[1] == (byte) 0xFE) {
                return StandardCharsets.UTF_16LE;
                // FF FE 00 00 is UTF-32LE
            } else if (data[0] == (byte) 0xFE && data[1] == (byte) 0xFF) {
                return StandardCharsets.UTF_16BE;
                // 00 00 FE FF is UTF-32BE
            } else if (data[0] == (byte) 0xEF && data[1] == (byte) 0xBB && data[2] == (byte) 0xBF) { // UTF-8 with BOM, same sig as ISO-8859-1
                return StandardCharsets.UTF_8;
            }
        }

        // iterate through sets to test, and return the first that is ok
        for (Charset charset : supportedCharsets) {
            if (charset != null && isCharset(data, len, charset)) {
                return charset;
            }
        }
        return def;
    }

    public static boolean isCharset(byte[] data, int len, Charset charset) {
        try {
            CharsetDecoder decoder = charset.newDecoder();
            decoder.reset();
            decoder.decode(ByteBuffer.wrap(data));
            return true;
        } catch (CharacterCodingException e) {
        }
        return false;
    }
}
