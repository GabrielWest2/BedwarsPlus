package com.gabe.bedwars.upgrade;

public class TeamUpgrades {
    private boolean hasSharp = false;
    private int protLevel = 0;
    private int maniacMiner = 0;
    private int genLevel = 0;
    private boolean hasAlarm = false;
    private boolean hasFatuge = false;
    private boolean hasHeal = false;

    public void buyHeal(){
        hasHeal = true;
    }

    public boolean hasHeal() {
        return hasHeal;
    }

    public void buySharp(){
        hasSharp = true;
    }

    public void upgradeProt(){
        protLevel++;
    }

    public void upgradeMiner(){
        maniacMiner++;
    }

    public void upgradeGen(){
        genLevel++;
    }

    public void buyAlarm(){
        hasAlarm = true;
    }

    public void usedAlarm(){
        hasAlarm = false;
    }

    public void buyFatuge(){
        hasFatuge = true;
    }

    public void usedFatuge(){
        hasFatuge = false;
    }

    public boolean hasSharp() {
        return hasSharp;
    }

    public int getProtLevel() {
        return protLevel;
    }

    public int getManiacMiner() {
        return maniacMiner;
    }

    public int getGenLevel() {
        return genLevel;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public boolean hasFatuge() {
        return hasFatuge;
    }

    public int getExtaIronAmount(){
        if(genLevel == 0){
            return 0;
        }
        if(genLevel == 1){
            return 1;
        }
        if(genLevel == 2){
            return 2;
        }
        if(genLevel == 3){
            return 2;
        }
        if(genLevel == 4){
            return 4;
        }
        return 0;
    }

    public double getEmeraldChance(){
        if(genLevel == 0){
            return 0;
        }
        if(genLevel == 1){
            return 0;
        }
        if(genLevel == 2){
            return 0;
        }
        if(genLevel == 3){
            return 0.1;
        }
        if(genLevel == 4){
            return 0.15;
        }
        return 0;
    }

    public double getGoldPercent(){
        if(genLevel == 0){
            return 0.12;
        }
        if(genLevel == 1){
            return 0.18;
        }
        if(genLevel == 2){
            return 0.24;
        }
        if(genLevel == 3){
            return 0.24;
        }
        if(genLevel == 4){
            return 0.36;
        }
        return 0.12;
    }
}
