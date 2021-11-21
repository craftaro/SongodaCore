package com.songoda.core.compatibility.bukkit;

import be.seeseemelk.mockbukkit.MockBukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CompatibleSoundTest {
    @BeforeEach
    void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @Disabled("CompatibleSound class needs some work beforehand")
    void getSound() {
        for (CompatibleSound compatibleSound : CompatibleSound.values()) {
            assertNotNull(compatibleSound.getSound());

            // compatibleSound.usesCompatibility()
        }
    }
}
