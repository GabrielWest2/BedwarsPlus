package com.gabe.bedwars.managers;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.errors.InvalidConfigurationLocation;
import com.gabe.bedwars.errors.InvalidConfigurationTeam;
import com.gabe.bedwars.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaManager {

    private final Set<Arena> arenaSet;
    File arenaYml;
    FileConfiguration arenaData;

    public ArenaManager(Bedwars plugin) {
        FileConfiguration config = plugin.getConfig();
        this.arenaSet = new HashSet<>();
        arenaYml = new File(plugin.getDataFolder() + "/arenas.yml");
        arenaData = YamlConfiguration.loadConfiguration(arenaYml);
    }


    public void deserialize() {
        //Get all arenas in the arenas.yml file
        List<String> arenas = new ArrayList<String>(arenaData.getKeys(false));
        //Loop through them
        for (String a : arenas) {
            //Print the name of the arena
            Bukkit.getLogger().info("[BW] Loading arena " + a);
            //Get the min and max players from config
            int min = arenaData.getInt(a + ".minplayers");
            int max = arenaData.getInt(a + ".maxplayers");
            //Initialize the lobby and main lobby locations;
            Location ml = null;
            Location l = null;
            //Try to get those locations from the file
            try {
                ml = deserializeLoc(arenaData.getString(a + ".mainlobby"));
                l = deserializeLoc(arenaData.getString(a + ".lobby"));
            } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                //Print the stack trace
                invalidConfigurationLocation.printStackTrace();
            }


            //Create the arena that was loaded from the config
            Arena arena = new Arena(a, min, max);

            List<String> diamondStr = arenaData.getStringList(a + ".diamond-gens");
            for (String s : diamondStr) {
                Bukkit.getLogger().info("[BW] Discovered diamond gen at " + s + " for arena " + a);
                try {
                    arena.addDiamondGen(deserializeLoc(s));
                } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                    invalidConfigurationLocation.printStackTrace();
                }
            }
            List<String> emeraldStr = arenaData.getStringList(a + ".emerald-gens");
            for (String s : emeraldStr) {
                Bukkit.getLogger().info("[BW] Discovered emerald gen at " + s + " for arena " + a);
                try {
                    arena.addEmeraldGen(deserializeLoc(s));
                } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                    invalidConfigurationLocation.printStackTrace();
                }
            }
            List<String> teamStr = arenaData.getStringList(a + ".teams");
            for (String s : teamStr) {
                Bukkit.getLogger().info("[BW] Discovered team " + s + " for arena " + a);
                try {
                    arena.addTeam(deserializeTeam(s));
                } catch (InvalidConfigurationTeam invalidConfigurationTeam) {
                    invalidConfigurationTeam.printStackTrace();
                }
            }
            //Load the lobby locations
            if (l != null) {
                arena.setLobbyLocation(l);
            }
            if (ml != null) {
                arena.setMainLobbyLocation(ml);
            }
            //Add the
            arenaSet.add(arena);
        }
    }

    private String serialiseTeam(Team team) {
        String str = team.getColor() + "," + team.getName() + "," + serialiseLoc(team.getSpawn()) + "," + serialiseLoc(team.getGenerator()) + "," + serialiseLoc(team.getBed());
        return str;
    }

    public ChatColor getColor(String string) {
        for (ChatColor s : ChatColor.values()) {
            if (s.toString().equals(string)) {
                return s;
            }
        }
        return null;
    }

    private Team deserializeTeam(String str) throws InvalidConfigurationTeam {
        String[] split = str.split(",");
        if (split.length == 5) {
            Team team = new Team(split[1], getColor(split[0]));

            if (!split[2].equals("null")) {
                try {
                    team.setSpawn(deserializeLoc(split[2]));
                } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                    invalidConfigurationLocation.printStackTrace();
                }
            }

            if (!split[3].equals("null")) {
                try {
                    team.setGenerator(deserializeLoc(split[3]));
                } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                    invalidConfigurationLocation.printStackTrace();
                }
            }
            if (!split[4].equals("null")) {
                try {
                    team.setBed(deserializeLoc(split[4]));
                } catch (InvalidConfigurationLocation invalidConfigurationLocation) {
                    invalidConfigurationLocation.printStackTrace();
                }
            }

            return team;
        } else {
            Bukkit.getLogger().info(String.valueOf(split.length));
            throw new InvalidConfigurationTeam("Invalid serialised team!");
        }
    }

    private String serialiseLoc(Location location) {
        if (location != null) {
            String str = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();
            return str;
        } else {
            return null;
        }
    }

    private Location deserializeLoc(String str) throws InvalidConfigurationLocation {
        String[] split = str.split(";");
        if (split.length == 4) {
            Location location = new Location(Bukkit.getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
            return location;
        } else {
            throw new InvalidConfigurationLocation("Invalid serialised location!");
        }
    }

    public void serialise(Arena arena) {

        arenaData.set(arena.getName() + ".minplayers", arena.getMinPlayers());
        arenaData.set(arena.getName() + ".maxplayers", arena.getMaxPlayers());

        String lobby = serialiseLoc(arena.getLobbyLocation());
        String mainLobby = serialiseLoc(arena.getMainLobbyLocation());
        arenaData.set(arena.getName() + ".lobby", lobby);
        arenaData.set(arena.getName() + ".mainlobby", mainLobby);
        List<String> teams = new ArrayList<>();
        for (Team t : arena.getTeams()) {
            teams.add(serialiseTeam(t));
        }
        arenaData.set(arena.getName() + ".teams", teams);
        List<String> diamondGens = new ArrayList<>();
        for (Location l : arena.getDiamondGens()) {
            diamondGens.add(serialiseLoc(l));
        }
        List<String> emeraldGens = new ArrayList<>();
        for (Location l : arena.getEmeraldGens()) {
            emeraldGens.add(serialiseLoc(l));
        }
        arenaData.set(arena.getName() + ".diamond-gens", diamondGens);
        arenaData.set(arena.getName() + ".emerald-gens", emeraldGens);
        saveCustomYml(arenaData, arenaYml);
    }

    public void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
        try {
            ymlConfig.save(ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Arena getArena(Player player) {
        return arenaSet.stream().filter(arena -> arena.getPlayers().contains(player)).findFirst().orElse(null);
    }

    public Arena getArena(String key) {
        return arenaSet.stream().filter(arena -> arena.getName().equals(key)).findFirst().orElse(null);
    }

    public Set<Arena> getArenaList() {
        return Collections.unmodifiableSet(arenaSet);
    }

    public void addArena(Arena arena) {
        this.arenaSet.add(arena);
    }

    public void removeArena(Arena arena) {
        this.arenaSet.remove(arena);
    }
}