package com.craftaro.core.utils;

import com.craftaro.core.chat.ColorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ColorUtils {
    private static final Map<ColorCode, ColorSet<Integer, Integer, Integer>> colorMap = new HashMap<>();

    static {
        colorMap.put(ColorCode.BLACK, new ColorSet<>(0, 0, 0));
        colorMap.put(ColorCode.DARK_BLUE, new ColorSet<>(0, 0, 170));
        colorMap.put(ColorCode.DARK_GREEN, new ColorSet<>(0, 170, 0));
        colorMap.put(ColorCode.DARK_AQUA, new ColorSet<>(0, 170, 170));
        colorMap.put(ColorCode.DARK_RED, new ColorSet<>(170, 0, 0));
        colorMap.put(ColorCode.DARK_PURPLE, new ColorSet<>(170, 0, 170));
        colorMap.put(ColorCode.GOLD, new ColorSet<>(255, 170, 0));
        colorMap.put(ColorCode.GRAY, new ColorSet<>(170, 170, 170));
        colorMap.put(ColorCode.DARK_GRAY, new ColorSet<>(85, 85, 85));
        colorMap.put(ColorCode.BLUE, new ColorSet<>(85, 85, 255));
        colorMap.put(ColorCode.GREEN, new ColorSet<>(85, 255, 85));
        colorMap.put(ColorCode.AQUA, new ColorSet<>(85, 255, 255));
        colorMap.put(ColorCode.RED, new ColorSet<>(255, 85, 85));
        colorMap.put(ColorCode.LIGHT_PURPLE, new ColorSet<>(255, 85, 255));
        colorMap.put(ColorCode.YELLOW, new ColorSet<>(255, 255, 85));
        colorMap.put(ColorCode.WHITE, new ColorSet<>(255, 255, 255));
    }

    private static class ColorSet<R, G, B> {
        R red;
        G green;
        B blue;

        ColorSet(R red, G green, B blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }

        public R getRed() {
            return this.red;
        }

        public G getGreen() {
            return this.green;
        }

        public B getBlue() {
            return this.blue;
        }
    }

    public static ColorCode fromRGB(int r, int g, int b) {
        TreeMap<Integer, ColorCode> closest = new TreeMap<>();
        colorMap.forEach((color, set) -> {
            int red = Math.abs(r - set.getRed());
            int green = Math.abs(g - set.getGreen());
            int blue = Math.abs(b - set.getBlue());

            closest.put(red + green + blue, color);
        });

        return closest.firstEntry().getValue();
    }
}
