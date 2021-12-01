package com.songoda.core.nms.v1_18_R1.world.spawner;

import com.songoda.core.nms.world.BBaseSpawner;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityPositionTypes;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.MobSpawnerAbstract;
import net.minecraft.world.level.MobSpawnerData;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftCreatureSpawner;
import org.bukkit.craftbukkit.v1_18_R1.event.CraftEventFactory;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Optional;
import java.util.Random;

public class BBaseSpawnerImpl implements BBaseSpawner {
    private final CreatureSpawner bukkitSpawner;
    private final MobSpawnerAbstract spawner;

    private static final Random spawnerFieldO = new Random();   // Field o in MobSpawnerAbstract is private - We use one random for *all* our spawners (should be fine, right?)

    public BBaseSpawnerImpl(CreatureSpawner bukkitSpawner, MobSpawnerAbstract spawner) {
        this.bukkitSpawner = bukkitSpawner;
        this.spawner = spawner;
    }

    /**
     * This method is based on {@link MobSpawnerAbstract#b(World, BlockPosition)}.
     */
    @SuppressWarnings("JavadocReference")
    @Override
    public boolean isNearPlayer() {
        BlockPosition bPos = getBlockPosition();

        return getWorld().a((double) bPos.u() + 0.5D, (double) bPos.v() + 0.5D, (double) bPos.w() + 0.5D, this.spawner.m);
    }

    /**
     * This method is based on {@link MobSpawnerAbstract#a(WorldServer, BlockPosition)}.
     */
    @Override
    public void tick() {
        WorldServer world = getWorld();
        BlockPosition bPos = getBlockPosition();

        if (this.spawner.c == -1) {
            this.delay(world, bPos);
        }

        if (this.spawner.c > 0) {
            --this.spawner.c;
            return;
        }

        boolean flag = false;
        int i = 0;

        while (true) {
            if (i >= this.spawner.j) {
                if (flag) {
                    this.delay(world, bPos);
                }
                break;
            }

            NBTTagCompound nbttagcompound = this.spawner.e.a();
            Optional<EntityTypes<?>> optional = EntityTypes.a(nbttagcompound);
            if (optional.isEmpty()) {
                this.delay(world, bPos);
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.c("Pos", 6);
            int j = nbttaglist.size();
            double d0 = j >= 1 ? nbttaglist.h(0) : (double) bPos.u() + (world.w.nextDouble() - world.w.nextDouble()) * (double) this.spawner.n + 0.5D;
            double d1 = j >= 2 ? nbttaglist.h(1) : (double) (bPos.v() + world.w.nextInt(3) - 1);
            double d2 = j >= 3 ? nbttaglist.h(2) : (double) bPos.w() + (world.w.nextDouble() - world.w.nextDouble()) * (double) this.spawner.n + 0.5D;
            if (world.b(optional.get().a(d0, d1, d2))) {
                label128:
                {
                    BlockPosition blockposition1 = new BlockPosition(d0, d1, d2);
                    if (this.spawner.e.b().isPresent()) {
                        if (!optional.get().f().d() && world.af() == EnumDifficulty.a) {
                            break label128;
                        }

                        MobSpawnerData.a mobspawnerdata_a = this.spawner.e.b().get();
                        if (!mobspawnerdata_a.a().a(world.a(EnumSkyBlock.b, blockposition1)) || !mobspawnerdata_a.b().a(world.a(EnumSkyBlock.a, blockposition1))) {
                            break label128;
                        }
                    } else if (!EntityPositionTypes.a((EntityTypes<?>) optional.get(), world, EnumMobSpawn.c, blockposition1, world.r_())) {
                        break label128;
                    }

                    Entity entity = EntityTypes.a(nbttagcompound, world, (entity1) -> {
                        entity1.b(d0, d1, d2, entity1.dm(), entity1.dn());
                        return entity1;
                    });
                    if (entity == null) {
                        this.delay(world, bPos);
                        return;
                    }

                    int k = world.a(entity.getClass(), (new AxisAlignedBB(bPos.u(), bPos.v(), bPos.w(), bPos.u() + 1, bPos.v() + 1, bPos.w() + 1)).g(this.spawner.n)).size();
                    if (k >= this.spawner.l) {
                        this.delay(world, bPos);
                        return;
                    }

                    entity.b(entity.dc(), entity.de(), entity.di(), world.w.nextFloat() * 360.0F, 0.0F);
                    if (entity instanceof EntityInsentient entityinsentient) {
                        if (this.spawner.e.b().isEmpty() && !entityinsentient.a(world, EnumMobSpawn.c) || !entityinsentient.a(world)) {
                            break label128;
                        }

                        if (this.spawner.e.a().e() == 1 && this.spawner.e.a().b("id", 8)) {
                            ((EntityInsentient) entity).a(world, world.d_(entity.cW()), EnumMobSpawn.c, null, null);
                        }

                        if (entityinsentient.t.spigotConfig.nerfSpawnerMobs) {
                            entityinsentient.aware = false;
                        }
                    }

                    if (CraftEventFactory.callSpawnerSpawnEvent(entity, bPos).isCancelled()) {
                        Entity vehicle = entity.cN();
                        if (vehicle != null) {
                            vehicle.ah();
                        }

                        for (Entity passenger : entity.cJ()) {
                            passenger.ah();
                        }
                    } else {
                        if (!world.tryAddFreshEntityWithPassengers(entity, CreatureSpawnEvent.SpawnReason.SPAWNER)) {
                            this.delay(world, bPos);
                            return;
                        }

                        world.c(2004, bPos, 0);
                        if (entity instanceof EntityInsentient) {
                            ((EntityInsentient) entity).L();
                        }

                        flag = true;
                    }
                }
            }

            ++i;
        }
    }

    /**
     * This method is based on {@link MobSpawnerAbstract#c(World, BlockPosition)}.
     */
    @SuppressWarnings("JavadocReference")
    private void delay(WorldServer world, BlockPosition bPos) {
        if (this.spawner.i <= this.spawner.h) {
            this.spawner.c = this.spawner.h;
        } else {
            this.spawner.c = this.spawner.h + spawnerFieldO.nextInt(this.spawner.i - this.spawner.h);
        }

        this.spawner.d.b(spawnerFieldO).ifPresent((weightedentry_b) -> {
            this.spawner.a(world, bPos, weightedentry_b.b());
        });
        this.spawner.a(world, bPos, 1);
    }

    private WorldServer getWorld() {
        return ((CraftWorld) this.bukkitSpawner.getWorld()).getHandle();
    }

    private BlockPosition getBlockPosition() {
        return ((CraftCreatureSpawner) this.bukkitSpawner).getPosition();
    }
}
