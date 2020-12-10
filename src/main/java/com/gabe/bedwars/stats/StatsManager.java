package com.gabe.bedwars.stats;

import com.gabe.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class StatsManager {
    private Bedwars plugin;

    public StatsManager(Bedwars plugin){
        this.plugin = plugin;
    }



    public void createFile(Player player) {
        File dataFolder = new File(plugin.getDataFolder() + File.separator + "PlayerStats");
        try {
            if(!dataFolder.exists()) {
                dataFolder.mkdir();
            }
        }catch(Exception e){
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating data folder!");
        }

        File yml = new File(plugin.getDataFolder(),"PlayerStats" + File.separator + player.getUniqueId()+".yml");
        YamlConfiguration config;
        if (!yml.exists()) {
            try {
                yml.createNewFile();
                config = YamlConfiguration.loadConfiguration(yml);
                config.set("games", 0);
                config.set("wins", 0);
                config.set("losses", 0);
                config.set("winstreak", 0);
                config.set("losestreak", 0);
                config.set("kills", 0);
                config.set("finals", 0);
                config.set("bedsbroken", 0);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(Bedwars.prefix +  ChatColor.RED + "Error creating " + yml.getName() + "!");
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(yml);

        try {
            config.save(yml);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}
