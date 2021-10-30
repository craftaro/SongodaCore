package com.songoda.core.utils;

import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.Location;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocationUtilsTest {
    @Test
    void isLocationMatching() {
        assertTrue(LocationUtils.isLocationMatching(
                new Location(null, 10, 20, 30),
                new Location(new WorldMock(), 10.25, 20.5, 30.75)
        ));

        assertFalse(LocationUtils.isLocationMatching(
                new Location(null, 10, 20, 30),
                new Location(new WorldMock(), -10.25, 20.5, 30.75)
        ));
    }

    @Disabled("Test not yet implemented")
    @Test
    void isInArea() {
    }
}
