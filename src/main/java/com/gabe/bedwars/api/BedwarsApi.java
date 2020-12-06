package com.gabe.bedwars.api;

import com.gabe.bedwars.Bedwars;
import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.managers.ArenaManager;
import com.gabe.bedwars.managers.GameManager;

import java.util.Set;

public class BedwarsApi {
    /**
     *
     * @author ThatKingGuy
     */



    /**
     * Returns a static ArenaManager
     * instance.
     * @see ArenaManager
     */
    public ArenaManager getArenaManager(){
        return Bedwars.getArenaManager();
    }

    /**
     * Returns a static GameManager
     * instance.
     * @see GameManager
     */
    public GameManager getGameManager(){
        return  Bedwars.getGameManager();
    }

    /**
     * Returns a Set of Arenas
     * @see Arena
     * @see Set
     */
    public Set<Arena> getSavedArenas(){
        return Bedwars.getArenaManager().getArenaList();
    }

    /**
     * Returns a Set of the ongoing Games
     * @see Game
     * @see Set
     */
    public Set<Game> getRunningGames(){
        return Bedwars.getGameManager().getGameList();
    }

    /**
     * Returns a game with the specified name
     * @see Game
     */
    public Game getGame(String str){
        for(Game game : Bedwars.getGameManager().getGameList()){
            if(game.getName().equals(str)){
                return game;
            }
        }
        return null;
    }
}
