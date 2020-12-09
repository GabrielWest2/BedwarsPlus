package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.team.Team;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand {
    public SetSpawnCommand(Player player, String[] args) {
        if (args.length > 1) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Arena a = Bedwars.getArenaManager().getArena(args[1]);
                Team team = null;
                for (Team t : a.getTeams()) {
                    if (t.getName().equals(args[2])) {
                        team = t;
                    }
                }
                if (team == null) {
                    Bedwars.sendMessage(player, "Team named &6" + args[2] + "&e doesn't exist.");
                    return;
                }
                Location playerLoc = player.getLocation();
                team.setSpawn(playerLoc);
                Bedwars.sendMessage(player, "Set team " + team.getColor() + team.getName() + "&e's spawn to &6x: " + playerLoc.getBlockX() + " y: " + playerLoc.getBlockY() + " z: " + playerLoc.getBlockZ() + "&e.");
            } else {

                Bedwars.sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa setspawn <name> <team>");
        }
    }
}
