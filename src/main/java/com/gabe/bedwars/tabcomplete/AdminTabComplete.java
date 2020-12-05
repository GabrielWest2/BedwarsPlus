package com.gabe.bedwars.tabcomplete;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminTabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("bedwars.admin") && args.length==1){
                    List<String> cmds = new ArrayList<>();

                    //Arena commands
                    if("create".startsWith(args[0])){
                        cmds.add("create");
                    }
                    if("setlobby".startsWith(args[0])){
                        cmds.add("setlobby");
                    }
                    if("setmainlobby".startsWith(args[0])){
                        cmds.add("setmainlobby");
                    }
                    if("addgen".startsWith(args[0])){
                        cmds.add("addgen");
                    }

                    //Team commands
                    if("addteam".startsWith(args[0])){
                        cmds.add("addteam");
                    }
                    if("setteamgen".startsWith(args[0])){
                        cmds.add("setteamgen");
                    }
                    if("setspawn".startsWith(args[0])){
                        cmds.add("setspawn");
                    }

                    if("setbed".startsWith(args[0])){
                        cmds.add("setbed");
                    }

                    //Misc. commands
                    if("save".startsWith(args[0])){
                        cmds.add("save");
                    }
                    if("debug".startsWith(args[0])){
                        cmds.add("debug");
                    }
                    return cmds;
            }
            if(player.hasPermission("bedwars.admin") && args.length==2){
                List<String> cmds = new ArrayList<>();

                if(args[0].equalsIgnoreCase("setlobby") || args[0].equalsIgnoreCase("setmainlobby") || args[0].equalsIgnoreCase("addgen") || args[0].equalsIgnoreCase("addteam") || args[0].equalsIgnoreCase("setteamgen") || args[0].equalsIgnoreCase("setspawn") || args[0].equalsIgnoreCase("setbed")){
                    for(Arena a : Bedwars.getArenaManager().getArenaList()){
                        if(a.getName().startsWith(args[1])){
                            cmds.add(a.getName());
                        }
                    }
                }

                return cmds;
            }
        }
        return null;
    }
}
