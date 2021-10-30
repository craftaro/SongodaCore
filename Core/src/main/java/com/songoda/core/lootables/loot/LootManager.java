package com.songoda.core.lootables.loot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.lootables.Lootables;
import com.songoda.core.lootables.Modify;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class LootManager {
    private final Map<String, Lootable> registeredLootables = new HashMap<>();

    private final Lootables lootables;

    public LootManager(Lootables lootables) {
        this.lootables = lootables;
    }

    public Lootable addLootable(Lootable lootable) {
        return registeredLootables.put(lootable.getKey(), lootable);
    }

    public void removeLootable(String key) {
        registeredLootables.remove(key);

        File file = new File(lootables.getLootablesDir(), key.toLowerCase() + ".json");
        file.delete();
    }

    public List<Drop> runLoot(Modify modify, boolean burning, boolean isCharged, ItemStack murderWeapon, EntityType looter, Loot loot, int rerollChance, int looting) {
        List<Drop> toDrop = new ArrayList<>();

        if (modify != null) {
            loot = modify.Modify(loot);
        }

        if (loot == null) {
            return toDrop;
        }

        if (loot.runChance(looting, murderWeapon) ||
                ((Math.random() * 100) - rerollChance < 0 || rerollChance == 100) &&
                        loot.runChance(looting, murderWeapon)) {

            if (loot.getOnlyDropFor().size() != 0
                    && loot.getOnlyDropFor().stream().noneMatch(type -> looter != null && type == looter)
                    || !isCharged && loot.isRequireCharged())
                return toDrop;

            if (loot.getChildLoot().size() > 0) {
                List<Loot> childLoot = loot.getChildLoot();
                Collections.shuffle(childLoot);

                int amt = loot.getChildDropCount();
                int success = 0;

                top:
                for (int i = 0; i < 100; i++) {
                    for (Loot value : childLoot) {
                        if (value == null) {
                            continue;
                        }

                        if (amt == success) {
                            break top;
                        }

                        List<Drop> drops = runLoot(modify, burning, isCharged, murderWeapon, looter, value, rerollChance, looting);

                        if (!drops.isEmpty()) {
                            success++;
                        }

                        toDrop.addAll(drops);
                    }
                }
            }

            CompatibleMaterial material = loot.getMaterial();
            String command = loot.getCommand();
            int xp = loot.getXp();

            if (material == null && command == null) {
                return toDrop;
            }

            int amount = loot.getAmountToDrop(looting);
            if (amount == 0) {
                return toDrop;
            }

            if (material != null) {
                ItemStack item = loot.getBurnedMaterial() != null &&
                        burning ? loot.getBurnedMaterial().getItem() : material.getItem();
                item.setAmount(amount);
                ItemMeta meta = item.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(loot.getMaterial().getMaterial())
                        : item.getItemMeta();

                if (loot.getName() != null) {
                    meta.setDisplayName(loot.getName());
                }

                if (loot.getLore() != null) {
                    meta.setLore(loot.getLore());
                }

                item.setItemMeta(meta);

                if (loot.getEnchants(item) != null) {
                    item = loot.getEnchants(item);
                }

                if (loot.getDamageMax() != 0 && loot.getDamageMin() != 0) {
                    short max = item.getType().getMaxDurability();
                    short min = (short) (max * (10 / 100.0f));

                    item.setDurability((short) (new Random().nextInt(max - min + 1) + min));
                }

                toDrop.add(new Drop(item));
            }

            if (command != null) {
                for (int i = 0; i < amount; i++) {
                    toDrop.add(new Drop(command));
                }
            }

            if (xp != 0) {
                for (int i = 0; i < amount; i++) {
                    toDrop.add(new Drop(xp));
                }
            }
        }

        return toDrop;
    }

    public void loadLootables() {
        registeredLootables.clear();

        File dir = new File(lootables.getLootablesDir());
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File file : directoryListing) {
                if (!file.getName().endsWith(".json")) {
                    continue;
                }

                try {
                    Gson gson = new Gson();
                    JsonReader reader = new JsonReader(new FileReader(file.getPath()));

                    Lootable lootable = gson.fromJson(reader, Lootable.class);

                    if (lootable.getRegisteredLoot().size() != 0) {
                        addLootable(lootable);
                    }

                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void saveLootables(boolean defaults) {
        File dir = new File(lootables.getLootablesDir());
        dir.mkdir();

        // Save to file
        for (Lootable lootable : registeredLootables.values()) {
            try {
                File file = new File(lootables.getLootablesDir(), lootable.getKey().toLowerCase() + ".json");

                if (file.exists() && defaults) {
                    continue;
                }

                try (Writer writer = new FileWriter(file.getPath())) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(lootable, writer);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (defaults) {
            registeredLootables.clear();
        }
    }

    public Map<String, Lootable> getRegisteredLootables() {
        return Collections.unmodifiableMap(registeredLootables);
    }
}
