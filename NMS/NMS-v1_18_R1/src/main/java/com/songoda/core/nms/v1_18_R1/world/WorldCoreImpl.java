package com.songoda.core.nms.v1_18_R1.world;

import com.songoda.core.nms.ReflectionUtils;
import com.songoda.core.nms.v1_18_R1.world.spawner.BBaseSpawnerImpl;
import com.songoda.core.nms.world.BBaseSpawner;
import com.songoda.core.nms.world.SItemStack;
import com.songoda.core.nms.world.SSpawner;
import com.songoda.core.nms.world.SWorld;
import com.songoda.core.nms.world.WorldCore;
import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.profiling.GameProfilerFiller;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.MobSpawnerAbstract;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.Chunk;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.material.Fluid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_18_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftBlock;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
        return new SWorldImpl(world);
    }

    @Override
    public BBaseSpawner getBaseSpawner(CreatureSpawner spawner) throws NoSuchFieldException, IllegalAccessException {
        Object cTileEntity = ReflectionUtils.getFieldValue(spawner, "tileEntity");

        return new BBaseSpawnerImpl(spawner, (MobSpawnerAbstract) ReflectionUtils.getFieldValue(cTileEntity, "a"));
    }

    /**
     * Method is based on {@link WorldServer#a(Chunk, int)}.
     */
    @Override
    public void randomTickChunk(org.bukkit.Chunk bukkitChunk, int tickAmount) {
        Chunk chunk = ((CraftChunk) bukkitChunk).getHandle();
        WorldServer world = chunk.q;

        ChunkCoordIntPair chunkcoordintpair = chunk.f();
        int j = chunkcoordintpair.d();
        int k = chunkcoordintpair.e();

        GameProfilerFiller gameprofilerfiller = world.ab();
        gameprofilerfiller.b("tickBlocks");
        if (tickAmount > 0) {
            ChunkSection[] achunksection = chunk.d();
            int l = achunksection.length;

            for (ChunkSection chunksection : achunksection) {
                if (chunksection.d()) {
                    int j1 = chunksection.g();

                    for (int k1 = 0; k1 < tickAmount; ++k1) {
                        BlockPosition blockposition2 = world.a(j, j1, k, 15);
                        gameprofilerfiller.a("randomTick");
                        IBlockData iblockdata1 = chunksection.a(blockposition2.u() - j, blockposition2.v() - j1, blockposition2.w() - k);
                        if (iblockdata1.o()) {
                            iblockdata1.b(world, blockposition2, world.w);
                        }

                        Fluid fluid = iblockdata1.n();
                        if (fluid.f()) {
                            fluid.b(world, blockposition2, world.w);
                        }

                        gameprofilerfiller.c();
                    }
                }
            }
        }

        gameprofilerfiller.c();
    }

    @Override
    public void updateAdjacentComparators(@NotNull Location loc) {
        Objects.requireNonNull(loc.getWorld());

        WorldServer serverLevel = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition blockPos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        Block nmsBlock = ((CraftBlock) loc.getBlock()).getNMS().b();

        serverLevel.c(blockPos, nmsBlock);
    }
}
