package com.gabe.bedwars.shop;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum ShopPage {
    BLOCKS(1, Material.ORANGE_TERRACOTTA, Arrays.asList(
            new ShopItem(Material.WHITE_WOOL,4,16,"Build stuff!", MoneyType.IRON),
            new ShopItem(Material.ORANGE_TERRACOTTA, 12, 16, "Basic block to defend your bed.", MoneyType.IRON),
            new ShopItem(Material.GLASS, 12, 4, "Immune to explosions!", MoneyType.IRON),
            new ShopItem(Material.END_STONE, 24, 12, "Solid block to defend your bed.", MoneyType.IRON),
            new ShopItem(Material.LADDER, 4, 16, "Useful to save cats stuck in trees.", MoneyType.IRON),
            new ShopItem(Material.OAK_PLANKS, 4, 16, "Good block to defend your bed against pickaxes.", MoneyType.GOLD),
            new ShopItem(Material.OBSIDIAN, 4, 4, "Extreme protection for your bed!", MoneyType.EMERALD))),

    WEAPONS(2, Material.GOLDEN_SWORD, Arrays.asList(
            new ShopItem(Material.STONE_SWORD,10,1,"choop.", MoneyType.IRON),
            new ShopItem(Material.IRON_SWORD,7,1,"CHOOP CHOOP!", MoneyType.GOLD),
            new ShopItem(Material.DIAMOND_SWORD,3,1,"CHOOPEDYEDY!", MoneyType.EMERALD)
    )),
    ARMOR(3,Material.CHAINMAIL_BOOTS, null),
    TOOLS(4,Material.STONE_PICKAXE, Arrays.asList(
            new ShopItem(Material.SHEARS,20,1,"Great for shearing sheap.", MoneyType.IRON),
            new ShopItem(Material.STONE_PICKAXE,10,1,"Very slow pic.", MoneyType.IRON),
            new ShopItem(Material.STONE_AXE,10,1,"Cuts through wood quickly.", MoneyType.IRON),
            new ShopItem(Material.IRON_PICKAXE,20,1,"Kinda slow pic.", MoneyType.IRON),
            new ShopItem(Material.IRON_AXE,20,1,"Choop.", MoneyType.IRON),
            new ShopItem(Material.GOLDEN_PICKAXE,6,1,"Fast pic..", MoneyType.GOLD),
            new ShopItem(Material.GOLDEN_AXE,6,1,"A Choop-er axe.", MoneyType.GOLD),
            new ShopItem(Material.DIAMOND_PICKAXE,2,1,"A pickaxe that can beak obsidian.", MoneyType.EMERALD),
            new ShopItem(Material.DIAMOND_AXE,2,1,"Choop-er-est axe there is.", MoneyType.EMERALD)
    )),
    RANGED(5,Material.BOW, Arrays.asList(
            new ShopItem(Material.ARROW,2,8,"Great for shearing sheap.", MoneyType.GOLD),
            new ShopItem(Material.BOW,12,1,"Shoot people out of the air.", MoneyType.GOLD)
    )),
    POTIONS(6,Material.BREWING_STAND, null),
    UTILITY(7, Material.TNT, Arrays.asList(
            new ShopItem(Material.GOLDEN_APPLE,3,1,"Well-rounded healing.", MoneyType.GOLD),
            new ShopItem(Material.FIRE_CHARGE,40,1,"Shoot people out of the air.", MoneyType.IRON),
            new ShopItem(Material.TNT,8,1,"BOOM.", MoneyType.GOLD),
            new ShopItem(Material.ENDER_PEARL,4,1,"Rush em.", MoneyType.EMERALD),
            new ShopItem(Material.WATER_BUCKET,6,1,"Annoying bed defense!", MoneyType.GOLD),
            new ShopItem(Material.SPONGE,6,4,"Soak up the water!", MoneyType.GOLD)
    ));

    private final int slot;
    private final Material mat;
    private final List<ShopItem> items;
    ShopPage(int slot, Material mat, List<ShopItem> items){
        this.slot = slot;
        this.mat = mat;
        this.items = items;
    }
    public int getSlot(){
        return slot;
    }

    public Material getMat() {
        return mat;
    }

    public List<ShopItem> getItems() {
        return items;
    }
}
