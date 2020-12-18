package com.gabe.bedwars.stats;

import com.gabe.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class StatsManager {
    private Bedwars plugin;
    File dataFolder;
    HashMap<UUID, PlayerStats> playerStats;

    public StatsManager(Bedwars plugin) {
        this.plugin = plugin;
        playerStats = new HashMap<>();
        dataFolder =  new File(plugin.getDataFolder() + File.separator + "PlayerStats");
    }


    public void createFile(Player player) {
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating data folder!");
        }

        File yml = new File(plugin.getDataFolder(), "PlayerStats" + File.separator + player.getUniqueId() + ".yml");
        YamlConfiguration config;
        if (!yml.exists()) {
            try {
                yml.createNewFile();
                config = YamlConfiguration.loadConfiguration(yml);
                config.set("games", 0);
                config.set("wins", 0);
                config.set("losses", 0);
                config.set("winStreak", 0);
                config.set("loseStreak", 0);
                config.set("kills", 0);
                config.set("finalKills", 0);
                config.set("bedsBroken", 0);
                config.save(yml);
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(Bedwars.prefix + ChatColor.RED + "Error creating " + yml.getName() + "!");
                e.printStackTrace();
            }
        }
    }

    private void saveFile(UUID uuid, PlayerStats stat) {
        dataFolder =  new File(plugin.getDataFolder() + File.separator + "PlayerStats");
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating data folder!");
        }
        if(uuid == null)
            return;
        File yml = new File(plugin.getDataFolder(), "PlayerStats" + File.separator + uuid + ".yml");
        YamlConfiguration config;
        if (!yml.exists()) {
            try {
                yml.createNewFile();
            }
            catch (Exception e) {
                    Bukkit.getConsoleSender().sendMessage(Bedwars.prefix + ChatColor.RED + "Error creating " + yml.getName() + "!");
                    e.printStackTrace();
                }
        }
        try {
            yml.createNewFile();
            config = YamlConfiguration.loadConfiguration(yml);
            config.set("games", stat.getGames());
            config.set("wins", stat.getWins());
            config.set("losses", stat.getLosses());
            config.set("winStreak", stat.getWinStreak());
            config.set("loseStreak", stat.getLoseStreak());
            config.set("kills", stat.getKills());
            config.set("finalKills", stat.getFinalKills());
            config.set("bedsBroken", stat.getBedsBroken());
            config.save(yml);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Bedwars.prefix + ChatColor.RED + "Error saving " + yml.getName() + "!");
            e.printStackTrace();
        }
        loadPlayerStats();
    }

    public void loadPlayerStats(){
        if(dataFolder.listFiles() != null) {
            for (File file : dataFolder.listFiles()) {
                String suuid = file.getName().trim().replaceAll(".yml","");
                Bukkit.getLogger().info(suuid);
                UUID uuid = UUID.fromString(suuid);

                PlayerStats stats = new PlayerStats();

                Bukkit.getLogger().info("Loading stats for player: "+Bukkit.getOfflinePlayer(uuid).getName());

                int games = YamlConfiguration.loadConfiguration(file).getInt("games");
                Bukkit.getLogger().info("games: "+games);
                int wins = YamlConfiguration.loadConfiguration(file).getInt("wins");
                Bukkit.getLogger().info("wins: "+wins);
                int losses = YamlConfiguration.loadConfiguration(file).getInt("losses");
                Bukkit.getLogger().info("losses: "+losses);
                int winStreak = YamlConfiguration.loadConfiguration(file).getInt("winStreak");
                Bukkit.getLogger().info("winStreak: "+winStreak);
                int loseStreak = YamlConfiguration.loadConfiguration(file).getInt("loseStreak");
                Bukkit.getLogger().info("loseStreak: "+loseStreak);
                int kills = YamlConfiguration.loadConfiguration(file).getInt("kills");
                Bukkit.getLogger().info("kills: "+kills);
                int finalKills = YamlConfiguration.loadConfiguration(file).getInt("finalKills");
                Bukkit.getLogger().info("finalKills: "+finalKills);
                int bedsBroken = YamlConfiguration.loadConfiguration(file).getInt("bedsBroken");
                Bukkit.getLogger().info("bedsBroken: "+bedsBroken);

                stats.setGames(games);
                stats.setWins(wins);
                stats.setLosses(losses);
                stats.setWinStreak(winStreak);
                stats.setLoseStreak(loseStreak);
                stats.setKills(kills);
                stats.setGames(games);
                stats.setGames(games);

                playerStats.put(uuid, stats);

            }
        }
    }

    public void savePlayerStats(){
        for(UUID uuid : playerStats.keySet()){
            PlayerStats stat = playerStats.get(uuid);
            saveFile(uuid, stat);
        }
    }

    public PlayerStats getStats(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        return playerStats.get(player.getUniqueId());
    }

    public void wonGame(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        PlayerStats stats = playerStats.get(player.getUniqueId());
        stats.setGames(stats.getGames()+1);
        stats.setWins(stats.getWins()+1);
        stats.setWinStreak(stats.getWinStreak()+1);
        stats.setLoseStreak(0);
    }
    public void lostGame(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        PlayerStats stats = playerStats.get(player.getUniqueId());
        stats.setGames(stats.getGames()+1);
        stats.setLosses(stats.getLosses()+1);
        stats.setWinStreak(0);
        stats.setLoseStreak(stats.getLoseStreak()+1);
    }
    public void gotKill(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        PlayerStats stats = playerStats.get(player.getUniqueId());
        stats.setKills(stats.getKills()+1);
    }
    public void gotFinalKill(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        PlayerStats stats = playerStats.get(player.getUniqueId());
        stats.setFinalKills(stats.getFinalKills()+1);
    }
    public void gotBed(Player player){
        if(playerStats.get(player.getUniqueId()) == null){
            playerStats.put(player.getUniqueId(), new PlayerStats());
        }
        PlayerStats stats = playerStats.get(player.getUniqueId());
        stats.setBedsBroken(stats.getBedsBroken()+1);
    }
}
