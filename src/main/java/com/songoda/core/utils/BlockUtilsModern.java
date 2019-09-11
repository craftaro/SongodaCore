package com.songoda.core.utils;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.Switch;
import org.bukkit.block.data.type.TrapDoor;

public class BlockUtilsModern {

    protected static void _updatePressurePlateModern(Block plate, int power) {
        BlockData blockData = plate.getBlockData();
        if (blockData instanceof AnaloguePowerable) {
            AnaloguePowerable a = (AnaloguePowerable) blockData;
            a.setPower(Math.max(a.getMaximumPower(), power));
            plate.setBlockData(a);
        }
    }

    protected static void _toggleLeverModern(Block lever) {
        BlockData blockData = lever.getBlockData();
        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(!s.isPowered());
            lever.setBlockData(s);
            //lever.getWorld().playEffect(lever.getLocation(), Effect.CLICK1, 0);
            lever.getState().update();
        }
    }

    protected static void _pressButtonModern(Block button) {
        BlockData blockData = button.getBlockData();
        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(true);
            button.setBlockData(s);
            //lever.getWorld().playEffect(lever.getLocation(), Effect.CLICK1, 0);
            button.getState().update();
        }
    }

    static void _releaseButtonModern(Block button) {
        BlockData blockData = button.getBlockData();
        if (blockData instanceof Switch) {
            Switch s = (Switch) blockData;
            s.setPowered(false);
            button.setBlockData(s);
            //lever.getWorld().playEffect(lever.getLocation(), Effect.CLICK1, 0);
            button.getState().update();
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
        if(data instanceof Ageable) {
            return ((Ageable) data).getAge() == ((Ageable) data).getMaximumAge();
        }
        return false;
    }

    protected static int _getMaxGrowthStage(Block block) {
        BlockData data = block.getBlockData();
        if(data instanceof Ageable) {
            return ((Ageable) data).getMaximumAge();
        }
        return -1;
    }

    protected static int _getMaxGrowthStage(Material material) {
        BlockData data = material.createBlockData();
        if(data instanceof Ageable) {
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
