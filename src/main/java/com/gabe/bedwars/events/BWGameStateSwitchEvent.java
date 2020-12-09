package com.gabe.bedwars.events;

import com.gabe.bedwars.GameState;
import com.gabe.bedwars.arenas.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BWGameStateSwitchEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final GameState from;
    private final GameState to;
    private final Game game;

    public BWGameStateSwitchEvent(GameState from, GameState to, Game game) {
        this.from = from;
        this.to = to;
        this.game = game;

    }

    public GameState getFrom() {
        return from;
    }

    public GameState getTo() {
        return to;
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