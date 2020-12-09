package com.gabe.bedwars.commands.admin;

import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class AddTeamShopCommand {
    public AddTeamShopCommand(Player player, String[] args){
        Villager villager = (Villager) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
        villager.setAI(false);
        villager.setCustomName("§t");
        villager.setCustomNameVisible(false);
        ArmorStand as = (ArmorStand) player.getWorld().spawn(player.getLocation().subtract(0, 0.2, 0), ArmorStand.class);
        as.setVisible(false);
        as.setCustomName(ChatColor.GOLD + "" + ChatColor.BOLD + "RIGHT CLICK");
        as.setCustomNameVisible(true);
        as.setGravity(false);
        as.setCollidable(false);

        ArmorStand as1 = (ArmorStand) player.getWorld().spawn(player.getLocation().add(0, 0.1, 0), ArmorStand.class);
        as1.setVisible(false);
        as1.setCustomName(ChatColor.AQUA + "Team Upgrades");
        as1.setCustomNameVisible(true);
        as1.setGravity(false);
        as1.setCollidable(false);
    }
}
