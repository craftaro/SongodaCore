package com.songoda.core.hooks.stackers;

import com.songoda.ultimatestacker.entity.EntityStack;
import com.songoda.ultimatestacker.utils.Methods;
import java.lang.reflect.Method;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;

public class UltimateStacker extends Stacker {

    private final com.songoda.ultimatestacker.UltimateStacker plugin;
    private boolean oldItemMethods = false;
    private Method oldUltimateStacker_updateItemAmount;

    public UltimateStacker() {
        this.plugin = com.songoda.ultimatestacker.UltimateStacker.getInstance();
        try {
            oldUltimateStacker_updateItemAmount = com.songoda.ultimatestacker.utils.Methods.class.getDeclaredMethod("updateItemAmount", Item.class, int.class);
            oldItemMethods = true;
        } catch (NoSuchMethodException | SecurityException ex) {
        }
    }

    @Override
    public String getName() {
        return "UltimateStacker";
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public boolean supportsItemStacking() {
        return true;
    }

    @Override
    public boolean supportsEntityStacking() {
        return true;
    }

    @Override
    public void setItemAmount(Item item, int amount) {
        if (oldItemMethods) {
            // TODO: direct reference when this is re-added to the API
            try {
                oldUltimateStacker_updateItemAmount.invoke(null, item, amount);
            } catch (Exception ex) {
                item.remove(); // not the best solution, but prevents duping
            }
        } else {
            Methods.updateItemAmount(item, item.getItemStack(), amount);
        }
    }

    @Override
    public int getItemAmount(Item item) {
        return Methods.getActualItemAmount(item);
    }

    @Override
    public boolean isStacked(LivingEntity entity) {
        return plugin.getEntityStackManager().isStacked(entity);
    }

    @Override
    public int getSize(LivingEntity entity) {
        return isStacked(entity) ? plugin.getEntityStackManager().getStack(entity).getAmount() : 0;
    }

    @Override
    public void remove(LivingEntity entity, int amount) {
        EntityStack stack = plugin.getEntityStackManager().getStack(entity);
        stack.setAmount(stack.getAmount() - amount);
    }

    @Override
    public void add(LivingEntity entity, int amount) {
        plugin.getEntityStackManager().getStack(entity).addAmount(amount);
    }
}
