package com.songoda.core.lootables.loot;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropUtils {
    public static void processStackedDrop(LivingEntity entity, List<Drop> drops, EntityDeathEvent event) {
        int xpToDrop = event.getDroppedExp();
        List<ItemStack> items = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        List<Integer> xp = new ArrayList<>();

        for (Drop drop : drops) {
            if (drop == null) continue;

            ItemStack droppedItem = drop.getItemStack();
            if (droppedItem != null) {
                droppedItem = droppedItem.clone();
                boolean success = false;

                for (ItemStack item : items) {
                    if (item.getType() != droppedItem.getType()
                            || item.getDurability() != droppedItem.getDurability()
                            || item.getAmount() + droppedItem.getAmount() > droppedItem.getMaxStackSize()) continue;
                    item.setAmount(item.getAmount() + droppedItem.getAmount());
                    success = true;

                    break;
                }

                if (!success) {
                    items.add(droppedItem);
                }
            }

            if (drop.getCommand() != null) {
                commands.add(drop.getCommand());
            }

            if (drop.getXp() != 0) {
                xp.add(drop.getXp());
            }
        }

        event.getDrops().clear();

        if (!items.isEmpty()) {
            dropItems(items, event);
        } else if (!commands.isEmpty()) {
            runCommands(entity, commands);
        }

        for (int x : xp) {
            xpToDrop += x;
        }

        event.setDroppedExp(xpToDrop);
    }

    private static void dropItems(List<ItemStack> items, EntityDeathEvent event) {
        for (ItemStack item : items) {
            event.getDrops().add(item);
        }
    }

    private static void runCommands(LivingEntity entity, List<String> commands) {
        for (String command : commands) {
            if (entity.getKiller() != null) {
                command = command.replace("%player%", entity.getKiller().getName());
            }

            if (!command.contains("%player%")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        }
    }
}
