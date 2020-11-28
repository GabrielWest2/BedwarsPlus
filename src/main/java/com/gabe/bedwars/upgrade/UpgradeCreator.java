package com.gabe.bedwars.upgrade;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpgradeCreator {

    public Inventory createUpgradeShop(TeamUpgrades upgrades, Player player){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY+"Team Upgrades");

        ItemStack p = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta pm = p.getItemMeta();
        pm.setDisplayName(" ");
        p.setItemMeta(pm);

        ItemStack s= new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta sm = s.getItemMeta();
        sm.setDisplayName(" ");
        s.setItemMeta(pm);

        ItemStack r= new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta rm = r.getItemMeta();
        rm.setDisplayName(" ");
        r.setItemMeta(rm);

        ItemStack o= new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta om = o.getItemMeta();
        om.setDisplayName(" ");
        o.setItemMeta(om);


        for(int i = 0;i<27;i++){
            inv.setItem(i, p);
        }

        ItemStack sharp = new ItemStack(Material.IRON_SWORD);
        ItemMeta sharpm = sharp.getItemMeta();
        sharpm.setDisplayName(getName("Sharpened Swords", upgrades.hasSharp()));
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"Your team permanently gains");
        lore.add(ChatColor.GRAY+"Sharpness I on all swords and");
        lore.add(ChatColor.GRAY+"axes!");
        lore.add(" ");
        lore.add(ChatColor.GRAY+"Cost: "+ChatColor.AQUA+"8 Diamonds");
        lore.add(" ");
        lore.add(bottomText(player, 8, upgrades.hasSharp()));
        sharpm.setLore(lore);
        sharp.setItemMeta(sharpm);

        inv.setItem(10, sharp);
        if(upgrades.hasSharp()){
            inv.setItem(10+9, s);
        }else{
            inv.setItem(10+9, r);
        }

        ItemStack prot = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta protm = prot.getItemMeta();
        protm.setDisplayName(getName("Reinforced Armor", (upgrades.getProtLevel()>0)));
        List<String> lore1 = new ArrayList<>();
        lore1.add(ChatColor.GRAY+"Your team permanently gains");
        lore1.add(ChatColor.GRAY+"Protection on all armor pieces.");
        lore1.add(" ");
        lore1.add(((upgrades.getProtLevel() >= 1) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: Protection I, "+ChatColor.AQUA+"5 Diamonds");
        lore1.add(((upgrades.getProtLevel() >= 2) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 2: Protection II, "+ChatColor.AQUA+"10 Diamonds");
        lore1.add(((upgrades.getProtLevel() >= 3) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 3: Protection II, "+ChatColor.AQUA+"20 Diamonds");
        lore1.add(((upgrades.getProtLevel() >= 4) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 4: Protection IV, "+ChatColor.AQUA+"30 Diamonds");
        lore1.add(" ");
        lore1.add(protBottomText(player, upgrades.getProtLevel()));
        protm.setLore(lore1);
        prot.setItemMeta(protm);

        inv.setItem(11, prot);
        if(upgrades.getProtLevel() == 4){
            inv.setItem(11+9, s);
        }else{
            if(upgrades.getProtLevel() ==0) {
                inv.setItem(11 + 9, r);
            }else{
                inv.setItem(11 + 9, o);
            }
        }


        ItemStack mine = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta minem = mine.getItemMeta();
        minem.setDisplayName(getName("Maniac Miner", (upgrades.getManiacMiner()>0)));
        List<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.GRAY+"All players on your team");
        lore2.add(ChatColor.GRAY+"permanently gain Haste.");
        lore2.add(" ");
        lore2.add(((upgrades.getManiacMiner() >= 1) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: Haste II, "+ChatColor.AQUA+"4 Diamonds");
        lore2.add(((upgrades.getManiacMiner() >= 2) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 2: Haste III, "+ChatColor.AQUA+"6 Diamonds");
        lore2.add(" ");
        lore2.add(hasteBottomText(player, upgrades.getManiacMiner()));
        minem.setLore(lore2);
        mine.setItemMeta(minem);

        inv.setItem(12, mine);
        if(upgrades.getManiacMiner() == 2){
            inv.setItem(12+9, s);
        }else{
            if(upgrades.getManiacMiner() ==0) {
                inv.setItem(12 + 9, r);
            }else{
                inv.setItem(12 + 9, o);
            }
        }

        ItemStack gen = new ItemStack(Material.FURNACE);
        ItemMeta genm = gen.getItemMeta();
        genm.setDisplayName(getName("Super Forge", (upgrades.getGenLevel()>0)));
        List<String> lore3 = new ArrayList<>();
        lore3.add(ChatColor.GRAY+"Upgrade resource spawning on");
        lore3.add(ChatColor.GRAY+"your island.");
        lore3.add(" ");
        lore3.add(((upgrades.getGenLevel() >= 1) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: +50% Resources, "+ChatColor.AQUA+"4 Diamonds");
        lore3.add(((upgrades.getGenLevel() >= 2) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: +100% Resources, "+ChatColor.AQUA+"8 Diamonds");
        lore3.add(((upgrades.getGenLevel() >= 3) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: Spawn emeralds, "+ChatColor.AQUA+"12 Diamonds");
        lore3.add(((upgrades.getGenLevel() >= 4) ? ChatColor.GREEN : ChatColor.GRAY)+"Tier 1: +200% Resources, "+ChatColor.AQUA+"16 Diamonds");

        lore3.add(" ");
        lore3.add(hasteBottomText(player, upgrades.getManiacMiner()));
        genm.setLore(lore3);
        gen.setItemMeta(genm);

        inv.setItem(13, gen);
        if(upgrades.getGenLevel() == 4){
            inv.setItem(13+9, s);
        }else{
            if(upgrades.getGenLevel() ==0) {
                inv.setItem(13 + 9, r);
            }else{
                inv.setItem(13 + 9, o);
            }
        }




        return inv;
    }

    public String getName(String string, boolean upgCase){
        if(upgCase){
            return ChatColor.GREEN+string;
        }else{
            return ChatColor.RED+string;
        }
    }

    public String bottomText(Player player, int price, boolean upgCase){
        if(upgCase){
            return ChatColor.GREEN+"UNLOCKED";
        }else{
            if(player.getInventory().contains(Material.DIAMOND, price)){
                return ChatColor.YELLOW+"Click to buy!";
            }else {
                return ChatColor.RED+"You do not have enough diamonds!";
            }
        }
    }

    public String protBottomText(Player player, int level){
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
        }

        if(level != 4) {
            if (player.getInventory().contains(Material.DIAMOND, price)) {
                return ChatColor.YELLOW + "Click to buy!";
            } else {
                return ChatColor.RED + "You do not have enough diamonds!";
            }
        }else{
            return ChatColor.GREEN+"Maxed out.";
        }

    }

    public String hasteBottomText(Player player, int level){
        int price = 4;
        if(level == 1){
            price = 6;
        }

        if(level != 2) {
            if (player.getInventory().contains(Material.DIAMOND, price)) {
                return ChatColor.YELLOW + "Click to buy!";
            } else {
                return ChatColor.RED + "You do not have enough diamonds!";
            }
        }else{
            return ChatColor.GREEN+"Maxed out.";
        }

    }
}
