package com.gabe.bedwars.commands.admin;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.DebugUtils;
import org.bukkit.entity.Player;

public class DebugCommand {
    public DebugCommand(Player player, String[] args){
        if (Bedwars.getArenaManager().getArenaList().size() > 0) {
            Bedwars.sendMessage(player, DebugUtils.generateDebug(Bedwars.getArenaManager()));
        } else {
            Bedwars.sendMessage(player, "There are no arenas to debug!");
        }
    }
}
