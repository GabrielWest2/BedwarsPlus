package com.gabe.bedwars.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class NameManager {
    private HashMap<UUID, String> names;

    public NameManager(){
        names = new HashMap<>();
    }

    public void addPlayer(Player player){
        names.put(player.getUniqueId(), player.getDisplayName());
    }

    public void restoreName(Player player){
        player.setDisplayName(names.get(player.getUniqueId()));
    }

    public void restoreNames(){
        for(UUID uuid : names.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            player.setDisplayName(names.get(player.getUniqueId()));
            names.remove(uuid);
        }
    }
}
