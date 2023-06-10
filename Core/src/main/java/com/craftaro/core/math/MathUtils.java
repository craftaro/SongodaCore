package com.craftaro.core.math;

import com.craftaro.core.CraftaroCoreConstants;

import java.util.HashMap;
import java.util.Map;

public class MathUtils {
    private static final Map<String, Double> cache = new HashMap<>();

    public static double eval(String toParse) {
        return eval(toParse, CraftaroCoreConstants.getProjectName() + " Eval Engine");
    }

    public static double eval(String toParse, String warningMessage) {
        return cache.computeIfAbsent(toParse, t -> new Eval(toParse, warningMessage).parse());
    }
}
