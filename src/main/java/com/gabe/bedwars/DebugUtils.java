package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.managers.ArenaManager;
import com.gabe.bedwars.team.Team;
import org.bukkit.Location;

public class DebugUtils {

    public static String generateDebug(ArenaManager arenaManager) {
        String debug = "Debug:\n";
        for (Arena a : arenaManager.getArenaList()) {
            debug += "   " + a.getName() + "\n" +
                    "     minplayers: " + a.getMinPlayers() + "\n" +
                    "     maxplayers: " + a.getMaxPlayers() + "\n";
            if (a.getLobbyLocation() != null) {
                debug += "     lobby: x:" + a.getLobbyLocation().getBlockX() + " y:" + a.getLobbyLocation().getBlockY() + " z:" + a.getLobbyLocation().getBlockZ() + "\n";
            } else {
                debug += "     lobby: null\n";
            }
            if (a.getMainLobbyLocation() != null) {
                debug += "     mainlobby: x:" + a.getMainLobbyLocation().getBlockX() + " y:" + a.getMainLobbyLocation().getBlockY() + " z:" + a.getMainLobbyLocation().getBlockZ() + "\n";
            } else {
                debug += "     mainlobby: null\n";
            }
            if (a.getDiamondGens().size() == 0) {
                debug += "     diamond-gens: null\n";
            } else {
                debug += "     diamond-gens:\n";
                for (Location location : a.getDiamondGens()) {
                    debug += "       x:" + location.getBlockX() + " y:" + location.getBlockY() + " z:" + location.getBlockZ() + "\n";
                }
            }
            if (a.getEmeraldGens().size() == 0) {
                debug += "     emerald-gens: null\n";
            } else {
                debug += "     emerald-gens:\n";
                for (Location location : a.getEmeraldGens()) {
                    debug += "       x:" + location.getBlockX() + " y:" + location.getBlockY() + " z:" + location.getBlockZ() + "\n";
                }
            }
            if (a.getTeams().size() == 0) {
                debug += "     teams: null\n";
            } else {
                debug += "     teams:\n";
                for (Team t : a.getTeams()) {
                    debug += "       " + t.getName() + ":\n" +
                            "         color: " + t.getColor() + t.getName() + "&e\n";
                    if (t.getSpawn() != null) {
                        debug += "         spawn: x:" + t.getSpawn().getBlockX() + " y:" + t.getSpawn().getBlockY() + " z:" + t.getSpawn().getBlockZ() + "\n";
                    } else {
                        debug += "         spawn: null\n";
                    }

                    if (t.getGenerator() != null) {
                        debug += "         gen: x:" + t.getGenerator().getBlockX() + " y:" + t.getGenerator().getBlockY() + " z:" + t.getGenerator().getBlockZ() + "\n";
                    } else {
                        debug += "         gen: null\n";
                    }

                    if (t.getBed() != null) {
                        debug += "         bed: x:" + t.getBed().getBlockX() + " y:" + t.getBed().getBlockY() + " z:" + t.getBed().getBlockZ() + "\n";
                    } else {
                        debug += "         bed: null\n";
                    }
                }
            }

        }
        return debug;
    }
}
