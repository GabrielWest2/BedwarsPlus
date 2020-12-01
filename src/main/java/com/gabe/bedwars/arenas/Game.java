package com.gabe.bedwars.arenas;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.GameState;
import com.gabe.bedwars.ScoreboardFactoryUtils;
import com.gabe.bedwars.managers.GameBlockManager;
import com.gabe.bedwars.managers.GameStatsManager;
import com.gabe.bedwars.managers.NameManager;
import com.gabe.bedwars.managers.TeamUpgradesManager;
import com.gabe.bedwars.team.GameTeam;
import com.gabe.bedwars.team.Team;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private final GameBlockManager blockManager;
    private final GameStatsManager statsManager;
    private final TeamUpgradesManager upgradesManager;
    private final NameManager nameManager;
    private boolean hasPlayers = false;
    private final Arena arena;
    private final Bedwars plugin;
    private int timer = -1;
    private Set<Player> players;
    private Set<GameTeam> teams;
    private GameState state;

    public Game(Arena arena, Bedwars plugin) {
        blockManager = new GameBlockManager();
        statsManager = new GameStatsManager();
        upgradesManager = new TeamUpgradesManager();
        nameManager = new NameManager();
        state = GameState.WAITING;
        this.arena = arena;
        this.plugin = plugin;
        this.players = new HashSet<>();
        this.teams = new HashSet<>();
        for (Team t : arena.getTeams()) {
            teams.add(new GameTeam(t.getName(), t.getColor(), t.getSpawn(), t.getGenerator(), t.getBed()));
        }
        //Start coroutines
        foodCheck();
        //Start generators
        teamGens();
        emeraldGens();
        diamondGens();
        //Start upgrade checks
        teamSharp();
        teamProt();
        teamHaste();
        //Boss bar
        bossBar(Bedwars.bossBar, BarColor.YELLOW);
    }

    /* ------------ GETTERS ------------- */

    public TeamUpgradesManager getUpgradesManager() {
        return upgradesManager;
    }

    public Arena getArena() {
        return arena;
    }

    public GameBlockManager getBlockManager() {
        return blockManager;
    }

    public GameStatsManager getStatsManager() {
        return statsManager;
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public String getName() {
        return arena.getName();
    }

    public Set<GameTeam> getTeams() {
        return Collections.unmodifiableSet(teams);
    }

    public GameTeam getTeam(Player player) {
        for (GameTeam team : teams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public GameState getState() {
        return state;
    }

    public int getCountDown() {
        return timer;
    }

    public Bedwars getPlugin() {
        return plugin;
    }

    /* ---------- COROUTINES ----------- */

    public void bossBar(List<String> titles, BarColor color) {
        new BukkitRunnable() {
            BossBar bossBar = Bukkit.createBossBar(titles.get(0), color, BarStyle.SOLID, BarFlag.CREATE_FOG);
            int i = 1;
            @Override
            public void run() {

                if(getState() == GameState.WAITING){
                    for(Player player : players){
                        bossBar.addPlayer(player);
                        bossBar.setTitle(ChatColor.translateAlternateColorCodes('&', titles.get(i)));
                        i++;
                        if (i >= titles.size()){
                            i = 0;
                        }
                    }
                }else{
                    bossBar.setVisible(false);
                    bossBar.removeAll();
                }
            }
        }.runTaskTimer(plugin, 0L,  Bedwars.bossbarDelay);
    }

    public void emeraldGens() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (Location loc : arena.getEmeraldGens()) {
                        loc.getWorld().dropItem(loc.clone().add(0, 0.4, 0), new ItemStack(Material.EMERALD, 1));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 60 * 20L);
    }

    public void diamondGens() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (Location loc : arena.getDiamondGens()) {
                        loc.getWorld().dropItem(loc.clone().add(0, 0.4, 0), new ItemStack(Material.DIAMOND, 1));
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20 * 20L);
    }

    public void teamGens() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (GameTeam team : teams) {
                        team.getGenerator().getWorld().dropItem(team.getGenerator().clone().add(0, 0.4, 0), new ItemStack(Material.IRON_INGOT, 1 + upgradesManager.getUpgrades(team).getExtaIronAmount()));
                        double rand = ThreadLocalRandom.current().nextDouble();
                        if (rand < upgradesManager.getUpgrades(team).getGoldPercent()) {
                            team.getGenerator().getWorld().dropItem(team.getGenerator().clone().add(0, 0.4, 0), new ItemStack(Material.GOLD_INGOT, 1));
                        }
                        if (rand < upgradesManager.getUpgrades(team).getEmeraldChance()) {
                            team.getGenerator().getWorld().dropItem(team.getGenerator().clone().add(0, 0.4, 0), new ItemStack(Material.EMERALD, 1));
                        }
                    }
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin, 0L, 1 * 20L);
    }

    public void teamProt() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (GameTeam team : teams) {
                        for (Player player : team.getPlayers()) {
                            if (upgradesManager.getUpgrades(team).getProtLevel() != 0) {
                                for (ItemStack item : player.getEquipment().getArmorContents()) {
                                    if (item == null) {
                                        continue;
                                    }
                                    if (!item.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                                        item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradesManager.getUpgrades(team).getProtLevel());
                                    } else {
                                        if (item.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) != upgradesManager.getUpgrades(team).getProtLevel()) {
                                            item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradesManager.getUpgrades(team).getProtLevel());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1 * 20L);
    }

    public void teamSharp() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (GameTeam team : teams) {
                        for (Player player : team.getPlayers()) {
                            if (upgradesManager.getUpgrades(team).hasSharp()) {
                                for (ItemStack item : player.getInventory()) {
                                    if (item != null) {
                                        if (item.getType().toString().contains("SWORD") || item.getType().toString().contains("_AXE")) {
                                            if (!item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) {
                                                item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1 * 20L);
    }

    public void teamHaste() {
        new BukkitRunnable() {
            public void run() {
                if (state == GameState.PLAYING) {
                    for (GameTeam team : teams) {
                        for (Player player : team.getPlayers()) {

                            if (upgradesManager.getUpgrades(team).getManiacMiner() != 0) {
                                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 16*20, upgradesManager.getUpgrades(team).getManiacMiner()));
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1 * 20L);
    }

    public void foodCheck() {
        new BukkitRunnable() {
            public void run() {
                for (Player player : players) {
                    player.setFoodLevel(20);
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void startCountDown() {
        new BukkitRunnable() {
            public void run() {
                updateScoreboards();
                if (hasPlayers) {
                    if (timer == -1) {
                        timer = 20;
                    } else if (timer == 0) {
                        timer = -1;
                        startGame();
                        cancel();
                    } else {
                        timer--;

                    }
                    if (timer != -1) {
                        for (Player player : players) {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                        }
                        String color;
                        if (timer % 5 == 0 && timer > 10) {
                            color = "&a";
                            broadcastTitle(color + timer);

                        } else if (timer < 11 && timer >= 6) {
                            color = "&e";
                            broadcastTitle(color + timer);

                        } else if (timer < 6) {
                            color = "&c";
                            broadcastTitle(color + timer);

                        }

                    }
                } else {
                    broadcast("&cCanceling game! Not enough players. (" + players.size() + "/" + arena.getMinPlayers() + ")");
                    timer = -1;
                    updateScoreboards();
                    cancel();
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /* ----------- PLAYERS ------------ */

    public void playerDied(Player player, String message) {
        broadcast(player.getName() + " " + message + ". " + (getTeam(player).hasBed() ? "" : "&f&lFINAL KILL"));
        if (!getTeam(player).hasBed()) {
            player.setGameMode(GameMode.SPECTATOR);
            getTeam(player).playerDied(player);
        }
        player.teleport(getTeam(player).getSpawn());

        if (!getTeam(player).hasBed()) {
            getTeam(player).died(player);
        } else {
            player.teleport(getTeam(player).getSpawn());
        }
    }

    public void playerBrokeBed(Player player, GameTeam team) {
        team.setHasBed(false);
        statsManager.playerBrokeBed(player);
        broadcastWithoutPrefix("&f&lBED DESTRUCTION > " + team.getColor() + team.getName() + "'s &cbed was destroyed by " + player.getName() + "!");
        for (Player p : team.getPlayers()){
            p.sendTitle(ChatColor.translateAlternateColorCodes('&', "&cBED DESTROYED"), "", 1,3,1);
            p.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 1);
        }
        updateScoreboards();
    }

    public void addPlayer(Player player) {
        if (getState() == GameState.WAITING) {
            if (players.size() < arena.getMaxPlayers()) {
                nameManager.addPlayer(player);
                players.add(player);
                statsManager.addPlayer(player);
                player.getEnderChest().setContents(new ItemStack[0]);
                player.getInventory().clear();
                player.getInventory().setItem(9, getLeaveItem());
                updateScoreboards();

                if(arena.getLobbyLocation() != null) {
                    player.teleport(arena.getLobbyLocation());
                }else{
                    Bedwars.sendMessage(player, "&cNo lobby has been set!");
                }
                broadcast(player.getName() + " has joined the game! &6(" + players.size() + "/" + arena.getMinPlayers() + ")");
                if (players.size() >= arena.getMinPlayers()) {
                    hasPlayers = true;
                    if (timer == -1) {
                        startCountDown();
                    }
                }
            }
        } else {
            Bedwars.sendMessage(player, "&cYou can not join this arena at this time.");
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        for (GameTeam t : teams) {
            if (t.getPlayers().contains(player)) {
                t.removePlayer(player);
            }
        }
        if (players.size() < arena.getMinPlayers()) {
            hasPlayers = false;
        }
        statsManager.removePlayer(player);
        player.teleport(arena.getMainLobbyLocation());
        broadcast(player.getName() + " has left the game! &6(" + players.size() + "/" + arena.getMinPlayers() + ")");
        updateScoreboards();
        nameManager.restoreName(player);
        player.getInventory().clear();
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    /* ------------ MISC ------------- */
    public ItemStack getLeaveItem(){
        ItemStack item = new ItemStack(Material.RED_BED);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&c&lReturn to Lobby &7(Right Click)"));
        im.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&',"&7Right-click to return to the lobby.")));
        return item;
    }

    public void broadcast(String msg) {
        String message = Bedwars.prefix + msg;
        for (Player p : players) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void broadcastWithoutPrefix(String msg) {
        String message = msg;
        for (Player p : players) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void broadcastTitle(String title) {
        for (Player p : players) {
            p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), "", 1, 20, 1);
        }
    }

    private void startGame() {
        state = GameState.PLAYING;
        List<Player> playersToAdd = new ArrayList<>();
        playersToAdd.addAll(players);

        while (playersToAdd.size() > 0) {
            for (GameTeam team : teams) {
                if (playersToAdd.size() != 0) {
                    team.addPlayer(playersToAdd.get(0));

                    playersToAdd.remove(0);
                } else {
                    break;
                }
            }
        }
        for(Player player : players){
            player.getInventory().clear();
        }
        for (GameTeam team : teams) {
            upgradesManager.addTeam(team);
            team.teleportPlayers();

        }
        updateScoreboards();
        equipArmor();
    }

    public void updateScoreboards() {
        for (Player player : players) {
            player.setScoreboard(ScoreboardFactoryUtils.makeBoard(player, state, this));
        }
    }

    public void equipArmor(){
        for(GameTeam team : teams){
            ItemStack helm = new ItemStack(Material.LEATHER_HELMET);
            LeatherArmorMeta helmM = (LeatherArmorMeta) helm.getItemMeta();
            helmM.setColor(Bedwars.translateChatColorToColor(team.getColor()));
            helm.setItemMeta(helmM);

            ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestM = (LeatherArmorMeta) chest.getItemMeta();
            chestM.setColor(Bedwars.translateChatColorToColor(team.getColor()));
            chest.setItemMeta(chestM);

            ItemStack leg = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta legM = (LeatherArmorMeta)  leg.getItemMeta();
            legM.setColor(Bedwars.translateChatColorToColor(team.getColor()));
            leg.setItemMeta(legM);

            ItemStack boot = new ItemStack(Material.LEATHER_BOOTS);
            LeatherArmorMeta bootM = (LeatherArmorMeta)  boot.getItemMeta();
            bootM.setColor(Bedwars.translateChatColorToColor(team.getColor()));
            boot.setItemMeta(bootM);

            for (Player player : team.getPlayers()){
                player.getEquipment().setHelmet(helm);
                player.getEquipment().setChestplate(chest);
                player.getEquipment().setLeggings(leg);
                player.getEquipment().setBoots(boot);
            }
        }
    }
}
