package com.craftaro.core.utils;

import org.bukkit.block.BlockFace;

/**
 * @deprecated No replacement available
 */
@Deprecated
public class RotationUtils {
    public static float faceToYaw(BlockFace face) {
        switch (face) {
            case EAST:
                return -90;
            case WEST:
                return 90;
            case NORTH:
                return 180;

            case SOUTH:
            default:
                return 0;
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
