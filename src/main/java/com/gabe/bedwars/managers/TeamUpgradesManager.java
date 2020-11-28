package com.gabe.bedwars.managers;

import com.gabe.bedwars.team.GameTeam;
import com.gabe.bedwars.upgrade.TeamUpgrades;

import java.util.HashMap;

public class TeamUpgradesManager {
    private HashMap<GameTeam, TeamUpgrades> stats;

    public TeamUpgradesManager(){
        stats = new HashMap<>();
    }

    public void addTeam(GameTeam team){
        stats.put(team, new TeamUpgrades());
    }

    public void removeTeam(GameTeam team){
        stats.remove(team);
    }

    public TeamUpgrades getUpgrades(GameTeam team){
        return stats.get(team);
    }
}
