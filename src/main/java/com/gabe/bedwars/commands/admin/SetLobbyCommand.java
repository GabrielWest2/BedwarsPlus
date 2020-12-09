package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import org.bukkit.entity.Player;

public class SetLobbyCommand {
    public SetLobbyCommand(Player player, String[] args) {
        if (args.length > 1) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Arena a = Bedwars.getArenaManager().getArena(args[1]);
                a.setLobbyLocation(player.getLocation().toBlockLocation());
                Bedwars.sendMessage(player, "Set the lobby of arena &6" + args[1] + "&e to &6x: " + a.getLobbyLocation().getBlockX() + " y: " + a.getLobbyLocation().getBlockY() + " z: " + a.getLobbyLocation().getBlockZ() + "&e.");
            } else {
                Bedwars.sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa setlobby <name>");
        }
    }
}
