package com.songoda.core.lootables.loot;

import com.bgsoftware.wildstacker.api.objects.StackedItem;
import com.songoda.SchedulerUtils;
import com.songoda.core.SongodaCore;
import com.songoda.ultimatestacker.UltimateStacker;
import com.songoda.ultimatestacker.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;

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
            int maxSize = Settings.MAX_STACK_ITEMS.getInt()-64;
            for (ItemStack item : items) {
                StackedItem stack = stacks.stream().filter(stackedItem -> stackedItem.getItem().getType() == item.getType()).findFirst().orElse(null);
                if (stack == null) {
                    stacks.add(new StackedItem(item, item.getAmount()));
                    continue;
                }
                int newAmount = stack.getAmount() + item.getAmount();
                while (newAmount > maxSize) {
                    newAmount -= maxSize;
                    stacks.add(new StackedItem(item, maxSize));
                }
                stack.setamount(newAmount);
            }
            SchedulerUtils.runLocationTask(UltimateStacker.getInstance(), event.getEntity().getLocation(), () -> {
                for (StackedItem stack : stacks) {
                    UltimateStacker.spawnStackedItem(stack.getItem(), stack.getAmount(), event.getEntity().getLocation());
                }
            });
            return;
        }
        event.getDrops().addAll(items);
    }

    private static void runCommands(LivingEntity entity, List<String> commands) {
        SchedulerUtils.runTask(SongodaCore.getHijackedPlugin(), () -> {
            for (String command : commands) {
                if (entity.getKiller() != null) {
                    command = command.replace("%player%", entity.getKiller().getName()
                            .replace("%x%", String.valueOf((int) entity.getLocation().getX()))
                            .replace("%y%", String.valueOf((int) entity.getLocation().getY()))
                            .replace("%z%", String.valueOf((int) entity.getLocation().getZ())));
                }

                if (!command.contains("%player%")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            }
        });

    }

    private static class StackedItem {

        private final ItemStack item;
        private int amount;

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

        public void setamount(int amount) {
            this.amount = amount;
        }
    }
}
