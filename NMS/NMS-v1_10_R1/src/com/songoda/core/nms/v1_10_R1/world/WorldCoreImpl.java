package com.songoda.core.nms.v1_10_R1.world;

import com.songoda.core.nms.world.SItemStack;
import com.songoda.core.nms.ReflectionUtils;
import com.songoda.core.nms.v1_10_R1.world.spawner.BBaseSpawnerImpl;
import com.songoda.core.nms.world.BBaseSpawner;
import com.songoda.core.nms.world.SSpawner;
import com.songoda.core.nms.world.SWorld;
import com.songoda.core.nms.world.WorldCore;
import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.ChunkSection;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.MobSpawnerAbstract;
import net.minecraft.server.v1_10_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.inventory.ItemStack;
import org.bukkit.craftbukkit.v1_10_R1.CraftChunk;

public class WorldCoreImpl implements WorldCore {
    @Override
    public SSpawner getSpawner(CreatureSpawner spawner) {
        return new SSpawnerImpl(spawner.getLocation());
    }

    @Override
    public SSpawner getSpawner(Location location) {
        return new SSpawnerImpl(location);
    }

    @Override
    public SItemStack getItemStack(ItemStack item) {
        return new SItemStackImpl(item);
    }

    @Override
    public SWorld getWorld(World world) {
        return new SWorldImpl();
    }

    @Override
    public BBaseSpawner getBaseSpawner(CreatureSpawner spawner) throws NoSuchFieldException, IllegalAccessException {
        Object cTileEntity = ReflectionUtils.getFieldValue(spawner, "spawner");

        return new BBaseSpawnerImpl((MobSpawnerAbstract) ReflectionUtils.getFieldValue(cTileEntity, "a"));
    }

    /**
     * Method is based on {@link WorldServer#j()}.
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public void randomTickChunk(org.bukkit.Chunk bukkitChunk, int tickAmount) throws NoSuchFieldException, IllegalAccessException {
        Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();

        if (tickAmount > 0) {
            int j = chunk.locX * 16;
            int k = chunk.locZ * 16;

            for (ChunkSection chunksection : chunk.getSections()) {
                if (chunksection != net.minecraft.server.v1_10_R1.Chunk.a && chunksection.shouldTick()) {
                    for (int i = 0; i < tickAmount; ++i) {
                        int worldL = (int) ReflectionUtils.getFieldValue(chunk.world, "l");
                        worldL = worldL * 3 + 1013904223;
                        ReflectionUtils.setFieldValue(chunk.world, "l", worldL);

                        int l1 = worldL >> 2;
                        int i2 = l1 & 15;
                        int j2 = l1 >> 8 & 15;
                        int k2 = l1 >> 16 & 15;

                        IBlockData iblockdata = chunksection.getType(i2, k2, j2);
                        Block block = iblockdata.getBlock();

                        if (block.isTicking()) {
                            block.a(chunk.world, new BlockPosition(i2 + j, k2 + chunksection.getYPosition(), j2 + k), iblockdata, chunk.world.random);
                        }
                    }
                }
            }
        }
    }
}
