package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.team.GameTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardFactory {


    public static Scoreboard makeBoard(Player player, GameState state, Game game){
        String primary = ChatColor.translateAlternateColorCodes('&', Bedwars.scoreboardPrimary);
        String secondary = ChatColor.translateAlternateColorCodes('&', Bedwars.scoreboardSecondary);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = null;
        if(state==GameState.WAITING){
            board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective("board", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lBEDWARS"));


            Score space = objective.getScore(" "); //Get a fake offline player
            space.setScore(10);
            Score map = objective.getScore(primary+"Map: "+secondary+game.getName()); //Get a fake offline player
            map.setScore(9);
            Score players = objective.getScore(primary+"Players: "+secondary+game.getPlayers().size()+"/"+game.getArena().getMaxPlayers()); //Get a fake offline player
            players.setScore(8);
            Score space1 = objective.getScore("  "); //Get a fake offline player
            space1.setScore(7);
            if(game.getCountDown() == -1){
                Score status = objective.getScore(primary+"Waiting for players..."); //Get a fake offline player
                status.setScore(6);
            }else {
                Score status = objective.getScore(primary+"Starting in: "+secondary+(game.getCountDown()-1)); //Get a fake offline player
                status.setScore(6);
            }
            Score space2 = objective.getScore("   "); //Get a fake offline player
            space2.setScore(5);

            Score ver = objective.getScore(primary+"Version: "+ChatColor.GRAY+"v"+game.getPlugin().getDescription().getVersion()); //Get a fake offline player
            ver.setScore(4);



            Score mode = objective.getScore(primary+"Mode: "+secondary+getMode(game)); //Get a fake offline player
            mode.setScore(3);
            Score space3 = objective.getScore("    "); //Get a fake offline player
            space3.setScore(2);
            Score server = objective.getScore(ChatColor.YELLOW+Bedwars.serverText); //Get a fake offline player
            server.setScore(1);

        }else{
            board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective("board", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            objective.setDisplayName(ChatColor.translateAlternateColorCodes('&',"&e&lBEDWARS"));

            Score server = objective.getScore(ChatColor.YELLOW+Bedwars.serverText); //Get a fake offline player
            server.setScore(1);

            Score space = objective.getScore(""); //Get a fake offline player
            space.setScore(2);

            Score beds = objective.getScore(primary+"Beds Broken: "+secondary+game.getStatsManager().getBeds(player)); //Get a fake offline player
            beds.setScore(3);
            Score fin = objective.getScore(primary+"Final Kills: "+secondary+game.getStatsManager().getFinals(player)); //Get a fake offline player
            fin.setScore(4);
            Score kills = objective.getScore(primary+"Kills: "+secondary+game.getStatsManager().getKills(player)); //Get a fake offline player
            kills.setScore(5);
            Score space1 = objective.getScore(" "); //Get a fake offline player
            space1.setScore(6);

            int line = 7;
            for(GameTeam team : game.getTeams()){

                Score t = objective.getScore(team.getColor()+team.getName().substring(0,1)+" "+ChatColor.WHITE+team.getName()+": "+(team.hasBed() ? ChatColor.GREEN+"✓"  : (team.getAlivePlayers().size()==0 ? ChatColor.RED+"✕" : secondary+""+team.getAlivePlayers().size() )) +" "+ChatColor.GRAY+(team.getPlayers().contains(player) ? "YOU" : "")); //Get a fake offline player
                t.setScore(line);
                line++;
            }


            Score space2 = objective.getScore("  "); //Get a fake offline player
            space2.setScore(line);
        }
        return board;
    }

    public static String getMode(Game game){
        int teams = game.getTeams().size();
        int teamSize = game.getArena().getMaxPlayers() / teams;
        String teamString;
        if(!Bedwars.gamemodes.containsKey(teamSize)) {
            teamString = teamSize+"";
            for (int i = 0; i < teams - 1; i++) {
                teamString += "v" + teamSize;
            }
        }else{
            teamString = Bedwars.gamemodes.get(teamSize);
        }
        return teamString;
    }
}
