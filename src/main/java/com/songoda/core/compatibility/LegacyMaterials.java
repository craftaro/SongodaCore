package com.songoda.core.compatibility;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Items that are compatible with server versions 1.7+
 * @since 2019-08-23
 * @author jascotty2
 */
public enum LegacyMaterials {
	/*
     TODO: add another handler for getBlockItem() for materials and fallback materials
    
     Legacy has some values not used in modern, eg:
    
     DIODE_BLOCK_OFF(93, Diode.class),
     DIODE_BLOCK_ON(94, Diode.class),
     SPRUCE_DOOR_ITEM(427),
     BIRCH_DOOR_ITEM(428),
     JUNGLE_DOOR_ITEM(429),
     ACACIA_DOOR_ITEM(430),
     DARK_OAK_DOOR_ITEM(431),
    
	 */
	ACACIA_BOAT("BOAT_ACACIA"),
	ACACIA_BUTTON(),
	ACACIA_DOOR("ACACIA_DOOR_ITEM"), // TODO: ACACIA_DOOR & WOODEN_DOOR are the legacy block variants
	ACACIA_FENCE(),
	ACACIA_FENCE_GATE(),
	ACACIA_LEAVES("LEAVES_2", (byte) 0),
	ACACIA_LOG("LOG_2", (byte) 0),
	ACACIA_PLANKS("WOOD", (byte) 4),
	ACACIA_PRESSURE_PLATE(),
	ACACIA_SAPLING("SAPLING", (byte) 4),
	ACACIA_SIGN(ServerVersion.V1_14, null),
	ACACIA_SLAB("WOOD_STEP", (byte) 4),
	ACACIA_STAIRS,
	ACACIA_TRAPDOOR(),
	ACACIA_WALL_SIGN(),
	ACACIA_WOOD("LOG_2", (byte) 12),
	ACTIVATOR_RAIL,
	AIR,
	ALLIUM("RED_ROSE", (byte) 2),
	ANDESITE("STONE", (byte) 5),
	ANDESITE_SLAB(),
	ANDESITE_STAIRS(),
	ANDESITE_WALL(),
	ANVIL("ANVIL", (byte) 0),
	APPLE,
	ARMOR_STAND(ServerVersion.V1_8, null),
	ARROW,
	ATTACHED_MELON_STEM("MELON_STEM"),
	ATTACHED_PUMPKIN_STEM("PUMPKIN_STEM"),
	AZURE_BLUET("RED_ROSE", (byte) 3),
	BAKED_POTATO("BAKED_POTATO"),
	BAMBOO(),
	BAMBOO_SAPLING(),
	BARREL(),
	BARRIER(),
	BAT_SPAWN_EGG("MONSTER_EGG", (byte) 65),
	BEACON,
	BEDROCK,
	BEEF("RAW_BEEF"),
	BEETROOT(), // the item
	BEETROOTS("BEETROOT_BLOCK"), // the crop
	BEETROOT_SEEDS(),
	BEETROOT_SOUP(),
	BELL(),
	BIRCH_BOAT("BOAT_BIRCH"),
	BIRCH_BUTTON(),
	BIRCH_DOOR("BIRCH_DOOR_ITEM"),
	BIRCH_FENCE(),
	BIRCH_FENCE_GATE(),
	BIRCH_LEAVES("LEAVES", (byte) 2),
	BIRCH_LOG("LOG", (byte) 2),
	BIRCH_PLANKS("WOOD", (byte) 2),
	BIRCH_PRESSURE_PLATE(),
	BIRCH_SAPLING("SAPLING", (byte) 2),
	BIRCH_SIGN(),
	BIRCH_SLAB("WOOD_STEP", (byte) 2),
	BIRCH_STAIRS(),
	BIRCH_TRAPDOOR(),
	BIRCH_WALL_SIGN(),
	BIRCH_WOOD("LOG", (byte) 14),
	BLACK_BANNER("BANNER", (byte) 0),
	BLACK_BED("BED", (byte) 15),
	BLACK_CARPET("CARPET", (byte) 15),
	BLACK_CONCRETE("CONCRETE", (byte) 15),
	BLACK_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 15),
	BLACK_DYE(),
	BLACK_GLAZED_TERRACOTTA(),
	BLACK_SHULKER_BOX(),
	BLACK_STAINED_GLASS("STAINED_GLASS", (byte) 15),
	BLACK_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 15),
	BLACK_TERRACOTTA("STAINED_CLAY", (byte) 15),
	BLACK_WALL_BANNER("WALL_BANNER", (byte) 0),
	BLACK_WOOL("WOOL", (byte) 15),
	BLAST_FURNACE,
	BLAZE_POWDER,
	BLAZE_ROD,
	BLAZE_SPAWN_EGG("MONSTER_EGG", (byte) 61),
	BLUE_BANNER("BANNER", (byte) 4),
	BLUE_BED("BED", (byte) 11),
	BLUE_CARPET("CARPET", (byte) 11),
	BLUE_CONCRETE("CONCRETE", (byte) 11),
	BLUE_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 11),
	BLUE_DYE(),
	BLUE_GLAZED_TERRACOTTA(),
	BLUE_ICE,
	BLUE_ORCHID("RED_ROSE", (byte) 1),
	BLUE_SHULKER_BOX(),
	BLUE_STAINED_GLASS("STAINED_GLASS", (byte) 11),
	BLUE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 11),
	BLUE_TERRACOTTA("STAINED_CLAY", (byte) 11),
	BLUE_WALL_BANNER("WALL_BANNER", (byte) 4),
	BLUE_WOOL("WOOL", (byte) 11),
	BONE,
	BONE_BLOCK,
	BONE_MEAL("INK_SACK", (byte) 15),
	BOOK,
	BOOKSHELF,
	BOW,
	BOWL,
	BRAIN_CORAL,
	BRAIN_CORAL_BLOCK,
	BRAIN_CORAL_FAN,
	BRAIN_CORAL_WALL_FAN,
	BREAD,
	BREWING_STAND,
	/**
	 * minecraft:brick (item)
	 */
	BRICK("CLAY_BRICK"),
	/**
	 * minecraft:brick_block (block)
	 */
	BRICKS("BRICK"),
	BRICK_SLAB("STEP", (byte) 4),
	BRICK_STAIRS,
	BRICK_WALL,
	BROWN_BANNER("BANNER", (byte) 3),
	BROWN_BED("BED", (byte) 12),
	BROWN_CARPET("CARPET", (byte) 12),
	BROWN_CONCRETE("CONCRETE", (byte) 12),
	BROWN_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 12),
	BROWN_DYE(),
	BROWN_GLAZED_TERRACOTTA(),
	BROWN_MUSHROOM,
	BROWN_MUSHROOM_BLOCK("HUGE_MUSHROOM_1"),
	BROWN_SHULKER_BOX(),
	BROWN_STAINED_GLASS("STAINED_GLASS", (byte) 12),
	BROWN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 12),
	BROWN_TERRACOTTA("STAINED_CLAY", (byte) 12),
	BROWN_WALL_BANNER("WALL_BANNER", (byte) 3),
	BROWN_WOOL("WOOL", (byte) 12),
	BUBBLE_COLUMN,
	BUBBLE_CORAL,
	BUBBLE_CORAL_BLOCK,
	BUBBLE_CORAL_FAN,
	BUBBLE_CORAL_WALL_FAN,
	BUCKET,
	CACTUS,
	CAKE,
	CAMPFIRE,
	CARROT("CARROT_ITEM"),
	CARROTS("CARROT"), // totally makes sense, lol
	CARROT_ON_A_STICK("CARROT_STICK"),
	CARTOGRAPHY_TABLE,
	CARVED_PUMPKIN("JACK_O_LANTERN"),
	CAT_SPAWN_EGG(),
	CAULDRON,
	CAVE_AIR(),
	CAVE_SPIDER_SPAWN_EGG("MONSTER_EGG", (byte) 59),
	CHAINMAIL_BOOTS,
	CHAINMAIL_CHESTPLATE,
	CHAINMAIL_HELMET,
	CHAINMAIL_LEGGINGS,
	CHAIN_COMMAND_BLOCK("COMMAND_CHAIN"),
	CHARCOAL("COAL", (byte) 1),
	CHEST,
	CHEST_MINECART("STORAGE_MINECART"),
	CHICKEN("RAW_CHICKEN"),
	CHICKEN_SPAWN_EGG("MONSTER_EGG", (byte) 93),
	CHIPPED_ANVIL("ANVIL", (byte) 1),
	CHISELED_QUARTZ_BLOCK("QUARTZ_BLOCK", (byte) 1),
	CHISELED_RED_SANDSTONE("RED_SANDSTONE", (byte) 1),
	CHISELED_SANDSTONE("SANDSTONE", (byte) 1),
	CHISELED_STONE_BRICKS("SMOOTH_BRICK", (byte) 3),
	CHORUS_FLOWER,
	CHORUS_FRUIT,
	CHORUS_PLANT,
	CLAY,
	CLAY_BALL,
	CLOCK("WATCH"),
	COAL,
	COAL_BLOCK,
	COAL_ORE,
	COARSE_DIRT("DIRT", (byte) 1),
	COBBLESTONE,
	COBBLESTONE_SLAB("STEP", (byte) 3),
	COBBLESTONE_STAIRS,
	COBBLESTONE_WALL("COBBLE_WALL"),
	COBWEB("WEB"),
	COCOA,
	COCOA_BEANS("INK_SACK", (byte) 3),
	COD("RAW_FISH", (byte) 0),
	COD_BUCKET,
	COD_SPAWN_EGG(),
	COMMAND_BLOCK("COMMAND"),
	COMMAND_BLOCK_MINECART("COMMAND_MINECART"),
	COMPARATOR("REDSTONE_COMPARATOR"),
	COMPASS,
	COMPOSTER,
	CONDUIT,
	COOKED_BEEF,
	COOKED_CHICKEN,
	COOKED_COD("COOKED_FISH", (byte) 0),
	COOKED_MUTTON,
	COOKED_PORKCHOP("GRILLED_PORK"),
	COOKED_RABBIT,
	COOKED_SALMON("COOKED_FISH", (byte) 1),
	COOKIE,
	CORNFLOWER,
	COW_SPAWN_EGG("MONSTER_EGG", (byte) 92),
	CRACKED_STONE_BRICKS("SMOOTH_BRICK", (byte) 2),
	CRAFTING_TABLE("WORKBENCH"),
	CREEPER_BANNER_PATTERN,
	CREEPER_HEAD("SKULL_ITEM", (byte) 4),
	CREEPER_SPAWN_EGG("MONSTER_EGG", (byte) 50),
	CREEPER_WALL_HEAD("SKULL", (byte) 4),
	CROSSBOW,
	CUT_RED_SANDSTONE,
	CUT_RED_SANDSTONE_SLAB(),
	CUT_SANDSTONE,
	CUT_SANDSTONE_SLAB(),
	CYAN_BANNER("BANNER", (byte) 6),
	CYAN_BED("BED", (byte) 9),
	CYAN_CARPET("CARPET", (byte) 9),
	CYAN_CONCRETE("CONCRETE", (byte) 9),
	CYAN_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 9),
	CYAN_DYE("INK_SACK", (byte) 6),
	CYAN_GLAZED_TERRACOTTA(),
	CYAN_SHULKER_BOX(),
	CYAN_STAINED_GLASS("STAINED_GLASS", (byte) 9),
	CYAN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 9),
	CYAN_TERRACOTTA("STAINED_CLAY", (byte) 9),
	CYAN_WALL_BANNER("WALL_BANNER", (byte) 6),
	CYAN_WOOL("WOOL", (byte) 9),
	DAMAGED_ANVIL("ANVIL", (byte) 2),
	DANDELION("YELLOW_FLOWER"),
	DARK_OAK_BOAT("BOAT_DARK_OAK"),
	DARK_OAK_BUTTON(),
	DARK_OAK_DOOR("DARK_OAK_DOOR_ITEM"),
	DARK_OAK_FENCE(),
	DARK_OAK_FENCE_GATE(),
	DARK_OAK_LEAVES("LEAVES_2", (byte) 1),
	DARK_OAK_LOG("LOG_2", (byte) 1),
	DARK_OAK_PLANKS("WOOD", (byte) 5),
	DARK_OAK_PRESSURE_PLATE(),
	DARK_OAK_SAPLING("SAPLING", (byte) 5),
	DARK_OAK_SIGN(),
	DARK_OAK_SLAB("WOOD_STEP", (byte) 5),
	DARK_OAK_STAIRS,
	DARK_OAK_TRAPDOOR(),
	DARK_OAK_WALL_SIGN(),
	DARK_OAK_WOOD("LOG_2", (byte) 13),
	DARK_PRISMARINE("PRISMARINE", (byte) 2),
	DARK_PRISMARINE_SLAB(),
	DARK_PRISMARINE_STAIRS,
	DAYLIGHT_DETECTOR,
	DEAD_BRAIN_CORAL,
	DEAD_BRAIN_CORAL_BLOCK,
	DEAD_BRAIN_CORAL_FAN,
	DEAD_BRAIN_CORAL_WALL_FAN,
	DEAD_BUBBLE_CORAL,
	DEAD_BUBBLE_CORAL_BLOCK,
	DEAD_BUBBLE_CORAL_FAN,
	DEAD_BUBBLE_CORAL_WALL_FAN,
	DEAD_BUSH("LONG_GRASS", (byte) 0),
	DEAD_FIRE_CORAL,
	DEAD_FIRE_CORAL_BLOCK,
	DEAD_FIRE_CORAL_FAN,
	DEAD_FIRE_CORAL_WALL_FAN,
	DEAD_HORN_CORAL,
	DEAD_HORN_CORAL_BLOCK,
	DEAD_HORN_CORAL_FAN,
	DEAD_HORN_CORAL_WALL_FAN,
	DEAD_TUBE_CORAL,
	DEAD_TUBE_CORAL_BLOCK,
	DEAD_TUBE_CORAL_FAN,
	DEAD_TUBE_CORAL_WALL_FAN,
	DEBUG_STICK,
	DETECTOR_RAIL,
	DIAMOND,
	DIAMOND_AXE,
	DIAMOND_BLOCK,
	DIAMOND_BOOTS,
	DIAMOND_CHESTPLATE,
	DIAMOND_HELMET,
	DIAMOND_HOE,
	DIAMOND_HORSE_ARMOR("DIAMOND_BARDING"),
	DIAMOND_LEGGINGS,
	DIAMOND_ORE,
	DIAMOND_PICKAXE,
	DIAMOND_SHOVEL("DIAMOND_SPADE"),
	DIAMOND_SWORD,
	DIORITE("STONE", (byte) 3),
	DIORITE_SLAB(),
	DIORITE_STAIRS,
	DIORITE_WALL,
	DIRT("DIRT", (byte) 0),
	DISPENSER,
	DOLPHIN_SPAWN_EGG(),
	DONKEY_SPAWN_EGG(),
	DRAGON_BREATH,
	DRAGON_EGG,
	DRAGON_HEAD("SKULL_ITEM", (byte) 5),
	DRAGON_WALL_HEAD("SKULL", (byte) 5),
	DRIED_KELP(),
	DRIED_KELP_BLOCK(),
	DROPPER,
	DROWNED_SPAWN_EGG(),
	EGG,
	ELDER_GUARDIAN_SPAWN_EGG("MONSTER_EGG", (byte) 4),
	ELYTRA,
	EMERALD,
	EMERALD_BLOCK,
	EMERALD_ORE,
	ENCHANTED_BOOK,
	ENCHANTED_GOLDEN_APPLE("GOLDEN_APPLE", (byte) 1),
	ENCHANTING_TABLE("ENCHANTMENT_TABLE"),
	ENDERMAN_SPAWN_EGG("MONSTER_EGG", (byte) 58),
	ENDERMITE_SPAWN_EGG("MONSTER_EGG", (byte) 67),
	ENDER_CHEST,
	ENDER_EYE("EYE_OF_ENDER"),
	ENDER_PEARL,
	END_CRYSTAL,
	END_GATEWAY,
	END_PORTAL("ENDER_PORTAL"),
	END_PORTAL_FRAME("ENDER_PORTAL_FRAME"),
	END_ROD,
	END_STONE("ENDER_STONE"),
	END_STONE_BRICKS,
	END_STONE_BRICK_SLAB(),
	END_STONE_BRICK_STAIRS,
	END_STONE_BRICK_WALL,
	EVOKER_SPAWN_EGG("MONSTER_EGG", (byte) 34),
	EXPERIENCE_BOTTLE("EXP_BOTTLE"),
	FARMLAND("SOIL"),
	FEATHER,
	FERMENTED_SPIDER_EYE,
	FERN("LONG_GRASS", (byte) 2),
	FILLED_MAP,
	FIRE,
	FIREWORK_ROCKET("FIREWORK"),
	FIREWORK_STAR("FIREWORK_CHARGE"),
	FIRE_CHARGE("FIREBALL"),
	FIRE_CORAL,
	FIRE_CORAL_BLOCK,
	FIRE_CORAL_FAN,
	FIRE_CORAL_WALL_FAN,
	FISHING_ROD,
	FLETCHING_TABLE,
	FLINT,
	FLINT_AND_STEEL,
	FLOWER_BANNER_PATTERN,
	FLOWER_POT("FLOWER_POT_ITEM"),
	FOX_SPAWN_EGG(),
	FROSTED_ICE,
	FURNACE,
	FURNACE_MINECART("POWERED_MINECART"),
	GHAST_SPAWN_EGG("MONSTER_EGG", (byte) 56),
	GHAST_TEAR,
	GLASS,
	GLASS_BOTTLE,
	GLASS_PANE("THIN_GLASS"),
	GLISTERING_MELON_SLICE("SPECKLED_MELON"),
	GLOBE_BANNER_PATTERN,
	GLOWSTONE,
	GLOWSTONE_DUST,
	GOLDEN_APPLE,
	GOLDEN_AXE("GOLD_AXE"),
	GOLDEN_BOOTS("GOLD_BOOTS"),
	GOLDEN_CARROT,
	GOLDEN_CHESTPLATE("GOLD_CHESTPLATE"),
	GOLDEN_HELMET("GOLD_HELMET"),
	GOLDEN_HOE("GOLD_HOE"),
	GOLDEN_HORSE_ARMOR("GOLD_BARDING"),
	GOLDEN_LEGGINGS("GOLD_LEGGINGS"),
	GOLDEN_PICKAXE("GOLD_PICKAXE"),
	GOLDEN_SHOVEL("GOLD_SPADE"),
	GOLDEN_SWORD("GOLD_SWORD"),
	GOLD_BLOCK,
	GOLD_INGOT,
	GOLD_NUGGET,
	GOLD_ORE,
	GRANITE("STONE", (byte) 1),
	GRANITE_SLAB(),
	GRANITE_STAIRS,
	GRANITE_WALL,
	GRASS("LONG_GRASS", (byte) 1),
	GRASS_BLOCK("GRASS"),
	GRASS_PATH,
	GRAVEL,
	GRAY_BANNER("BANNER", (byte) 8),
	GRAY_BED("BED", (byte) 7),
	GRAY_CARPET("CARPET", (byte) 7),
	GRAY_CONCRETE("CONCRETE", (byte) 7),
	GRAY_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 7),
	GRAY_DYE("INK_SACK", (byte) 8),
	GRAY_GLAZED_TERRACOTTA(),
	GRAY_SHULKER_BOX(),
	GRAY_STAINED_GLASS("STAINED_GLASS", (byte) 7),
	GRAY_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 7),
	GRAY_TERRACOTTA("STAINED_CLAY", (byte) 7),
	GRAY_WALL_BANNER("WALL_BANNER", (byte) 8),
	GRAY_WOOL("WOOL", (byte) 7),
	GREEN_BANNER("BANNER", (byte) 2),
	GREEN_BED("BED", (byte) 13),
	GREEN_CARPET("CARPET", (byte) 13),
	GREEN_CONCRETE(ServerVersion.V1_13, "CONCRETE", (byte) 13),
	GREEN_CONCRETE_POWDER(ServerVersion.V1_13, "CONCRETE_POWDER", (byte) 13),
	GREEN_DYE(ServerVersion.V1_14, "CACTUS_GREEEN", ServerVersion.V1_13, "INK_SACK", (byte) 2),
	GREEN_GLAZED_TERRACOTTA(),
	GREEN_SHULKER_BOX(),
	GREEN_STAINED_GLASS("STAINED_GLASS", (byte) 13),
	GREEN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 13),
	GREEN_TERRACOTTA("STAINED_CLAY", (byte) 13),
	GREEN_WALL_BANNER(ServerVersion.V1_13, "WALL_BANNER", (byte) 2),
	GREEN_WOOL("WOOL", (byte) 13),
	GRINDSTONE,
	GUARDIAN_SPAWN_EGG("MONSTER_EGG", (byte) 68),
	GUNPOWDER("SULPHUR"),
	HAY_BLOCK,
	HEART_OF_THE_SEA,
	HEAVY_WEIGHTED_PRESSURE_PLATE("IRON_PLATE"),
	HOPPER,
	HOPPER_MINECART("HOPPER_MINECART"),
	HORN_CORAL,
	HORN_CORAL_BLOCK,
	HORN_CORAL_FAN,
	HORN_CORAL_WALL_FAN,
	HORSE_SPAWN_EGG("MONSTER_EGG", (byte) 100),
	HUSK_SPAWN_EGG("MONSTER_EGG", (byte) 23),
	ICE,
	INFESTED_CHISELED_STONE_BRICKS("MONSTER_EGGS", (byte) 5),
	INFESTED_COBBLESTONE("MONSTER_EGGS", (byte) 1),
	INFESTED_CRACKED_STONE_BRICKS("MONSTER_EGGS", (byte) 4),
	INFESTED_MOSSY_STONE_BRICKS("MONSTER_EGGS", (byte) 3),
	INFESTED_STONE("MONSTER_EGGS", (byte) 0),
	INFESTED_STONE_BRICKS("MONSTER_EGGS", (byte) 2),
	INK_SAC("INK_SACK", (byte) 0),
	IRON_AXE,
	IRON_BARS("IRON_FENCE"),
	IRON_BLOCK,
	IRON_BOOTS,
	IRON_CHESTPLATE,
	IRON_DOOR, // TODO: legacy block id is IRON_DOOR_BLOCK
	IRON_HELMET,
	IRON_HOE,
	IRON_HORSE_ARMOR("IRON_BARDING"),
	IRON_INGOT,
	IRON_LEGGINGS,
	IRON_NUGGET,
	IRON_ORE,
	IRON_PICKAXE,
	IRON_SHOVEL("IRON_SPADE"),
	IRON_SWORD,
	IRON_TRAPDOOR(),
	ITEM_FRAME,
	JACK_O_LANTERN,
	JIGSAW,
	JUKEBOX,
	JUNGLE_BOAT("BOAT_JUNGLE"),
	JUNGLE_BUTTON(),
	JUNGLE_DOOR("JUNGLE_DOOR_ITEM"),
	JUNGLE_FENCE(),
	JUNGLE_FENCE_GATE(),
	JUNGLE_LEAVES("LEAVES", (byte) 3),
	JUNGLE_LOG("LOG", (byte) 3),
	JUNGLE_PLANKS("WOOD", (byte) 3),
	JUNGLE_PRESSURE_PLATE(),
	JUNGLE_SAPLING("SAPLING", (byte) 3),
	JUNGLE_SIGN(),
	JUNGLE_SLAB("WOOD_STEP", (byte) 3),
	JUNGLE_STAIRS("JUNGLE_WOOD_STAIRS"),
	JUNGLE_TRAPDOOR(),
	JUNGLE_WALL_SIGN(),
	JUNGLE_WOOD("LOG", (byte) 15),
	KELP,
	KELP_PLANT,
	KNOWLEDGE_BOOK,
	LADDER,
	LANTERN,
	LAPIS_BLOCK,
	LAPIS_LAZULI("INK_SACK", (byte) 4),
	LAPIS_ORE,
	LARGE_FERN("DOUBLE_PLANT", (byte) 3),
	LAVA,
	LAVA_BUCKET,
	LEAD("LEASH"),
	LEATHER,
	LEATHER_BOOTS,
	LEATHER_CHESTPLATE,
	LEATHER_HELMET,
	LEATHER_HORSE_ARMOR(),
	LEATHER_LEGGINGS,
	LECTERN,
	LEVER,
	LIGHT_BLUE_BANNER("BANNER", (byte) 12),
	LIGHT_BLUE_BED("BED", (byte) 3),
	LIGHT_BLUE_CARPET("CARPET", (byte) 3),
	LIGHT_BLUE_CONCRETE("CONCRETE", (byte) 3),
	LIGHT_BLUE_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 3),
	LIGHT_BLUE_DYE("INK_SACK", (byte) 12),
	LIGHT_BLUE_GLAZED_TERRACOTTA(),
	LIGHT_BLUE_SHULKER_BOX(),
	LIGHT_BLUE_STAINED_GLASS("STAINED_GLASS", (byte) 3),
	LIGHT_BLUE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 3),
	LIGHT_BLUE_TERRACOTTA("STAINED_CLAY", (byte) 3),
	LIGHT_BLUE_WALL_BANNER("WALL_BANNER", (byte) 12),
	LIGHT_BLUE_WOOL("WOOL", (byte) 3),
	LIGHT_GRAY_BANNER("BANNER", (byte) 7),
	LIGHT_GRAY_BED("BED", (byte) 8),
	LIGHT_GRAY_CARPET("CARPET", (byte) 8),
	LIGHT_GRAY_CONCRETE("CONCRETE", (byte) 8),
	LIGHT_GRAY_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 8),
	LIGHT_GRAY_DYE("INK_SACK", (byte) 7),
	LIGHT_GRAY_GLAZED_TERRACOTTA("SILVER_GLAZED_TERRACOTTA"),
	LIGHT_GRAY_SHULKER_BOX(ServerVersion.V1_11, "SILVER_SHULKER_BOX"),
	LIGHT_GRAY_STAINED_GLASS("STAINED_GLASS", (byte) 8),
	LIGHT_GRAY_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 8), // this is nearly invisible in a chest, lol
	LIGHT_GRAY_TERRACOTTA("STAINED_CLAY", (byte) 8),
	LIGHT_GRAY_WALL_BANNER("WALL_BANNER", (byte) 7),
	LIGHT_GRAY_WOOL("WOOL", (byte) 8),
	LIGHT_WEIGHTED_PRESSURE_PLATE("GOLD_PLATE"),
	LILAC("LONG_GRASS", (byte) 1),
	LILY_OF_THE_VALLEY,
	LILY_PAD("WATER_LILY"),
	LIME_BANNER("BANNER", (byte) 10),
	LIME_BED("BED", (byte) 5),
	LIME_CARPET("CARPET", (byte) 5),
	LIME_CONCRETE("CONCRETE", (byte) 5),
	LIME_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 5),
	LIME_DYE("INK_SACK", (byte) 10),
	LIME_GLAZED_TERRACOTTA(),
	LIME_SHULKER_BOX(),
	LIME_STAINED_GLASS("STAINED_GLASS", (byte) 5),
	LIME_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 5),
	LIME_TERRACOTTA("STAINED_CLAY", (byte) 5),
	LIME_WALL_BANNER("WALL_BANNER", (byte) 10),
	LIME_WOOL("WOOL", (byte) 5),
	LINGERING_POTION,
	LLAMA_SPAWN_EGG("MONSTER_EGG", (byte) 103),
	LOOM,
	MAGENTA_BANNER("BANNER", (byte) 13),
	MAGENTA_BED("BED", (byte) 2),
	MAGENTA_CARPET("CARPET", (byte) 2),
	MAGENTA_CONCRETE("CONCRETE", (byte) 2),
	MAGENTA_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 2),
	MAGENTA_DYE("INK_SACK", (byte) 13),
	MAGENTA_GLAZED_TERRACOTTA(),
	MAGENTA_SHULKER_BOX(),
	MAGENTA_STAINED_GLASS("STAINED_GLASS", (byte) 2),
	MAGENTA_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 2),
	MAGENTA_TERRACOTTA("STAINED_CLAY", (byte) 2),
	MAGENTA_WALL_BANNER("WALL_BANNER", (byte) 13),
	MAGENTA_WOOL("WOOL", (byte) 2),
	MAGMA_BLOCK("MAGMA"),
	MAGMA_CREAM,
	MAGMA_CUBE_SPAWN_EGG("MONSTER_EGG", (byte) 62),
	MAP,
	MELON("MELON_BLOCK"),
	MELON_SEEDS,
	MELON_SLICE("MELON"),
	MELON_STEM,
	MILK_BUCKET,
	MINECART,
	MOJANG_BANNER_PATTERN,
	MOOSHROOM_SPAWN_EGG("MONSTER_EGG", (byte) 96),
	MOSSY_COBBLESTONE,
	MOSSY_COBBLESTONE_SLAB(),
	MOSSY_COBBLESTONE_STAIRS,
	MOSSY_COBBLESTONE_WALL,
	MOSSY_STONE_BRICKS("SMOOTH_BRICK", (byte) 1),
	MOSSY_STONE_BRICK_SLAB(),
	MOSSY_STONE_BRICK_STAIRS,
	MOSSY_STONE_BRICK_WALL,
	MOVING_PISTON("PISTON_MOVING_PIECE"),
	MULE_SPAWN_EGG("MONSTER_EGG", (byte) 32),
	MUSHROOM_STEM("HUGE_MUSHROOM_1", (byte) 10), // also ("HUGE_MUSHROOM_2", (byte) 10)
	MUSHROOM_STEW("MUSHROOM_SOUP"),
	MUSIC_DISC_11("RECORD_11"),
	MUSIC_DISC_13("GOLD_RECORD"),
	MUSIC_DISC_BLOCKS("RECORD_3"),
	MUSIC_DISC_CAT("GREEN_RECORD"),
	MUSIC_DISC_CHIRP("RECORD_4"),
	MUSIC_DISC_FAR("RECORD_5"),
	MUSIC_DISC_MALL("RECORD_6"),
	MUSIC_DISC_MELLOHI("RECORD_7"),
	MUSIC_DISC_STAL("RECORD_8"),
	MUSIC_DISC_STRAD("RECORD_9"),
	MUSIC_DISC_WAIT("RECORD_12"),
	MUSIC_DISC_WARD("RECORD_10"),
	MUTTON,
	MYCELIUM("MYCEL"),
	NAME_TAG,
	NAUTILUS_SHELL,
	NETHERRACK,
	NETHER_BRICK("NETHER_BRICK_ITEM"),
	NETHER_BRICKS("NETHER_BRICK"),
	NETHER_BRICK_FENCE(),
	NETHER_BRICK_SLAB("STEP", (byte) 6),
	NETHER_BRICK_STAIRS,
	NETHER_BRICK_WALL,
	NETHER_PORTAL("PORTAL"),
	NETHER_QUARTZ_ORE("QUARTZ_ORE"),
	NETHER_STAR,
	NETHER_WART("NETHER_STALK"),
	NETHER_WART_BLOCK,
	NOTE_BLOCK,
	OAK_BOAT("BOAT"),
	OAK_BUTTON("WOOD_BUTTON"),
	OAK_DOOR("WOOD_DOOR"), // TODO: legacy block id is WOODEN_DOOR
	OAK_FENCE("FENCE"),
	OAK_FENCE_GATE("FENCE_GATE"),
	OAK_LEAVES("LEAVES", (byte) 0),
	OAK_LOG("LOG", (byte) 0),
	OAK_PLANKS("WOOD", (byte) 0),
	OAK_PRESSURE_PLATE("WOOD_PLATE"),
	OAK_SAPLING("SAPLING", (byte) 0),
	OAK_SIGN(ServerVersion.V1_14, "SIGN"),
	OAK_SLAB("WOOD_STEP", (byte) 0),
	OAK_STAIRS("WOOD_STAIRS"),
	OAK_TRAPDOOR("TRAP_DOOR"),
	OAK_WALL_SIGN(ServerVersion.V1_14, "WALL_SIGN"),
	OAK_WOOD("LOG", (byte) 12),
	OBSERVER,
	OBSIDIAN,
	OCELOT_SPAWN_EGG("MONSTER_EGG", (byte) 98),
	ORANGE_BANNER("BANNER", (byte) 14),
	ORANGE_BED("BED", (byte) 1),
	ORANGE_CARPET("CARPET", (byte) 1),
	ORANGE_CONCRETE("CONCRETE", (byte) 1),
	ORANGE_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 1),
	ORANGE_DYE("INK_SACK", (byte) 14),
	ORANGE_GLAZED_TERRACOTTA(),
	ORANGE_SHULKER_BOX(),
	ORANGE_STAINED_GLASS("STAINED_GLASS", (byte) 1),
	ORANGE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 1),
	ORANGE_TERRACOTTA("STAINED_CLAY", (byte) 1),
	ORANGE_TULIP("RED_ROSE", (byte) 5),
	ORANGE_WALL_BANNER("WALL_BANNER", (byte) 14),
	ORANGE_WOOL("WOOL", (byte) 1),
	OXEYE_DAISY("RED_ROSE", (byte) 8),
	PACKED_ICE,
	PAINTING,
	PANDA_SPAWN_EGG(),
	PAPER,
	PARROT_SPAWN_EGG("MONSTER_EGG", (byte) 105),
	PEONY("DOUBLE_PLANT", (byte) 5),
	PETRIFIED_OAK_SLAB("WOOD_STEP", (byte) 0),
	PHANTOM_MEMBRANE(),
	PHANTOM_SPAWN_EGG(),
	PIG_SPAWN_EGG("MONSTER_EGG", (byte) 90),
	PILLAGER_SPAWN_EGG(),
	PINK_BANNER("BANNER", (byte) 9),
	PINK_BED("BED", (byte) 6),
	PINK_CARPET("CARPET", (byte) 6),
	PINK_CONCRETE("CONCRETE", (byte) 6),
	PINK_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 6),
	PINK_DYE("INK_SACK", (byte) 9),
	PINK_GLAZED_TERRACOTTA(),
	PINK_SHULKER_BOX(),
	PINK_STAINED_GLASS("STAINED_GLASS", (byte) 6),
	PINK_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 6),
	PINK_TERRACOTTA("STAINED_CLAY", (byte) 6),
	PINK_TULIP("RED_ROSE", (byte) 7),
	PINK_WALL_BANNER("WALL_BANNER", (byte) 9),
	PINK_WOOL("WOOL", (byte) 6),
	PISTON("PISTON_BASE"),
	PISTON_HEAD("PISTON_EXTENSION"),
	PLAYER_HEAD("SKULL_ITEM", (byte) 3),
	PLAYER_WALL_HEAD("SKULL", (byte) 3),
	PODZOL("DIRT", (byte) 2),
	POISONOUS_POTATO,
	POLAR_BEAR_SPAWN_EGG("MONSTER_EGG", (byte) 102),
	POLISHED_ANDESITE("STONE", (byte) 6),
	POLISHED_ANDESITE_SLAB(),
	POLISHED_ANDESITE_STAIRS,
	POLISHED_DIORITE("STONE", (byte) 4),
	POLISHED_DIORITE_SLAB(),
	POLISHED_DIORITE_STAIRS,
	POLISHED_GRANITE("STONE", (byte) 2),
	POLISHED_GRANITE_SLAB(),
	POLISHED_GRANITE_STAIRS,
	POPPED_CHORUS_FRUIT,
	POPPY("RED_ROSE", (byte) 0),
	PORKCHOP("PORK"),
	POTATO("POTATO_ITEM"),
	POTATOES("POTATO"),
	POTION,
	POTTED_ACACIA_SAPLING,
	POTTED_ALLIUM,
	POTTED_AZURE_BLUET,
	POTTED_BAMBOO,
	POTTED_BIRCH_SAPLING,
	POTTED_BLUE_ORCHID,
	POTTED_BROWN_MUSHROOM,
	POTTED_CACTUS,
	POTTED_CORNFLOWER,
	POTTED_DANDELION,
	POTTED_DARK_OAK_SAPLING,
	POTTED_DEAD_BUSH,
	POTTED_FERN,
	POTTED_JUNGLE_SAPLING,
	POTTED_LILY_OF_THE_VALLEY,
	POTTED_OAK_SAPLING,
	POTTED_ORANGE_TULIP,
	POTTED_OXEYE_DAISY,
	POTTED_PINK_TULIP,
	POTTED_POPPY,
	POTTED_RED_MUSHROOM,
	POTTED_RED_TULIP,
	POTTED_SPRUCE_SAPLING,
	POTTED_WHITE_TULIP,
	POTTED_WITHER_ROSE,
	POWERED_RAIL,
	PRISMARINE("PRISMARINE", (byte) 0),
	PRISMARINE_BRICKS("PRISMARINE", (byte) 1),
	PRISMARINE_BRICK_SLAB(),
	PRISMARINE_BRICK_STAIRS,
	PRISMARINE_CRYSTALS,
	PRISMARINE_SHARD,
	PRISMARINE_SLAB(),
	PRISMARINE_STAIRS,
	PRISMARINE_WALL,
	PUFFERFISH("RAW_FISH", (byte) 3),
	PUFFERFISH_BUCKET,
	PUFFERFISH_SPAWN_EGG(),
	PUMPKIN,
	PUMPKIN_PIE,
	PUMPKIN_SEEDS,
	PUMPKIN_STEM,
	PURPLE_BANNER("BANNER", (byte) 5),
	PURPLE_BED("BED", (byte) 10),
	PURPLE_CARPET("CARPET", (byte) 10),
	PURPLE_CONCRETE("CONCRETE", (byte) 10),
	PURPLE_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 10),
	PURPLE_DYE("INK_SACK", (byte) 5),
	PURPLE_GLAZED_TERRACOTTA(),
	PURPLE_SHULKER_BOX(),
	PURPLE_STAINED_GLASS("STAINED_GLASS", (byte) 10),
	PURPLE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 10),
	PURPLE_TERRACOTTA("STAINED_CLAY", (byte) 10),
	PURPLE_WALL_BANNER("WALL_BANNER", (byte) 5),
	PURPLE_WOOL("WOOL", (byte) 10),
	PURPUR_BLOCK,
	PURPUR_PILLAR,
	PURPUR_SLAB(),
	PURPUR_STAIRS,
	QUARTZ,
	QUARTZ_BLOCK("QUARTZ_BLOCK", (byte) 0),
	QUARTZ_PILLAR("QUARTZ_BLOCK", (byte) 2),
	QUARTZ_SLAB("STEP", (byte) 7),
	QUARTZ_STAIRS,
	RABBIT,
	RABBIT_FOOT,
	RABBIT_HIDE,
	RABBIT_SPAWN_EGG("MONSTER_EGG", (byte) 101),
	RABBIT_STEW,
	RAIL("RAILS"),
	RAVAGER_SPAWN_EGG(),
	REDSTONE,
	REDSTONE_BLOCK,
	REDSTONE_LAMP("REDSTONE_LAMP_OFF"),
	REDSTONE_ORE,
	REDSTONE_TORCH("REDSTONE_TORCH_ON"),
	REDSTONE_WALL_TORCH,
	REDSTONE_WIRE,
	RED_BANNER("BANNER", (byte) 1),
	RED_BED("BED", (byte) 14),
	RED_CARPET("CARPET", (byte) 14),
	RED_CONCRETE("CONCRETE", (byte) 14),
	RED_CONCRETE_POWDER("CONCRETE_POWDER", (byte) 14),
	RED_DYE(ServerVersion.V1_14, "ROSE_RED", ServerVersion.V1_13, "INK_SACK", (byte) 1),
	RED_GLAZED_TERRACOTTA(),
	RED_MUSHROOM,
	RED_MUSHROOM_BLOCK("HUGE_MUSHROOM_2"),
	RED_NETHER_BRICKS("RED_NETHER_BRICK"),
	RED_NETHER_BRICK_SLAB(),
	RED_NETHER_BRICK_STAIRS,
	RED_NETHER_BRICK_WALL,
	RED_SAND("SAND", (byte) 1),
	RED_SANDSTONE("RED_SANDSTONE", (byte) 0),
	RED_SANDSTONE_SLAB("STONE_SLAB2", (byte) 0),
	RED_SANDSTONE_STAIRS,
	RED_SANDSTONE_WALL,
	RED_SHULKER_BOX(),
	RED_STAINED_GLASS("STAINED_GLASS", (byte) 14),
	RED_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 14),
	RED_TERRACOTTA("STAINED_CLAY", (byte) 14),
	RED_TULIP("RED_ROSE", (byte) 4),
	RED_WALL_BANNER("WALL_BANNER", (byte) 1),
	RED_WOOL("WOOL", (byte) 14),
	REPEATER("DIODE"),
	REPEATING_COMMAND_BLOCK("COMMAND_REPEATING"),
	ROSE_BUSH("DOUBLE_PLANT", (byte) 4),
	ROTTEN_FLESH,
	SADDLE,
	SALMON("RAW_FISH", (byte) 1),
	SALMON_BUCKET,
	SALMON_SPAWN_EGG(),
	SAND("SAND", (byte) 0),
	SANDSTONE("SANDSTONE", (byte) 0),
	SANDSTONE_SLAB("STEP", (byte) 1),
	SANDSTONE_STAIRS,
	SANDSTONE_WALL,
	SCAFFOLDING,
	SCUTE,
	SEAGRASS,
	SEA_LANTERN,
	SEA_PICKLE,
	SHEARS,
	SHEEP_SPAWN_EGG("MONSTER_EGG", (byte) 91),
	SHIELD,
	SHULKER_BOX(),
	SHULKER_SHELL(),
	SHULKER_SPAWN_EGG("MONSTER_EGG", (byte) 69),
	SILVERFISH_SPAWN_EGG("MONSTER_EGG", (byte) 60),
	SKELETON_HORSE_SPAWN_EGG("MONSTER_EGG", (byte) 28),
	SKELETON_SKULL("SKULL_ITEM", (byte) 0),
	SKELETON_SPAWN_EGG("MONSTER_EGG", (byte) 51),
	SKELETON_WALL_SKULL("SKULL", (byte) 0),
	SKULL_BANNER_PATTERN,
	SLIME_BALL,
	SLIME_BLOCK,
	SLIME_SPAWN_EGG("MONSTER_EGG", (byte) 55),
	SMITHING_TABLE,
	SMOKER,
	SMOOTH_QUARTZ,
	SMOOTH_QUARTZ_SLAB(),
	SMOOTH_QUARTZ_STAIRS,
	SMOOTH_RED_SANDSTONE("RED_SANDSTONE", (byte) 2),
	SMOOTH_RED_SANDSTONE_SLAB("STONE_SLAB2", (byte) 0),
	SMOOTH_RED_SANDSTONE_STAIRS,
	SMOOTH_SANDSTONE("SANDSTONE", (byte) 2),
	SMOOTH_SANDSTONE_SLAB(),
	SMOOTH_SANDSTONE_STAIRS,
	SMOOTH_STONE,
	SMOOTH_STONE_SLAB(),
	SNOW,
	SNOWBALL("SNOW_BALL"),
	SNOW_BLOCK,
	SOUL_SAND,
	SPAWNER("MOB_SPAWNER"),
	SPECTRAL_ARROW,
	SPIDER_EYE,
	SPIDER_SPAWN_EGG("MONSTER_EGG", (byte) 52),
	SPLASH_POTION("POTION"), // legacy uses data to make the specific potions and splash variants
	SPONGE("SPONGE", (byte) 0),
	SPRUCE_BOAT("BOAT_SPRUCE"),
	SPRUCE_BUTTON(),
	SPRUCE_DOOR("SPRUCE_DOOR_ITEM"),
	SPRUCE_FENCE(),
	SPRUCE_FENCE_GATE(),
	SPRUCE_LEAVES("LEAVES", (byte) 1),
	SPRUCE_LOG("LOG", (byte) 1),
	SPRUCE_PLANKS("WOOD", (byte) 1),
	SPRUCE_PRESSURE_PLATE(),
	SPRUCE_SAPLING("SAPLING", (byte) 1),
	SPRUCE_SIGN(),
	SPRUCE_SLAB("WOOD_STEP", (byte) 1),
	SPRUCE_STAIRS("SPRUCE_WOOD_STAIRS"),
	SPRUCE_TRAPDOOR(),
	SPRUCE_WALL_SIGN(),
	SPRUCE_WOOD("LOG", (byte) 13),
	SQUID_SPAWN_EGG("MONSTER_EGG", (byte) 94),
	STICK,
	STICKY_PISTON("PISTON_STICKY_BASE"),
	STONE(),
	STONECUTTER,
	STONE_AXE,
	/**
	 * minecraft:stonebrick
	 */
	STONE_BRICKS("SMOOTH_BRICK", (byte) 0),
	STONE_BRICK_SLAB("STEP", (byte) 5),
	STONE_BRICK_STAIRS("SMOOTH_STAIRS"),
	STONE_BRICK_WALL(),
	STONE_BUTTON,
	STONE_HOE,
	STONE_PICKAXE,
	STONE_PRESSURE_PLATE("STONE_PLATE"),
	STONE_SHOVEL("STONE_SPADE"),
	STONE_SLAB("STEP", (byte) 0),
	STONE_STAIRS(),
	STONE_SWORD,
	STRAY_SPAWN_EGG("MONSTER_EGG", (byte) 6),
	STRING,
	STRIPPED_ACACIA_LOG,
	STRIPPED_ACACIA_WOOD,
	STRIPPED_BIRCH_LOG,
	STRIPPED_BIRCH_WOOD,
	STRIPPED_DARK_OAK_LOG,
	STRIPPED_DARK_OAK_WOOD,
	STRIPPED_JUNGLE_LOG,
	STRIPPED_JUNGLE_WOOD,
	STRIPPED_OAK_LOG,
	STRIPPED_OAK_WOOD,
	STRIPPED_SPRUCE_LOG,
	STRIPPED_SPRUCE_WOOD,
	STRUCTURE_BLOCK,
	STRUCTURE_VOID,
	SUGAR,
	SUGAR_CANE,
	SUNFLOWER("DOUBLE_PLANT", (byte) 0),
	SUSPICIOUS_STEW,
	SWEET_BERRIES,
	SWEET_BERRY_BUSH,
	TALL_GRASS("DOUBLE_PLANT", (byte) 2),
	TALL_SEAGRASS,
	TERRACOTTA("HARD_CLAY"),
	TIPPED_ARROW,
	TNT,
	TNT_MINECART("EXPLOSIVE_MINECART"),
	TORCH,
	TOTEM_OF_UNDYING("TOTEM"),
	TRADER_LLAMA_SPAWN_EGG(ServerVersion.V1_14, "LLAMA_SPAWN_EGG"),
	TRAPPED_CHEST,
	TRIDENT,
	TRIPWIRE,
	TRIPWIRE_HOOK,
	TROPICAL_FISH("RAW_FISH", (byte) 2), // (aka clownfish)
	TROPICAL_FISH_BUCKET,
	TROPICAL_FISH_SPAWN_EGG(),
	TUBE_CORAL,
	TUBE_CORAL_BLOCK,
	TUBE_CORAL_FAN,
	TUBE_CORAL_WALL_FAN,
	TURTLE_EGG,
	TURTLE_HELMET,
	TURTLE_SPAWN_EGG(),
	VEX_SPAWN_EGG("MONSTER_EGG", (byte) 35),
	VILLAGER_SPAWN_EGG("MONSTER_EGG", (byte) 120),
	VINDICATOR_SPAWN_EGG("MONSTER_EGG", (byte) 36),
	VINE,
	VOID_AIR,
	WALL_TORCH,
	WANDERING_TRADER_SPAWN_EGG(),
	WATER,
	WATER_BUCKET,
	WET_SPONGE("SPONGE", (byte) 1),
	WHEAT,
	WHEAT_SEEDS("SEEDS"),
	WHITE_BANNER("BANNER", (byte) 15),
	WHITE_BED("BED", (byte) 0),
	WHITE_CARPET("CARPET", (byte) 0),
	WHITE_CONCRETE("CONCRETE", (byte) 0),
	WHITE_CONCRETE_POWDER("CONCRETE", (byte) 0),
	WHITE_DYE("INK_SACK", (byte) 15),
	WHITE_GLAZED_TERRACOTTA(),
	WHITE_SHULKER_BOX(),
	WHITE_STAINED_GLASS("STAINED_GLASS", (byte) 0),
	WHITE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 0),
	WHITE_TERRACOTTA("STAINED_CLAY", (byte) 0),
	WHITE_TULIP("RED_ROSE", (byte) 6),
	WHITE_WALL_BANNER("WALL_BANNER", (byte) 15),
	WHITE_WOOL("WOOL", (byte) 0),
	WITCH_SPAWN_EGG("MONSTER_EGG", (byte) 66),
	WITHER_ROSE,
	WITHER_SKELETON_SKULL("SKULL_ITEM", (byte) 1),
	WITHER_SKELETON_SPAWN_EGG("MONSTER_EGG", (byte) 5),
	WITHER_SKELETON_WALL_SKULL("SKULL", (byte) 1),
	WOLF_SPAWN_EGG("MONSTER_EGG", (byte) 95),
	WOODEN_AXE("WOOD_AXE"),
	WOODEN_HOE("WOOD_HOE"),
	WOODEN_PICKAXE("WOOD_PICKAXE"),
	WOODEN_SHOVEL("WOOD_SPADE"),
	WOODEN_SWORD("WOOD_SWORD"),
	WRITABLE_BOOK("BOOK_AND_QUILL"),
	WRITTEN_BOOK,
	YELLOW_BANNER("BANNER", (byte) 11),
	YELLOW_BED("BED", (byte) 4),
	YELLOW_CARPET("CARPET", (byte) 4),
	YELLOW_CONCRETE("CONCRETE", (byte) 4),
	YELLOW_CONCRETE_POWDER("CONCRETE", (byte) 4),
	YELLOW_DYE(ServerVersion.V1_14, "DANDELION_YELLOW", ServerVersion.V1_13, "INK_SACK", (byte) 11),
	YELLOW_GLAZED_TERRACOTTA(),
	YELLOW_SHULKER_BOX(),
	YELLOW_STAINED_GLASS("STAINED_GLASS", (byte) 4),
	YELLOW_STAINED_GLASS_PANE("STAINED_GLASS_PANE", (byte) 4),
	YELLOW_TERRACOTTA("STAINED_CLAY", (byte) 4),
	YELLOW_WALL_BANNER("WALL_BANNER", (byte) 11),
	YELLOW_WOOL("WOOL", (byte) 4),
	ZOMBIE_HEAD("SKULL_ITEM", (byte) 2),
	ZOMBIE_HORSE_SPAWN_EGG("MONSTER_EGG", (byte) 29),
	ZOMBIE_PIGMAN_SPAWN_EGG("MONSTER_EGG", (byte) 57),
	ZOMBIE_SPAWN_EGG("MONSTER_EGG", (byte) 54),
	ZOMBIE_VILLAGER_SPAWN_EGG("MONSTER_EGG", (byte) 27),
	ZOMBIE_WALL_HEAD("SKULL", (byte) 2),;

	private final String modern, modern2, legacy;
	private final LegacyAnalouges compatibleMaterial;
	private final boolean legacyRequiresData;
	// some materials (I'm looking at you, GREEN_DYE) have changed ID more than once
	// minVersion is the min for modern, and minVersion2 is min to use legacyCompat1
	private final ServerVersion minVersion, minVersion2;
	private final byte legacyData;
	private final Material material;
	private final Byte data;
	// quick test to see if our version is < 1.13
	protected static final boolean useLegacy = ServerVersion.isServerVersionBelow(ServerVersion.V1_13);
	// map to speed up name->material lookups
	private static final Map<String, LegacyMaterials> lookupMap = new HashMap();

	static {
		for (LegacyMaterials m : values()) {
			lookupMap.put(m.name(), m);
			lookupMap.put(m.material + ":" + (m.data == null ? "" : m.data), m);
		}
		for (LegacyMaterials m : values()) {
			if (!lookupMap.containsKey(m.legacy)) {
				lookupMap.put(m.legacy, m);
			}
		}
	}

	LegacyMaterials() {
		this(ServerVersion.UNKNOWN, null, null);
	}

	LegacyMaterials(String legacy) {
		this(ServerVersion.V1_13, null, null, legacy, null);
	}

	LegacyMaterials(String legacy, byte legacyData) {
		this(ServerVersion.V1_13, null, null, legacy, legacyData);
	}

	LegacyMaterials(ServerVersion modernMinimum, String legacy) {
		this(modernMinimum, null, null, legacy, null);
	}

	LegacyMaterials(ServerVersion modernMinimum, String legacy, Byte legacyData) {
		this(modernMinimum, null, null, legacy, legacyData);
	}

	LegacyMaterials(ServerVersion modernMinimum, String modern2, ServerVersion modern2Minimum, String legacyMaterial, Byte legacyData) {
		this.modern = name();
		this.modern2 = modern2;
		this.minVersion = modernMinimum;
		this.minVersion2 = modern2Minimum;
		this.legacy = legacyMaterial;
		this.legacyData = legacyData == null ? 0 : legacyData;
		this.legacyRequiresData = legacyData != null;
		this.compatibleMaterial = LegacyAnalouges.lookupAnalouge(modern);

		if (compatibleMaterial != null && ServerVersion.isServerVersionBelow(compatibleMaterial.versionLessThan)) {
			// server older than this item: use a proxy
			material = compatibleMaterial.material;
			data = compatibleMaterial.data;
		} else if (ServerVersion.isServerVersionAtLeast(minVersion)) {
			material = Material.getMaterial(modern);
			data = null;
		} else if (modern2 != null && ServerVersion.isServerVersionAtLeast(minVersion2)) {
			material = Material.getMaterial(modern2);
			data = null;
		} else if (legacyMaterial != null && (compatibleMaterial == null || ServerVersion.isServerVersionAtLeast(compatibleMaterial.versionLessThan))) {
			// we're using a server that has the legacy value available
			material = Material.getMaterial(legacyMaterial);
			data = legacyRequiresData ? legacyData : null;
		} else if (compatibleMaterial != null) {
			// no match: use a proxy
			material = compatibleMaterial.material;
			data = compatibleMaterial.data;
		} else {
			material = null;
			data = null;
		}
	}

	/**
	 * Get the Bukkit Material for this material
	 *
	 * @return
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Get an item that resembles this material for the current server version
	 *
	 * @return
	 */
	public ItemStack getItem() {
		if (usesCompatibility()) {
			return compatibleMaterial.getItem();
		}
		return data != null ? new ItemStack(material, 1, data) : new ItemStack(material);
	}

	/**
	 *
	 * Does this material need to use a legacy fallback?
	 *
	 * @return
	 */
	public boolean usesLegacy() {
		return legacy != null && ServerVersion.isServerVersionBelow(minVersion);
	}

	/**
	 * Does this material need to use a fallback item on this server?
	 *
	 * @return
	 */
	public boolean usesCompatibility() {
        return compatibleMaterial != null && material == compatibleMaterial.material;
		//return compatibleMaterial != null && ServerVersion.isServerVersionBelow(compatibleMaterial.versionLessThan);
	}

	/**
	 * Get the legacy data value for this material if there is one, or -1 if
	 * none
	 *
	 * @return
	 */
	public byte getData() {
		return data != null ? data : -1;
	}

	/**
	 * Check if current material requires a data value.
	 *
	 * @return true if server is legacy and this item requires data to be defined.
	 */
	public boolean usesData() {
		return data != null;
	}

	/**
	 * Lookup a Legacy Material by its modern id name. <br />
	 * This also can grab materials by their legacy, but only if there is no
	 * modern material by that name.
	 *
	 * @param name item to lookup
	 * @return LegacyMaterial or null if none found
	 */
	public static LegacyMaterials getMaterial(String name) {
		return name == null ? null : lookupMap.get(name.toUpperCase());
	}

	/**
	 * Lookup a Legacy Material by its modern id name. <br />
	 * This also can grab materials by their legacy, but only if there is no
	 * modern material by that name.
	 *
	 * @param name item to lookup
     * @param def default item if this is not a valid material
	 * @return LegacyMaterial or null if none found
	 */
	public static LegacyMaterials getMaterial(String name, LegacyMaterials def) {
		return name == null ? def : lookupMap.getOrDefault(name.toUpperCase(), def);
	}

	/**
	 * Lookup a Legacy Material by bukkit material.
	 *
	 * @param mat item to lookup
	 * @return LegacyMaterial or null if none found
	 */
	public static LegacyMaterials getMaterial(Material mat) {
		return mat == null ? null : lookupMap.get(mat.name());
	}

	/**
	 * Lookup a Legacy Material by Itemstack.
	 *
	 * @param item item to lookup
	 * @return LegacyMaterial or null if none found
	 */
	public static LegacyMaterials getMaterial(ItemStack item) {
		if (item == null) {
			return null;
		}
		String key = item.getType() + ":";
		LegacyMaterials m = lookupMap.get(key);
		return m != null ? m : lookupMap.get(key + item.getDurability());
	}

    static LinkedHashSet<LegacyMaterials> all = null;

    public static Set<LegacyMaterials> getAllValidItemMaterials() {
        if (all == null) {
            all = new LinkedHashSet();
            for (LegacyMaterials mat : values()) {
                if (mat.isValidItem() && !mat.usesCompatibility()) {
                    all.add(mat);
                }
            }
        }
        return Collections.unmodifiableSet(all);
    }

	/**
	 * Lookup a Legacy Material by its modern id name and return its associated
	 * Item. <br />
	 * This also can grab materials by their legacy, but only if there is no
	 * modern material by that name.
	 *
	 * @param name item to lookup
	 * @return ItemStack for this material, or null if none found
	 */
	public static ItemStack getItem(String name) {
		if (name == null) {
			return null;
		}
		LegacyMaterials m = lookupMap.get(name.toUpperCase());
		if (m != null) {
			return m.getItem();
		}
		Material mat = Material.getMaterial(name);
		return mat != null ? new ItemStack(mat) : null;
	}

	/**
	 * Check to see if an item matches this specific material type
	 * 
	 * @param item Item to check
	 * @return true if material of the ItemStack matches this item, corrected for legacy data
	 */
	public boolean matches(ItemStack item) {
		return item != null && item.getType() == material && (data == null || item.getDurability() == data); // eons ago, ItemStack.getData() would return a byte. 1.7 doesn't, though.
	}

	/**
	 * Some blocks change to other materials when placed down. This checks to
	 * see if this one is one of those.
	 *
	 * @return
	 */
	public boolean hasDifferentBlockItem() {
		switch (this) {
			case STRING:
				return true;
			case ACACIA_DOOR:
			case BIRCH_DOOR:
			case DARK_OAK_DOOR:
			case JUNGLE_DOOR:
			case SPRUCE_DOOR:
			case IRON_DOOR:
			case OAK_DOOR:
			case ACACIA_SIGN:
			case BIRCH_SIGN:
			case DARK_OAK_SIGN:
			case JUNGLE_SIGN:
			case OAK_SIGN:
			case SPRUCE_SIGN:
			case BLACK_BANNER:
			case BLUE_BANNER:
			case BROWN_BANNER:
			case CYAN_BANNER:
			case GRAY_BANNER:
			case GREEN_BANNER:
			case LIGHT_BLUE_BANNER:
			case LIGHT_GRAY_BANNER:
			case LIME_BANNER:
			case MAGENTA_BANNER:
			case ORANGE_BANNER:
			case PINK_BANNER:
			case PURPLE_BANNER:
			case RED_BANNER:
			case WHITE_BANNER:
			case YELLOW_BANNER:
			case BLACK_BED:
			case BLUE_BED:
			case BROWN_BED:
			case CYAN_BED:
			case GRAY_BED:
			case GREEN_BED:
			case LIGHT_BLUE_BED:
			case LIGHT_GRAY_BED:
			case LIME_BED:
			case MAGENTA_BED:
			case ORANGE_BED:
			case PINK_BED:
			case PURPLE_BED:
			case RED_BED:
			case WHITE_BED:
			case YELLOW_BED:
			case REDSTONE:
			case REPEATER:
			case SUGAR_CANE:
			case CAKE:
			case COMPARATOR:
				return usesLegacy();
		}
		return false;
	}

	/**
	 * Check to see if this is a material that can exist as a block
	 *
	 * @return
	 */
	public boolean isBlock() {
		return material != null && material.isBlock();
	}

	/**
	 * Check to see if this is an item that can be consumed to restore hunger
	 *
	 * @return
	 */
	public boolean isEdible() {
		return material != null && material.isEdible();
	}

	/**
	 * Check if the material is a block and can be built on
	 *
	 * @return
	 */
	public boolean isSolid() {
		return material != null && material.isSolid();
	}

	/**
	 * Check if the material is a block and does not block any light
	 *
	 * @return
	 */
	public boolean isTransparent() {
		return material != null && material.isTransparent();
	}

	/**
	 * Check if the material is a block and can catch fire
	 *
	 * @return
	 */
	public boolean isFlammable() {
		return material != null && material.isFlammable();
	}

	/**
	 * Check if the material is a block and can be destroyed by burning
	 *
	 * @return
	 */
	public boolean isBurnable() {
		return material != null && material.isBurnable();
	}

	/**
	 * Checks if this Material can be used as fuel in a Furnace
	 *
	 * @return
	 */
	public boolean isFuel() {
		// this function is not implemented in some older versions, so we need this here..
		switch (this) {
			case ACACIA_BUTTON:
			case ACACIA_DOOR:
			case ACACIA_FENCE:
			case ACACIA_FENCE_GATE:
			case ACACIA_LOG:
			case ACACIA_PLANKS:
			case ACACIA_PRESSURE_PLATE:
			case ACACIA_SAPLING:
			case ACACIA_SIGN:
			case ACACIA_SLAB:
			case ACACIA_STAIRS:
			case ACACIA_TRAPDOOR:
			case ACACIA_WOOD:
			case BAMBOO:
			case BARREL:
			case BIRCH_BUTTON:
			case BIRCH_DOOR:
			case BIRCH_FENCE:
			case BIRCH_FENCE_GATE:
			case BIRCH_LOG:
			case BIRCH_PLANKS:
			case BIRCH_PRESSURE_PLATE:
			case BIRCH_SAPLING:
			case BIRCH_SIGN:
			case BIRCH_SLAB:
			case BIRCH_STAIRS:
			case BIRCH_TRAPDOOR:
			case BIRCH_WOOD:
			case BLACK_BANNER:
			case BLACK_CARPET:
			case BLACK_WOOL:
			case BLUE_BANNER:
			case BLUE_CARPET:
			case BLUE_WOOL:
			case BOOKSHELF:
			case BROWN_BANNER:
			case BROWN_CARPET:
			case BROWN_WOOL:
			case CARTOGRAPHY_TABLE:
			case CHEST:
			case COAL_BLOCK:
			case COMPOSTER:
			case CRAFTING_TABLE:
			case CYAN_BANNER:
			case CYAN_CARPET:
			case CYAN_WOOL:
			case DARK_OAK_BUTTON:
			case DARK_OAK_DOOR:
			case DARK_OAK_FENCE:
			case DARK_OAK_FENCE_GATE:
			case DARK_OAK_LOG:
			case DARK_OAK_PLANKS:
			case DARK_OAK_PRESSURE_PLATE:
			case DARK_OAK_SAPLING:
			case DARK_OAK_SIGN:
			case DARK_OAK_SLAB:
			case DARK_OAK_STAIRS:
			case DARK_OAK_TRAPDOOR:
			case DARK_OAK_WOOD:
			case DAYLIGHT_DETECTOR:
			case DEAD_BUSH:
			case DRIED_KELP_BLOCK:
			case FLETCHING_TABLE:
			case GRAY_BANNER:
			case GRAY_CARPET:
			case GRAY_WOOL:
			case GREEN_BANNER:
			case GREEN_CARPET:
			case GREEN_WOOL:
			case JUKEBOX:
			case JUNGLE_BUTTON:
			case JUNGLE_DOOR:
			case JUNGLE_FENCE:
			case JUNGLE_FENCE_GATE:
			case JUNGLE_LOG:
			case JUNGLE_PLANKS:
			case JUNGLE_PRESSURE_PLATE:
			case JUNGLE_SAPLING:
			case JUNGLE_SIGN:
			case JUNGLE_SLAB:
			case JUNGLE_STAIRS:
			case JUNGLE_TRAPDOOR:
			case JUNGLE_WOOD:
			case LADDER:
			case LECTERN:
			case LIGHT_BLUE_BANNER:
			case LIGHT_BLUE_CARPET:
			case LIGHT_BLUE_WOOL:
			case LIGHT_GRAY_BANNER:
			case LIGHT_GRAY_CARPET:
			case LIGHT_GRAY_WOOL:
			case LIME_BANNER:
			case LIME_CARPET:
			case LIME_WOOL:
			case LOOM:
			case MAGENTA_BANNER:
			case MAGENTA_CARPET:
			case MAGENTA_WOOL:
			case NOTE_BLOCK:
			case OAK_BUTTON:
			case OAK_DOOR:
			case OAK_FENCE:
			case OAK_FENCE_GATE:
			case OAK_LOG:
			case OAK_PLANKS:
			case OAK_PRESSURE_PLATE:
			case OAK_SAPLING:
			case OAK_SIGN:
			case OAK_SLAB:
			case OAK_STAIRS:
			case OAK_TRAPDOOR:
			case OAK_WOOD:
			case ORANGE_BANNER:
			case ORANGE_CARPET:
			case ORANGE_WOOL:
			case PINK_BANNER:
			case PINK_CARPET:
			case PINK_WOOL:
			case PURPLE_BANNER:
			case PURPLE_CARPET:
			case PURPLE_WOOL:
			case RED_BANNER:
			case RED_CARPET:
			case RED_WOOL:
			case SCAFFOLDING:
			case SMITHING_TABLE:
			case SPRUCE_BUTTON:
			case SPRUCE_DOOR:
			case SPRUCE_FENCE:
			case SPRUCE_FENCE_GATE:
			case SPRUCE_LOG:
			case SPRUCE_PLANKS:
			case SPRUCE_PRESSURE_PLATE:
			case SPRUCE_SAPLING:
			case SPRUCE_SIGN:
			case SPRUCE_SLAB:
			case SPRUCE_STAIRS:
			case SPRUCE_TRAPDOOR:
			case SPRUCE_WOOD:
			case STRIPPED_ACACIA_LOG:
			case STRIPPED_ACACIA_WOOD:
			case STRIPPED_BIRCH_LOG:
			case STRIPPED_BIRCH_WOOD:
			case STRIPPED_DARK_OAK_LOG:
			case STRIPPED_DARK_OAK_WOOD:
			case STRIPPED_JUNGLE_LOG:
			case STRIPPED_JUNGLE_WOOD:
			case STRIPPED_OAK_LOG:
			case STRIPPED_OAK_WOOD:
			case STRIPPED_SPRUCE_LOG:
			case STRIPPED_SPRUCE_WOOD:
			case TRAPPED_CHEST:
			case WHITE_BANNER:
			case WHITE_CARPET:
			case WHITE_WOOL:
			case YELLOW_BANNER:
			case YELLOW_CARPET:
			case YELLOW_WOOL:
			case ACACIA_BOAT:
			case BIRCH_BOAT:
			case BLAZE_ROD:
			case BOW:
			case BOWL:
			case CHARCOAL:
			case COAL:
			case CROSSBOW:
			case DARK_OAK_BOAT:
			case FISHING_ROD:
			case JUNGLE_BOAT:
			case LAVA_BUCKET:
			case OAK_BOAT:
			case SPRUCE_BOAT:
			case STICK:
			case WOODEN_AXE:
			case WOODEN_HOE:
			case WOODEN_PICKAXE:
			case WOODEN_SHOVEL:
			case WOODEN_SWORD:
				return true;
		}
		return false;
	}

	/**
	 * Check if the material is a block and completely blocks vision
	 *
	 * @return
	 */
	public boolean isOccluding() {
		return material != null && material.isOccluding();
	}

	/**
	 * @return True if this material is affected by gravity.
	 */
	public boolean hasGravity() {
		return material != null && material.hasGravity();
	}

	/**
	 * Checks if this Material is an obtainable item.
	 *
	 * @return
	 */
	public boolean isItem() {
		// this function is not implemented in some older versions, so we need this here..
		switch (this) {
			case CAVE_AIR:
			case VOID_AIR:
			case ACACIA_WALL_SIGN:
			case ATTACHED_MELON_STEM:
			case ATTACHED_PUMPKIN_STEM:
			case BAMBOO_SAPLING:
			case BEETROOTS:
			case BIRCH_WALL_SIGN:
			case BLACK_WALL_BANNER:
			case BLUE_WALL_BANNER:
			case BRAIN_CORAL_WALL_FAN:
			case BROWN_WALL_BANNER:
			case BUBBLE_COLUMN:
			case BUBBLE_CORAL_WALL_FAN:
			case CARROTS:
			case COCOA:
			case CREEPER_WALL_HEAD:
			case CYAN_WALL_BANNER:
			case DARK_OAK_WALL_SIGN:
			case DEAD_BRAIN_CORAL_WALL_FAN:
			case DEAD_BUBBLE_CORAL_WALL_FAN:
			case DEAD_FIRE_CORAL_WALL_FAN:
			case DEAD_HORN_CORAL_WALL_FAN:
			case DEAD_TUBE_CORAL_WALL_FAN:
			case DRAGON_WALL_HEAD:
			case END_GATEWAY:
			case END_PORTAL:
			case FIRE:
			case FIRE_CORAL_WALL_FAN:
			case FROSTED_ICE:
			case GRAY_WALL_BANNER:
			case GREEN_WALL_BANNER:
			case HORN_CORAL_WALL_FAN:
			case JUNGLE_WALL_SIGN:
			case KELP_PLANT:
			case LAVA:
			case LIGHT_BLUE_WALL_BANNER:
			case LIGHT_GRAY_WALL_BANNER:
			case LIME_WALL_BANNER:
			case MAGENTA_WALL_BANNER:
			case MELON_STEM:
			case MOVING_PISTON:
			case NETHER_PORTAL:
			case OAK_WALL_SIGN:
			case ORANGE_WALL_BANNER:
			case PINK_WALL_BANNER:
			case PISTON_HEAD:
			case PLAYER_WALL_HEAD:
			case POTATOES:
			case POTTED_ACACIA_SAPLING:
			case POTTED_ALLIUM:
			case POTTED_AZURE_BLUET:
			case POTTED_BAMBOO:
			case POTTED_BIRCH_SAPLING:
			case POTTED_BLUE_ORCHID:
			case POTTED_BROWN_MUSHROOM:
			case POTTED_CACTUS:
			case POTTED_CORNFLOWER:
			case POTTED_DANDELION:
			case POTTED_DARK_OAK_SAPLING:
			case POTTED_DEAD_BUSH:
			case POTTED_FERN:
			case POTTED_JUNGLE_SAPLING:
			case POTTED_LILY_OF_THE_VALLEY:
			case POTTED_OAK_SAPLING:
			case POTTED_ORANGE_TULIP:
			case POTTED_OXEYE_DAISY:
			case POTTED_PINK_TULIP:
			case POTTED_POPPY:
			case POTTED_RED_MUSHROOM:
			case POTTED_RED_TULIP:
			case POTTED_SPRUCE_SAPLING:
			case POTTED_WHITE_TULIP:
			case POTTED_WITHER_ROSE:
			case PUMPKIN_STEM:
			case PURPLE_WALL_BANNER:
			case REDSTONE_WALL_TORCH:
			case REDSTONE_WIRE:
			case RED_WALL_BANNER:
			case SKELETON_WALL_SKULL:
			case SPRUCE_WALL_SIGN:
			case SWEET_BERRY_BUSH:
			case TALL_SEAGRASS:
			case TRIPWIRE:
			case TUBE_CORAL_WALL_FAN:
			case WALL_TORCH:
			case WATER:
			case WHITE_WALL_BANNER:
			case WITHER_SKELETON_WALL_SKULL:
			case YELLOW_WALL_BANNER:
			case ZOMBIE_WALL_HEAD:
				return false;
		}
		return true;
	}

	/**
	 * Checks if this Material can be interacted with. <br />
	 * This method will return true if there is at least one state in which
	 * additional interact handling is performed for the material.
	 *
	 * @return
	 */
	public boolean isInteractable() {
		// this function is not implemented in some older versions, so we need this here..
		switch (this) {
			case ACACIA_BUTTON:
			case ACACIA_DOOR:
			case ACACIA_FENCE:
			case ACACIA_FENCE_GATE:
			case ACACIA_SIGN:
			case ACACIA_STAIRS:
			case ACACIA_TRAPDOOR:
			case ACACIA_WALL_SIGN:
			case ANDESITE_STAIRS:
			case ANVIL:
			case BARREL:
			case BEACON:
			case BELL:
			case BIRCH_BUTTON:
			case BIRCH_DOOR:
			case BIRCH_FENCE:
			case BIRCH_FENCE_GATE:
			case BIRCH_SIGN:
			case BIRCH_STAIRS:
			case BIRCH_TRAPDOOR:
			case BIRCH_WALL_SIGN:
			case BLACK_BED:
			case BLACK_SHULKER_BOX:
			case BLAST_FURNACE:
			case BLUE_BED:
			case BLUE_SHULKER_BOX:
			case BREWING_STAND:
			case BRICK_STAIRS:
			case BROWN_BED:
			case BROWN_SHULKER_BOX:
			case CAKE:
			case CAMPFIRE:
			case CARTOGRAPHY_TABLE:
			case CAULDRON:
			case CHAIN_COMMAND_BLOCK:
			case CHEST:
			case CHIPPED_ANVIL:
			case COBBLESTONE_STAIRS:
			case COMMAND_BLOCK:
			case COMPARATOR:
			case COMPOSTER:
			case CRAFTING_TABLE:
			case CYAN_BED:
			case CYAN_SHULKER_BOX:
			case DAMAGED_ANVIL:
			case DARK_OAK_BUTTON:
			case DARK_OAK_DOOR:
			case DARK_OAK_FENCE:
			case DARK_OAK_FENCE_GATE:
			case DARK_OAK_SIGN:
			case DARK_OAK_STAIRS:
			case DARK_OAK_TRAPDOOR:
			case DARK_OAK_WALL_SIGN:
			case DARK_PRISMARINE_STAIRS:
			case DAYLIGHT_DETECTOR:
			case DIORITE_STAIRS:
			case DISPENSER:
			case DRAGON_EGG:
			case DROPPER:
			case ENCHANTING_TABLE:
			case ENDER_CHEST:
			case END_STONE_BRICK_STAIRS:
			case FLETCHING_TABLE:
			case FLOWER_POT:
			case FURNACE:
			case GRANITE_STAIRS:
			case GRAY_BED:
			case GRAY_SHULKER_BOX:
			case GREEN_BED:
			case GREEN_SHULKER_BOX:
			case GRINDSTONE:
			case HOPPER:
			case IRON_DOOR:
			case IRON_TRAPDOOR:
			case JIGSAW:
			case JUKEBOX:
			case JUNGLE_BUTTON:
			case JUNGLE_DOOR:
			case JUNGLE_FENCE:
			case JUNGLE_FENCE_GATE:
			case JUNGLE_SIGN:
			case JUNGLE_STAIRS:
			case JUNGLE_TRAPDOOR:
			case JUNGLE_WALL_SIGN:
			case LECTERN:
			case LEVER:
			case LIGHT_BLUE_BED:
			case LIGHT_BLUE_SHULKER_BOX:
			case LIGHT_GRAY_BED:
			case LIGHT_GRAY_SHULKER_BOX:
			case LIME_BED:
			case LIME_SHULKER_BOX:
			case LOOM:
			case MAGENTA_BED:
			case MAGENTA_SHULKER_BOX:
			case MOSSY_COBBLESTONE_STAIRS:
			case MOSSY_STONE_BRICK_STAIRS:
			case MOVING_PISTON:
			case NETHER_BRICK_FENCE:
			case NETHER_BRICK_STAIRS:
			case NOTE_BLOCK:
			case OAK_BUTTON:
			case OAK_DOOR:
			case OAK_FENCE:
			case OAK_FENCE_GATE:
			case OAK_SIGN:
			case OAK_STAIRS:
			case OAK_TRAPDOOR:
			case OAK_WALL_SIGN:
			case ORANGE_BED:
			case ORANGE_SHULKER_BOX:
			case PINK_BED:
			case PINK_SHULKER_BOX:
			case POLISHED_ANDESITE_STAIRS:
			case POLISHED_DIORITE_STAIRS:
			case POLISHED_GRANITE_STAIRS:
			case POTTED_ACACIA_SAPLING:
			case POTTED_ALLIUM:
			case POTTED_AZURE_BLUET:
			case POTTED_BAMBOO:
			case POTTED_BIRCH_SAPLING:
			case POTTED_BLUE_ORCHID:
			case POTTED_BROWN_MUSHROOM:
			case POTTED_CACTUS:
			case POTTED_CORNFLOWER:
			case POTTED_DANDELION:
			case POTTED_DARK_OAK_SAPLING:
			case POTTED_DEAD_BUSH:
			case POTTED_FERN:
			case POTTED_JUNGLE_SAPLING:
			case POTTED_LILY_OF_THE_VALLEY:
			case POTTED_OAK_SAPLING:
			case POTTED_ORANGE_TULIP:
			case POTTED_OXEYE_DAISY:
			case POTTED_PINK_TULIP:
			case POTTED_POPPY:
			case POTTED_RED_MUSHROOM:
			case POTTED_RED_TULIP:
			case POTTED_SPRUCE_SAPLING:
			case POTTED_WHITE_TULIP:
			case POTTED_WITHER_ROSE:
			case PRISMARINE_BRICK_STAIRS:
			case PRISMARINE_STAIRS:
			case PUMPKIN:
			case PURPLE_BED:
			case PURPLE_SHULKER_BOX:
			case PURPUR_STAIRS:
			case QUARTZ_STAIRS:
			case REDSTONE_ORE:
			case RED_BED:
			case RED_NETHER_BRICK_STAIRS:
			case RED_SANDSTONE_STAIRS:
			case RED_SHULKER_BOX:
			case REPEATER:
			case REPEATING_COMMAND_BLOCK:
			case SANDSTONE_STAIRS:
			case SHULKER_BOX:
			case SMITHING_TABLE:
			case SMOKER:
			case SMOOTH_QUARTZ_STAIRS:
			case SMOOTH_RED_SANDSTONE_STAIRS:
			case SMOOTH_SANDSTONE_STAIRS:
			case SPRUCE_BUTTON:
			case SPRUCE_DOOR:
			case SPRUCE_FENCE:
			case SPRUCE_FENCE_GATE:
			case SPRUCE_SIGN:
			case SPRUCE_STAIRS:
			case SPRUCE_TRAPDOOR:
			case SPRUCE_WALL_SIGN:
			case STONECUTTER:
			case STONE_BRICK_STAIRS:
			case STONE_BUTTON:
			case STONE_STAIRS:
			case STRUCTURE_BLOCK:
			case SWEET_BERRY_BUSH:
			case TNT:
			case TRAPPED_CHEST:
			case WHITE_BED:
			case WHITE_SHULKER_BOX:
			case YELLOW_BED:
			case YELLOW_SHULKER_BOX: {
				return true;
			}
		}
		return false;
	}

    /**
     * @return true if this material is valid as an item
     */
    public boolean isValidItem() {
        switch(this) {
            case ACACIA_WALL_SIGN:
            case AIR:
            case ATTACHED_MELON_STEM:
            case ATTACHED_PUMPKIN_STEM:
            case BAMBOO_SAPLING:
            case BEETROOTS:
            case BIRCH_WALL_SIGN:
            case BLACK_WALL_BANNER:
            case BLUE_WALL_BANNER:
            case BRAIN_CORAL_WALL_FAN:
            case BROWN_WALL_BANNER:
            case BUBBLE_COLUMN:
            case BUBBLE_CORAL_WALL_FAN:
            case CARROTS:
            case CAVE_AIR:
            case COCOA:
            case CREEPER_WALL_HEAD:
            case CYAN_WALL_BANNER:
            case DARK_OAK_WALL_SIGN:
            case DEAD_BRAIN_CORAL_WALL_FAN:
            case DEAD_BUBBLE_CORAL_WALL_FAN:
            case DEAD_FIRE_CORAL_WALL_FAN:
            case DEAD_HORN_CORAL_WALL_FAN:
            case DEAD_TUBE_CORAL_WALL_FAN:
            case DRAGON_WALL_HEAD:
            case END_GATEWAY:
            case END_PORTAL:
            case FIRE: // used to be able to in older versions
            case FIRE_CORAL_WALL_FAN:
            case FROSTED_ICE:
            case GRAY_WALL_BANNER:
            case GREEN_WALL_BANNER:
            case HORN_CORAL_WALL_FAN:
            case JUNGLE_WALL_SIGN:
            case KELP_PLANT:
            case LAVA:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_WALL_BANNER:
            case MELON_STEM:
            case MOVING_PISTON:
            case NETHER_PORTAL:
            case OAK_WALL_SIGN:
            case ORANGE_WALL_BANNER:
            case PINK_WALL_BANNER:
            case PISTON_HEAD:
            case PLAYER_WALL_HEAD:
            case POTATOES:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BAMBOO:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_CORNFLOWER:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_LILY_OF_THE_VALLEY:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case POTTED_WITHER_ROSE:
            case PUMPKIN_STEM:
            case PURPLE_WALL_BANNER:
            case REDSTONE_WALL_TORCH:
            case REDSTONE_WIRE:
            case RED_WALL_BANNER:
            case SKELETON_WALL_SKULL:
            case SPRUCE_WALL_SIGN:
            case SWEET_BERRY_BUSH:
            case TALL_SEAGRASS:
            case TRIPWIRE:
            case TUBE_CORAL_WALL_FAN:
            case VOID_AIR:
            case WALL_TORCH:
            case WATER:
            case WHITE_WALL_BANNER:
            case WITHER_SKELETON_WALL_SKULL:
                return false;
        }
        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_12)) {
            switch (this) {
                case ACACIA_WOOD:
                case BIRCH_WOOD:
                case BREWING_STAND:
                case CAULDRON:
                case DARK_OAK_WOOD:
                case JUNGLE_WOOD:
                case OAK_WOOD:
                case SPRUCE_WOOD:
                case STRIPPED_ACACIA_WOOD:
                case STRIPPED_BIRCH_WOOD:
                case STRIPPED_DARK_OAK_WOOD:
                case STRIPPED_JUNGLE_WOOD:
                case STRIPPED_OAK_WOOD:
                case STRIPPED_SPRUCE_WOOD:
                    return false;
            }
        }
        return true;
    }

    /**
     * @return true if this material is a food that can be cooked and is in its cooked state
     */
    public boolean isCooked() {
        switch(this) { 
            case BAKED_POTATO: 
            case COOKED_BEEF: 
            case COOKED_CHICKEN: 
            case COOKED_COD: 
            case COOKED_MUTTON: 
            case COOKED_PORKCHOP: 
            case COOKED_RABBIT: 
            case COOKED_SALMON:  
            case DRIED_KELP: 
                return true;
        }
        return false;
    }
    
    /**
     * @return true if this material is a food that can be cooked and is in its raw state
     */
    public boolean isRaw() {
        switch(this) {
            case BEEF: 
            case CHICKEN: 
            case COD: 
            case KELP: // not edible, but is the raw state of DRIED_KELP
            case MUTTON: 
            case PORKCHOP: 
            case POTATO: 
            case RABBIT: 
            case SALMON: 
                return true;
        }
        return false;
    }

    public static LegacyMaterials getGlassPaneColor(int color) {
        switch (color) {
            case 0:
                return WHITE_STAINED_GLASS_PANE;
            case 1:
                return ORANGE_STAINED_GLASS_PANE;
            case 2:
                return MAGENTA_STAINED_GLASS_PANE;
            case 3:
                return LIGHT_BLUE_STAINED_GLASS_PANE;
            case 4:
                return YELLOW_STAINED_GLASS_PANE;
            case 5:
                return LIME_STAINED_GLASS_PANE;
            case 6:
                return PINK_STAINED_GLASS_PANE;
            case 7:
                return GRAY_STAINED_GLASS_PANE;
            case 8:
                return LIGHT_GRAY_STAINED_GLASS_PANE;
            case 9:
                return CYAN_STAINED_GLASS_PANE;
            case 10:
                return PURPLE_STAINED_GLASS_PANE;
            case 11:
                return BLUE_STAINED_GLASS_PANE;
            case 12:
                return BROWN_STAINED_GLASS_PANE;
            case 13:
                return GREEN_STAINED_GLASS_PANE;
            case 14:
                return RED_STAINED_GLASS_PANE;
            case 15:
                return BLACK_STAINED_GLASS_PANE;
        }
        return WHITE_STAINED_GLASS_PANE;
    }

    public static LegacyMaterials getGlassColor(int color) {
        switch (color) {
            case 0:
                return WHITE_STAINED_GLASS;
            case 1:
                return ORANGE_STAINED_GLASS;
            case 2:
                return MAGENTA_STAINED_GLASS;
            case 3:
                return LIGHT_BLUE_STAINED_GLASS;
            case 4:
                return YELLOW_STAINED_GLASS;
            case 5:
                return LIME_STAINED_GLASS;
            case 6:
                return PINK_STAINED_GLASS;
            case 7:
                return GRAY_STAINED_GLASS;
            case 8:
                return LIGHT_GRAY_STAINED_GLASS;
            case 9:
                return CYAN_STAINED_GLASS;
            case 10:
                return PURPLE_STAINED_GLASS;
            case 11:
                return BLUE_STAINED_GLASS;
            case 12:
                return BROWN_STAINED_GLASS;
            case 13:
                return GREEN_STAINED_GLASS;
            case 14:
                return RED_STAINED_GLASS;
            case 15:
                return BLACK_STAINED_GLASS;
        }
        return WHITE_STAINED_GLASS;
    }

    public static LegacyMaterials getDyeColor(int color) {
        switch (color) {
            case 0:
                return BLACK_DYE;
            case 1:
                return RED_DYE;
            case 2:
                return GREEN_DYE;
            case 3:
                return BROWN_DYE;
            case 4:
                return BLUE_DYE;
            case 5:
                return PURPLE_DYE;
            case 6:
                return CYAN_DYE;
            case 7:
                return LIGHT_GRAY_DYE;
            case 8:
                return GRAY_DYE;
            case 9:
                return PINK_DYE;
            case 10:
                return LIME_DYE;
            case 11:
                return YELLOW_DYE;
            case 12:
                return LIGHT_BLUE_DYE;
            case 13:
                return MAGENTA_DYE;
            case 14:
                return ORANGE_DYE;
            case 15:
                return WHITE_DYE;
        }
        return WHITE_DYE;
    }
}
