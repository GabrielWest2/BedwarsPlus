package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.arenas.Game;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameManager {
    private final Bedwars plugin;
    private Set<Game> games;
    public GameManager(Bedwars plugin){
        this.plugin = plugin;
        games = new HashSet<>();
        for(Arena a : plugin.getArenaManager().getArenaList()){
            Game game = new Game(a, plugin);
            games.add(game);
        }
    }

    public Game getGame(String str){
        for(Game game : games){
            if(game.getName().equals(str)){
                return game;
            }
        }
        return null;
    }

    public Game getGame(Player player){
        for(Game game : games){
            if(game.getPlayers().contains(player)){
                return game;
            }
        }
        return null;
    }

    public Set<Game> getGameList(){
        return Collections.unmodifiableSet(games);
    }
}
