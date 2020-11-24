package com.gabe.bedwars.listeners;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.GameManager;
import com.gabe.bedwars.GameState;
import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListener implements Listener {
    private final GameManager gameManager;

    public GameListener(Bedwars plugin) {
        this.gameManager = Bedwars.getGameManager();
    }

    private boolean isClose(Location loc1, Location loc2){
        if(loc1.getWorld() != loc2.getWorld()){
            return false;
        }

        if(!(loc1.getBlockX() == loc2.getBlockX() || loc1.getBlockX() == loc2.getBlockX() + 1 || loc1.getBlockX() == loc2.getBlockX() - 1 )){
            return false;
        }

        if(!(loc1.getBlockY() == loc2.getBlockY() || loc1.getBlockY() == loc2.getBlockY() + 1 || loc1.getBlockY() == loc2.getBlockY() - 1 )){
            return false;
        }

        if(!(loc1.getBlockZ() == loc2.getBlockZ() || loc1.getBlockZ() == loc2.getBlockZ() + 1 || loc1.getBlockZ() == loc2.getBlockZ() - 1 )){
            return false;
        }

        return true;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        if(game == null) return;
        game.removePlayer(player);
        Bukkit.getLogger().info("player left");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        if(game != null){
            if(game.getState() != GameState.PLAYING){
                Bedwars.sendMessage(player,"&cYou cant break blocks at this time!");
                event.setCancelled(true);
                return;
            }
            if(event.getBlock().getType().name().contains("BED")){
                event.setCancelled(true);
                Location location = event.getBlock().getLocation();
                Bukkit.getLogger().info(player.getName()+" broke bed");
                for(GameTeam team : game.getTeams()){
                    if(team.getBed() != null){
                        if(isClose(team.getBed(), location)) {
                            Bukkit.getLogger().info(team.getName()+" got broken bed");
                            if (!team.getPlayers().contains(player)){
                                if(!team.hasBed()){
                                    Bedwars.sendMessage(player, "&cThis bed has already been destroyed!");
                                    return;
                                }
                                game.playerBrokeBed(player, team);
                            }else{
                                Bedwars.sendMessage(player, "&cYou cant break your own bed!");
                            }
                        }
                    }
                }
                return;
            }
            if(game.getBlockManager().canBreak(event.getBlock().getLocation())){
                game.getBlockManager().brokeBlock(event.getBlock().getLocation());
            }else{
                Bedwars.sendMessage(player,"&cYou cant break that block!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        if(game != null){
            if(game.getState() != GameState.PLAYING){
                event.setCancelled(true);
                Bedwars.sendMessage(player,"&cYou cant place blocks at this time!");
                return;
            }
          game.getBlockManager().placedBlock(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = (Player) event.getEntity();

        Game game = gameManager.getGame(player);
        if(game != null){
            if(game.getState() == GameState.PLAYING){
                event.setCancelled(true);
                player.teleport(game.getTeam(player).getSpawn());
                if(player.getKiller() != null){
                    Player attacker = player.getKiller();
                    game.playerDied(event.getEntity(), "was murdered by "+attacker.getName());
                    Bedwars.sendMessage(attacker,"&aYou killed "+event.getEntity().getName()+"!");
                    if(game.getTeam(player).hasBed()){
                        game.getStatsManager().playerGotKill(attacker);
                    }else {
                        game.getStatsManager().playerGotFinalKill(attacker);
                    }

                }else{
                    game.playerDied(event.getEntity(), "died");
                }
                game.updateScoreboards();
                return;
            }else{
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent event){
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        event.setCancelled(true);
        if(game != null){
            Bedwars.sendMessage(player, "You can not sleep in the beds!");
        }
    }
}
