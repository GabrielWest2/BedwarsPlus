package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import org.bukkit.entity.Player;

public class SaveCommand {
    public SaveCommand(Player player, String[] args) {
        if (args.length > 1) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Bedwars.getArenaManager().serialise(Bedwars.getArenaManager().getArena(args[1]));
                Bedwars.sendMessage(player, "Saved arena &6" + args[1] + "&e. To play, restart the server then type &6/bw join "+args[1]);
            } else {

                Bedwars.sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa save <name>");
        }
    }
}
