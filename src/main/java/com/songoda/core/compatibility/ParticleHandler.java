package com.songoda.core.compatibility;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;

public class ParticleHandler {
    
    public static void redstoneParticles(Location location, int red, int green, int blue) {
        redstoneParticles(location, red, green, blue, 1F, 1, 0);
    }
    
    /**
     * Spawn colored redstone particles
     * 
     * @param location area to spawn the particle in
     * @param red red value 0-255
     * @param green green value 0-255
     * @param blue blue value 0-255
     * @param size (1.13+) size of the particles
     * @param count how many particles to spawn
     * @param radius how far to spread out the particles from location
     */
    public static void redstoneParticles(Location location, int red, int green, int blue, float size, int count, float radius) {
        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
            float xx = (float) (0 + (Math.random() * 1));
            float yy = (float) (0 + (Math.random() * 1));
            float zz = (float) (0 + (Math.random() * 1));
            location.getWorld().spawnParticle(Particle.REDSTONE, location, count, xx, yy, zz, 1, new Particle.DustOptions(Color.fromBGR(blue, green, red), size));
        } else if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)) {
            for (int i = 0; i < count; i++) {
                float xx = (float) (radius * (Math.random() - Math.random()));
                float yy = (float) (radius * (Math.random() - Math.random()));
                float zz = (float) (radius * (Math.random() - Math.random()));
                Location at = location.clone().add(xx, yy, zz);
                location.getWorld().spawnParticle(Particle.REDSTONE, at, 0, red / 255F, green / 255F, blue / 255F, size); // particle, location, count, red, green, blue, extra data
            }
        } else {
            // WE NEED MAGIC!
            for (int i = 0; i < count; i++) {
                float xx = (float) (radius * (Math.random() - Math.random()));
                float yy = (float) (radius * (Math.random() - Math.random()));
                float zz = (float) (radius * (Math.random() - Math.random()));
                Location at = location.clone().add(xx, yy, zz);
                LegacyParticleEffects.createParticle(at, LegacyParticleEffects.Type.REDSTONE, 
                        red / 255F, green / 255F, blue / 255F, 1F, 0, null);
            }
        }
    }

	public static void bonemealSmoke(Location l) {
		final org.bukkit.World w = l.getWorld();
		w.playEffect(l, Effect.SMOKE, BlockFace.SOUTH_EAST);
		w.playEffect(l, Effect.SMOKE, BlockFace.SOUTH);
		w.playEffect(l, Effect.SMOKE, BlockFace.SOUTH_WEST);
		w.playEffect(l, Effect.SMOKE, BlockFace.EAST);
		w.playEffect(l, Effect.SMOKE, BlockFace.SELF);
		w.playEffect(l, Effect.SMOKE, BlockFace.WEST);
		w.playEffect(l, Effect.SMOKE, BlockFace.NORTH_EAST);
		w.playEffect(l, Effect.SMOKE, BlockFace.NORTH);
		w.playEffect(l, Effect.SMOKE, BlockFace.NORTH_WEST);
	}

}
