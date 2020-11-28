package com.gabe.bedwars.managers;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.GameStats;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GameStatsManager {
    private HashMap<Player, GameStats> statsMap;


    public GameStatsManager(){
        statsMap = new HashMap<>();
    }

    public void addPlayer(Player player){
        statsMap.put(player, new GameStats());
    }

    public void removePlayer(Player player){
        statsMap.remove(player);
    }

    public GameStats getStats(Player player){
      return statsMap.get(player);
    }

    public void playerGotKill(Player player){
        statsMap.get(player).addKill();
    }

    public void playerGotFinalKill(Player player){
        statsMap.get(player).addFinalKill();
    }

    public void playerBrokeBed(Player player){
        statsMap.get(player).brokeBed();
    }

    public int getKills(Player player){
        return statsMap.get(player).getKills();
    }

    public int getFinals(Player player){
        return statsMap.get(player).getFinalKills();
    }

    public int getBeds(Player player){
        return statsMap.get(player).getBedsBroken();
    }
}
