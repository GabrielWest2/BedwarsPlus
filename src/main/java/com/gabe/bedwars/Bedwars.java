package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.commands.admin.*;
import com.gabe.bedwars.listeners.GameListener;
import com.gabe.bedwars.listeners.ShopListener;
import com.gabe.bedwars.listeners.UpgradeListener;
import com.gabe.bedwars.managers.ArenaManager;
import com.gabe.bedwars.managers.GameManager;
import com.gabe.bedwars.shop.ShopCreator;
import com.gabe.bedwars.stats.PlayerStats;
import com.gabe.bedwars.stats.StatsManager;
import com.gabe.bedwars.tabcomplete.AdminTabComplete;
import com.gabe.bedwars.upgrade.UpgradeCreator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public final class Bedwars extends JavaPlugin {

    /* ---------- MANAGERS ----------- */
    private static ArenaManager arenaManager;
    private static GameManager gameManager;
    public static ShopCreator shopCreator;
    public static UpgradeCreator upgradeCreator;
    private static StatsManager statsManager;
    /* ---------- DEFAULT CONFIG VALUES ----------- */
    public static String primary = "&e";
    public static String secondary = "&6";
    public static String scoreboardPrimary = "&f";
    public static String scoreboardSecondary = "&a";
    public static String success = "&a";
    public static String error = "&c";
    public static String serverText = "yourserver.net";
    public static String prefix = "&8[&6BW&8] &8> &e";
    public static HashMap<Integer, String> gamemodes;
    public static List<String> bossBar = Arrays.asList("&e&lPlaying &f&lBEDWARS &e&lon &a&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &b&lYOURSERVER.NET", "&e&lPlaying &f&lBEDWARS &e&lon &6&lYOURSERVER.NET");
    public static long bossbarDelay = 20;

    private YamlConfiguration config;


    private String help1;
    private String help2;
    private TextComponent arrow;

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

    public static StatsManager getStatsManager() {
        return statsManager;
    }

    /* ---------- OVERRIDE ----------- */
    public void onEnable() {
        TextComponent message = new TextComponent(">>");
        message.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        message.setBold(true);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/bwa help 2"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§b§lPage 2 >>")));

        help1 = "\n§8§l§m*---------§r §d§lBedwarsPlus §b§lv" + this.getDescription().getVersion()+ "§r §8§l§m-----------*\n" +
                "§7/bwa §bcreate <name> <minplayers> <maxplayers>\n" +
                "§7/bwa §bdebug\n" +
                "§7/bwa §bsetlobby <name>\n" +
                "§7/bwa §bsetmainlobby <name>\n" +
                "§7/bwa §baddteam <name> <team name> <color>\n" +
                "§7/bwa §bsetspawn <name> <team>\n" +
                "§7/bwa §bsetteamgen <name> <team>\n" +
                "§7/bwa §bsetbed <name> <team>\n" +
                "§7/bwa §baddgen <name> <diamond/emerald>\n" +
                "§7/bwa §badditemshop\n" +
                "§7/bwa §baddteamshop\n" +
                "§7/bwa §bsave <name>\n" +
                "§7/bwa §breload\n" +
                "§8§l§m*-------------§r §d§lPage 1 §b%next_arrow% §8§l§m-------------*";
        help2 = " §8§l§m-------------*\n";
        arrow = message;
        gamemodes = new HashMap<>();
        saveConfig();
        getCommand("bwa").setTabCompleter(new AdminTabComplete());
        arenaManager = new ArenaManager(this);
        arenaManager.deserialize();
        gameManager = new GameManager(this);
        statsManager = new StatsManager(this);
        shopCreator = new ShopCreator();
        upgradeCreator = new UpgradeCreator();
        statsManager.loadPlayerStats();
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpgradeListener(), this);
    }

    public void onDisable() {
        statsManager.savePlayerStats();
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
                saveDefaultConfig();
                List<List<String>> helps = new ArrayList<>();
                List<String> helptest = new ArrayList<>();
                helptest.add("hi");
                helptest.add("hi1");
                config.set("test", helps);
                saveConfig();
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
            try {
                loadValues();
            } catch (IOException e) {
                Bukkit.getLogger().info(ChatColor.RED+"FAILED TO LOAD CONFIG");
            }

        }
    }

    private void loadValues() throws IOException {
        File configFile = new File(getDataFolder() + "/config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        List<String> strings = config.getStringList("gamemodes");
        config.save(configFile);

        gamemodes = new HashMap<>();
        for(String string : strings){
            if(string.split(":").length == 2){
                int size = Integer.parseInt(string.split(":")[0]);
                String name = string.split(":")[1];
                gamemodes.put(size, name);
            }else{
                throw new IOException();
            }
        }
        serverText = config.getString("servername");
        if(serverText == null)
            throw new IOException();
        bossbarDelay = config.getInt("bossbardelay");
        bossBar = config.getStringList("bossbar");
        prefix = config.getString("prefix");
        if(prefix == null)
            throw new IOException();
        primary = config.getString("primary-color");
        if(primary == null)
            throw new IOException();
        secondary = config.getString("secondary-color");
        if(secondary == null)
            throw new IOException();
        success = config.getString("success-color");
        if(success == null)
            throw new IOException();
        error = config.getString("error-color");
        if(error == null)
            throw new IOException();

        scoreboardPrimary = config.getString("scoreboard-primary");
        if(scoreboardPrimary == null)
            throw new IOException();

        scoreboardSecondary = config.getString("scoreboard-secondary");
        if(scoreboardSecondary == null)
            throw new IOException();

    }

    public void sendHelpMessage(Player player){
        TextComponent helpMenu = null;

        String[] helpParts = help1.split("%next_arrow%");

        if(helpParts.length>1){
            helpMenu = new TextComponent(helpParts[0]);
            for (int i = 0; i< helpParts.length-2; i++){
                helpMenu.addExtra(arrow);
                helpMenu.addExtra(helpParts[i+1]);
            }
        }else{
            helpMenu = new TextComponent(help1);
        }




        player.spigot().sendMessage(helpMenu);
    }

    /* ---------- COMMANDS ----------- */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (label.equalsIgnoreCase("bwa")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("help")) {
                        if (player.hasPermission("bedwarsplus.admin.help")) {
                            sendHelpMessage(player);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }

                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (player.hasPermission("bedwarsplus.admin.reload")) {
                            try {
                                loadValues();
                            } catch (IOException e) {
                                sendRawMessage(player, "&cError during config reload!");
                                return true;
                            }
                            for(Game game : gameManager.getGameList()){
                                game.updateScoreboards();
                            }
                            sendMessage(player, "&aSuccessfully reloaded config!");
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("create")) {
                        if (player.hasPermission("bedwarsplus.admin.create")) {
                            CreateCommand cmd = new CreateCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("debug")) {
                        if (player.hasPermission("bedwarsplus.admin.debug")) {
                            DebugCommand cmd = new DebugCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addteam")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            AddTeamCommand cmd = new AddTeamCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("save")) {
                        if (player.hasPermission("bedwarsplus.admin.save")) {
                            SaveCommand cmd = new SaveCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setlobby")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetLobbyCommand cmd = new SetLobbyCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setmainlobby")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetMainLobbyCommand cmd = new SetMainLobbyCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addgen")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            AddGenCommand cmd = new AddGenCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setteamgen")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetTeamGenCommand cmd = new SetTeamGenCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setspawn")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetSpawnCommand cmd = new SetSpawnCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("setbed")) {
                        if (player.hasPermission("bedwarsplus.admin.configure")) {
                            SetBedCommand cmd = new SetBedCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("additemshop")) {
                        if (player.hasPermission("bedwarsplus.admin.shop")) {
                            AddItemShopCommand cmd = new AddItemShopCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("addteamshop")) {
                        if (player.hasPermission("bedwarsplus.admin.shop")) {
                            AddTeamShopCommand cmd = new AddTeamShopCommand(player, args);
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission!");
                        }
                    } else {
                        Bedwars.sendMessage(player, "&cThat is not a command. Try /bwa help.");
                    }
                } else {
                    sendHelpMessage(player);
                }
            }
            if (label.equalsIgnoreCase("bw")) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("join")) {
                        if (player.hasPermission("bedwarspro.player.join")) {
                            if (args.length > 1) {
                                if (gameManager.getGame(args[1]) != null) {
                                    if (gameManager.getGame(args[1]).getPlayers().contains(player)) {
                                        Bedwars.sendMessage(player, "&cYou are already in this game!");
                                        return true;
                                    }
                                    Bedwars.sendMessage(player, "Joined arena named &6" + args[1] + "&e.");
                                    gameManager.getGame(args[1]).addPlayer(player);

                                } else {
                                    Bedwars.sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exists.");
                                }
                            } else {
                                Bedwars.sendMessage(player, "&cIncorrect Usage. Do /bw join <name> ");
                            }
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission to do that.");
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {
                        if (player.hasPermission("bedwarspro.player.leave")) {
                            if (gameManager.getGame(player) != null) {
                                Bedwars.sendMessage(player, "Left arena " + gameManager.getGame(player).getName() + "!");
                                gameManager.getGame(player).removePlayer(player);

                            } else {
                                Bedwars.sendMessage(player, "&cYou are not in a game!");
                            }
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission to do that.");
                        }
                    } else if (args[0].equalsIgnoreCase("play")) {
                        if (player.hasPermission("bedwarspro.player.play")) {
                            if (gameManager.getGame(player) != null) {
                                Bedwars.sendMessage(player, "&cYou are currently in a game!");
                                return true;
                            }
                            for (Game game : gameManager.getGameList()) {
                                if (game.getState() == GameState.WAITING) {
                                    game.addPlayer(player);
                                    return true;
                                }
                            }
                            Bedwars.sendMessage(player, "&cSorry, there are no available games at this time.");
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission to do that.");
                        }
                    } else if (args[0].equalsIgnoreCase("stats")) {
                        if (player.hasPermission("bedwarspro.player.stats")) {
                            PlayerStats stats = statsManager.getStats(player);
                            Bedwars.sendMessage(player, "\n&8*------------------------------------* \n" +
                                    "&cBedwars Stats:\n" +
                                    "&eGames Played: &6" + stats.getGames() + "\n" +
                                    "&eWins: &6" + stats.getWins() + "\n" +
                                    "&eLosses: &6" + stats.getLosses() + "\n" +
                                    "&eWin Streak: &6" + stats.getWinStreak() + "\n" +
                                    "&eLoss Streak: &6" + stats.getLoseStreak() + "\n" +
                                    "&eKills: &6" + stats.getKills() + "\n" +
                                    "&eFinal Kills: &6" + stats.getFinalKills() + "\n" +
                                    "&eBeds Destroyed: &6" + stats.getBedsBroken() + "\n" +
                                    "&8*------------------------------------* \n");
                        } else {
                            Bedwars.sendMessage(player, "&cYou do not have permission to do that.");
                        }
                    } else {
                        Bedwars.sendMessage(player, "&cInvalid command. Try /bw help.");
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
        msg = "&e" + msg;
        if(primary != null)
            msg = msg.replaceAll("&e", primary);
        if(secondary != null)
            msg = msg.replaceAll("&6", secondary);
        if(success != null)
            msg = msg.replaceAll("&a", success);
        if(error != null)
            msg = msg.replaceAll("&c", error);
        String message = prefix + msg;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendRawMessage(Player player, String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }

    public static void sendMessage(Player player, boolean usePrefix, String msg) {
        if (usePrefix) {
            msg = "&e" + msg;
            if(primary != null)
                msg = msg.replaceAll("&e", primary);
            if(secondary != null)
                msg = msg.replaceAll("&6", secondary);
            if(success != null)
                msg = msg.replaceAll("&a", success);
            if(error != null)
                msg = msg.replaceAll("&c", error);
            String message = prefix + msg;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            msg = "&e" + msg;
            if(primary != null)
                msg = msg.replaceAll("&e", primary);
            if(secondary != null)
                msg = msg.replaceAll("&6", secondary);
            if(success != null)
                msg = msg.replaceAll("&a", success);
            if(error != null)
                msg = msg.replaceAll("&c", error);
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
