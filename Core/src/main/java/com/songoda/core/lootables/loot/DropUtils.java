package com.songoda.core.lootables.loot;

import com.songoda.core.SongodaCore;
import com.songoda.ultimatestacker.UltimateStacker;
import com.songoda.ultimatestacker.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        if (SongodaCore.isRegistered("UltimateStacker")) {
            List<StackedItem> stacks = new ArrayList<>();
            for (ItemStack item : items) {
                if (stacks.isEmpty()) {
                    stacks.add(new StackedItem(item, item.getAmount()));
                    continue;
                }
                for (StackedItem stackedItem : stacks.toArray(new StackedItem[0])) {
                    if (stackedItem.getMaterial().equals(item.getType())) {
                        int newAmount = stackedItem.getAmount() + item.getAmount();
                        int maxSize = Settings.MAX_STACK_ITEMS.getInt();
                        while (newAmount > maxSize) {
                            newAmount -= maxSize;
                            stacks.add(new StackedItem(item, newAmount));
                        }
                        if (newAmount > 0) {
                            stacks.add(new StackedItem(item, newAmount));
                        }
                    } else {
                        stacks.add(new StackedItem(item, item.getAmount()));
                    }
                }
            }
            for (StackedItem stack : stacks) {
                UltimateStacker.spawnStackedItem(stack.getItem(), stack.getAmount(), event.getEntity().getLocation());
            }
            return;
        }
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

    private static class StackedItem {

        private final ItemStack item;
        private final int amount;

        public StackedItem(ItemStack item, int amount) {
            this.item = item;
            this.amount = amount;
        }

        public Material getMaterial() {
            return item.getType();
        }

        public ItemStack getItem() {
            return item;
        }

        public int getAmount() {
            return amount;
        }
    }
}
