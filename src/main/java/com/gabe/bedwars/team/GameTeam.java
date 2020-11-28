package com.gabe.bedwars.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameTeam {

    private Set<Player> players;
    private Set<Player> alivePlayers;
    private Set<Player> spec;
    private final String name;
    private final ChatColor color;
    private final Location spawn;
    private final Location generator;
    private final Location bed;
    private boolean hasBed = true;
    public GameTeam(String name, ChatColor color, Location spawn, Location generator, Location bed){
        players = new HashSet<>();
        alivePlayers = new HashSet<>();
        spec = new HashSet<>();
        this.name = name;
        this.color = color;
        this.spawn = spawn;
        this.generator = generator;
        this.bed = bed;
    }

    public void playerDied(Player player){
        alivePlayers.remove(player);
    }

    public Set<Player> getAlivePlayers(){
        return Collections.unmodifiableSet(alivePlayers);
    }

    public Location getBed(){
        return bed;
    }

    public boolean hasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public Location getGenerator(){
        return generator;
    }
    public ChatColor getColor(){
        return color;
    }

    public Set<Player> getPlayers(){
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(Player player){
        players.add(player);
        alivePlayers.add(player);
    }

    public void died(Player player){
        spec.add(player);
        alivePlayers.remove(player);
    }

    public void removePlayer(Player player){
        players.remove(player);
        spec.remove(player);
        alivePlayers.remove(player);
        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

    public void teleportPlayers(){
        for(Player player : players){
            player.teleport(spawn);
        }
        setUpPlayers();
    }

    public String getName() {
        return name;
    }

    public Location getSpawn(){
        return spawn;
    }

    public void setUpPlayers(){
        for(Player player : players){
            player.setPlayerListName(color+player.getName());
        }
    }
}
