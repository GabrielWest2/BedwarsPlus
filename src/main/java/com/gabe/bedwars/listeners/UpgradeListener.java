package com.gabe.bedwars.listeners;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.GameManager;
import com.gabe.bedwars.upgrade.TeamUpgrades;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UpgradeListener implements Listener {

    @EventHandler
    public void upgradeInteract(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Upgrades")){
            return;
        }
        if(event.getCursor() ==null){
            return;
        }
        event.setCancelled(true);

        ItemStack click = event.getCurrentItem();
        if(click ==null){
            return;
        }

        if(click.getItemMeta() == null){
            Bukkit.getLogger().info(click.getType().toString());
            return;
        }


        Player player = (Player) event.getWhoClicked();
        TeamUpgrades upgrades = Bedwars.getGameManager().getGame(player).getUpgradesManager().getUpgrades(Bedwars.getGameManager().getGame(player).getTeam(player));
        if(click.getItemMeta().getDisplayName().contains("Sharpened Swords")){
            if(player.getInventory().contains(Material.DIAMOND, 8)) {
                if (!upgrades.hasSharp()) {
                    upgrades.buySharp();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.getInventory().removeItem(new ItemStack(Material.DIAMOND, 8));
                    player.openInventory(Bedwars.getUpgradeCreator().createUpgradeShop(upgrades, player));
                }
            }
        }

        if(click.getItemMeta().getDisplayName().contains("Reinforced Armor")){
            if(player.getInventory().contains(Material.DIAMOND, getPriceToUpgrade(upgrades.getProtLevel()))) {
                if (upgrades.getProtLevel() < 4) {
                    player.getInventory().removeItem(new ItemStack(Material.DIAMOND, getPriceToUpgrade(upgrades.getProtLevel())));
                    upgrades.upgradeProt();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.openInventory(Bedwars.getUpgradeCreator().createUpgradeShop(upgrades, player));
                }
            }
        }

        if(click.getItemMeta().getDisplayName().contains("Maniac Miner")){
            if (upgrades.getManiacMiner() < 2) {
                 if(player.getInventory().contains(Material.DIAMOND, getPriceToUpgradeHaste(upgrades.getManiacMiner()))) {
                     player.getInventory().removeItem(new ItemStack(Material.DIAMOND, getPriceToUpgradeHaste(upgrades.getManiacMiner())));
                     upgrades.upgradeMiner();
                     player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                     player.openInventory(Bedwars.getUpgradeCreator().createUpgradeShop(upgrades, player));
                }
            }
        }

        if(click.getItemMeta().getDisplayName().contains("Super Forge")){
            if (upgrades.getGenLevel() < 4) {
                if(player.getInventory().contains(Material.DIAMOND, getPriceToUpgradeGen(upgrades.getGenLevel()))) {
                    player.getInventory().removeItem(new ItemStack(Material.DIAMOND, getPriceToUpgradeGen(upgrades.getGenLevel())));
                    upgrades.upgradeGen();
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    player.openInventory(Bedwars.getUpgradeCreator().createUpgradeShop(upgrades, player));
                }
            }
        }
    }

    public int getPriceToUpgrade(int level){
        int price = 5;
        switch (level){
            case 1:
                price = 10;
                break;
            case 2:
                price = 20;
                break;
            case 3:
                price = 30;
                break;
            default:
                break;
        }
        return price;
    }

    public int getPriceToUpgradeGen(int level){
        int price = 4;
        switch (level){
            case 1:
                price = 8;
                break;
            case 2:
                price = 12;
                break;
            case 3:
                price = 16;
                break;
            default:
                break;
        }
        return price;
    }

    public int getPriceToUpgradeHaste(int level){
        int price = 4;
        if(level == 1){
            price = 6;
        }
        return price;
    }
}
