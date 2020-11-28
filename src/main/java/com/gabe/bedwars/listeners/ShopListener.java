package com.gabe.bedwars.listeners;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.shop.ShopPage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


public class ShopListener implements Listener {
    @EventHandler
    public void shopInteract(InventoryClickEvent event){
        if(!event.getView().getTitle().contains("Shop")){
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
            return;
        }


        Player player = (Player) event.getWhoClicked();
        if(getPage(click.getItemMeta().getDisplayName()) != null){
            player.openInventory(Bedwars.getShopCreator().createShop(getPage(click.getItemMeta().getDisplayName()), player));
            return;
        }

        if(isItem(click)){
            if(player.getInventory().containsAtLeast(getMat(click), getCost(click))) {
                for(int i = 0; i<getCost(click);i++) {
                    player.getInventory().removeItem(getMat(click));
                }
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,1,1);
                if(!click.getType().toString().contains("BOOTS")) {
                    player.getInventory().addItem(new ItemStack(click.getType(), click.getAmount()));
                }else{
                    if(click.getType().toString().contains("MAIL")){
                        player.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                        player.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
                    }
                    if(click.getType().toString().contains("IRON")){
                        player.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                        player.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
                    }
                    if(click.getType().toString().contains("DIAMOND")){
                        player.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
                        player.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
                    }
                }
            }else {
                Bedwars.sendMessage(player,"&cYou do not have enough of the required material!");
            }
        }
    }

    public int getCost(ItemStack item){
        return Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getLore().get(0)).split(" ")[1]);
    }

    public boolean isItem(ItemStack item){
        if(item.getItemMeta().getLore() == null){
            return false;
        }
        if(item.getItemMeta().getLore().size() < 3){
            return false;
        }
        if(!item.getItemMeta().getLore().get(0).contains("Cost:")){
            return false;
        }

        return true;
    }

    public ItemStack getMat(ItemStack item){
        ItemStack  i = null;
        if(item.getItemMeta().getLore().get(0).contains("Iron")){
            i = new ItemStack(Material.IRON_INGOT);
        }
        if(item.getItemMeta().getLore().get(0).contains("Gold")){
            i = new ItemStack(Material.GOLD_INGOT);
        }
        if(item.getItemMeta().getLore().get(0).contains("Emerald")){
            i = new ItemStack(Material.EMERALD);
        }
        return i;
    }

    public ShopPage getPage(String str){
        String s = ChatColor.stripColor(str);
        for(ShopPage p : ShopPage.values()){
            if(p.toString().equalsIgnoreCase(s.toUpperCase())){
                return p;
            }
        }
        return null;
    }
}
