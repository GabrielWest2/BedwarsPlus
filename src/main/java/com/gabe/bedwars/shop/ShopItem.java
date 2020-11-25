package com.gabe.bedwars.shop;

import org.bukkit.Material;

public class ShopItem {
    private final Material mat;
    private final MoneyType moneyType;
    private final int price;
    private final int amount;
    private final String desc;

    public ShopItem(Material mat, int price, int amount, String desc, MoneyType type) {
        this.mat = mat;
        this.price = price;
        this.amount = amount;
        this.desc = desc;
        this.moneyType = type;
    }

    public int getAmount() {
        return amount;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public Material getMat() {
        return mat;
    }

    public MoneyType getMoneyType() {
        return moneyType;
    }
}
