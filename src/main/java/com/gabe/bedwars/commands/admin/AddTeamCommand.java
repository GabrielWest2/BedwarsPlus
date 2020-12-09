package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.team.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AddTeamCommand {

    public AddTeamCommand(Player player, String[] args) {
        if (args.length > 3) {
            if (Bedwars.getArenaManager().getArena(args[1]) != null) {
                Arena a = Bedwars.getArenaManager().getArena(args[1]);
                for (Team t : a.getTeams()) {
                    if (t.getName().equalsIgnoreCase(args[2])) {
                        Bedwars.sendMessage(player, "There is already a team named &6" + args[2] + "&e.");
                        return;
                    }
                }

                if (Bedwars.isColor(args[3])) {
                    ChatColor color = ChatColor.valueOf(args[3].toUpperCase());
                    Team t = new Team(args[2], color);
                    a.addTeam(t);
                    Bedwars.sendMessage(player, "Created team named " + t.getColor() + args[2] + "&e.");
                } else {
                    Bedwars.sendMessage(player, "Invalid color: &6" + args[3] + "&e.");
                }

            } else {
                Bedwars.sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist!");
            }
        } else {
            Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bwa addteam <name> <team name> <color>");
        }
    }
}
