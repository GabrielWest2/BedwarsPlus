package com.gabe.bedwars.arenas;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.GameState;
import com.gabe.bedwars.ScoreboardFactory;
import com.gabe.bedwars.managers.GameBlockManager;
import com.gabe.bedwars.managers.GameStatsManager;
import com.gabe.bedwars.team.GameTeam;
import com.gabe.bedwars.team.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private final Arena arena;
    private final Bedwars plugin;
    private Set<Player> players;
    private Set<GameTeam> teams;
    private GameState state;
    private int timer = -1;
    private boolean hasPlayers = false;
    private final GameBlockManager blockManager;
    private final GameStatsManager statsManager;
    public GameBlockManager getBlockManager(){
        return  blockManager;
    }

    public Game(Arena arena, Bedwars plugin){
        blockManager = new GameBlockManager(plugin);
        statsManager = new GameStatsManager(plugin);
        state = GameState.WAITING;
        this.arena = arena;
        this.plugin = plugin;
        this.players = new HashSet<>();
        this.teams = new HashSet<>();
        for(Team t : arena.getTeams()){
            teams.add(new GameTeam(t.getName(), t.getColor(), t.getSpawn(), t.getGenerator(), t.getBed()));
        }
        //Start coroutines
        foodCheck();
        teamGens();
    }

    /* ------------ GETTERS ------------- */

    public Arena getArena(){
        return arena;
    }

    public GameStatsManager getStatsManager(){
        return statsManager;
    }

    public Set<Player> getPlayers(){
        return Collections.unmodifiableSet(players);
    }

    public String getName(){
        return arena.getName();
    }

    public Set<GameTeam> getTeams(){
        return Collections.unmodifiableSet(teams);
    }

    public GameTeam getTeam(Player player){
        for(GameTeam team : teams){
            if(team.getPlayers().contains(player)) {
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

    public Bedwars getPlugin(){
        return plugin;
    }

    /* ---------- COROUTINES ----------- */

    public void teamGens(){
        new BukkitRunnable()
        {
            public void run()
            {
                if(state == GameState.PLAYING) {
                    for (GameTeam team : teams) {
                        team.getGenerator().getWorld().dropItem(team.getGenerator().clone().add(0,0.4,0), new ItemStack(Material.IRON_INGOT, 1));
                        double rand = ThreadLocalRandom.current().nextDouble();
                        if (rand < 0.12 ) {
                            team.getGenerator().getWorld().dropItem(team.getGenerator().clone().add(0,0.4,0), new ItemStack(Material.GOLD_INGOT, 1));
                        }
                    }
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin,0L,4*20L);
    }

    public void foodCheck(){
        new BukkitRunnable()
        {
            public void run()
            {
                for(Player player : players){
                    player.setFoodLevel(20);
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin,0L,20L);
    }

    public void startCountDown(){
        new BukkitRunnable()
        {
            public void run()
            {
                updateScoreboards();
                if(hasPlayers) {
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
                        for(Player player : players){
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING,1,1);
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
                }else{
                    broadcast("&cCanceling game! Not enough players. ("+players.size()+"/"+arena.getMinPlayers()+")");
                    timer = -1;
                    cancel();
                }

                //Use cancel(); if you want to close this repeating task.
            }
        }.runTaskTimer(plugin,0L,20L);
    }

    /* ----------- PLAYERS ------------ */

    public void playerDied(Player player, String message){
        broadcast(player.getName()+" "+message+". "+(getTeam(player).hasBed() ? "":"&f&lFINAL KILL"));
        if(!getTeam(player).hasBed()){
            player.setGameMode(GameMode.SPECTATOR);
            getTeam(player).playerDied(player);
        }
        player.teleport(getTeam(player).getSpawn());

        if(!getTeam(player).hasBed()){
            getTeam(player).died(player);
        }else{
            player.teleport(getTeam(player).getSpawn());
        }
    }

    public void playerBrokeBed(Player player, GameTeam team){
        team.setHasBed(false);
        statsManager.playerBrokeBed(player);
        broadcastWithoutPrefix("&f&lBED DESTRUCTION > " + team.getColor() + team.getName() + "'s &cbed was destroyed by " + player.getName() + "!");
        updateScoreboards();
    }

    public void addPlayer(Player player){
        if(getState() == GameState.WAITING) {
            if(players.size() < arena.getMaxPlayers()) {
                players.add(player);
                statsManager.addPlayer(player);
                updateScoreboards();

                player.teleport(arena.getLobbyLocation());
                broadcast(player.getName() + " has joined the game! &6(" + players.size() + "/" + arena.getMinPlayers() + ")");
                if (players.size() >= arena.getMinPlayers()){
                    hasPlayers = true;
                    if(timer== -1) {
                        startCountDown();
                    }
                }
            }
        }else{
            Bedwars.sendMessage(player, "&cYou can not join this arena at this time.");
        }
    }

    public void removePlayer(Player player){
        players.remove(player);
        for(GameTeam t : teams){
            if(t.getPlayers().contains(player)){
                t.removePlayer(player);
            }
        }
        if(players.size()<arena.getMinPlayers()){
            hasPlayers = false;
        }
        statsManager.removePlayer(player);
        player.teleport(arena.getMainLobbyLocation());
        broadcast(player.getName()+" has left the game! &6("+players.size()+"/"+arena.getMinPlayers()+")");
        updateScoreboards();
    }

    /* ------------ MISC ------------- */
    public void broadcast(String msg){
        String message = "&8[&6BW&8] &8> &e"+msg;
        for(Player p : players) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void broadcastWithoutPrefix(String msg){
        String message = msg;
        for(Player p : players) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public void broadcastTitle(String title){
        for(Player p : players) {
            p.sendTitle(ChatColor.translateAlternateColorCodes('&', title),"",1,20,1);
        }
    }

    private void startGame(){
        state = GameState.PLAYING;
        List<Player> playersToAdd = new ArrayList<>();
        playersToAdd.addAll(players);
        int teamSize = Math.round(players.size()+0.0f / arena.getTeams().size());

        while(playersToAdd.size() > 0){
            for(GameTeam team : teams){
                if(playersToAdd.size() != 0) {
                    team.addPlayer(playersToAdd.get(0));
                    playersToAdd.remove(0);
                }else {
                    break;
                }
            }
        }
        for(GameTeam team: teams){
            team.teleportPlayers();
        }
        updateScoreboards();

    }

    public void updateScoreboards(){
        for (Player player : players){
            player.setScoreboard(ScoreboardFactory.makeBoard(player, state, this));
        }
    }

}
