package com.songoda.core.utils;

import com.songoda.core.SongodaCore;
import com.songoda.core.compatibility.ClassMapping;
import com.songoda.core.compatibility.MethodMapping;
import com.songoda.core.compatibility.ServerVersion;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TrapDoor;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @deprecated This class will be removed in the future and replaced with a more maintainable system.
 */
@Deprecated
public class BlockUtilsModern {
    protected static void _updatePressurePlateModern(Block plate, int power) {
        BlockData blockData = plate.getBlockData();
        boolean update = false;

        if (blockData instanceof AnaloguePowerable) {
            AnaloguePowerable a = (AnaloguePowerable) blockData;
            int toPower = Math.min(a.getMaximumPower(), power);

            if ((update = toPower != a.getPower())) {
                a.setPower(toPower);
                plate.setBlockData(a);
            }
        } else if (blockData instanceof Powerable) {
            Powerable p = (Powerable) blockData;

            if ((update = p.isPowered() != (power != 0))) {
                p.setPowered(power != 0);
                plate.setBlockData(p);
            }
        }

        if (update) {
            _updateRedstoneNeighbours(plate);
        }
    }

    protected static void _toggleLeverModern(Block lever) {
        BlockData blockData = lever.getBlockData();

        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(!s.isPowered());
            lever.setBlockData(s);
            _updateRedstoneNeighbours(lever);
        }
    }

    protected static void _pressButtonModern(Block button) {
        BlockData blockData = button.getBlockData();

        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(true);
            button.setBlockData(s);
            _updateRedstoneNeighbours(button);
        }
    }

    static void _releaseButtonModern(Block button) {
        BlockData blockData = button.getBlockData();

        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(false);
            button.setBlockData(s);
            _updateRedstoneNeighbours(button);
        }
    }

    private static Class<?> clazzCraftWorld;
    private static Class<?> clazzCraftBlock;
    private static Class<?> clazzLeverBlock;
    private static Class<?> clazzButtonBlock;
    private static Class<?> clazzPressurePlateBlock;
    private static Method craftWorld_getHandle, craftBlock_getNMS, craftBlock_getPostition, craftBlockData_getState,
            nmsLever_updateNeighbours, nmsButton_updateNeighbours, nmsPlate_updateNeighbours, nmsBlockData_getBlock;

    static {
        try {
            // Cache reflection.
            clazzCraftWorld = ClassMapping.CRAFT_WORLD.getClazz();
            clazzCraftBlock = ClassMapping.CRAFT_BLOCK.getClazz();

            craftWorld_getHandle = MethodMapping.CB_GENERIC__GET_HANDLE.getMethod(clazzCraftWorld);
            craftBlock_getPostition = MethodMapping.CB_BLOCK__GET_POSITION.getMethod(clazzCraftBlock);

            craftBlock_getNMS = MethodMapping.CB_BLOCK__GET_NMS.getMethod(clazzCraftBlock);
            Class<?> clazzBlockData = ClassMapping.BLOCK_BASE.getClazz("BlockData");
            nmsBlockData_getBlock = MethodMapping.I_BLOCK_DATA__GET_BLOCK.getMethod(clazzBlockData);

            Class<?> clazzCraftBlockData = ClassMapping.CRAFT_BLOCK_DATA.getClazz();
            craftBlockData_getState = MethodMapping.CB_BLOCK_DATA__GET_STATE.getMethod(clazzCraftBlockData);

            Class<?> clazzWorld = ClassMapping.WORLD.getClazz();
            Class<?> clazzBlockState = ClassMapping.I_BLOCK_DATA.getClazz();
            Class<?> clazzBlockPos = ClassMapping.BLOCK_POSITION.getClazz();
            clazzLeverBlock = ClassMapping.BLOCK_LEVER.getClazz();
            clazzButtonBlock = ClassMapping.BLOCK_BUTTON_ABSTRACT.getClazz();
            clazzPressurePlateBlock = ClassMapping.BLOCK_PRESSURE_PLATE_ABSTRACT.getClazz();

            nmsLever_updateNeighbours = clazzLeverBlock.getDeclaredMethod(ServerVersion.isServerVersionAbove(ServerVersion.V1_13)
                    ? "e" : "b", clazzBlockState, clazzWorld, clazzBlockPos);
            nmsLever_updateNeighbours.setAccessible(true);

            nmsButton_updateNeighbours = clazzButtonBlock.getDeclaredMethod(ServerVersion.isServerVersionAbove(ServerVersion.V1_13)
                    ? "f" : "c", clazzBlockState, clazzWorld, clazzBlockPos);
            nmsButton_updateNeighbours.setAccessible(true);

            nmsPlate_updateNeighbours = clazzPressurePlateBlock.getDeclaredMethod("a", clazzWorld, clazzBlockPos);
            nmsPlate_updateNeighbours.setAccessible(true);
        } catch (Throwable ex) {
            Logger.getLogger(BlockUtilsModern.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void _updateRedstoneNeighbours(Block block) {
        try {
            // spigot made some changes to how data updates work in 1.13+
            // updating the data value of a redstone power source
            // does NOT update attatched block power,
            // even if you update the block state. (Still broken last I checked in 1.15.2)
            // so now we're going to manually call the updateNeighbours block method

            // invoke and cast objects.
            Object cworld = clazzCraftWorld.cast(block.getWorld());
            Object mworld = craftWorld_getHandle.invoke(cworld);
            Object cblock = clazzCraftBlock.cast(block);
            Object mblock = nmsBlockData_getBlock.invoke(craftBlock_getNMS.invoke(cblock));
            Object mpos = craftBlock_getPostition.invoke(cblock);

            // now for testing stuff
            if (clazzLeverBlock.isAssignableFrom(mblock.getClass())) {
                final Object mstate = craftBlockData_getState.invoke(block.getBlockData());
                nmsLever_updateNeighbours.invoke(mblock, mstate, mworld, mpos);
            } else if (clazzButtonBlock.isAssignableFrom(mblock.getClass())) {
                final Object mstate = craftBlockData_getState.invoke(block.getBlockData());
                nmsButton_updateNeighbours.invoke(mblock, mstate, mworld, mpos);
            } else if (clazzPressurePlateBlock.isAssignableFrom(mblock.getClass())) {
                nmsPlate_updateNeighbours.invoke(mblock, mworld, mpos);
            } else {
                SongodaCore.getLogger().warning("Unknown redstone: " + mblock.getClass().getName());
            }
//
//			if(mblock instanceof net.minecraft.server.v1_15_R1.BlockLever) {
//				Method updateNeighbours = net.minecraft.server.v1_15_R1.BlockLever.class.getDeclaredMethod("e", net.minecraft.server.v1_15_R1.IBlockData.class, net.minecraft.server.v1_15_R1.World.class, net.minecraft.server.v1_15_R1.BlockPosition.class);
//				updateNeighbours.setAccessible(true);
//				// IBlockData = block state after being powered
//
//				updateNeighbours.invoke(mblock,
//						((org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData) block.getBlockData()).getState(),
//						mworld,
//						mpos);
//			} else if(mblock instanceof net.minecraft.server.v1_15_R1.BlockButtonAbstract) {
//				Method updateNeighbours = net.minecraft.server.v1_15_R1.BlockButtonAbstract.class.getDeclaredMethod("f", net.minecraft.server.v1_15_R1.IBlockData.class, net.minecraft.server.v1_15_R1.World.class, net.minecraft.server.v1_15_R1.BlockPosition.class);
//				updateNeighbours.setAccessible(true);
//				// IBlockData = block state after being powered
//
//				updateNeighbours.invoke(mblock,
//						((org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData) block.getBlockData()).getState(),
//						mworld,
//						mpos);
//			} else if(mblock instanceof net.minecraft.server.v1_15_R1.BlockPressurePlateAbstract) {
//				Method updateNeighbours = net.minecraft.server.v1_15_R1.BlockPressurePlateAbstract.class.getDeclaredMethod("a", net.minecraft.server.v1_15_R1.World.class, net.minecraft.server.v1_15_R1.BlockPosition.class);
//				updateNeighbours.setAccessible(true);
//				// IBlockData = block state after being powered
//
//				updateNeighbours.invoke(mblock,
//						mworld,
//						mpos);
//			}
        } catch (Throwable ex) {
            Logger.getLogger(BlockUtilsModern.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static void _toggleDoorStatesModern(boolean allowDoorToOpen, Block... doors) {
        for (Block door : doors) {
            BlockData blockData;
            if (door == null || !((blockData = door.getBlockData()) instanceof Door)) {
                continue;
            }

            Door data = (Door) blockData;
            if (!allowDoorToOpen && !data.isOpen()) {
                continue;
            }

            // The lower half of the door contains the open/close state
            if (data.getHalf() == Bisected.Half.TOP) {
                Block lowerHalf = door.getRelative(BlockFace.DOWN);

                if (lowerHalf.getBlockData() instanceof Door) {
                    Door lowerData = (Door) lowerHalf.getBlockData();
                    lowerData.setOpen(!data.isOpen());
                    lowerHalf.setBlockData(lowerData);
                }
            } else {
                data.setOpen(!data.isOpen());
                door.setBlockData(data);
            }

            // Play the door open/close sound
            door.getWorld().playEffect(door.getLocation(), Effect.DOOR_TOGGLE, 0);
        }
    }

    protected static Block _getDoubleDoorModern(Block block) {
        BlockData bd = block.getBlockData();
        Block door = null;

        if (bd instanceof Door) {
            final Door d = (Door) bd;
            final BlockFace face = d.getFacing();

            if (face.getModX() == 0) {
                if (d.getHinge() == Door.Hinge.RIGHT) {
                    door = block.getRelative(face.getModZ(), 0, 0);
                } else {
                    door = block.getRelative(-face.getModZ(), 0, 0);
                }
            } else {
                if (d.getHinge() == Door.Hinge.RIGHT) {
                    door = block.getRelative(0, 0, -face.getModX());
                } else {
                    door = block.getRelative(0, 0, face.getModX());
                }
            }
        }

        return door != null && door.getBlockData() instanceof Door
                && ((Door) door.getBlockData()).getHinge() != ((Door) bd).getHinge() ? door : null;
    }

    protected static BlockFace _getDoorClosedDirectionModern(Block door) {
        if (BlockUtils.DOORS.contains(door.getType())) {
            BlockData bd = door.getBlockData();

            if (bd instanceof Door) {
                Door d = (Door) bd;

                // The lower half of the door contains the open/close state
                if (d.getHalf() == Bisected.Half.TOP) {
                    door = door.getRelative(BlockFace.DOWN);

                    if (door.getBlockData() instanceof Door) {
                        d = (Door) door.getBlockData();
                    } else {
                        return null;
                    }
                }

                final BlockFace face = d.getFacing();

                // now we /could/ also correct for the hinge (top block), it's not needed information
                if (face.getModX() == 0) {
                    return d.isOpen() ? BlockFace.EAST : BlockFace.SOUTH;
                } else {
                    return d.isOpen() ? BlockFace.SOUTH : BlockFace.EAST;
                }
            }
        } else if (BlockUtils.FENCE_GATES.contains(door.getType())) {
            BlockData bd = door.getBlockData();

            if (bd instanceof Gate) {
                Gate g = (Gate) bd;
                final BlockFace face = g.getFacing();

                if (face.getModX() == 0) {
                    return g.isOpen() ? BlockFace.EAST : BlockFace.SOUTH;
                } else {
                    return g.isOpen() ? BlockFace.SOUTH : BlockFace.EAST;
                }
            }
        } else if (BlockUtils.TRAP_DOORS.contains(door.getType())) {
            BlockData bd = door.getBlockData();

            if (bd instanceof TrapDoor) {
                TrapDoor t = (TrapDoor) bd;

                if (!t.isOpen()) {
                    return BlockFace.UP;
                } else {
                    return t.getFacing();
                }
            }
        }

        return null;
    }

    protected static boolean _isCropFullyGrown(Block block) {
        BlockData data = block.getBlockData();

        if (data instanceof Ageable) {
            return ((Ageable) data).getAge() == ((Ageable) data).getMaximumAge();
        }

        return false;
    }

    protected static int _getMaxGrowthStage(Block block) {
        BlockData data = block.getBlockData();

        if (data instanceof Ageable) {
            return ((Ageable) data).getMaximumAge();
        }

        return -1;
    }

    protected static int _getMaxGrowthStage(Material material) {
        BlockData data = material.createBlockData();

        if (data instanceof Ageable) {
            return ((Ageable) data).getMaximumAge();
        }

        return -1;
    }

    public static void _setGrowthStage(Block block, int stage) {
        BlockData data = block.getBlockData();

        if (data instanceof Ageable) {
            ((Ageable) data).setAge(Math.max(0, Math.min(stage, ((Ageable) data).getMaximumAge())));
            block.setBlockData(data);
        }
    }

    public static void _incrementGrowthStage(Block block) {
        BlockData data = block.getBlockData();

        if (data instanceof Ageable) {
            final int max = ((Ageable) data).getMaximumAge();
            final int age = ((Ageable) data).getAge();

            if (age < max) {
                ((Ageable) data).setAge(age + 1);
                block.setBlockData(data);
            }
        }
    }

    public static void _resetGrowthStage(Block block) {
        BlockData data = block.getBlockData();

        if (data instanceof Ageable) {
            ((Ageable) data).setAge(0);
            block.setBlockData(data);
        }
    }
}
