package com.gabe.bedwars.listeners;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.GameManager;
import com.gabe.bedwars.GameState;
import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.shop.ShopPage;
import com.gabe.bedwars.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GameListener implements Listener {
    private final GameManager gameManager;

    public GameListener() {
        this.gameManager = Bedwars.getGameManager();
    }

    private boolean isClose(Location loc1, Location loc2) {
        if (loc1.getWorld() != loc2.getWorld()) {
            return false;
        }

        if (!(loc1.getBlockX() == loc2.getBlockX() || loc1.getBlockX() == loc2.getBlockX() + 1 || loc1.getBlockX() == loc2.getBlockX() - 1)) {
            return false;
        }

        if (!(loc1.getBlockY() == loc2.getBlockY() || loc1.getBlockY() == loc2.getBlockY() + 1 || loc1.getBlockY() == loc2.getBlockY() - 1)) {
            return false;
        }

        if (!(loc1.getBlockZ() == loc2.getBlockZ() || loc1.getBlockZ() == loc2.getBlockZ() + 1 || loc1.getBlockZ() == loc2.getBlockZ() - 1)) {
            return false;
        }

        return true;
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        if (game == null) return;
        game.removePlayer(player);
        Bukkit.getLogger().info("player left");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        if (game != null) {
            if (game.getState() != GameState.PLAYING) {
                Bedwars.sendMessage(player, "&cYou cant break blocks at this time!");
                event.setCancelled(true);
                return;
            }
            if (event.getBlock().getType().name().contains("BED")) {
                event.setCancelled(true);
                Location location = event.getBlock().getLocation();
                Bukkit.getLogger().info(player.getName() + " broke bed");
                for (GameTeam team : game.getTeams()) {
                    if (team.getBed() != null) {
                        if (isClose(team.getBed(), location)) {
                            Bukkit.getLogger().info(team.getName() + " got broken bed");
                            if (!team.getPlayers().contains(player)) {
                                if (!team.hasBed()) {
                                    Bedwars.sendMessage(player, "&cThis bed has already been destroyed!");
                                    return;
                                }
                                game.playerBrokeBed(player, team);
                            } else {
                                Bedwars.sendMessage(player, "&cYou cant break your own bed!");
                            }
                        }
                    }
                }
                return;
            }
            if (game.getBlockManager().canBreak(event.getBlock().getLocation())) {
                game.getBlockManager().brokeBlock(event.getBlock().getLocation());
            } else {
                Bedwars.sendMessage(player, "&cYou cant break that block!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.TNT){
            event.getBlock().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
            event.getBlock().setType(Material.AIR);
        }else {

            Player player = event.getPlayer();
            Game game = gameManager.getGame(player);
            if (game != null) {
                if (game.getState() != GameState.PLAYING) {
                    event.setCancelled(true);
                    Bedwars.sendMessage(player, "&cYou cant place blocks at this time!");
                    return;
                }

                game.getBlockManager().placedBlock(event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void playerDamageEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof NPC) {
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                Game game = gameManager.getGame(player);
                if (game != null) {
                    event.setCancelled(true);
                    Bedwars.sendMessage(player, "&cYou cant damage the villagers!");
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Game game = gameManager.getGame(player);
            if (game != null) {
                if (game.getState() == GameState.PLAYING) {
                    if (event.getDamager() instanceof Player) {
                        Player damager = (Player) event.getDamager();
                        Bedwars.sendMessage(damager, "&a" + player.getName() + " is now at &c" + Math.round(player.getHealth()) + " ❤");
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
        event.getLocation().getWorld().playSound(event.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        List<Block> blocks = new ArrayList<>();
        blocks.addAll(event.blockList());
        for (Block block : blocks) {
            for (Game game : gameManager.getGameList()) {
                if (game.getBlockManager().canBreak(block.getLocation())) {
                    block.setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        if (!(entity instanceof NPC))
            return;

        if (entity.getCustomName().contains("i")) {
            event.setCancelled(true);
            player.openInventory(Bedwars.getShopCreator().createShop(ShopPage.BLOCKS, player));
        }


        if (gameManager.getGame(player) == null) {
            return;
        }
        if (gameManager.getGame(player).getTeam(player) == null) {
            return;
        }

        if (entity.getCustomName().contains("t")) {
            event.setCancelled(true);
            player.openInventory(Bedwars.getUpgradeCreator().createUpgradeShop(gameManager.getGame(player).getUpgradesManager().getUpgrades(gameManager.getGame(player).getTeam(player)), player));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = (Player) event.getEntity();

        Game game = gameManager.getGame(player);
        if (game != null) {
            if (game.getState() == GameState.PLAYING) {
                event.setCancelled(true);
                //JUST STSTSTOP THE PLAYER FROM DYING!!
                player.setHealth(20);
                player.setFallDistance(0F);
                player.setVelocity(new Vector(0, 0, 0));
                player.setHealth(20);
                player.setFallDistance(0F);
                Location spawn = game.getTeam(player).getSpawn();
                player.setFallDistance(0F);
                player.setHealth(20);
                player.setVelocity(new Vector(0, 0, 0));
                player.teleport(spawn);
                if (player.getKiller() != null) {
                    Player attacker = player.getKiller();
                    game.playerDied(event.getEntity(), "was murdered by " + game.getTeam(attacker).getColor() + attacker.getName());
                    for (ItemStack i : player.getInventory()) {
                        if (i.getType() == Material.IRON_INGOT || i.getType() == Material.GOLD_INGOT || i.getType() == Material.EMERALD || i.getType() == Material.DIAMOND) {
                            player.getKiller().getInventory().addItem(i);
                            player.getInventory().remove(i);

                        } else {
                            player.getInventory().remove(i);
                        }
                    }
                    Bedwars.sendMessage(attacker, "&aYou killed " + event.getEntity().getName() + "!");
                    if (game.getTeam(player).hasBed()) {
                        game.getStatsManager().playerGotKill(attacker);
                    } else {
                        game.getStatsManager().playerGotFinalKill(attacker);
                    }

                } else {
                    game.playerDied(event.getEntity(), "died");
                }
                game.updateScoreboards();
                return;
            } else {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Game game = gameManager.getGame(player);
        event.setCancelled(true);
        if (game != null) {
            Bedwars.sendMessage(player, "You can not sleep in the beds!");
        }
    }

    @EventHandler
    public void invclick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("Shop")) {
            return;
        }
        if (event.getView().getTitle().contains("Upgrades")) {
            return;
        }
        if (event.getCursor() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (gameManager.getGame(player) != null) {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent e){
        Action eventAction = e.getAction();
        Player player = e.getPlayer();

        if (eventAction == Action.RIGHT_CLICK_AIR || eventAction == Action.RIGHT_CLICK_BLOCK){
            if (player.getItemInHand().getType().equals(Material.FIRE_CHARGE)) {
                player.launchProjectile(Fireball.class).setVelocity(player.getLocation().getDirection().multiply(0.5));
                player.getInventory().remove(Material.FIRE_CHARGE);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player b = (Player) event.getEntity();

            if (b.getInventory().getHelmet() != null) {
                ItemStack helmet = b.getInventory().getHelmet();

                if (helmet.getType().getMaxDurability() > 0) {
                    event.setCancelled(true);
                }
            }

            if (b.getInventory().getChestplate() != null) {
                ItemStack chestplate = b.getInventory().getChestplate();

                if (chestplate.getType().getMaxDurability() > 0) {
                    event.setCancelled(true);
                }
            }

            if (b.getInventory().getLeggings() != null) {
                ItemStack leggings = b.getInventory().getLeggings();

                if (leggings.getType().getMaxDurability() > 0) {
                    event.setCancelled(true);
                }
            }

            if (b.getInventory().getBoots() != null) {
                ItemStack boots = b.getInventory().getBoots();

                if (boots.getType().getMaxDurability() > 0) {
                    event.setCancelled(true);
                }
            }
        }
    }



    @EventHandler
    public void drinkPot(PlayerItemConsumeEvent event){
        Bukkit.getLogger().info(event.getItem().getType().toString());
        if(event.getItem().getType().toString().contains("POTION")){
            Bukkit.getLogger().info("drankpot");
            Player player = event.getPlayer();
            event.getItem().setType(Material.AIR)
            ;
            player.updateInventory();
        }
    }
}
