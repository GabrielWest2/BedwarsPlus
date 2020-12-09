package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import org.bukkit.entity.Player;

public class SetMainLobbyCommand {
    public SetMainLobbyCommand(Player player, String[] args){
        if (args.length > 1) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Arena a = Bedwars.getArenaManager().getArena(args[1]);
                a.setMainLobbyLocation(player.getLocation().toBlockLocation());
                Bedwars.sendMessage(player, "Set the mainlobby of arena &6" + args[1] + "&e to &6x: " + a.getMainLobbyLocation().getBlockX() + " y: " + a.getMainLobbyLocation().getBlockY() + " z: " + a.getMainLobbyLocation().getBlockZ() + "&e.");
            } else {

                Bedwars.sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa setmainlobby <name>");
        }
    }
}
