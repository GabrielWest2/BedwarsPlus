package com.gabe.bedwars.events;

import com.gabe.bedwars.shop.ShopPage;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BWPlayerBuyItemEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack item;
    private final ShopPage page;
    private boolean cancelled = false;

    public BWPlayerBuyItemEvent(Player player, ItemStack item, ShopPage page) {
        this.player = player;
        this.item = item;
        this.page = page;
    }

    public Player getPlayer() { return player; }

    public ItemStack getItem() {
        return item;
    }

    public ShopPage getPage() {
        return page;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
