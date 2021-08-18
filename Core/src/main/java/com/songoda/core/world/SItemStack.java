package com.songoda.core.world;

import com.songoda.core.compatibility.CompatibleHand;
import com.songoda.core.compatibility.CompatibleSound;
import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.nms.NmsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SItemStack {

    protected final com.songoda.core.nms.world.SItemStack sItem;
    protected final ItemStack item;

    public SItemStack(ItemStack item) {
        this.item = item;
        this.sItem = NmsManager.getWorld().getItemStack(item);
    }

    public SItemStack(CompatibleHand hand, Player player) {
        this.item = hand.getItem(player);
        this.sItem = NmsManager.getWorld().getItemStack(item);
    }

    /**
     * Damage the selected item
     *
     * @param player the player who's item you want to damage
     * @param damage the amount of damage to apply to the item
     */
    public ItemStack addDamage(Player player, int damage) {
        if (item == null)
            return null;

        if (item.getItemMeta() == null)
            return item;

        int maxDurability = item.getType().getMaxDurability();
        int durability;

        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_11)
                ? NmsManager.getNbt().of(item).has("Unbreakable")
                : item.getItemMeta().isUnbreakable()) {
            return item;
        } else if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_13)) {
            // ItemStack.setDurability(short) still works in 1.13-1.14, but use these methods now
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable) {
                Damageable damageable = ((Damageable) meta);
                damageable.setDamage(((Damageable) meta).getDamage() + damage);
                item.setItemMeta(meta);
                durability = damageable.getDamage();
            } else {
                return item;
            }
        } else {
            item.setDurability((short) Math.max(0, item.getDurability() + damage));
            durability = item.getDurability();
        }
        if (durability >= maxDurability && player != null)
            destroy(player);

        return item;
    }

    public void destroy(Player player) {
        destroy(player, 1);
    }

    public void destroy(Player player, int amount) {
        PlayerItemBreakEvent breakEvent = new PlayerItemBreakEvent(player, item);
        Bukkit.getServer().getPluginManager().callEvent(breakEvent);
        sItem.breakItem(player, amount);
        CompatibleSound.ENTITY_ITEM_BREAK.play(player);
    }

    public ItemStack getItem() {
        return item;
    }
}
