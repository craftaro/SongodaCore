package com.songoda.core.math;

import com.songoda.core.SongodaCoreConstants;

import java.util.HashMap;
import java.util.Map;

public class MathUtils {
    private static final Map<String, Double> cache = new HashMap<>();

    public static double eval(String toParse) {
        return eval(toParse, SongodaCoreConstants.getProjectName() + " Eval Engine");
    }

    public static double eval(String toParse, String warningMessage) {
        return cache.computeIfAbsent(toParse, t -> new Eval(toParse, warningMessage).parse());
    }
}
