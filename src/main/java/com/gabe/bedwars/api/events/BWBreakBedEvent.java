package com.gabe.bedwars.api.events;

import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.team.GameTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BWBreakBedEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Game game;
    private final GameTeam team;
    private boolean cancelled = false;


    public BWBreakBedEvent(Game game, Player player, GameTeam team) {
        this.player = player;
        this.game = game;
        this.team = team;
    }

    public Player getPlayer() { return player; }

    public Game getGame() {
        return game;
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

    public GameTeam getTeam() {
        return team;
    }
}
