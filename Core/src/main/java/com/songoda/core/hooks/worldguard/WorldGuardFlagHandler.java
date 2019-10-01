package com.songoda.core.hooks.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * Hooks for adding a custom WorldGuard flag
 *
 * Note: Hooks must be added before WG loads!
 */
public class WorldGuardFlagHandler {

    static Boolean wgPlugin = null;
    static Object worldGuardPlugin;
    static boolean wg_v7 = false;
    static boolean legacy_v60 = false;
    static boolean legacy_v62 = false;
    static boolean legacy_v5 = false;
    static boolean hooksInstalled = false;
    static Map<String, Object> flags = new HashMap();

    /**
     * Attempt to register a worldGuard flag (ALLOW/DENY) <br />
     * Note: This must be called before WorldGuard loads, or it will fail.
     *
     * @param flag name of the flag to set
     * @param state default value of the flag
     */
    public static void addHook(String flag, boolean state) {
        if (wgPlugin == null && (wgPlugin = (worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard")) != null)) {
            // a number of flags were introduced in 7.x that aren't in 5 or 6
            try {
                // if this class exists, we're on 7.x
                Class.forName("com.sk89q.worldguard.protection.flags.WeatherTypeFlag");
                wg_v7 = true;
            } catch (ClassNotFoundException ex) {
                try {
                    // if this class exists, we're on 6.2
                    Class.forName("com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry");
                    legacy_v62 = true;
                } catch (ClassNotFoundException ex2) {
                    try {
                        // if this class exists, we're on 6.0
                        Class.forName("com.sk89q.worldguard.protection.flags.BuildFlag");
                        legacy_v60 = true;
                    } catch (ClassNotFoundException ex3) {
                        try {
                            // if this class exists, we're on 5.x
                            Class.forName("com.sk89q.worldguard.protection.flags.DefaultFlag");
                            legacy_v5 = true;
                        } catch (ClassNotFoundException ex4) {
                            // ¯\_(ツ)_/¯
                            wgPlugin = false;
                        }
                    }
                }
            }
        }
        if (!wgPlugin) {
            return;
        }

        if (legacy_v62 || legacy_v60 || legacy_v5) {
            addLegacyHook(flag, state);
            return;
        }

        StateFlag addFlag = new StateFlag(flag, state);
        try {
            WorldGuard.getInstance().getFlagRegistry().register(addFlag);
            flags.put(flag, addFlag);
        } catch (Exception ex) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add flag {0} to WorldGuard", addFlag.getName());
            Flag wgFlag = (StateFlag) WorldGuard.getInstance().getFlagRegistry().get(addFlag.getName());
            if (wgFlag == null) {
                wgPlugin = false;
                Bukkit.getServer().getLogger().log(Level.WARNING, "Could not hook WorldGuard");
            } else {
                flags.put(flag, wgFlag);
                Bukkit.getServer().getLogger().log(Level.WARNING, "Loaded existing {1} {0}", new Object[]{wgFlag.getName(), wgFlag.getClass().getSimpleName()});
            }
        }
    }

    // reflection to add hooks
    private static void addLegacyHook(String flag, boolean state) {
        try {
            // 6.0 has the same classpath for StateFlag as the current version does
            // does this flag exist already?
            Class defaultFlagClazz = Class.forName("com.sk89q.worldguard.protection.flags.DefaultFlag");
            Field flagField = defaultFlagClazz.getField("flagsList");
            Flag<?>[] flagsOld = (Flag<?>[]) flagField.get(null);
            Flag wgFlag = Stream.of(flagsOld)
                    .filter(f -> ((Flag<?>) f).getName().equalsIgnoreCase(flag))
                    .findFirst().orElse(null);
            if (wgFlag != null) {
                // we already have one
                flags.put(flag, wgFlag);
                Bukkit.getServer().getLogger().log(Level.WARNING, "Loaded existing {1} {0}", new Object[]{wgFlag.getName(), wgFlag.getClass().getSimpleName()});
                return;
            }

            // if not, we need to add one
            wgFlag = new StateFlag(flag, state);

            // we need to sneak our flag into the array
            // make a copy first
            Flag<?>[] flagsNew = new Flag<?>[flagsOld.length + 1];
            System.arraycopy(flagsOld, 0, flagsNew, 0, flagsOld.length);

            // add ours
            flagsNew[flagsNew.length - 1] = wgFlag;

            // and put the new list into place
            setStaticField(flagField, flagsNew);

            if (legacy_v62) { // SimpleFlagRegistry is NOT in 6.0
                // register this flag in the registry
                Object flagRegistry = getPrivateField(worldGuardPlugin.getClass(), worldGuardPlugin, "flagRegistry");
                Class simpleFlagRegistryClazz = Class.forName("com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry");
                Method registerSimpleFlagRegistry = simpleFlagRegistryClazz.getDeclaredMethod("register", Flag.class);
                registerSimpleFlagRegistry.invoke(flagRegistry, wgFlag);
            }

            // all good!
            flags.put(flag, wgFlag);
        } catch (Exception ex) {
            //Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to set legacy WorldGuard Flags", ex);
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add flag {0} to WorldGuard " + (legacy_v62 ? "6.2" : (legacy_v60 ? "6.0" : "5")), flag);
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not hook WorldGuard");
            wgPlugin = false;
        }
    }

    private static Object getPrivateField(Class<?> c, Object handle, String fieldName) throws Exception {
        Field field = c.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(handle);
    }

    private static void setStaticField(Field field, Object value) throws Exception {
        field.setAccessible(true);
        Field modifier = Field.class.getDeclaredField("modifiers");
        modifier.setAccessible(true);
        modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, value);
    }

    public static boolean isEnabled() {
        return wgPlugin != null && wgPlugin;
    }

    /**
     * Checks this location to see what this flag is set to
     *
     * @param l location to check
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Location l, String flag) {
        if (wgPlugin == null || !wgPlugin) {
            return null;
        }
        Object flagObj = flags.get(flag);
        // There's a different way to get this in the old version
        if (legacy_v62 || legacy_v60 || legacy_v5) {
            return flagObj == null ? null : getBooleanFlagLegacy(l, flagObj);
        }

        // for convinience, we can load a flag if we don't know it
        if (flagObj == null) {
            flags.put(flag, flagObj = WorldGuard.getInstance().getFlagRegistry().get(flag));
        }

        // so, what's up?
        if (flagObj instanceof StateFlag) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(l);
            return query.testState(loc, (RegionAssociable) null, (StateFlag) flagObj);
        }
        return null;
    }

    /**
     * Query all regions that are in or intersect this chunk
     *
     * @param c chunk to check for regions in
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Chunk c, String flag) {
        if (wgPlugin == null || !wgPlugin) {
            return null;
        }
        Object flagObj = flags.get(flag);
        // There's a different way to get this in the old version
        if (legacy_v62 || legacy_v60 || legacy_v5) {
            return flagObj == null ? null : getBooleanFlagLegacy(c, flagObj);
        }

        // for convinience, we can load a flag if we don't know it
        if (flagObj == null) {
            flags.put(flag, flagObj = WorldGuard.getInstance().getFlagRegistry().get(flag));
        }

        // so, what's up?
        if (flagObj instanceof StateFlag) {
            RegionManager worldManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(c.getWorld()));
            if (worldManager == null) {
                return null;
            }
            ProtectedCuboidRegion chunkRegion = new ProtectedCuboidRegion("__TEST__",
                    BlockVector3.at(c.getX() << 4, c.getWorld().getMaxHeight(), c.getZ() << 4),
                    BlockVector3.at((c.getX() << 4) + 15, 0, (c.getZ() << 4) + 15));
            ApplicableRegionSet set = worldManager.getApplicableRegions(chunkRegion);
            State result = set.queryState((RegionAssociable) null, (StateFlag) flagObj);
            if (result == null && set.size() == 0) {
                return null;
            }
            return result == State.ALLOW;
        }
        return null;
    }

    static Method legacy_getRegionManager = null;
    static Method legacy_getApplicableRegions_Region = null;
    static Method legacy_getApplicableRegions_Location = null;
    static Method legacy5_applicableRegionSet_getFlag = null;
    static Constructor legacy_newProtectedCuboidRegion;
    static Class legacy_blockVectorClazz;
    static Constructor legacy_newblockVector;
    static Class legacy_VectorClazz;
    static Constructor legacy_newVectorClazz;
    static Method legacy_getApplicableRegions_Vector = null;

    private static Boolean getBooleanFlagLegacy(Location l, Object flag) {
        try {
            // cache reflection methods
            if (legacy_getRegionManager == null) {
                legacy_getRegionManager = worldGuardPlugin.getClass()
                        .getDeclaredMethod("getRegionManager", org.bukkit.World.class);
                legacy_getApplicableRegions_Region = RegionManager.class.getDeclaredMethod("getApplicableRegions",
                        Class.forName("com.sk89q.worldguard.protection.regions.ProtectedRegion"));
                legacy_getApplicableRegions_Location = RegionManager.class.getDeclaredMethod("getApplicableRegions",
                        Location.class);
                legacy_blockVectorClazz = Class.forName("com.sk89q.worldedit.BlockVector");
                legacy_newblockVector = legacy_blockVectorClazz.getConstructor(int.class, int.class, int.class);
                legacy_newProtectedCuboidRegion = Class.forName("com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion")
                        .getConstructor(String.class, legacy_blockVectorClazz, legacy_blockVectorClazz);
                legacy_VectorClazz = Class.forName("com.sk89q.worldedit.Vector");
                legacy_newVectorClazz = legacy_VectorClazz.getConstructor(int.class, int.class, int.class);
                legacy_getApplicableRegions_Vector = RegionManager.class.getDeclaredMethod("getApplicableRegions", legacy_VectorClazz);
            }

            // grab the applicable manager for this world
            Object worldManager = (RegionManager) legacy_getRegionManager.invoke(worldGuardPlugin, l.getWorld());
            if (worldManager == null) {
                return null;
            }

            // create a vector object
            Object vec = legacy_newVectorClazz.newInstance(l.getBlockX(), l.getBlockY(), l.getBlockZ());
            // now look for any intersecting regions
            Object set = legacy_getApplicableRegions_Vector.invoke(worldManager, legacy_VectorClazz.cast(vec));

            // so what's the verdict?
            State result;
            if (legacy_v62 || legacy_v60) {
                result = (State) ((ApplicableRegionSet) set).queryState((RegionAssociable) null, (StateFlag) flag);
            } else {
                // v5 has a different class signature for ApplicableRegionSet
                // also doesn't have a "queryState" function
                //getFlag(T flag)
                if (legacy5_applicableRegionSet_getFlag == null) {
                    legacy5_applicableRegionSet_getFlag = Class.forName("com.sk89q.worldguard.protection.ApplicableRegionSet").getMethod("getFlag", Object.class);
                }
                result = (State) legacy5_applicableRegionSet_getFlag.invoke(set, flag);
            }
            if (result == null && set != null && ((Iterable) set).iterator().hasNext()) {
                return null;
            }
            return result == State.ALLOW;

        } catch (Exception ex) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not grab flags from WorldGuard", ex);
        }
        return null;
    }

    private static Boolean getBooleanFlagLegacy(Chunk c, Object flag) {
        // ApplicableRegionSet and RegionManager have the same classpath as the current version
        // ProtectedCuboidRegion uses a different constructor, though
        try {
            // cache reflection methods
            if (legacy_getRegionManager == null) {
                legacy_getRegionManager = worldGuardPlugin.getClass()
                        .getDeclaredMethod("getRegionManager", org.bukkit.World.class);
                legacy_getApplicableRegions_Region = RegionManager.class.getDeclaredMethod("getApplicableRegions",
                        Class.forName("com.sk89q.worldguard.protection.regions.ProtectedRegion"));
                legacy_getApplicableRegions_Location = RegionManager.class.getDeclaredMethod("getApplicableRegions",
                        Location.class);
                legacy_blockVectorClazz = Class.forName("com.sk89q.worldedit.BlockVector");
                legacy_newblockVector = legacy_blockVectorClazz.getConstructor(int.class, int.class, int.class);
                legacy_newProtectedCuboidRegion = Class.forName("com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion")
                        .getConstructor(String.class, legacy_blockVectorClazz, legacy_blockVectorClazz);
                legacy_VectorClazz = Class.forName("com.sk89q.worldedit.Vector");
                legacy_newVectorClazz = legacy_VectorClazz.getConstructor(int.class, int.class, int.class);
                legacy_getApplicableRegions_Vector = RegionManager.class.getDeclaredMethod("getApplicableRegions", legacy_VectorClazz);
            }

            // grab the applicable manager for this world
            Object worldManager = (RegionManager) legacy_getRegionManager.invoke(worldGuardPlugin, c.getWorld());
            if (worldManager == null) {
                return null;
            }

            // Create a legacy ProtectedCuboidRegion
            Object chunkRegion = legacy_newProtectedCuboidRegion.newInstance("__TEST__",
                    legacy_newblockVector.newInstance(c.getX() << 4, c.getWorld().getMaxHeight(), c.getZ() << 4),
                    legacy_newblockVector.newInstance((c.getX() << 4) + 15, 0, (c.getZ() << 4) + 15));

            // now look for any intersecting regions
            Object set = legacy_getApplicableRegions_Region.invoke(worldManager, chunkRegion);

            // so what's the verdict?
            State result;
            if (legacy_v62 || legacy_v60) {
                result = (State) ((ApplicableRegionSet) set).queryState((RegionAssociable) null, (StateFlag) flag);
            } else {
                // v5 has a different class signature for ApplicableRegionSet
                // also doesn't have a "queryState" function
                //getFlag(T flag)
                if (legacy5_applicableRegionSet_getFlag == null) {
                    legacy5_applicableRegionSet_getFlag = Class.forName("com.sk89q.worldguard.protection.ApplicableRegionSet").getMethod("getFlag", Flag.class);
                }
                result = (State) legacy5_applicableRegionSet_getFlag.invoke(set, flag);
            }
            if (result == null && set != null && ((Iterable) set).iterator().hasNext()) {
                return null;
            }
            return result == State.ALLOW;

        } catch (Exception ex) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not grab flags from WorldGuard", ex);
        }
        return null;
    }
}
