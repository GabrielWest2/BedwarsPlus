package com.gabe.bedwars.arenas;

public class GameStats {
    private int kills = 0;
    private int finalKills = 0;
    private int bedsBroken = 0;

    public void brokeBed(){
        bedsBroken++;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void addFinalKill(){
        finalKills++;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void addKill(){
        kills++;
    }

    public int getKills() {
        return kills;
    }
}
