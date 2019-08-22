/**
 * Hooks for adding a custom WorldGuard flag
 * 
 * Note: Hooks must be added before WG loads!
 */
package com.songoda.core.library.hooks.hooks;

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

public class WorldGuardFlagHandler {
    
	static Boolean wgPlugin = null;
    static Object worldGuardPlugin;
    static boolean legacy = false;
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
            try {
                // if this class exists, we're on an older version
                Class.forName("com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry");
                legacy = true;
            } catch (ClassNotFoundException ex) {
            }
        }
        if (!wgPlugin) return;

        if (legacy) {
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
				Bukkit.getServer().getLogger().log(Level.WARNING, "Loaded existing {1} {0}", new Object[] {wgFlag.getName(), wgFlag.getClass().getSimpleName()});
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
                    .filter(f -> ((Flag<?>)f).getName().equalsIgnoreCase(flag))
                    .findFirst().orElse(null);
            if (wgFlag != null) {
                // we already have one
                flags.put(flag, wgFlag);
				Bukkit.getServer().getLogger().log(Level.WARNING, "Loaded existing {1} {0}", new Object[] {wgFlag.getName(), wgFlag.getClass().getSimpleName()});
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

			// register this flag in the registry
			Object flagRegistry = getPrivateField(worldGuardPlugin.getClass(), worldGuardPlugin, "flagRegistry");
            Class simpleFlagRegistryClazz = Class.forName("com.sk89q.worldguard.protection.flags.registry.SimpleFlagRegistry");
            Method registerSimpleFlagRegistry = simpleFlagRegistryClazz.getDeclaredMethod("register", Flag.class);
            registerSimpleFlagRegistry.invoke(flagRegistry, wgFlag);

            // all good!
            flags.put(flag, wgFlag);
        } catch (Exception ex) {
            //Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to set legacy WorldGuard Flags", ex);
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add flag {0} to WorldGuard", flag);
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
     * @param l location to check
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Location l, String flag) {
        if (wgPlugin == null || !wgPlugin) return null;
        Object flagObj = flags.get(flag);
        // There's a different way to get this in the old version
        if (legacy)
            return flagObj == null ? null : getBooleanFlagLegacy(l, flagObj);

        // for convinience, we can load a flag if we don't know it
        if (flagObj == null && !legacy)
            flags.put(flag, flagObj = WorldGuard.getInstance().getFlagRegistry().get(flag));

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
     * @param c chunk to check for regions in
     * @param flag ALLOW/DENY flag to check
     * @return flag state, or null if undefined
     */
    public static Boolean getBooleanFlag(Chunk c, String flag) {
        if (wgPlugin == null || !wgPlugin) return null;
        Object flagObj = flags.get(flag);
        // There's a different way to get this in the old version
        if (legacy)
            return flagObj == null ? null : getBooleanFlagLegacy(c, flagObj);

        // for convinience, we can load a flag if we don't know it
        if (flagObj == null)
            flags.put(flag, flagObj = WorldGuard.getInstance().getFlagRegistry().get(flag));

        // so, what's up?
        if (flagObj instanceof StateFlag) {
            RegionManager worldManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(BukkitAdapter.adapt(c.getWorld()));
            if (worldManager == null)
                return null;
            ProtectedCuboidRegion chunkRegion = new ProtectedCuboidRegion("__TEST__",
                BlockVector3.at(c.getX() << 4, c.getWorld().getMaxHeight(), c.getZ() << 4),
                BlockVector3.at((c.getX() << 4) + 15, 0, (c.getZ() << 4) + 15));
            ApplicableRegionSet set = worldManager.getApplicableRegions(chunkRegion);
            State result = set.queryState((RegionAssociable) null, (StateFlag) flagObj);
            if (result == null && set.size() == 0)
                return null;
            return result == State.ALLOW;
        }
        return null;
    }

    static Method legacy_getRegionManager = null;
    static Method legacy_getApplicableRegions_Region = null;
    static Method legacy_getApplicableRegions_Location = null;
    static Constructor legacy_newProtectedCuboidRegion;
    static Class legacy_blockVectorClazz;
    static Constructor legacy_newblockVector;

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
            }

            // grab the applicable manager for this world
            Object worldManager = (RegionManager) legacy_getRegionManager.invoke(worldGuardPlugin, l.getWorld());
            if (worldManager == null)
                return null;

            // now look for any intersecting regions
            ApplicableRegionSet set = (ApplicableRegionSet) legacy_getApplicableRegions_Region.invoke(worldManager, l);

            // so what's the verdict?
            State result = set.queryState((RegionAssociable) null, (StateFlag) flag);
            if (result == null && set.size() == 0)
                return null;
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
            }

            // grab the applicable manager for this world
            Object worldManager = (RegionManager) legacy_getRegionManager.invoke(worldGuardPlugin, c.getWorld());
            if (worldManager == null)
                return null;

            // Create a legacy ProtectedCuboidRegion
            Object chunkRegion = legacy_newProtectedCuboidRegion.newInstance("__TEST__",
                legacy_newblockVector.newInstance(c.getX() << 4, c.getWorld().getMaxHeight(), c.getZ() << 4),
                legacy_newblockVector.newInstance((c.getX() << 4) + 15, 0, (c.getZ() << 4) + 15));

            // now look for any intersecting regions
            ApplicableRegionSet set = (ApplicableRegionSet) legacy_getApplicableRegions_Region.invoke(worldManager, chunkRegion);

            // so what's the verdict?
            State result = set.queryState((RegionAssociable) null, (StateFlag) flag);
            if (result == null && set.size() == 0)
                return null;
            return result == State.ALLOW;

        } catch (Exception ex) {
			Bukkit.getServer().getLogger().log(Level.WARNING, "Could not grab flags from WorldGuard", ex);
        }
        return null;
    }
}
