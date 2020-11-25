package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardFactory {

    static ScoreboardManager manager = Bukkit.getScoreboardManager();

    public static Scoreboard makeBoard(Player player, GameState state, Game game){
        Scoreboard board = null;
        if(state==GameState.WAITING){
            board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective("board", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lBEDWARS"));


            Score space = objective.getScore(" "); //Get a fake offline player
            space.setScore(10);
            Score map = objective.getScore(ChatColor.WHITE+"Map: "+ChatColor.GREEN+game.getName()); //Get a fake offline player
            map.setScore(9);
            Score players = objective.getScore(ChatColor.WHITE+"Players: "+ChatColor.GREEN+game.getPlayers().size()+"/"+game.getArena().getMaxPlayers()); //Get a fake offline player
            players.setScore(8);
            Score space1 = objective.getScore("  "); //Get a fake offline player
            space1.setScore(7);
            if(game.getCountDown() == -1){
                Score status = objective.getScore(ChatColor.WHITE+"Waiting for players..."); //Get a fake offline player
                status.setScore(6);
            }else {
                Score status = objective.getScore(ChatColor.WHITE+"Starting in: "+ChatColor.GREEN+(game.getCountDown()-1)); //Get a fake offline player
                status.setScore(6);
            }
            Score space2 = objective.getScore("   "); //Get a fake offline player
            space2.setScore(5);

            Score ver = objective.getScore(ChatColor.WHITE+"Version: "+ChatColor.GRAY+"v"+game.getPlugin().getDescription().getVersion()); //Get a fake offline player
            ver.setScore(4);
            Score space3 = objective.getScore("    "); //Get a fake offline player
            space3.setScore(3);
            Score server = objective.getScore(ChatColor.YELLOW+"www.gbookpro.com"); //Get a fake offline player
            server.setScore(2);

        }else{
            board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective("board", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lBEDWARS"));

            Score server = objective.getScore(ChatColor.YELLOW+"www.gbookpro.com"); //Get a fake offline player
            server.setScore(1);

            Score space = objective.getScore(""); //Get a fake offline player
            space.setScore(2);

            Score beds = objective.getScore(ChatColor.WHITE+"Beds Broken: "+ChatColor.GREEN+game.getStatsManager().getBeds(player)); //Get a fake offline player
            beds.setScore(3);
            Score fin = objective.getScore(ChatColor.WHITE+"Final Kills: "+ChatColor.GREEN+game.getStatsManager().getFinals(player)); //Get a fake offline player
            fin.setScore(4);
            Score kills = objective.getScore(ChatColor.WHITE+"Kills: "+ChatColor.GREEN+game.getStatsManager().getKills(player)); //Get a fake offline player
            kills.setScore(5);
            Score space1 = objective.getScore(" "); //Get a fake offline player
            space1.setScore(6);

            int line = 7;
            for(GameTeam team : game.getTeams()){

                Score t = objective.getScore(team.getColor()+team.getName().substring(0,1)+" "+ChatColor.WHITE+team.getName()+": "+(team.hasBed() ? ChatColor.GREEN+"✓"  : (team.getAlivePlayers().size()==0 ? ChatColor.RED+"✕" : ChatColor.GREEN+""+team.getAlivePlayers().size() )) +" "+ChatColor.GRAY+(team.getPlayers().contains(player) ? "YOU" : "")); //Get a fake offline player
                t.setScore(line);
                line++;
            }


            Score space2 = objective.getScore("  "); //Get a fake offline player
            space2.setScore(line);
        }
        return board;
    }
}
