package com.songoda.core.utils;

import org.bukkit.block.BlockFace;

public class RotationUtils {
    public static float faceToYaw(BlockFace face) {
        switch (face) {
            case NORTH:
                return 180F;
            case EAST:
                return -90F;
            case SOUTH:
                return 0F;
            case WEST:
                return 90F;
            default:
                return 0F;
        }
    }

    public static BlockFace yawToFace(float face) {
        switch (Math.round((face + 360) / 90) * 90) {
            case 0:
            case 360:
                return BlockFace.SOUTH;
            case 180:
            case 540:
                return BlockFace.NORTH;
            case 270:
            case 630:
                return BlockFace.EAST;
            case 90:
            case 450:
                return BlockFace.WEST;
            default:
                // idk
                return BlockFace.SOUTH;
        }
    }
}
