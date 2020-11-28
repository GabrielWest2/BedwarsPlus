package com.gabe.bedwars.arenas;

import com.gabe.bedwars.team.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Arena {

    private final String name;
    private Location lobbyLocation = null;
    private Location mainLobbyLocation = null;
    private Set<Player> players = null;
    private Set<Team> teams = null;
    private Set<Location> diamondGens;
    private Set<Location> emeraldGens;

    private final int minPlayers;
    private final int maxPlayers;

    public Arena(String name, int minPlayers, int maxPlayers) {
        this.name = name;

        teams = new HashSet<>();
        players = new HashSet<>();
        diamondGens = new HashSet<>();
        emeraldGens = new HashSet<>();

        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public Location getMainLobbyLocation() {
        return mainLobbyLocation;
    }

    public void setMainLobbyLocation(Location mainLobbyLocation) {
        this.mainLobbyLocation = mainLobbyLocation;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }


    public String getName() {
        return name;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void addDiamondGen(Location location){
        diamondGens.add(location);
    }

    public void addEmeraldGen(Location location){
        emeraldGens.add(location);
    }

    public Set<Location> getDiamondGens() {
        return diamondGens;
    }

    public Set<Location> getEmeraldGens() {
        return emeraldGens;
    }

}