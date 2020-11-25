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

    WEAPONS(2, Material.GOLDEN_SWORD, null),
    ARMOR(3,Material.CHAINMAIL_BOOTS, null),
    TOOLS(4,Material.STONE_PICKAXE, null),
    ARCHERY(5,Material.BOW, null),
    POTIONS(6,Material.BREWING_STAND, null),
    UTILITY(7, Material.TNT, null);

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
