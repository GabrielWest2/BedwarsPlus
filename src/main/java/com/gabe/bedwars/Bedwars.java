package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.commands.admin.*;
import com.gabe.bedwars.listeners.GameListener;
import com.gabe.bedwars.listeners.ShopListener;
import com.gabe.bedwars.listeners.UpgradeListener;
import com.gabe.bedwars.managers.ArenaManager;
import com.gabe.bedwars.managers.GameManager;
import com.gabe.bedwars.shop.ShopCreator;
import com.gabe.bedwars.stats.StatsManager;
import com.gabe.bedwars.tabcomplete.AdminTabComplete;
import com.gabe.bedwars.upgrade.UpgradeCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public final class Bedwars extends JavaPlugin {

    /* ---------- MANAGERS ----------- */
    private static ArenaManager arenaManager;
    private static GameManager gameManager;
    public static ShopCreator shopCreator;
    public static UpgradeCreator upgradeCreator;
    private static StatsManager statsManager;
    /* ---------- DEFAULT CONFIG VALUES ----------- */
    public static String serverText = "yourserver.net";
    public static String prefix = "&8[&6BW&8] &8> &e";
    public static List<String> bossBar = Arrays.asList("&e&lPlaying &f&lBEDWARS &e&lon &a&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &b&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &6&lYOURSERVER.NET");
    public static long bossbarDelay = 20;

    private YamlConfiguration config;
    private String help = "\n&8*------------------------------------* \n" +
            "&cBedwarsPlus Admin Help: &6v" + this.getDescription().getVersion() + "\n" +
            "&e/bwa &6create <name> <minplayers> <maxplayers>\n" +
            "&e/bwa &6debug\n" + "&e/bwa &6setlobby <name>\n" +
            "&e/bwa &6setmainlobby <name>\n" +
            "&e/bwa &6addteam <name> <team name> <color>\n" +
            "&e/bwa &6setspawn <name> <team>\n" +
            "&e/bwa &6setteamgen <name> <team>\n" +
            "&e/bwa &6setbed <name> <team>\n" +
            "&e/bwa &6addgen <name> <diamond/emerald>\n" +
            "&e/bwa &6additemshop\n" +
            "&e/bwa &6addteamshop\n" +
            "&e/bwa &6save <name>\n" +
            "&8*------------------------------------* \n";

    /* ---------- GETTERS ----------- */
    public static UpgradeCreator getUpgradeCreator() {
        return upgradeCreator;
    }

    public static ShopCreator getShopCreator() {
        return shopCreator;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static StatsManager getStatsManager() {return statsManager; }

    /* ---------- OVERRIDE ----------- */
    public void onEnable() {
        while (!this.isEnabled()){

        }
        saveConfig();
        getCommand("bwa").setTabCompleter(new AdminTabComplete());
        arenaManager = new ArenaManager(this);
        arenaManager.deserialize();
        gameManager = new GameManager(this);
        statsManager = new StatsManager(this);
        shopCreator = new ShopCreator();
        upgradeCreator = new UpgradeCreator();
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpgradeListener(), this);
    }

    public void onDisable() {
        //arenaManager.serialise();
        if (gameManager != null) {
            for (Game game : gameManager.getGameList()) {
                if (gameManager.getGameList() != null) {
                    if (game != null) {
                        game.reset();
                    }
                }
            }
        }
    }

    /* ---------- CONFIG ----------- */
    public void saveConfig() {
        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {

                file.createNewFile();


                config = YamlConfiguration.loadConfiguration(file);
                config.options().copyDefaults(true);


                config.set("servername", "mc.server.net");
                config.set("prefix", "&8[&6BW&8] &8> &e");
                config.set("bossbardelay", 60);
                config.set("bossbar", Arrays.asList("&e&lPlaying &f&lBEDWARS &e&lon &a&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &b&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &6&lYOURSERVER.NET"));

                try {
                    config.save(file);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            config = YamlConfiguration.loadConfiguration(file);
            loadValues();
        }
    }

    private void loadValues() {
        serverText = config.getString("servername");
        bossbarDelay = config.getInt("bossbardelay");
        bossBar = config.getStringList("bossbar");
        prefix = config.getString("prefix");
    }

    /* ---------- COMMANDS ----------- */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("bwa")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("help")) {
                        if(player.hasPermission("bedwarsplus.admin.help")) {
                            sendMessage(player, false, help);
                        }else {
                            sendMessage(player, "&cYou do not have permission!");
                        }

                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (player.hasPermission("bedwarsplus.admin.create")) {
                            CreateCommand cmd = new CreateCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("debug")) {
                        if (player.hasPermission("bedwarsplus.admin.debug")) {
                            DebugCommand cmd = new DebugCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addteam")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            AddTeamCommand cmd = new AddTeamCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("save")) {
                        if (player.hasPermission("bedwarsplus.admin.save")) {
                            SaveCommand cmd = new SaveCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setlobby")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetLobbyCommand cmd = new SetLobbyCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setmainlobby")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetMainLobbyCommand cmd = new SetMainLobbyCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addgen")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            AddGenCommand cmd = new AddGenCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setteamgen")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetTeamGenCommand cmd = new SetTeamGenCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setspawn")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetSpawnCommand cmd = new SetSpawnCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setbed")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetBedCommand cmd = new SetBedCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("additemshop")) {
                        if (player.hasPermission("bedwarsplus.admin.shop")) {
                            AddItemShopCommand cmd = new AddItemShopCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addteamshop")) {
                        if (player.hasPermission("bedwarsplus.admin.shop")) {
                            AddTeamShopCommand cmd = new AddTeamShopCommand(player, args);
                        } else {
                            sendMessage(player, "&cYou do not have permission!");
                        }
                    } else {
                        sendMessage(player, "&cThat is not a command. Try /bwa help.");
                    }
                } else {
                    sendMessage(player, false, help);
                }
            }
            if (label.equalsIgnoreCase("bw")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("join")) {
                        statsManager.createFile(player);
                        if (player.hasPermission("bedwarspro.player.join")) {
                            if (args.length > 1) {
                                if (gameManager.getGame(args[1]) != null) {
                                    if (gameManager.getGame(args[1]).getPlayers().contains(player)) {
                                        sendMessage(player, "&cYou are already in this game!");
                                        return true;
                                    }
                                    sendMessage(player, "Joined arena named &6" + args[1] + "&e.");
                                    gameManager.getGame(args[1]).addPlayer(player);

                                } else {
                                    sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exists.");
                                }
                            } else {
                                sendMessage(player, "&cIncorrect Usage. Do /bw join <name> ");
                            }
                        } else {
                            player.sendMessage("&cYou do not have permission to do that.");
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (player.hasPermission("bedwarspro.player.leave")) {
                            if (gameManager.getGame(player) != null) {
                                sendMessage(player, "Left arena " + gameManager.getGame(player).getName() + "!");
                                gameManager.getGame(player).removePlayer(player);

                            } else {
                                sendMessage(player, "&cYou are not in a game!");
                            }
                        } else {
                            player.sendMessage("&cYou do not have permission to do that.");
                        }
                    } else if (args[0].equalsIgnoreCase("play")) {
                        if (player.hasPermission("bedwarspro.player.play")) {
                            if (gameManager.getGame(player) == null) {
                                sendMessage(player, "&cYou are currently in a game!");
                                return true;
                            }
                            for (Game game : gameManager.getGameList()) {
                                if (game.getState() == GameState.WAITING) {
                                    game.addPlayer(player);
                                    return true;
                                }
                            }
                            sendMessage(player, "&cSorry, there are no available games at this time.");
                        } else {
                            player.sendMessage("&cYou do not have permission to do that.");
                        }
                    } else {
                        sendMessage(player, "&cInvalid command. Try /bw help.");
                    }
                }

            }
        } else {
            sender.sendMessage("This command can only be executed by a player!");
        }
        return true;
    }

    /* ---------- UTILS ----------- */
    public static boolean isColor(String string) {
        for (ChatColor c : ChatColor.values()) {
            if (c.name().equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    public static void sendMessage(Player player, String msg) {
        String message = prefix + msg;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(Player player, boolean usePrefix, String msg) {
        if (usePrefix) {
            String message = prefix + msg;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public static Color translateChatColorToColor(ChatColor chatColor) {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DARK_AQUA:
                return Color.BLUE;
            case DARK_BLUE:
                return Color.BLUE;
            case DARK_GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_PURPLE:
                return Color.PURPLE;
            case DARK_RED:
                return Color.RED;
            case GOLD:
                return Color.YELLOW;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIGHT_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
            default:
                break;
        }

        return null;
    }
}
