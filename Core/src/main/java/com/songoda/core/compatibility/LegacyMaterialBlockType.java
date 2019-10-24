package com.songoda.core.compatibility;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;

/**
 * Starting in Minecraft 1.13, separate materials for blocks and items were
 * phased out. This provides a translation for those values.
 *
 * @since 2019-09-12
 * @author jascotty2
 */
public enum LegacyMaterialBlockType {

    ACACIA_DOOR("ACACIA_DOOR", true),
    BED("BED_BLOCK", true),
    BIRCH_DOOR("BIRCH_DOOR", true),
    FURNACE("FURNACE", "BURNING_FURNACE"),
    CAKE("CAKE_BLOCK"),
	CARROTS("CARROT"), // totally makes sense, lol
    CAULDRON("CAULDRON_BLOCK"),
    COMPARATOR("REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR_ON"),
    DARK_OAK_DOOR("DARK_OAK_DOOR", true),
    DAYLIGHT_DETECTOR("DAYLIGHT_DETECTOR", "DAYLIGHT_DETECTOR_INVERTED"),
    /*
     <     DOUBLE_STEP,
     <     DOUBLE_STONE_SLAB2,
     */
    FLOWER_POT("FLOWER_POT"),
    IRON_DOOR("IRON_DOOR_BLOCK", true),
    JUNGLE_DOOR("JUNGLE_DOOR", true),
	LAVA("STATIONARY_LAVA"),
    NETHER_WART("NETHER_WARTS"),
    /*
     <     PURPUR_DOUBLE_SLAB
     */
	POTATOES("POTATO"),
    REDSTONE_LAMP("REDSTONE_LAMP_OFF", "REDSTONE_LAMP_ON"),
    REDSTONE_ORE("REDSTONE_ORE", "GLOWING_REDSTONE_ORE"),
    REDSTONE_TORCH("REDSTONE_TORCH_ON", "REDSTONE_TORCH_OFF"),
    SPRUCE_DOOR("SPRUCE_DOOR"),
    SUGAR_CANE("SUGAR_CANE_BLOCK"),
	WATER("STATIONARY_WATER"),
    WHEAT("CROPS");
    final String blockMaterialName;
    final String alternateBlockMaterialName;
    final Material blockMaterial, alternateBlockMaterial;
    final boolean requiresData; // some blocks require data to render properly (double blocks)
    final static Map<String, LegacyMaterialBlockType> lookupTable = new HashMap();
    final static Map<String, LegacyMaterialBlockType> reverseLookupTable = new HashMap();

    static {
        for (LegacyMaterialBlockType t : values()) {
            lookupTable.put(t.name(), t);
            reverseLookupTable.put(t.blockMaterialName, t);
            if(t.alternateBlockMaterialName != null) {
                reverseLookupTable.put(t.alternateBlockMaterialName, t);
            }
        }
    }

    private LegacyMaterialBlockType(String blockMaterial) {
        this(blockMaterial, null, false);
    }

    private LegacyMaterialBlockType(String blockMaterial, boolean requiresData) {
        this(blockMaterial, null, requiresData);
    }

    private LegacyMaterialBlockType(String blockMaterial, String alternateMaterial) {
        this(blockMaterial, alternateMaterial, false);
    }

    private LegacyMaterialBlockType(String blockMaterial, String alternateMaterial, boolean requiresData) {
        this.blockMaterialName = blockMaterial;
        this.alternateBlockMaterialName = alternateMaterial;
        this.requiresData = requiresData;
        this.blockMaterial = Material.getMaterial(blockMaterialName);
        this.alternateBlockMaterial = Material.getMaterial(alternateBlockMaterialName);
    }

    public String getBlockMaterialName() {
        return blockMaterialName;
    }

    public String getAlternateMaterialName() {
        return alternateBlockMaterialName;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public Material getAlternateBlockMaterial() {
        return alternateBlockMaterial;
    }

    public boolean requiresData() {
        return requiresData;
    }

    public static LegacyMaterialBlockType getMaterial(String lookup) {
        return lookupTable.get(lookup);
    }

    public static LegacyMaterialBlockType getFromLegacy(String lookup) {
        return reverseLookupTable.get(lookup);
    }

}
