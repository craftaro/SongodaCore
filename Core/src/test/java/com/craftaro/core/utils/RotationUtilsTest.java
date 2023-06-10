package com.craftaro.core.utils;

import org.bukkit.block.BlockFace;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RotationUtilsTest {
    @Test
    void faceToYaw() {
        assertEquals(180, RotationUtils.faceToYaw(BlockFace.NORTH));
        assertEquals(-90, RotationUtils.faceToYaw(BlockFace.EAST));
        assertEquals(0, RotationUtils.faceToYaw(BlockFace.SOUTH));
        assertEquals(90, RotationUtils.faceToYaw(BlockFace.WEST));

        assertEquals(0, RotationUtils.faceToYaw(BlockFace.UP));
        assertEquals(0, RotationUtils.faceToYaw(BlockFace.DOWN));
    }

    @Test
    void yawToFace() {
        assertEquals(BlockFace.NORTH, RotationUtils.yawToFace(180 - 25));
        assertEquals(BlockFace.NORTH, RotationUtils.yawToFace(180));
        assertEquals(BlockFace.NORTH, RotationUtils.yawToFace(180 + 25));

        assertEquals(BlockFace.EAST, RotationUtils.yawToFace(-90 - 25));
        assertEquals(BlockFace.EAST, RotationUtils.yawToFace(-90));
        assertEquals(BlockFace.EAST, RotationUtils.yawToFace(-90 + 25));

        assertEquals(BlockFace.SOUTH, RotationUtils.yawToFace(-25));
        assertEquals(BlockFace.SOUTH, RotationUtils.yawToFace(0));
        assertEquals(BlockFace.SOUTH, RotationUtils.yawToFace(25));

        assertEquals(BlockFace.WEST, RotationUtils.yawToFace(90 - 25));
        assertEquals(BlockFace.WEST, RotationUtils.yawToFace(90));
        assertEquals(BlockFace.WEST, RotationUtils.yawToFace(90 + 25));

        assertEquals(BlockFace.SOUTH, RotationUtils.yawToFace(Float.MAX_VALUE));
    }
}
