package com.songoda.core.compatibility;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Near-Materials for older servers 1.7+
 * @since 2019-08-23
 * @author jascotty2
 */
public enum LegacyMaterialAnalouge {

	/* 1.15 */
	BEE_SPAWN_EGG(ServerVersion.V1_15, "PARROT_SPAWN_EGG", ServerVersion.V1_12, "MONSTER_EGG", (byte) 65),
	BEE_NEST(ServerVersion.V1_15, "BIRCH_LOG", "LOG", (byte) 2),
	BEEHIVE(ServerVersion.V1_15, "SLIME_BLOCK", ServerVersion.V1_8, "WOOL", (byte) 4),
	HONEY_BLOCK(ServerVersion.V1_15, "SLIME_BLOCK", ServerVersion.V1_8, "WOOL", (byte) 4),
	HONEY_BOTTLE(ServerVersion.V1_15, "DRAGON_BREATH", ServerVersion.V1_9, "POTION", (byte) 0),
	HONEYCOMB(ServerVersion.V1_15, "SUNFLOWER", "DOUBLE_PLANT", (byte) 0),
	HONEYCOMB_BLOCK(ServerVersion.V1_15, "SLIME_BLOCK", ServerVersion.V1_8, "WOOL", (byte) 4),

    ACACIA_BOAT(ServerVersion.V1_9, "BOAT"),
    ACACIA_BUTTON(ServerVersion.V1_13, "WOOD_BUTTON"),
    ACACIA_DOOR(ServerVersion.V1_8, "WOOD_DOOR"), // TODO? ACACIA_DOOR & WOODEN_DOOR are the legacy block variants
    ACACIA_FENCE(ServerVersion.V1_8, "FENCE"),
    ACACIA_FENCE_GATE(ServerVersion.V1_8, "FENCE_GATE"),
    ACACIA_PRESSURE_PLATE(ServerVersion.V1_13, "WOOD_PLATE"),
    ACACIA_SIGN(ServerVersion.V1_14, "SIGN", "SIGN"),
    ACACIA_TRAPDOOR(ServerVersion.V1_13, "TRAP_DOOR"),
    ACACIA_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
    ANDESITE(ServerVersion.V1_8, "STONE"),
    ANDESITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    ANDESITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    ANDESITE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    ARMOR_STAND(ServerVersion.V1_8, "STICK"), // idk, we just need *something*
    BAMBOO(ServerVersion.V1_14, "SUGAR_CANE", "SUGAR_CANE_BLOCK"),
    BAMBOO_SAPLING(ServerVersion.V1_14, "SUGAR_CANE"),
    BARREL(ServerVersion.V1_14, "TRAPPED_CHEST"),
    BARRIER(ServerVersion.V1_8, "STAINED_GLASS", (byte) 14), // plain glass would make more sense if this were to be a block..
    BEETROOT(ServerVersion.V1_9, "RAW_BEEF"),
    BEETROOT_SEEDS(ServerVersion.V1_9, "SEEDS"),
    BEETROOT_SOUP(ServerVersion.V1_9, "MUSHROOM_SOUP"),
    BEETROOTS(ServerVersion.V1_9, "CROPS"),
    BELL(ServerVersion.V1_14, "GOLD_BLOCK", "GOLD_BLOCK"),
    BIRCH_BOAT(ServerVersion.V1_9, "BOAT"),
    BIRCH_BUTTON(ServerVersion.V1_13, "WOOD_BUTTON"),
    BIRCH_DOOR(ServerVersion.V1_8, "WOOD_DOOR"),
    BIRCH_FENCE(ServerVersion.V1_8, "FENCE"),
    BIRCH_FENCE_GATE(ServerVersion.V1_8, "FENCE_GATE"),
    BIRCH_PRESSURE_PLATE(ServerVersion.V1_13, "WOOD_PLATE"),
    BIRCH_SIGN(ServerVersion.V1_14, "SIGN", "SIGN"),
    BIRCH_STAIRS(ServerVersion.V1_13, "WOOD_STAIRS"),
    BIRCH_TRAPDOOR(ServerVersion.V1_13, "TRAP_DOOR"),
    BIRCH_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
    BLACK_BANNER(ServerVersion.V1_8, "SIGN"),
    BLACK_BED(ServerVersion.V1_12, "BED"),
    BLACK_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 15),
    BLACK_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 15),
    BLACK_DYE(ServerVersion.V1_14, "INK_SAC", "INK_SACK", (byte) 0),
    BLACK_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 15),
    BLACK_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    BLACK_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    BLAST_FURNACE(ServerVersion.V1_14, "FURNACE"),
    BLUE_BANNER(ServerVersion.V1_8, "SIGN"),
    BLUE_BED(ServerVersion.V1_12, "BED"),
    BLUE_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 11),
    BLUE_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 11),
    BLUE_DYE(ServerVersion.V1_14, "LAPIS_LAZULI", "INK_SACK", (byte) 4),
    BLUE_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 11),
    BLUE_ICE(ServerVersion.V1_13, "PACKED_ICE"),
    BLUE_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    BLUE_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    BONE_BLOCK(ServerVersion.V1_13, "QUARTZ_BLOCK"),
    BRAIN_CORAL(ServerVersion.V1_13, "WOOL", (byte) 6),
    BRAIN_CORAL_BLOCK(ServerVersion.V1_13, "WOOL", (byte) 6),
    BRAIN_CORAL_FAN(ServerVersion.V1_13, "WOOL", (byte) 6),
    BRAIN_CORAL_WALL_FAN(ServerVersion.V1_13, "WOOL", (byte) 6),
    BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    BROWN_BANNER(ServerVersion.V1_8, "SIGN"),
    BROWN_BED(ServerVersion.V1_12, "BED"),
    BROWN_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 12),
    BROWN_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 12),
    BROWN_DYE(ServerVersion.V1_14, "COCOA_BEANS", "INK_SACK", (byte) 3),
    BROWN_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 12),
    BROWN_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    BROWN_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    BUBBLE_COLUMN(ServerVersion.V1_13, "WATER"),
    BUBBLE_CORAL(ServerVersion.V1_13, "WOOL", (byte) 2),
    BUBBLE_CORAL_BLOCK(ServerVersion.V1_13, "WOOL", (byte) 2),
    BUBBLE_CORAL_FAN(ServerVersion.V1_13, "WOOL", (byte) 2),
    BUBBLE_CORAL_WALL_FAN(ServerVersion.V1_13, "WOOL", (byte) 2),
    CAMPFIRE(ServerVersion.V1_14, "FURNACE", "BURNING_FURNACE"),
    CARTOGRAPHY_TABLE(ServerVersion.V1_14, "BOOKSHELF"),
    CAT_SPAWN_EGG(ServerVersion.V1_14, "OCELOT_SPAWN_EGG", "MONSTER_EGG", (byte) 98),
    CAVE_AIR(ServerVersion.V1_13, "AIR"),
    CHAIN_COMMAND_BLOCK(ServerVersion.V1_9, "COMMAND"),
    CHISELED_RED_SANDSTONE(ServerVersion.V1_8, "SANDSTONE", (byte) 1),
    CHORUS_FLOWER(ServerVersion.V1_9, "WOOL", (byte) 2),
    CHORUS_FRUIT(ServerVersion.V1_9, "APPLE"),
    CHORUS_PLANT(ServerVersion.V1_9, "WOOL", (byte) 10),
    COARSE_DIRT(ServerVersion.V1_8, "DIRT"),
    COD_BUCKET(ServerVersion.V1_13, "WATER_BUCKET"),
    COD_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 60),
    COMPOSTER(ServerVersion.V1_14, "FLOWER_POT", "FLOWER_POT_ITEM"),
    CONDUIT(ServerVersion.V1_13, "MELON"),
    COOKED_MUTTON(ServerVersion.V1_8, "COOKED_BEEF"),
    COOKED_RABBIT(ServerVersion.V1_8, "COOKED_BEEF"),
    CORNFLOWER(ServerVersion.V1_14, "BLUE_ORCHID", "RED_ROSE", (byte) 1),
    CREEPER_BANNER_PATTERN(ServerVersion.V1_14, "PAPER"),
    CROSSBOW(ServerVersion.V1_14, "BOW"),
    CUT_RED_SANDSTONE(ServerVersion.V1_13, "RED_SANDSTONE", (byte) 2, ServerVersion.V1_8, "SANDSTONE", (byte) 0),
    CUT_RED_SANDSTONE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    CUT_SANDSTONE(ServerVersion.V1_13, "SANDSTONE", (byte) 2),
    CUT_SANDSTONE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    CYAN_BANNER(ServerVersion.V1_8, "SIGN"),
    CYAN_BED(ServerVersion.V1_12, "BED"),
    CYAN_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 9),
    CYAN_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 9),
    CYAN_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 9),
    CYAN_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    CYAN_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    DARK_OAK_BOAT(ServerVersion.V1_9, "BOAT"),
    DARK_OAK_BUTTON(ServerVersion.V1_13, "WOOD_BUTTON"),
    DARK_OAK_DOOR(ServerVersion.V1_8, "WOOD_DOOR"),
    DARK_OAK_FENCE(ServerVersion.V1_8, "FENCE"),
    DARK_OAK_FENCE_GATE(ServerVersion.V1_8, "FENCE_GATE"),
    DARK_OAK_PRESSURE_PLATE(ServerVersion.V1_13, "WOOD_PLATE"),
    DARK_OAK_SIGN(ServerVersion.V1_14, "SIGN"),
    DARK_OAK_TRAPDOOR(ServerVersion.V1_13, "TRAP_DOOR"),
    DARK_OAK_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
    DARK_PRISMARINE(ServerVersion.V1_8, "WOOL", (byte) 7),
    DARK_PRISMARINE_SLAB(ServerVersion.V1_13, "STEP", (byte) 0),
    DARK_PRISMARINE_STAIRS(ServerVersion.V1_13, "NETHER_BRICK_STAIRS"),
    DEAD_BRAIN_CORAL(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BRAIN_CORAL_BLOCK(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BRAIN_CORAL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BRAIN_CORAL_WALL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BUBBLE_CORAL(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BUBBLE_CORAL_BLOCK(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BUBBLE_CORAL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_BUBBLE_CORAL_WALL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_FIRE_CORAL(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_FIRE_CORAL_BLOCK(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_FIRE_CORAL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_FIRE_CORAL_WALL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_HORN_CORAL(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_HORN_CORAL_BLOCK(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_HORN_CORAL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_HORN_CORAL_WALL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_TUBE_CORAL(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_TUBE_CORAL_BLOCK(ServerVersion.V1_13, "COBBLESTONE"),
    DEAD_TUBE_CORAL_FAN(ServerVersion.V1_13, "COBBLESTONE"), // these could also be a dead_bush ?
    DEAD_TUBE_CORAL_WALL_FAN(ServerVersion.V1_13, "COBBLESTONE"),
    DEBUG_STICK(ServerVersion.V1_13, "STICK"),
    DIORITE(ServerVersion.V1_8, "STONE"),
    DIORITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    DIORITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    DIORITE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    DOLPHIN_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 0),
    DONKEY_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 100),
    DRAGON_BREATH(ServerVersion.V1_9, "POTION", (byte) 0), // or maybe glowstone..
    DRAGON_HEAD(ServerVersion.V1_9, "SKULL_ITEM", (byte) 4),
    DRAGON_WALL_HEAD(ServerVersion.V1_9, "SKULL", (byte) 4),
    DRIED_KELP(ServerVersion.V1_13, "POTATO_ITEM"),
    DRIED_KELP_BLOCK(ServerVersion.V1_13, "HAY_BLOCK"),
    DROWNED_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 54),
    ELDER_GUARDIAN_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 51),
    ELYTRA(ServerVersion.V1_9, "IRON_CHESTPLATE"),
    END_CRYSTAL(ServerVersion.V1_9, "STAINED_GLASS", (byte) 0),
    END_GATEWAY(ServerVersion.V1_9, "BEACON"),
    END_PORTAL(ServerVersion.V1_9, "PORTAL"),
    END_PORTAL_FRAME(ServerVersion.V1_9, "SANDSTONE", (byte) 1),
    END_ROD(ServerVersion.V1_9, "STAINED_GLASS_PANE", (byte) 0),
    END_STONE(ServerVersion.V1_9, "SANDSTONE", (byte) 0),
    END_STONE_BRICK_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    END_STONE_BRICK_STAIRS(ServerVersion.V1_14, "SANDSTONE_STAIRS"),
    END_STONE_BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    END_STONE_BRICKS(ServerVersion.V1_9, "SANDSTONE", (byte) 2),
    ENDERMITE_SPAWN_EGG(ServerVersion.V1_8, "MONSTER_EGG", (byte) 60),
    EVOKER_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 51),
    FILLED_MAP(ServerVersion.V1_13, "EMPTY_MAP"),
    FIRE_CORAL(ServerVersion.V1_13, "WOOL", (byte) 14),
    FIRE_CORAL_BLOCK(ServerVersion.V1_13, "WOOL", (byte) 14),
    FIRE_CORAL_FAN(ServerVersion.V1_13, "WOOL", (byte) 14),
    FIRE_CORAL_WALL_FAN(ServerVersion.V1_13, "WOOL", (byte) 14),
    FLETCHING_TABLE(ServerVersion.V1_14, "CRAFTING_TABLE", "WORKBENCH"),
    FLOWER_BANNER_PATTERN(ServerVersion.V1_14, "PAPER"),
    FOX_SPAWN_EGG(ServerVersion.V1_14, "OCELOT_SPAWN_EGG", "MONSTER_EGG", (byte) 98),
    FROSTED_ICE(ServerVersion.V1_13, "ICE"),
    GLOBE_BANNER_PATTERN(ServerVersion.V1_14, "PAPER"),
    GRANITE(ServerVersion.V1_8, "STONE"),
    GRANITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    GRANITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    GRANITE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    GRASS_PATH(ServerVersion.V1_9, "DIRT", (byte) 1, ServerVersion.V1_8, "DIRT"),
    GRAY_BANNER(ServerVersion.V1_8, "SIGN"),
    GRAY_BED(ServerVersion.V1_12, "BED"),
    GRAY_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 7),
    GRAY_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 7),
    GRAY_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 7),
    GRAY_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    GRAY_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    GREEN_BANNER(ServerVersion.V1_8, "SIGN"),
    GREEN_BED(ServerVersion.V1_12, "BED"),
    GREEN_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 13),
    GREEN_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 13),
    GREEN_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 13),
    GREEN_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    GREEN_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    GRINDSTONE(ServerVersion.V1_14, "ANVIL"),
    GUARDIAN_SPAWN_EGG(ServerVersion.V1_8, "MONSTER_EGG", (byte) 51),
    HEART_OF_THE_SEA(ServerVersion.V1_13, "DIAMOND"),
    HORN_CORAL(ServerVersion.V1_13, "WOOL", (byte) 4),
    HORN_CORAL_BLOCK(ServerVersion.V1_13, "WOOL", (byte) 4),
    HORN_CORAL_FAN(ServerVersion.V1_13, "WOOL", (byte) 4),
    HORN_CORAL_WALL_FAN(ServerVersion.V1_13, "WOOL", (byte) 4),
    HUSK_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 54),
    IRON_NUGGET(ServerVersion.V1_11, "IRON_INGOT"),
    IRON_TRAPDOOR(ServerVersion.V1_8, "TRAP_DOOR"),
    JIGSAW(ServerVersion.V1_14, "ANVIL"),
    JUNGLE_BOAT(ServerVersion.V1_9, "BOAT"),
    JUNGLE_BUTTON(ServerVersion.V1_13, "WOOD_BUTTON"),
    JUNGLE_DOOR(ServerVersion.V1_8, "WOOD_DOOR"),
    JUNGLE_FENCE(ServerVersion.V1_8, "FENCE"),
    JUNGLE_FENCE_GATE(ServerVersion.V1_8, "FENCE_GATE"),
    JUNGLE_PRESSURE_PLATE(ServerVersion.V1_13, "WOOD_PLATE"),
    JUNGLE_SIGN(ServerVersion.V1_14, "SIGN"),
    JUNGLE_TRAPDOOR(ServerVersion.V1_13, "TRAP_DOOR"),
    JUNGLE_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
    KELP(ServerVersion.V1_13, "POTATO_ITEM"),
    KELP_PLANT(ServerVersion.V1_13, "WATER"), // idk.
    KNOWLEDGE_BOOK(ServerVersion.V1_12, "BOOK"),
    LANTERN(ServerVersion.V1_14, "GLOWSTONE"),
    LEATHER_HORSE_ARMOR(ServerVersion.V1_14, "IRON_BARDING"),
    LECTERN(ServerVersion.V1_14, "BOOKSHELF"),
    LIGHT_BLUE_BANNER(ServerVersion.V1_8, "SIGN"),
    LIGHT_BLUE_BED(ServerVersion.V1_12, "BED"),
    LIGHT_BLUE_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 3),
    LIGHT_BLUE_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 3),
    LIGHT_BLUE_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 3),
    LIGHT_BLUE_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    LIGHT_BLUE_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    LIGHT_GRAY_BANNER(ServerVersion.V1_8, "SIGN"),
    LIGHT_GRAY_BED(ServerVersion.V1_12, "BED"),
    LIGHT_GRAY_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 8),
    LIGHT_GRAY_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 8),
    LIGHT_GRAY_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 8),
    LIGHT_GRAY_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    LIGHT_GRAY_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    LILY_OF_THE_VALLEY(ServerVersion.V1_14, "AZURE_BLUET", "RED_ROSE", (byte) 3),
    LIME_BANNER(ServerVersion.V1_8, "SIGN"),
    LIME_BED(ServerVersion.V1_12, "BED"),
    LIME_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 5),
    LIME_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 5),
    LIME_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 5),
    LIME_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    LIME_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    LINGERING_POTION(ServerVersion.V1_9, "POTION", (byte) 0),
    LLAMA_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    LOOM(ServerVersion.V1_14, "CRAFTING_TABLE", "WORKBENCH"),
    MAGENTA_BANNER(ServerVersion.V1_8, "SIGN"),
    MAGENTA_BED(ServerVersion.V1_12, "BED"),
    MAGENTA_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 2),
    MAGENTA_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 2),
    MAGENTA_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 2),
    MAGENTA_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    MAGENTA_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    MAGMA_BLOCK(ServerVersion.V1_10, "NETHER_BRICK"),
    MOJANG_BANNER_PATTERN(ServerVersion.V1_14, "PAPER"),
    MOSSY_COBBLESTONE_SLAB(ServerVersion.V1_14, "COBBLESTONE_SLAB", "STEP", (byte) 3),
    MOSSY_COBBLESTONE_STAIRS(ServerVersion.V1_14, "COBBLESTONE_STAIRS"),
    MOSSY_COBBLESTONE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    MOSSY_STONE_BRICK_SLAB(ServerVersion.V1_14, "BRICK_SLAB", "STEP", (byte) 4),
    MOSSY_STONE_BRICK_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    MOSSY_STONE_BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    MULE_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 100),
    MUTTON(ServerVersion.V1_8, "RAW_BEEF"),
    NAUTILUS_SHELL(ServerVersion.V1_13, "SNOW_BALL"),
    NETHER_BRICK_FENCE(ServerVersion.V1_13, "FENCE"),
    NETHER_BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    NETHER_WART_BLOCK(ServerVersion.V1_10, "NETHERRACK"),
    OAK_BOAT(ServerVersion.V1_9, "BOAT"),
    OBSERVER(ServerVersion.V1_11, "DISPENSER"),
    ORANGE_BANNER(ServerVersion.V1_8, "SIGN"),
    ORANGE_BED(ServerVersion.V1_12, "BED"),
    ORANGE_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 1),
    ORANGE_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 1),
    ORANGE_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 1),
    ORANGE_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    ORANGE_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    PANDA_SPAWN_EGG(ServerVersion.V1_14, "COW_SPAWN_EGG", "MONSTER_EGG", (byte) 92),
    PARROT_SPAWN_EGG(ServerVersion.V1_12, "MONSTER_EGG", (byte) 65),
    PHANTOM_MEMBRANE(ServerVersion.V1_13, "FEATHER"),
    PHANTOM_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 65),
    PILLAGER_SPAWN_EGG(ServerVersion.V1_14, "VILLAGER_SPAWN_EGG", "MONSTER_EGG", (byte) 120),
    PINK_BANNER(ServerVersion.V1_8, "SIGN"),
    PINK_BED(ServerVersion.V1_12, "BED"),
    PINK_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 6),
    PINK_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 6),
    PINK_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 6),
    PINK_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    PINK_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    PODZOL(ServerVersion.V1_8, "DIRT"),
    POLAR_BEAR_SPAWN_EGG(ServerVersion.V1_10, "MONSTER_EGG", (byte) 0),
    POLISHED_ANDESITE(ServerVersion.V1_8, "STONE"),
    POLISHED_ANDESITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    POLISHED_ANDESITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    POLISHED_DIORITE(ServerVersion.V1_8, "STONE"),
    POLISHED_DIORITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    POLISHED_DIORITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    POLISHED_GRANITE(ServerVersion.V1_8, "STONE"),
    POLISHED_GRANITE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    POLISHED_GRANITE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    POPPED_CHORUS_FRUIT(ServerVersion.V1_9, "GOLDEN_APPLE"),
    POTTED_ACACIA_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_ALLIUM(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_AZURE_BLUET(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_BAMBOO(ServerVersion.V1_14, "FLOWER_POT", "FLOWER_POT_ITEM"),
    POTTED_BIRCH_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_BLUE_ORCHID(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_BROWN_MUSHROOM(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_CACTUS(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_CORNFLOWER(ServerVersion.V1_14, "FLOWER_POT", "FLOWER_POT_ITEM"),
    POTTED_DANDELION(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_DARK_OAK_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_DEAD_BUSH(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_FERN(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_JUNGLE_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_LILY_OF_THE_VALLEY(ServerVersion.V1_14, "FLOWER_POT", "FLOWER_POT_ITEM"),
    POTTED_OAK_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_ORANGE_TULIP(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_OXEYE_DAISY(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_PINK_TULIP(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_POPPY(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_RED_MUSHROOM(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_RED_TULIP(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_SPRUCE_SAPLING(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_WHITE_TULIP(ServerVersion.V1_13, "FLOWER_POT_ITEM"),
    POTTED_WITHER_ROSE(ServerVersion.V1_14, "FLOWER_POT", "FLOWER_POT_ITEM"),
    PRISMARINE(ServerVersion.V1_8, "WOOL", (byte) 9),
    PRISMARINE_BRICK_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    PRISMARINE_BRICK_STAIRS(ServerVersion.V1_13, "SMOOTH_STAIRS"),
    PRISMARINE_BRICKS(ServerVersion.V1_8, "WOOL", (byte) 9),
    PRISMARINE_CRYSTALS(ServerVersion.V1_8, "CLAY_BALL"),
    PRISMARINE_SHARD(ServerVersion.V1_8, "FLINT"),
    PRISMARINE_SLAB(ServerVersion.V1_13, "STEP", (byte) 0),
    PRISMARINE_STAIRS(ServerVersion.V1_13, "COBBLESTONE_STAIRS"),
    PRISMARINE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    PUFFERFISH_BUCKET(ServerVersion.V1_13, "WATER_BUCKET"),
    PUFFERFISH_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 60),
    PURPLE_BANNER(ServerVersion.V1_8, "SIGN"),
    PURPLE_BED(ServerVersion.V1_12, "BED"),
    PURPLE_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 10),
    PURPLE_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 10),
    PURPLE_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 10),
    PURPLE_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    PURPLE_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    PURPUR_BLOCK(ServerVersion.V1_9, "WOOL", (byte) 2),
    PURPUR_PILLAR(ServerVersion.V1_9, "WOOL", (byte) 2),
    PURPUR_SLAB(ServerVersion.V1_9, "STEP", (byte) 0),
    PURPUR_STAIRS(ServerVersion.V1_9, "BRICK_STAIRS"),
    RABBIT(ServerVersion.V1_8, "RAW_BEEF"),
    RABBIT_FOOT(ServerVersion.V1_8, "ROTTEN_FLESH"),
    RABBIT_HIDE(ServerVersion.V1_8, "ROTTEN_FLESH"),
    RABBIT_SPAWN_EGG(ServerVersion.V1_8, "MONSTER_EGG", (byte) 0),
    RABBIT_STEW(ServerVersion.V1_8, "MUSHROOM_SOUP"),
    RAVAGER_SPAWN_EGG(ServerVersion.V1_14, "COW_SPAWN_EGG", "MONSTER_EGG", (byte) 92),
    RED_BANNER(ServerVersion.V1_8, "SIGN"),
    RED_BED(ServerVersion.V1_12, "BED"),
    RED_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 14),
    RED_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 14),
    RED_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 14),
    RED_NETHER_BRICK_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    RED_NETHER_BRICK_STAIRS(ServerVersion.V1_14, "NETHER_BRICK_STAIRS"),
    RED_NETHER_BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    RED_NETHER_BRICKS(ServerVersion.V1_10, "NETHER_BRICK"),
    RED_SANDSTONE(ServerVersion.V1_8, "SANDSTONE", (byte) 0),
    RED_SANDSTONE_SLAB(ServerVersion.V1_8, "STEP", (byte) 0),
    RED_SANDSTONE_STAIRS(ServerVersion.V1_8, "SANDSTONE_STAIRS"),
    RED_SANDSTONE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    RED_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    RED_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    REDSTONE_WALL_TORCH(ServerVersion.V1_13, "REDSTONE_TORCH_ON"),
    REPEATING_COMMAND_BLOCK(ServerVersion.V1_9, "COMMAND"),
    SALMON_BUCKET(ServerVersion.V1_13, "WATER_BUCKET"),
    SALMON_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 60),
    SANDSTONE_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    SCAFFOLDING(ServerVersion.V1_14, "LADDER"),
    SCUTE(ServerVersion.V1_13, "SLIME_BALL"),
    SEA_LANTERN(ServerVersion.V1_8, "GLOWSTONE"),
    SEA_PICKLE(ServerVersion.V1_13, "CACTUS"),
    SEAGRASS(ServerVersion.V1_13, "LONG_GRASS", (byte) 1),
    SHIELD(ServerVersion.V1_9, "BANNER", (byte) 3, ServerVersion.V1_8, "WOOD_DOOR"),
    SHULKER_BOX(ServerVersion.V1_13, "ENDER_CHEST"),
    SHULKER_SHELL(ServerVersion.V1_11, "SUGAR"),
    SHULKER_SPAWN_EGG(ServerVersion.V1_9, "MONSTER_EGG", (byte) 0),
    SKELETON_HORSE_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    SKULL_BANNER_PATTERN(ServerVersion.V1_14, "PAPER"),
    SLIME_BLOCK(ServerVersion.V1_8, "WOOL", (byte) 5),
    SMITHING_TABLE(ServerVersion.V1_14, "CRAFTING_TABLE", "WORKBENCH"),
    SMOKER(ServerVersion.V1_14, "FURNACE"),
    SMOOTH_QUARTZ(ServerVersion.V1_13, "QUARTZ_BLOCK", (byte) 0),
    SMOOTH_QUARTZ_SLAB(ServerVersion.V1_14, "QUARTZ_SLAB", "STEP", (byte) 7),
    SMOOTH_QUARTZ_STAIRS(ServerVersion.V1_14, "QUARTZ_STAIRS"),
    SMOOTH_RED_SANDSTONE(ServerVersion.V1_13, "RED_SANDSTONE", (byte) 0, ServerVersion.V1_8, "SANDSTONE", (byte) 0),
    SMOOTH_RED_SANDSTONE_SLAB(ServerVersion.V1_14, "SANDSTONE_SLAB", "STEP", (byte) 1),
    SMOOTH_RED_SANDSTONE_STAIRS(ServerVersion.V1_14, "RED_SANDSTONE_STAIRS", ServerVersion.V1_8, "SANDSTONE_STAIRS"),
    SMOOTH_SANDSTONE_SLAB(ServerVersion.V1_14, "SANDSTONE_SLAB", "STEP", (byte) 1),
    SMOOTH_SANDSTONE_STAIRS(ServerVersion.V1_14, "SANDSTONE_STAIRS"),
    SMOOTH_STONE(ServerVersion.V1_13, "STONE"), // DOUBLE_STEP is a closer texture match
    SMOOTH_STONE_SLAB(ServerVersion.V1_14, "STONE_SLAB", "STEP", (byte) 0),
    SPECTRAL_ARROW(ServerVersion.V1_9, "ARROW"),
    SPRUCE_BOAT(ServerVersion.V1_9, "BOAT"),
    SPRUCE_BUTTON(ServerVersion.V1_13, "WOOD_BUTTON"),
    SPRUCE_DOOR(ServerVersion.V1_8, "WOOD_DOOR"),
    SPRUCE_FENCE(ServerVersion.V1_8, "FENCE"),
    SPRUCE_FENCE_GATE(ServerVersion.V1_8, "FENCE_GATE"),
    SPRUCE_PRESSURE_PLATE(ServerVersion.V1_13, "WOOD_PLATE"),
    SPRUCE_SIGN(ServerVersion.V1_14, "SIGN"),
    SPRUCE_TRAPDOOR(ServerVersion.V1_13, "TRAP_DOOR"),
    SPRUCE_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
    //STONE(ServerVersion.V1_8, "STONE"), // funny how that happened, heh. Non-data to data to non-data again
    STONE_BRICK_WALL(ServerVersion.V1_14, "COBBLESTONE_WALL", "COBBLE_WALL"),
    STONE_SLAB(ServerVersion.V1_13, "STEP", (byte) 0),
    STONE_STAIRS(ServerVersion.V1_14, "STONE_BRICK_STAIRS", "SMOOTH_STAIRS"),
    STONECUTTER(ServerVersion.V1_14, "ANVIL"),
    STRAY_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    STRIPPED_ACACIA_LOG(ServerVersion.V1_13, "LOG_2", (byte) 0),
    STRIPPED_ACACIA_WOOD(ServerVersion.V1_13, "LOG_2", (byte) 12),
    STRIPPED_BIRCH_LOG(ServerVersion.V1_13, "LOG", (byte) 2),
    STRIPPED_BIRCH_WOOD(ServerVersion.V1_13, "LOG", (byte) 14),
    STRIPPED_DARK_OAK_LOG(ServerVersion.V1_13, "LOG_2", (byte) 1),
    STRIPPED_DARK_OAK_WOOD(ServerVersion.V1_13, "LOG_2", (byte) 13),
    STRIPPED_JUNGLE_LOG(ServerVersion.V1_13, "LOG", (byte) 3),
    STRIPPED_JUNGLE_WOOD(ServerVersion.V1_13, "LOG", (byte) 15),
    STRIPPED_OAK_LOG(ServerVersion.V1_13, "LOG", (byte) 0),
    STRIPPED_OAK_WOOD(ServerVersion.V1_13, "LOG", (byte) 12),
    STRIPPED_SPRUCE_LOG(ServerVersion.V1_13, "LOG", (byte) 1),
    STRIPPED_SPRUCE_WOOD(ServerVersion.V1_13, "LOG", (byte) 13),
    STRUCTURE_BLOCK(ServerVersion.V1_9, "COMMAND"),
    STRUCTURE_VOID(ServerVersion.V1_10, "BARRIER", ServerVersion.V1_8, "STAINED_GLASS", (byte) 14), // Block would be air, but these make more sense as an item
    SUSPICIOUS_STEW(ServerVersion.V1_14, "MUSHROOM_SOUP"),
    SWEET_BERRIES(ServerVersion.V1_14, "POTATO", "POTATO_ITEM"),
    SWEET_BERRY_BUSH(ServerVersion.V1_14, "OAK_LEAVES", "LEAVES", (byte) 3),
    TALL_SEAGRASS(ServerVersion.V1_13, "LONG_GRASS", (byte) 1),
    TIPPED_ARROW(ServerVersion.V1_9, "ARROW"),
    TOTEM_OF_UNDYING(ServerVersion.V1_11, "CHAINMAIL_CHESTPLATE"),
    TRADER_LLAMA_SPAWN_EGG(ServerVersion.V1_14, "LLAMA_SPAWN_EGG", "MONSTER_EGG", (byte) 103), // todo? should we change the item here? llamas are v1.11+
    TRIDENT(ServerVersion.V1_13, "ARROW"),
    TROPICAL_FISH_BUCKET(ServerVersion.V1_13, "WATER_BUCKET"),
    TROPICAL_FISH_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 60),
    TUBE_CORAL(ServerVersion.V1_13, "WOOL", (byte) 11),
    TUBE_CORAL_BLOCK(ServerVersion.V1_13, "WOOL", (byte) 11),
    TUBE_CORAL_FAN(ServerVersion.V1_13, "WOOL", (byte) 11),
    TUBE_CORAL_WALL_FAN(ServerVersion.V1_13, "WOOL", (byte) 11),
    TURTLE_EGG(ServerVersion.V1_13, "DRAGON_EGG"),
    TURTLE_HELMET(ServerVersion.V1_13, "LEATHER_HELMET"), // would be cool to color it green..
    TURTLE_SPAWN_EGG(ServerVersion.V1_13, "MONSTER_EGG", (byte) 60),
    VEX_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    VINDICATOR_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    VOID_AIR(ServerVersion.V1_13, "AIR"),
    WALL_TORCH(ServerVersion.V1_13, "TORCH"),
    WANDERING_TRADER_SPAWN_EGG(ServerVersion.V1_14, "VILLAGER_SPAWN_EGG", "MONSTER_EGG", (byte) 120),
    WHITE_BANNER(ServerVersion.V1_8, "SIGN"),
    WHITE_BED(ServerVersion.V1_12, "BED"),
    WHITE_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 0),
    WHITE_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 0),
    WHITE_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 0),
    WHITE_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    WHITE_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    WITHER_ROSE(ServerVersion.V1_14, "POPPY", "RED_ROSE", (byte) 0),
    WITHER_SKELETON_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    YELLOW_BANNER(ServerVersion.V1_8, "SIGN"),
    YELLOW_BED(ServerVersion.V1_12, "BED"),
    YELLOW_CONCRETE(ServerVersion.V1_12, "STAINED_CLAY", (byte) 4),
    YELLOW_CONCRETE_POWDER(ServerVersion.V1_12, "STAINED_CLAY", (byte) 4),
    YELLOW_GLAZED_TERRACOTTA(ServerVersion.V1_12, "STAINED_CLAY", (byte) 4),
    YELLOW_SHULKER_BOX(ServerVersion.V1_11, "ENDER_CHEST"),
    YELLOW_WALL_BANNER(ServerVersion.V1_8, "WALL_SIGN"),
    ZOMBIE_HORSE_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),
    ZOMBIE_VILLAGER_SPAWN_EGG(ServerVersion.V1_11, "MONSTER_EGG", (byte) 0),

    ;

    final ServerVersion versionLessThan;
    final String modernMaterial;
    final String legacyMaterial;
    final Byte legacyData;
    final ServerVersion legacyMinimumVersion;
    final String compatibleMaterial;
    final Byte compatibleData;
    final Material material;
    final Byte data;

    // map to speed up name->material lookups
    private static final Map<String, LegacyMaterialAnalouge> lookupMap = new HashMap();

    static {
        for (LegacyMaterialAnalouge m : values()) {
            lookupMap.put(m.name(), m);
        }
    }

    public static LegacyMaterialAnalouge lookupAnalouge(String material) {
        return lookupMap.get(material);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial, byte legacyData) {
        this(versionLessThan, null, legacyMaterial, legacyData, null, null, null);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial) {
        this(versionLessThan, null, legacyMaterial, null, null, null, null);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String modernAnalouge, String legacyMaterial) {
        this(versionLessThan, modernAnalouge, legacyMaterial, null, null, null, null);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String modernAnalouge, String legacyMaterial, byte legacyData) {
        this(versionLessThan, modernAnalouge, legacyMaterial, legacyData, null, null, null);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial, byte legacyData, ServerVersion legacyMinimum, String compatMaterial, byte compatData) {
        this(versionLessThan, null, legacyMaterial, legacyData, legacyMinimum, compatMaterial, compatData);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial, ServerVersion legacyMinimum, String compatMaterial, byte compatData) {
        this(versionLessThan, null, legacyMaterial, null, legacyMinimum, compatMaterial, compatData);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial, byte legacyData, ServerVersion legacyMinimum, String compatMaterial) {
        this(versionLessThan, null, legacyMaterial, legacyData, legacyMinimum, compatMaterial, null);
    }

    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String legacyMaterial, ServerVersion legacyMinimum, String compatMaterial) {
        this(versionLessThan, null, legacyMaterial, null, legacyMinimum, compatMaterial, null);
    }

    /**
     *
     * @param versionLessThan AKA, what server version was this material added to minecraft?
     * @param modernAnalouge post-1.13 material name, if applicable
     * @param legacyMaterial pre-1.13 material name
     * @param legacyData data for defining specific legacy items
     */
    private LegacyMaterialAnalouge(ServerVersion versionLessThan, String modernAnalouge, String legacyMaterial, Byte legacyData, ServerVersion legacyMinimum, String compatMaterial, Byte compatData) {
        this.versionLessThan = versionLessThan;
        this.modernMaterial = modernAnalouge;
        this.legacyMaterial = legacyMaterial;
        this.legacyData = legacyData;
        
        this.legacyMinimumVersion = legacyMinimum;
        this.compatibleMaterial = compatMaterial;
        this.compatibleData = compatData;

        if (ServerVersion.isServerVersionBelow(versionLessThan)) {
            if(legacyMinimumVersion != null && ServerVersion.isServerVersionBelow(legacyMinimumVersion)) {
                // fallback material not available, so use its fallback
                material = Material.getMaterial(compatibleMaterial);
                data = compatibleData;
            } else if (modernMaterial == null || ServerVersion.isServerVersionBelow(ServerVersion.V1_13)) {
                // use legacy material if on legacy
                material = Material.getMaterial(legacyMaterial);
                data = legacyData;
            } else if (modernMaterial != null) {
                material = Material.getMaterial(modernMaterial);
                data = null;
            } else {
                material = null;
                data = null;
            }
        } else {
            material = null;
            data = null;
        }
    }

    public Material getMaterial() {
        return material;
    }

    public boolean usesData() {
        return data != null;
    }

    public byte getData() {
        return data == null ? 0 : data;
    }

    public ItemStack getItem() {
        if (material == null) {
            return null;
        }
        return data != null ? new ItemStack(material, 1, data) : new ItemStack(material);
    }
}
