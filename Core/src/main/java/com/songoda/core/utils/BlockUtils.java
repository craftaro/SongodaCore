package com.songoda.core.utils;

import com.songoda.core.compatibility.ClassMapping;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.compatibility.MethodMapping;
import com.songoda.core.compatibility.ServerVersion;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockUtils {
    protected static final Set<Material> DOORS;
    protected static final Set<Material> PRESSURE_PLATES;
    protected static final Set<Material> FENCE_GATES;
    protected static final Set<Material> TRAP_DOORS;
    protected static final boolean useLegacy = Material.getMaterial("OAK_LOG") == null;
    protected static Method legacySetBlockData = null;
    protected static Method legacyUpdateBlockData = null;

    static {
        DOORS = EnumSet.noneOf(Material.class);
        PRESSURE_PLATES = EnumSet.noneOf(Material.class);
        FENCE_GATES = EnumSet.noneOf(Material.class);
        TRAP_DOORS = EnumSet.noneOf(Material.class);

        for (Material material : Material.values()) {
            String name = material.name();
            if (name.contains("DOOR") && !name.contains("ITEM")) {
                if (name.contains("TRAP")) {
                    TRAP_DOORS.add(material);
                } else {
                    DOORS.add(material);
                }
            } else if (name.contains("GATE") && !name.contains("END")) {
                FENCE_GATES.add(material);
            } else if (name.contains("_PLATE")) {
                PRESSURE_PLATES.add(material);
            }
        }

        if (useLegacy) {
            try {
                //legacyUpdateBlockData = Block.class.getDeclaredMethod("update");
                legacySetBlockData = Block.class.getDeclaredMethod("setData", byte.class);
            } catch (NoSuchMethodException ignore) {
            }
        }
    }

    /**
     * Interact with this block to either update redstone or open doors
     *
     * @param b block to update
     *
     * @return if this block's state was updated
     */
    public static boolean tryInteract(Block b) {
        final Material bType = b.getType();

        if (isOpenable(bType)) {
            toggleDoorStates(true, b);
            return true;
        } else if (bType == Material.LEVER) {
            toggleLever(b);
            return true;
        } else if (bType.name().endsWith("_BUTTON")) {
            pressButton(b);
            return true;
        }

        return false;
    }

    /**
     * Change a pressure plate's redstone state
     *
     * @param plate plate to update
     * @param power power to set to 0-15 (wood plates are active if greater than 0)
     */
    public static void updatePressurePlate(Block plate, int power) {
        if (useLegacy && legacySetBlockData != null) {
            _updatePressurePlateLegacy(plate, power);
        } else {
            BlockUtilsModern._updatePressurePlateModern(plate, power);
        }
    }

    private static void _updatePressurePlateLegacy(Block plate, int power) {
        final Material m = plate.getType();

        try {
            if (m.name().equals("GOLD_PLATE") || m.name().equals("IRON_PLATE")) {
                legacySetBlockData.invoke(plate, (byte) (power & 0x15));
            } else if (m.name().endsWith("_PLATE")) {
                legacySetBlockData.invoke(plate, (byte) (power == 0 ? 0 : 1));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
        }
    }

    public static void pressButton(Block button) {
        if (useLegacy && legacySetBlockData != null) {
            _pressButtonLegacy(button);
        } else {
            BlockUtilsModern._pressButtonModern(button);
        }
    }

    public static void releaseButton(Block button) {
        if (useLegacy && legacySetBlockData != null) {
            _releaseButtonLegacy(button);
        } else {
            BlockUtilsModern._releaseButtonModern(button);
        }
    }

    private static void _pressButtonLegacy(Block button) {
        final Material m = button.getType();

        if (!m.name().endsWith("_BUTTON")) {
            return;
        }

        try {
            legacySetBlockData.invoke(button, (byte) (button.getData() | (31 & 0x8)));
            button.getState().update();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
        }
    }

    private static void _releaseButtonLegacy(Block button) {
        final Material m = button.getType();

        if (!m.name().endsWith("_BUTTON")) {
            return;
        }

        try {
            legacySetBlockData.invoke(button, (byte) (button.getData() & ~0x8));
            button.getState().update();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
        }
    }

    public static void toggleLever(Block lever) {
        if (useLegacy && legacySetBlockData != null) {
            _toggleLeverLegacy(lever);
        } else {
            BlockUtilsModern._toggleLeverModern(lever);
        }
    }

    private static void _toggleLeverLegacy(Block lever) {
        final Material m = lever.getType();

        if (m != Material.LEVER) {
            return;
        }

        try {
            legacySetBlockData.invoke(lever, (byte) (lever.getData() ^ 0x8));
            lever.getState().update();
            //lever.getWorld().playEffect(lever.getLocation(), Effect.CLICK1, 0);
            // now we need to update the redstone around it...
//            int data = lever.getData() & ~0x8;
//            Block attached;
//            switch(data) {
//                case 0:
//                    attached = lever.getRelative(BlockFace.UP);
//                    break;
//                case 1:
//                    attached = lever.getRelative(BlockFace.WEST);
//                    break;
//                case 2:
//                    attached = lever.getRelative(BlockFace.EAST);
//                    break;
//                case 3:
//                    attached = lever.getRelative(BlockFace.NORTH);
//                    break;
//                case 4:
//                    attached = lever.getRelative(BlockFace.SOUTH);
//                    break;
//                case 5:
//                    attached = lever.getRelative(BlockFace.DOWN);
//                    break;
//                default:
//                    return;
//            }
//
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
        }
    }

    /**
     * Change all of the given door states to be inverse; that is, if a door is
     * open, it will be closed afterwards. If the door is closed, it will become
     * open.
     * <p/>
     * Note that the blocks given must be the bottom block of the door.
     *
     * @param allowDoorToOpen If FALSE, and the door is currently CLOSED, it
     *                        will NOT be opened!
     * @param doors           Blocks given must be the bottom block of the door
     */
    public static void toggleDoorStates(boolean allowDoorToOpen, Block... doors) {
        if (useLegacy && legacySetBlockData != null) {
            _toggleDoorStatesLegacy(allowDoorToOpen, doors);
        } else {
            BlockUtilsModern._toggleDoorStatesModern(allowDoorToOpen, doors);
        }
    }

    private static void _toggleDoorStatesLegacy(boolean allowDoorToOpen, Block... doors) {
        try {
            for (Block door : doors) {
                if (door == null) {
                    continue;
                }

                boolean isTop = (door.getData() & 0x8) != 0;
                if (isTop) {
                    // The lower half of the door contains the direction & open/close state
                    door = door.getRelative(BlockFace.DOWN);
                }

                // If we aren't allowing the door to open, check if it's already closed
                if (!allowDoorToOpen && (door.getData() & 0x4) == 0) {
                    // The door is already closed and we don't want to open it
                    // the bit 0x4 is set when the door is open
                    continue;
                }

                // Now xor both data values with 0x4, the flag that states if the door is open
                legacySetBlockData.invoke(door, (byte) (door.getData() ^ 0x4));

                // Play the door open/close sound
                door.getWorld().playEffect(door.getLocation(), Effect.DOOR_TOGGLE, 0);
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
        }
    }

    /**
     * Get the double door for the given block
     */
    public static Block getDoubleDoor(Block block) {
        // TODO? if legacy, just search N/S/E/W to see if there's another door nearby
        if (!isOpenable(block.getType())) {
            return null;
        }

        return BlockUtilsModern._getDoubleDoorModern(block);
    }

    public static boolean isOpenable(Material m) {
        return DOORS.contains(m) ||
                FENCE_GATES.contains(m) ||
                TRAP_DOORS.contains(m);
    }

    public static BlockFace getDoorClosedDirection(Block door) {
        return useLegacy ? _getDoorClosedDirectionLegacy(door) : BlockUtilsModern._getDoorClosedDirectionModern(door);
    }

    private static BlockFace _getDoorClosedDirectionLegacy(Block door) {
        final Material type = door.getType();

        if (DOORS.contains(type)) {
            boolean isTop = (door.getData() & 0x8) != 0;

            if (isTop) {
                // The lower half of the door contains the direction & open/close state
                door = door.getRelative(BlockFace.DOWN);
                if (door.getType() != type) {
                    return null;
                }
            }

            boolean isOpen = (door.getData() & 0x4) != 0;

            //int facing = (door.getData() & 0x3);
            // [east, south, west, north]
            boolean facingNS = (door.getData() & 0x1) != 0;
            if (facingNS) {
                return isOpen ? BlockFace.EAST : BlockFace.SOUTH;
            } else {
                return isOpen ? BlockFace.SOUTH : BlockFace.EAST;
            }
        } else if (FENCE_GATES.contains(door.getType())) {
            boolean isOpen = (door.getData() & 0x4) != 0;

            //int facing = (door.getData() & 0x3);
            // so fence gate orientations are [south, west, north, east]
            boolean facingNS = (door.getData() & 0x1) == 0;
            if (facingNS) {
                return isOpen ? BlockFace.EAST : BlockFace.SOUTH;
            } else {
                return isOpen ? BlockFace.SOUTH : BlockFace.EAST;
            }
        } else if (TRAP_DOORS.contains(door.getType())) {
            boolean isOpen = (door.getData() & 0x4) != 0;

            // [south, north, east, west]
            boolean facingNS = (door.getData() & 0x3) <= 1;
            if (facingNS) {
                return isOpen ? BlockFace.EAST : BlockFace.SOUTH;
            } else {
                return isOpen ? BlockFace.SOUTH : BlockFace.EAST;
            }
        }

        return null;
    }

    /* Only to be used by #setBlockFast */
    private static Class<?> clazzIBlockData, clazzBlocks, clazzCraftWorld, clazzBlockPosition;
    /* Only to be used by #setBlockFast */
    private static Method getHandle, getByCombinedId, setType, getChunkAt, getBlockData;

    /**
     * Set a block to a certain type by updating the block directly in the
     * NMS chunk.
     * <p>
     * The chunk must be loaded and players must relog if they have the
     * chunk loaded in order to use this method.
     */
    public static void setBlockFast(World world, int x, int y, int z, Material material, byte data) {
        try {
            // Cache reflection
            if (clazzIBlockData == null) {
                clazzIBlockData = ClassMapping.I_BLOCK_DATA.getClazz();
                clazzBlockPosition = ClassMapping.BLOCK_POSITION.getClazz();
                clazzCraftWorld = ClassMapping.CRAFT_WORLD.getClazz();
                clazzBlocks = ClassMapping.BLOCKS.getClazz();
                Class<?> clazzBlock = ClassMapping.BLOCK.getClazz();
                Class<?> clazzWorld = ClassMapping.WORLD.getClazz();
                Class<?> clazzChunk = ClassMapping.CHUNK.getClazz();

                getHandle = clazzCraftWorld.getMethod("getHandle");
                getChunkAt = MethodMapping.WORLD__GET_CHUNK_AT.getMethod(clazzWorld);

                if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
                    getBlockData = MethodMapping.BLOCK__GET_BLOCK_DATA.getMethod(ClassMapping.BLOCK.getClazz());
                    setType = MethodMapping.CHUNK__SET_BLOCK_STATE.getMethod(ClassMapping.CHUNK.getClazz());
                } else {
                    getByCombinedId = clazzBlock.getMethod("getByCombinedId", int.class);
                    setType = clazzChunk.getMethod("a", clazzBlockPosition, clazzIBlockData);
                }
            }

            // invoke and cast objects.
            Object craftWorld = clazzCraftWorld.cast(world);
            Object nmsWorld = getHandle.invoke(craftWorld);
            Object chunk = getChunkAt.invoke(nmsWorld, x >> 4, z >> 4);
            Object blockPosition = clazzBlockPosition.getConstructor(int.class, int.class, int.class).newInstance(x & 0xF, y, z & 0xF);

            // Invoke final method.
            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
                Object block = clazzBlocks.getField(material.name()).get(null);
                Object IBlockData = getBlockData.invoke(block);
                setType.invoke(chunk, blockPosition, IBlockData, true);
            } else {
                Object IBlockData = getByCombinedId.invoke(null, material.getId() + (data << 12));
                setType.invoke(chunk, blockPosition, IBlockData);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void setBlockFast(World world, int x, int y, int z, CompatibleMaterial material, byte data) {
        setBlockFast(world, x, y, z, material.getBlockMaterial(), data);
    }

    /**
     * Checks if a crop is at its max growth stage
     *
     * @param block The crop block to check
     *
     * @return true if the block is a crop and at its max growth stage
     */
    public static boolean isCropFullyGrown(Block block) {
        if (block == null) {
            return false;
        }

        if (!useLegacy) {
            return BlockUtilsModern._isCropFullyGrown(block);
        }

        CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(block.getType());
        if (mat == null || !mat.isCrop()) {
            return false;
        }

        return block.getData() >= (mat == CompatibleMaterial.BEETROOTS || mat == CompatibleMaterial.NETHER_WART ? 3 : 7);
    }

    /**
     * Gets the max growth stage for the given block
     *
     * @param block The crop block to check
     *
     * @return The max growth stage of the given crop type, or -1 if not a crop
     */
    public static int getMaxGrowthStage(Block block) {
        if (block == null) {
            return -1;
        }

        if (!useLegacy) {
            return BlockUtilsModern._getMaxGrowthStage(block);
        }

        CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(block.getType());
        if (mat == null || !mat.isCrop()) {
            return -1;
        }

        return (mat == CompatibleMaterial.BEETROOTS
                || mat == CompatibleMaterial.NETHER_WART ? 3 : 7);
    }

    /**
     * Gets the max growth stage for the given material
     *
     * @param material The material of the crop
     *
     * @return The max growth stage of the given crop type
     */
    public static int getMaxGrowthStage(Material material) {
        if (material == null) {
            return -1;
        }

        if (!useLegacy) {
            return BlockUtilsModern._getMaxGrowthStage(material);
        }

        CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(material);
        if (mat == null || !mat.isCrop()) {
            return -1;
        }

        return (mat == CompatibleMaterial.BEETROOTS || mat == CompatibleMaterial.NETHER_WART ? 3 : 7);
    }

    /**
     * Sets the max growth stage for the given block
     *
     * @param block The crop block to change
     * @param stage new growth stage to use
     */
    public static void setGrowthStage(Block block, int stage) {
        if (block == null) {
        } else if (!useLegacy) {
            BlockUtilsModern._setGrowthStage(block, stage);
        } else {
            CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(block.getType());
            if (mat != null && mat.isCrop()) {
                try {
                    legacySetBlockData.invoke(block, (byte) Math.max(0, Math.min(stage, (mat == CompatibleMaterial.BEETROOTS
                            || mat == CompatibleMaterial.NETHER_WART ? 3 : 7))));
                } catch (Exception ex) {
                    Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
                }
            }
        }
    }

    /**
     * Increments the growth stage for the given block
     *
     * @param block The crop block to grow
     */
    public static void incrementGrowthStage(Block block) {
        if (block == null) {
        } else if (!useLegacy) {
            BlockUtilsModern._incrementGrowthStage(block);
        } else {
            CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(block.getType());

            if (mat != null && mat.isCrop() &&
                    block.getData() < (mat == CompatibleMaterial.BEETROOTS || mat == CompatibleMaterial.NETHER_WART ? 3 : 7)) {
                try {
                    legacySetBlockData.invoke(block, (byte) (block.getData() + 1));
                } catch (Exception ex) {
                    Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
                }
            }
        }
    }

    /**
     * Sets a crop's growth back to stage 0
     *
     * @param block The crop block to set
     */
    public static void resetGrowthStage(Block block) {
        if (block == null) {
        } else if (!useLegacy) {
            BlockUtilsModern._resetGrowthStage(block);
        } else {
            CompatibleMaterial mat = CompatibleMaterial.getBlockMaterial(block.getType());

            if (mat != null && mat.isCrop()) {
                try {
                    legacySetBlockData.invoke(block, (byte) 0);
                } catch (Exception ex) {
                    Logger.getLogger(BlockUtils.class.getName()).log(Level.SEVERE, "Unexpected method error", ex);
                }
            }
        }
    }

    /**
     * Check to see if this material does not impede player/mob movement at all.
     *
     * @param m material to check
     *
     * @return true if this material doesn't have a solid hitbox
     */
    public static boolean canPassThrough(Material m) {
        switch (m.name()) {
            case "ACACIA_BUTTON":
            case "ACACIA_PRESSURE_PLATE":
            case "ACACIA_SAPLING":
            case "ACACIA_SIGN":
            case "ACACIA_WALL_SIGN":
            case "ACTIVATOR_RAIL":
            case "AIR":
            case "ATTACHED_MELON_STEM":
            case "ATTACHED_PUMPKIN_STEM":
            case "AZURE_BLUET":
            case "BEETROOTS":
            case "BIRCH_BUTTON":
            case "BIRCH_PRESSURE_PLATE":
            case "BIRCH_SAPLING":
            case "BIRCH_SIGN":
            case "BIRCH_WALL_SIGN":
            case "BLACK_WALL_BANNER":
            case "BLUE_BANNER":
            case "BLUE_ORCHID":
            case "BLUE_WALL_BANNER":
            case "BRAIN_CORAL_FAN":
            case "BRAIN_CORAL_WALL_FAN":
            case "BROWN_BANNER":
            case "BROWN_MUSHROOM":
            case "BROWN_WALL_BANNER":
            case "BUBBLE_CORAL_FAN":
            case "BUBBLE_CORAL_WALL_FAN":
            case "CARROTS":
            case "CAVE_AIR":
            case "COBWEB":
            case "CORNFLOWER":
            case "CYAN_BANNER":
            case "CYAN_WALL_BANNER":
            case "DANDELION":
            case "DARK_OAK_BUTTON":
            case "DARK_OAK_PRESSURE_PLATE":
            case "DARK_OAK_SAPLING":
            case "DARK_OAK_SIGN":
            case "DARK_OAK_WALL_SIGN":
            case "DEAD_BRAIN_CORAL_FAN":
            case "DEAD_BRAIN_CORAL_WALL_FAN":
            case "DEAD_BUBBLE_CORAL_FAN":
            case "DEAD_BUBBLE_CORAL_WALL_FAN":
            case "DEAD_BUSH":
            case "DEAD_FIRE_CORAL_FAN":
            case "DEAD_FIRE_CORAL_WALL_FAN":
            case "DEAD_HORN_CORAL_FAN":
            case "DEAD_HORN_CORAL_WALL_FAN":
            case "DEAD_TUBE_CORAL_FAN":
            case "DEAD_TUBE_CORAL_WALL_FAN":
            case "DETECTOR_RAIL":
            case "END_PORTAL":
            case "FERN":
            case "FIRE":
            case "FIRE_CORAL_FAN":
            case "FIRE_CORAL_WALL_FAN":
            case "GRASS":
            case "GRAY_BANNER":
            case "GRAY_WALL_BANNER":
            case "GREEN_BANNER":
            case "GREEN_WALL_BANNER":
            case "HEAVY_WEIGHTED_PRESSURE_PLATE":
            case "HORN_CORAL_FAN":
            case "HORN_CORAL_WALL_FAN":
            case "JUNGLE_BUTTON":
            case "JUNGLE_PRESSURE_PLATE":
            case "JUNGLE_SAPLING":
            case "JUNGLE_SIGN":
            case "JUNGLE_WALL_SIGN":
            case "KELP":
            case "LADDER":
            case "LARGE_FERN":
            case "LAVA":
            case "LEVER":
            case "LIGHT_BLUE_BANNER":
            case "LIGHT_BLUE_WALL_BANNER":
            case "LIGHT_GRAY_BANNER":
            case "LIGHT_GRAY_WALL_BANNER":
            case "LIGHT_WEIGHTED_PRESSURE_PLATE":
            case "LILAC":
            case "LILY_OF_THE_VALLEY":
            case "LIME_BANNER":
            case "MAGENTA_BANNER":
            case "MAGENTA_WALL_BANNER":
            case "MELON_STEM":
            case "NETHER_PORTAL":
            case "NETHER_WART":
            case "OAK_BUTTON":
            case "OAK_PRESSURE_PLATE":
            case "OAK_SAPLING":
            case "OAK_SIGN":
            case "OAK_WALL_SIGN":
            case "ORANGE_BANNER":
            case "ORANGE_TULIP":
            case "ORANGE_WALL_BANNER":
            case "OXEYE_DAISY":
            case "PEONY":
            case "PINK_BANNER":
            case "PINK_TULIP":
            case "PINK_WALL_BANNER":
            case "POTATOES":
            case "POWERED_RAIL":
            case "PUMPKIN_STEM":
            case "PURPLE_BANNER":
            case "PURPLE_WALL_BANNER":
            case "RAIL":
            case "REDSTONE_TORCH":
            case "REDSTONE_WALL_TORCH":
            case "REDSTONE_WIRE":
            case "RED_BANNER":
            case "RED_MUSHROOM":
            case "RED_TULIP":
            case "RED_WALL_BANNER":
            case "ROSE_BUSH":
            case "SCAFFOLDING":
            case "SEAGRASS":
            case "SPRUCE_BUTTON":
            case "SPRUCE_PRESSURE_PLATE":
            case "SPRUCE_SAPLING":
            case "SPRUCE_SIGN":
            case "SPRUCE_WALL_SIGN":
            case "STONE_BUTTON":
            case "STONE_PRESSURE_PLATE":
            case "STRUCTURE_VOID":
            case "SUGAR_CANE":
            case "SUNFLOWER":
            case "SWEET_BERRY_BUSH":
            case "TALL_GRASS":
            case "TALL_SEAGRASS":
            case "TORCH":
            case "TRIPWIRE":
            case "TRIPWIRE_HOOK":
            case "TUBE_CORAL_FAN":
            case "TUBE_CORAL_WALL_FAN":
            case "VINE":
            case "VOID_AIR":
            case "WALL_TORCH":
            case "WATER":
            case "WHEAT":
            case "WHITE_BANNER":
            case "WHITE_TULIP":
            case "WHITE_WALL_BANNER":
            case "WITHER_ROSE":
            case "YELLOW_BANNER":
            case "YELLOW_WALL_BANNER":
                // Legacy values:
            case "WEB":
            case "LONG_GRASS":
            case "YELLOW_FLOWER":
            case "RED_ROSE":
            case "CROPS":
            case "SIGN_POST":
            case "RAILS":
            case "WALL_SIGN":
            case "STONE_PLATE":
            case "WOOD_PLATE":
            case "REDSTONE_TORCH_OFF":
            case "REDSTONE_TORCH_ON":
            case "SUGAR_CANE_BLOCK":
            case "PORTAL":
            case "ENDER_PORTAL":
            case "CARROT":
            case "POTATO":
            case "WOOD_BUTTON":
            case "GOLD_PLATE":
            case "IRON_PLATE":
            case "DOUBLE_PLANT":
            case "STANDING_BANNER":
            case "WALL_BANNER":
            case "BEETROOT_BLOCK":
                return true;
            default:
                return false;
        }
    }

    /**
     * Check to see if a player can walk into this material<br />
     * This includes blocks like slabs and stairs
     *
     * @param m material to check
     *
     * @return true if this is a block that can be walked through or up
     */
    public static boolean canWalkTo(Material m) {
        switch (m.name()) {
            case "ACACIA_BUTTON":
            case "ACACIA_PRESSURE_PLATE":
            case "ACACIA_SAPLING":
            case "ACACIA_SIGN":
            case "ACACIA_SLAB":
            case "ACACIA_STAIRS":
            case "ACACIA_TRAPDOOR":
            case "ACACIA_WALL_SIGN":
            case "ACTIVATOR_RAIL":
            case "AIR":
            case "ANDESITE_SLAB":
            case "ANDESITE_STAIRS":
            case "ATTACHED_MELON_STEM":
            case "ATTACHED_PUMPKIN_STEM":
            case "AZURE_BLUET":
            case "BEETROOTS":
            case "BIRCH_BUTTON":
            case "BIRCH_DOOR":
            case "BIRCH_FENCE_GATE":
            case "BIRCH_PRESSURE_PLATE":
            case "BIRCH_SAPLING":
            case "BIRCH_SIGN":
            case "BIRCH_SLAB":
            case "BIRCH_STAIRS":
            case "BIRCH_TRAPDOOR":
            case "BIRCH_WALL_SIGN":
            case "BLACK_CARPET":
            case "BLACK_WALL_BANNER":
            case "BLUE_BANNER":
            case "BLUE_CARPET":
            case "BLUE_ORCHID":
            case "BLUE_WALL_BANNER":
            case "BRAIN_CORAL_FAN":
            case "BRAIN_CORAL_WALL_FAN":
            case "BRICK_SLAB":
            case "BRICK_STAIRS":
            case "BROWN_BANNER":
            case "BROWN_CARPET":
            case "BROWN_MUSHROOM":
            case "BROWN_WALL_BANNER":
            case "BUBBLE_CORAL_FAN":
            case "BUBBLE_CORAL_WALL_FAN":
            case "CAKE":
            case "CAMPFIRE":
            case "CARROTS":
            case "CAVE_AIR":
            case "COBBLESTONE_SLAB":
            case "COBBLESTONE_STAIRS":
            case "COBWEB":
            case "COMPARATOR":
            case "CORNFLOWER":
            case "CUT_RED_SANDSTONE_SLAB":
            case "CUT_SANDSTONE_SLAB":
            case "CYAN_BANNER":
            case "CYAN_CARPET":
            case "CYAN_WALL_BANNER":
            case "DANDELION":
            case "DARK_OAK_BUTTON":
            case "DARK_OAK_DOOR":
            case "DARK_OAK_FENCE_GATE":
            case "DARK_OAK_PRESSURE_PLATE":
            case "DARK_OAK_SAPLING":
            case "DARK_OAK_SIGN":
            case "DARK_OAK_SLAB":
            case "DARK_OAK_STAIRS":
            case "DARK_OAK_TRAPDOOR":
            case "DARK_OAK_WALL_SIGN":
            case "DARK_PRISMARINE_SLAB":
            case "DARK_PRISMARINE_STAIRS":
            case "DAYLIGHT_DETECTOR":
            case "DEAD_BRAIN_CORAL_FAN":
            case "DEAD_BRAIN_CORAL_WALL_FAN":
            case "DEAD_BUBBLE_CORAL_FAN":
            case "DEAD_BUBBLE_CORAL_WALL_FAN":
            case "DEAD_BUSH":
            case "DEAD_FIRE_CORAL_FAN":
            case "DEAD_FIRE_CORAL_WALL_FAN":
            case "DEAD_HORN_CORAL_FAN":
            case "DEAD_HORN_CORAL_WALL_FAN":
            case "DEAD_TUBE_CORAL_FAN":
            case "DEAD_TUBE_CORAL_WALL_FAN":
            case "DETECTOR_RAIL":
            case "DIORITE_SLAB":
            case "DIORITE_STAIRS":
            case "END_PORTAL":
            case "END_STONE_BRICK_SLAB":
            case "END_STONE_BRICK_STAIRS":
            case "FERN":
            case "FIRE":
            case "FIRE_CORAL_FAN":
            case "FIRE_CORAL_WALL_FAN":
            case "FLOWER_POT":
            case "GRANITE_SLAB":
            case "GRANITE_STAIRS":
            case "GRASS":
            case "GRAY_BANNER":
            case "GRAY_CARPET":
            case "GRAY_WALL_BANNER":
            case "GREEN_BANNER":
            case "GREEN_WALL_BANNER":
            case "HEAVY_WEIGHTED_PRESSURE_PLATE":
            case "HORN_CORAL_FAN":
            case "HORN_CORAL_WALL_FAN":
            case "IRON_DOOR":
            case "JUNGLE_BUTTON":
            case "JUNGLE_DOOR":
            case "JUNGLE_FENCE_GATE":
            case "JUNGLE_PRESSURE_PLATE":
            case "JUNGLE_SAPLING":
            case "JUNGLE_SIGN":
            case "JUNGLE_SLAB":
            case "JUNGLE_STAIRS":
            case "JUNGLE_TRAPDOOR":
            case "JUNGLE_WALL_SIGN":
            case "KELP":
            case "LADDER":
            case "LARGE_FERN":
            case "LAVA":
            case "LEVER":
            case "LIGHT_BLUE_BANNER":
            case "LIGHT_BLUE_CARPET":
            case "LIGHT_BLUE_WALL_BANNER":
            case "LIGHT_GRAY_BANNER":
            case "LIGHT_GRAY_CARPET":
            case "LIGHT_GRAY_WALL_BANNER":
            case "LIGHT_WEIGHTED_PRESSURE_PLATE":
            case "LILAC":
            case "LILY_OF_THE_VALLEY":
            case "LILY_PAD":
            case "LIME_BANNER":
            case "LIME_CARPET":
            case "MAGENTA_BANNER":
            case "MAGENTA_CARPET":
            case "MAGENTA_WALL_BANNER":
            case "MELON_STEM":
            case "MOSSY_COBBLESTONE_SLAB":
            case "MOSSY_COBBLESTONE_STAIRS":
            case "MOSSY_STONE_BRICK_SLAB":
            case "MOSSY_STONE_BRICK_STAIRS":
            case "NETHER_BRICK_SLAB":
            case "NETHER_BRICK_STAIRS":
            case "NETHER_PORTAL":
            case "NETHER_WART":
            case "OAK_BUTTON":
            case "OAK_DOOR":
            case "OAK_FENCE_GATE":
            case "OAK_PRESSURE_PLATE":
            case "OAK_SAPLING":
            case "OAK_SIGN":
            case "OAK_SLAB":
            case "OAK_STAIRS":
            case "OAK_TRAPDOOR":
            case "OAK_WALL_SIGN":
            case "ORANGE_BANNER":
            case "ORANGE_CARPET":
            case "ORANGE_TULIP":
            case "ORANGE_WALL_BANNER":
            case "OXEYE_DAISY":
            case "PEONY":
            case "PETRIFIED_OAK_SLAB":
            case "PINK_BANNER":
            case "PINK_CARPET":
            case "PINK_TULIP":
            case "PINK_WALL_BANNER":
            case "POLISHED_ANDESITE_SLAB":
            case "POLISHED_ANDESITE_STAIRS":
            case "POLISHED_DIORITE_SLAB":
            case "POLISHED_DIORITE_STAIRS":
            case "POLISHED_GRANITE_SLAB":
            case "POLISHED_GRANITE_STAIRS":
            case "POTATOES":
            case "POTTED_ACACIA_SAPLING":
            case "POTTED_ALLIUM":
            case "POTTED_AZURE_BLUET":
            case "POTTED_BAMBOO":
            case "POTTED_BIRCH_SAPLING":
            case "POTTED_BLUE_ORCHID":
            case "POTTED_BROWN_MUSHROOM":
            case "POTTED_CACTUS":
            case "POTTED_CORNFLOWER":
            case "POTTED_DANDELION":
            case "POTTED_DARK_OAK_SAPLING":
            case "POTTED_DEAD_BUSH":
            case "POTTED_FERN":
            case "POTTED_JUNGLE_SAPLING":
            case "POTTED_LILY_OF_THE_VALLEY":
            case "POTTED_OAK_SAPLING":
            case "POTTED_ORANGE_TULIP":
            case "POTTED_OXEYE_DAISY":
            case "POTTED_PINK_TULIP":
            case "POTTED_POPPY":
            case "POTTED_RED_MUSHROOM":
            case "POTTED_RED_TULIP":
            case "POTTED_SPRUCE_SAPLING":
            case "POTTED_WHITE_TULIP":
            case "POTTED_WITHER_ROSE":
            case "POWERED_RAIL":
            case "PRISMARINE_BRICK_SLAB":
            case "PRISMARINE_BRICK_STAIRS":
            case "PRISMARINE_SLAB":
            case "PRISMARINE_STAIRS":
            case "PUMPKIN_STEM":
            case "PURPLE_BANNER":
            case "PURPLE_CARPET":
            case "PURPLE_WALL_BANNER":
            case "PURPUR_SLAB":
            case "PURPUR_STAIRS":
            case "RAIL":
            case "REDSTONE_TORCH":
            case "REDSTONE_WALL_TORCH":
            case "REDSTONE_WIRE":
            case "RED_BANNER":
            case "RED_CARPET":
            case "RED_MUSHROOM":
            case "RED_SANDSTONE_SLAB":
            case "RED_SANDSTONE_STAIRS":
            case "RED_TULIP":
            case "RED_WALL_BANNER":
            case "REPEATER":
            case "ROSE_BUSH":
            case "SANDSTONE_SLAB":
            case "SANDSTONE_STAIRS":
            case "SCAFFOLDING":
            case "SEAGRASS":
            case "SMOOTH_QUARTZ_SLAB":
            case "SMOOTH_QUARTZ_STAIRS":
            case "SMOOTH_RED_SANDSTONE_SLAB":
            case "SMOOTH_RED_SANDSTONE_STAIRS":
            case "SMOOTH_SANDSTONE_SLAB":
            case "SMOOTH_SANDSTONE_STAIRS":
            case "SMOOTH_STONE_SLAB":
            case "SPRUCE_BUTTON":
            case "SPRUCE_DOOR":
            case "SPRUCE_FENCE_GATE":
            case "SPRUCE_PRESSURE_PLATE":
            case "SPRUCE_SAPLING":
            case "SPRUCE_SIGN":
            case "SPRUCE_SLAB":
            case "SPRUCE_STAIRS":
            case "SPRUCE_TRAPDOOR":
            case "SPRUCE_WALL_SIGN":
            case "STONECUTTER":
            case "STONE_BRICK_SLAB":
            case "STONE_BRICK_STAIRS":
            case "STONE_BUTTON":
            case "STONE_PRESSURE_PLATE":
            case "STONE_SLAB":
            case "STONE_STAIRS":
            case "STRUCTURE_VOID":
            case "SUGAR_CANE":
            case "SUNFLOWER":
            case "SWEET_BERRY_BUSH":
            case "TALL_GRASS":
            case "TALL_SEAGRASS":
            case "TORCH":
            case "TRIPWIRE":
            case "TRIPWIRE_HOOK":
            case "TUBE_CORAL_FAN":
            case "TUBE_CORAL_WALL_FAN":
            case "VINE":
            case "VOID_AIR":
            case "WALL_TORCH":
            case "WATER":
            case "WHEAT":
            case "WHITE_BANNER":
            case "WHITE_CARPET":
            case "WHITE_TULIP":
            case "WHITE_WALL_BANNER":
            case "WITHER_ROSE":
            case "YELLOW_BANNER":
            case "YELLOW_CARPET":
            case "YELLOW_WALL_BANNER":
                // Legacy values:
            case "WEB":
            case "LONG_GRASS":
            case "YELLOW_FLOWER":
            case "RED_ROSE":
            case "STEP":
            case "WOOD_STAIRS":
            case "CROPS":
            case "SIGN_POST":
            case "RAILS":
            case "WOODEN_DOOR":
            case "WALL_SIGN":
            case "STONE_PLATE":
            case "IRON_DOOR_BLOCK":
            case "WOOD_PLATE":
            case "REDSTONE_TORCH_OFF":
            case "REDSTONE_TORCH_ON":
            case "SNOW":
            case "SUGAR_CANE_BLOCK":
            case "PORTAL":
            case "CAKE_BLOCK":
            case "DIODE_BLOCK_OFF":
            case "DIODE_BLOCK_ON":
            case "TRAP_DOOR":
            case "FENCE_GATE":
            case "SMOOTH_STAIRS":
            case "ENDER_PORTAL":
            case "WOOD_STEP":
            case "SPRUCE_WOOD_STAIRS":
            case "BIRCH_WOOD_STAIRS":
            case "JUNGLE_WOOD_STAIRS":
            case "CARROT":
            case "POTATO":
            case "WOOD_BUTTON":
            case "GOLD_PLATE":
            case "IRON_PLATE":
            case "REDSTONE_COMPARATOR_OFF":
            case "REDSTONE_COMPARATOR_ON":
            case "QUARTZ_STAIRS":
            case "DOUBLE_PLANT":
            case "STANDING_BANNER":
            case "WALL_BANNER":
            case "DAYLIGHT_DETECTOR_INVERTED":
            case "DOUBLE_STONE_SLAB2":
            case "STONE_SLAB2":
            case "BEETROOT_BLOCK":
                return true;
            default:
                return false;
        }
    }
}
