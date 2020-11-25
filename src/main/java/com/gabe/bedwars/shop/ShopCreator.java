package com.gabe.bedwars.shop;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopCreator {

    public Inventory createShop(ShopPage page, Player player){
        Inventory inv = Bukkit.createInventory(null, 45, ChatColor.DARK_GRAY+"Shop");

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

        for(ShopPage sp : ShopPage.values()){
            ItemStack i = new ItemStack(sp.getMat());
            ItemMeta im = i.getItemMeta();
            im.setDisplayName(ChatColor.RESET+""+ChatColor.GREEN+ StringUtils.capitalize(sp.name().toLowerCase()));
            im.setLore(Collections.singletonList(ChatColor.YELLOW+"Click to view!"));
            i.setItemMeta(im);
            inv.setItem(sp.getSlot(), i);
        }
        inv.setItem(0, r);
        inv.setItem(8, r);
        inv.setItem(35, r);
        inv.setItem(27, r);
        inv.setItem(26, r);
        inv.setItem(18, r);
        inv.setItem(17, r);
        inv.setItem(8, r);
        for(int i = 9;i<18;i++){
            if(page.getSlot()+9 == i){
                inv.setItem(i, s);
            }else {
                inv.setItem(i, p);
            }
        }
        for(int i = 36;i<45;i++){
            inv.setItem(i, r);
        }
        if(page.getItems() != null) {
            for (ShopItem item : page.getItems()) {
                inv.addItem(createItem(item, player));
            }
        }
        inv.setItem(9, r);
        inv.setItem(17, r);

        return inv;
    }

    public ItemStack createItem(ShopItem item, Player player){
        ItemStack i = new ItemStack(item.getMat(),item.getAmount());
        ItemMeta im = i.getItemMeta();
        if(player.getInventory().containsAtLeast(getCost(item), item.getPrice())){
            im.setDisplayName(ChatColor.GREEN + StringUtils.capitalize(item.getMat().toString().toLowerCase().replaceAll("_"," ")));
        }else{
            im.setDisplayName(ChatColor.RED + StringUtils.capitalize(item.getMat().toString().toLowerCase().replaceAll("_"," ")));
        }
        List<String> lore = new ArrayList<>();
         switch (item.getMoneyType()){
             case IRON:
                 lore.add(ChatColor.GRAY+"Cost: "+ChatColor.WHITE+item.getPrice()+" "+getMatName(item));
                 break;
             case GOLD:
                 lore.add(ChatColor.GRAY+"Cost: "+ChatColor.GOLD+item.getPrice()+" "+getMatName(item));
                 break;
             case EMERALD:
                 lore.add(ChatColor.GRAY+"Cost: "+ChatColor.DARK_GREEN+item.getPrice()+" "+getMatName(item));
                 break;
         }
         lore.add(" ");
         lore.add(ChatColor.GRAY+item.getDesc());
         lore.add(" ");
        if(player.getInventory().containsAtLeast(getCost(item), item.getPrice())){
            lore.add(ChatColor.YELLOW+"Click to purchase!");
        }else{
            lore.add(ChatColor.RED+"You don't have enough "+getMatName(item)+ChatColor.RED+"!");
        }
         im.setLore(lore);
         i.setItemMeta(im);
         return i;
    }

    public ItemStack getCost(ShopItem item){
        ItemStack i = null;
        if(item.getMoneyType() == MoneyType.IRON){
          i = new ItemStack(Material.IRON_INGOT);
        }
        if(item.getMoneyType() == MoneyType.GOLD){
            i = new ItemStack(Material.GOLD_INGOT);
        }
        if(item.getMoneyType() == MoneyType.EMERALD){
            i = new ItemStack(Material.EMERALD);
        }
        return i;
    }

    public String getMatName(ShopItem item){
        String  i = null;
        if(item.getMoneyType() == MoneyType.IRON){
            i = ChatColor.WHITE + "Iron";
        }
        if(item.getMoneyType() == MoneyType.GOLD){
            i = ChatColor.GOLD + "Gold";
        }
        if(item.getMoneyType() == MoneyType.EMERALD){
            i = ChatColor.GREEN + "Emerald";
        }
        return i;
    }
}
