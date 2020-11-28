package com.gabe.bedwars.managers;

import com.gabe.bedwars.Bedwars;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class GameBlockManager {

    private Set<Location> blocks;

    public GameBlockManager(){
        blocks = new HashSet<>();
    }

    public boolean canBreak(Location location){
        return blocks.contains(location);
    }

    public void placedBlock(Location location){
        blocks.add(location);
    }

    public void brokeBlock(Location location){
        blocks.remove(location);
    }

    public void restoreMap(){
        for (Location loc : blocks){
            loc.getWorld().getBlockAt(loc).setType(Material.AIR);
        }
    }
}
