package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import org.bukkit.entity.Player;

public class CreateCommand {

    public CreateCommand(Player player, String[] args){
        if (args.length > 3) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Bedwars.sendMessage(player, "Arena named &6" + args[1] + "&e already exists.");
            } else {
                try {
                    Integer.parseInt(args[2]);
                }catch (Exception e){
                    Bedwars.sendMessage(player, "Invalid number: &6"+args[2]+"&e.");
                    return;
                }
                try {
                    Integer.parseInt(args[3]);
                }catch (Exception e){
                    Bedwars.sendMessage(player, "Invalid number: &6"+args[3]+"&e.");
                    return;
                }
                Bedwars.getArenaManager().addArena(new Arena(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3])));
                Bedwars.sendMessage(player, "Created arena named &6" + args[1] + "&e and set min players to &6" + args[2] + "&e and max players to &6" + args[3] + "&e.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa create <name> <minplayers> <maxplayers>");
        }
    }
}
