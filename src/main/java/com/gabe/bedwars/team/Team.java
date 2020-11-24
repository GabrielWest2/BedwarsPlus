package com.gabe.bedwars.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Team {
    private final String name;
    private final ChatColor color;
    private Location spawn;
    private Location generator;
    private Location bed;

    public Team(String name, ChatColor color){
        this.name = name;
        this.color = color;
    }

    public Location getBed() {
        return bed;
    }

    public void setBed(Location bed) {
        this.bed = bed;
    }

    public Location getGenerator() {
        return generator;
    }

    public void setGenerator(Location generator) {
        this.generator = generator;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
