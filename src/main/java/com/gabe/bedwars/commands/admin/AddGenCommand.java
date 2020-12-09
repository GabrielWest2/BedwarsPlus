package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AddGenCommand {
    public AddGenCommand(Player player, String[] args){
        if (args.length > 2) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {

                Arena a = Bedwars.getArenaManager().getArena(args[1]);
                if (args[2].equalsIgnoreCase("diamond") || args[2].equalsIgnoreCase("emerald")) {
                    Location playerLoc = player.getLocation();
                    if (args[2].equalsIgnoreCase("diamond")) {
                        a.addDiamondGen(playerLoc);
                        Bedwars.sendMessage(player, "Added a &bDIAMOND &egenerator at &6x: " + playerLoc.getBlockX() + " y: " + playerLoc.getBlockY() + " z: " + playerLoc.getBlockZ() + "&e to &6" + args[1] + "&e.");
                    } else {
                        a.addEmeraldGen(playerLoc);
                        Bedwars.sendMessage(player, "Added a &aEMERALD &egenerator at &6x: " + playerLoc.getBlockX() + " y: " + playerLoc.getBlockY() + " z: " + playerLoc.getBlockZ() + "&e to &6" + args[1] + "&e.");
                    }
                } else {
                    Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa addgen <name> <diamond/emerald>");
                }
            } else {

                Bedwars.sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa addgen <name> <diamond/emerald>");
        }
    }
}
