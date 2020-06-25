package com.songoda.core.compatibility;

import org.bukkit.block.Biome;

import java.util.*;

/**
 * Biomes that are compatible with server versions 1.7+
 *
 * @author Brianna
 * @since 2020-03-27
 */
public enum CompatibleBiome {

    /* 1.16 */
    SOUL_SAND_VALLEY(ServerVersion.V1_16),
    CRIMSON_FOREST(ServerVersion.V1_16),
    WARPED_FOREST(ServerVersion.V1_16),
    BASALT_DELTAS(ServerVersion.V1_16),

    BADLANDS(ServerVersion.V1_13, v("MESA")),
    BADLANDS_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "MESA_CLEAR_ROCK"), v("MESA_PLATEAU")),
    BAMBOO_JUNGLE(ServerVersion.V1_14),
    BAMBOO_JUNGLE_HILLS(ServerVersion.V1_14),
    BEACH(ServerVersion.V1_13, v(ServerVersion.V1_9, "BEACHES")),
    BIRCH_FOREST(),
    BIRCH_FOREST_HILLS(),
    COLD_OCEAN(ServerVersion.V1_13),
    DARK_FOREST(ServerVersion.V1_13, v("ROOFED_FOREST")),
    DARK_FOREST_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_ROOFED_FOREST")),
    DEEP_COLD_OCEAN(ServerVersion.V1_13),
    DEEP_FROZEN_OCEAN(ServerVersion.V1_13),
    DEEP_LUKEWARM_OCEAN(ServerVersion.V1_13),
    DEEP_OCEAN(),
    DEEP_WARM_OCEAN(ServerVersion.V1_13),
    DESERT(),
    DESERT_HILLS(),
    DESERT_LAKES(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_DESERT")),
    END_BARRENS(ServerVersion.V1_13),
    END_HIGHLANDS(ServerVersion.V1_13),
    END_MIDLANDS(ServerVersion.V1_13),
    ERODED_BADLANDS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_MESA")),
    FLOWER_FOREST(),
    FOREST(),
    FROZEN_OCEAN(),
    FROZEN_RIVER(),
    GIANT_SPRUCE_TAIGA(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_REDWOOD_TAIGA")),
    GIANT_SPRUCE_TAIGA_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_REDWOOD_TAIGA_HILLS")),
    GIANT_TREE_TAIGA(ServerVersion.V1_13, v(ServerVersion.V1_9, "REDWOOD_TAIGA")),
    GIANT_TREE_TAIGA_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "REDWOOD_TAIGA_HILLS")),
    GRAVELLY_MOUNTAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_EXTREME_HILLS ")),
    ICE_SPIKES(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_ICE_FLATS")),
    JUNGLE(),
    JUNGLE_EDGE(),
    JUNGLE_HILLS(),
    LUKEWARM_OCEAN(),
    MODIFIED_BADLANDS_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_MESA_CLEAR_ROCK")),
    MODIFIED_GRAVELLY_MOUNTAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_EXTREME_HILLS_WITH_TREES"), v("EXTREME_HILLS_MOUNTAINS")),
    MODIFIED_JUNGLE(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_JUNGLE"), v("JUNGLE_MOUNTAINS")),
    MODIFIED_JUNGLE_EDGE(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_JUNGLE_EDGE"), v("JUNGLE_EDGE_MOUNTAINS")),
    MODIFIED_WOODED_BADLANDS_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_MESA_ROCK"), v("MESA_PLATEAU_FOREST_MOUNTAINS")),
    MOUNTAINS(ServerVersion.V1_13, v("EXTREME_HILLS")),
    MOUNTAIN_EDGE(ServerVersion.V1_13, v(ServerVersion.V1_9, "SMALLER_EXTREME_HILLS")),
    MUSHROOM_FIELDS(ServerVersion.V1_13, v("MUSHROOM_ISLAND")),
    MUSHROOM_FIELD_SHORE(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUSHROOM_ISLAND_SHORE"), v("MUSHROOM_SHORE")),
    NETHER_WASTES(ServerVersion.V1_16, v(ServerVersion.V1_13, "NETHER"), v("HELL")),
    OCEAN(),
    PLAINS(),
    RIVER(),
    SAVANNA(),
    SAVANNA_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "SAVANNA_ROCK"), v("SAVANNA_PLATEAU")),
    SHATTERED_SAVANNA(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_SAVANNA"), v("SAVANNA_MOUNTAINS")),
    SHATTERED_SAVANNA_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_SAVANNA_ROCK"), v("SAVANNA_PLATEAU_MOUNTAINS")),
    SMALL_END_ISLANDS(ServerVersion.V1_13),
    SNOWY_BEACH(ServerVersion.V1_13, v("COLD_BEACH")),
    SNOWY_MOUNTAINS(ServerVersion.V1_13, v("ICE_MOUNTAINS")),
    SNOWY_TAIGA(ServerVersion.V1_13, v(ServerVersion.V1_9, "TAIGA_COLD")),
    SNOWY_TAIGA_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "TAIGA_COLD_HILLS"), v("COLD_TAIGA_HILLS")),
    SNOWY_TAIGA_MOUNTAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_TAIGA_COLD"), v("COLD_TAIGA_MOUNTAINS")),
    SNOWY_TUNDRA(ServerVersion.V1_13, v(ServerVersion.V1_9, "ICE_FLATS")),
    STONE_SHORE(ServerVersion.V1_13, v("STONE_BEACH")),
    SUNFLOWER_PLAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_PLAINS"), v("SUNFLOWER_PLAINS")),
    SWAMP(ServerVersion.V1_13, v("SWAMPLAND")),
    SWAMP_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_SWAMPLAND"), v("SWAMPLAND_MOUNTAINS")),
    TAIGA(),
    TAIGA_HILLS(),
    TAIGA_MOUNTAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "TAIGA_MOUNTAINS"), v("MUTATED_TAIGA")),
    TALL_BIRCH_FOREST(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_BIRCH_FOREST"), v("BIRCH_FOREST_MOUNTAINS")),
    TALL_BIRCH_HILLS(ServerVersion.V1_13, v(ServerVersion.V1_9, "MUTATED_BIRCH_FOREST_HILLS"), v("MESA_PLATEAU_FOREST_MOUNTAINS")),
    THE_END(ServerVersion.V1_13, v("SKY")),
    THE_VOID(ServerVersion.V1_13, v(ServerVersion.V1_9, "VOID")),
    WARM_OCEAN(ServerVersion.V1_13),
    WOODED_BADLANDS_PLATEAU(ServerVersion.V1_13, v(ServerVersion.V1_9, "MESA_ROCK"), v("MESA_PLATEAU_FOREST")),
    WOODED_HILLS(ServerVersion.V1_13, v("FOREST_HILLS")),
    WOODED_MOUNTAINS(ServerVersion.V1_13, v(ServerVersion.V1_9, "EXTREME_HILLS_WITH_TREES"), v("EXTREME_HILLS_PLUS"));

    private static final Map<String, CompatibleBiome> lookupMap = new HashMap();
    private static final Set<CompatibleBiome> compatibleBiomes = new HashSet<>();

    static {
        for (CompatibleBiome biome : values())
            for (Version version : biome.getVersions())
                lookupMap.put(version.biome, biome);

        for (CompatibleBiome biome : CompatibleBiome.values())
            if (biome.isCompatible())
                compatibleBiomes.add(biome);
    }

    private Deque<Version> versions = new ArrayDeque<>();

    CompatibleBiome() {
        versions.add(v(ServerVersion.UNKNOWN, name()));
    }

    CompatibleBiome(ServerVersion version, Version... versions) {
        this.versions.add(v(version, name()));
        this.versions.addAll(Arrays.asList(versions));
    }

    public boolean isCompatible() {
        Version version = versions.getLast();
        ServerVersion.isServerVersionAtLeast(version.version);
        return true;
    }

    public List<Version> getVersions() {
        return new LinkedList<>(versions);
    }

    public Biome getBiome() {
        for (Version version : versions)
            if (ServerVersion.isServerVersionAtLeast(version.version))
                return Biome.valueOf(version.biome);
        return null;
    }

    public static CompatibleBiome getBiome(Biome biome) {
        return biome == null ? null : lookupMap.get(biome.name());
    }

    public static Set<CompatibleBiome> getCompatibleBiomes() {
        return compatibleBiomes;
    }

    private static Version v(ServerVersion version, String biome) {
        return new Version(version, biome);
    }

    private static Version v(String biome) {
        return new Version(ServerVersion.UNKNOWN, biome);
    }

    private static class Version {

        final ServerVersion version;
        final String biome;

        public Version(ServerVersion version, String biome) {
            this.version = version;
            this.biome = biome;
        }

    }
}
