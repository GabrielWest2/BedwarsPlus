package com.gabe.bedwars.events;

import com.gabe.bedwars.arenas.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BWPlayerDeathEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Game game;

    public BWPlayerDeathEvent(Player player, Game game){
        this.game = game;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
