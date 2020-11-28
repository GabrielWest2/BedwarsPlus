package com.gabe.bedwars;

import com.gabe.bedwars.arenas.Arena;
import com.gabe.bedwars.arenas.Game;
import com.gabe.bedwars.listeners.GameListener;
import com.gabe.bedwars.listeners.ShopListener;
import com.gabe.bedwars.listeners.UpgradeListener;
import com.gabe.bedwars.managers.ArenaManager;
import com.gabe.bedwars.shop.ShopCreator;
import com.gabe.bedwars.tabcomplete.AdminTabComplete;
import com.gabe.bedwars.team.Team;
import com.gabe.bedwars.upgrade.UpgradeCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bedwars extends JavaPlugin {

    private ArenaManager arenaManager;
    private static GameManager gameManager;
    public static GameManager getGameManager(){
        return gameManager;
    }
    public static ShopCreator shopCreator;
    public static UpgradeCreator upgradeCreator;
    public static UpgradeCreator getUpgradeCreator(){ return upgradeCreator; }
    public static ShopCreator getShopCreator(){
        return shopCreator;
    }
    String help  =  "\n&8*------------------------------------* \n" +
                    "&cBedwars Admin Help: &6v"+this.getDescription().getVersion()+"\n" +
                    "&e/bwa &6create <name> <minplayers> <maxplayers>\n" +
                    "&e/bwa &6debug\n" +
                    "&e/bwa &6setlobby <name>\n" +
                    "&e/bwa &6setmainlobby <name>\n" +
                    "&e/bwa &6addteam <name> <team name> <color>\n" +
                    "&e/bwa &6setspawn <name> <team>\n" +
                    "&e/bwa &6setteamgen <name> <team>\n" +
                    "&e/bwa &6setbed <name> <team>\n" +
                    "&e/bwa &6addgen <name> <diamond/emerald>\n" +
                    "&e/bwa &6additemshop\n" +
                    "&e/bwa &6addteamshop\n" +
                    "&e/bwa &6save <name>\n" +
                    "&8*------------------------------------* \n";

    public void onEnable() {

        getCommand("bwa").setTabCompleter(new AdminTabComplete());
        arenaManager = new ArenaManager(this);
        arenaManager.deserialize();
        gameManager = new GameManager(this);
        shopCreator = new ShopCreator();
        upgradeCreator = new UpgradeCreator();
        Bukkit.getPluginManager().registerEvents(new GameListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopListener(), this);
        Bukkit.getPluginManager().registerEvents(new UpgradeListener(), this);


    }

    public void onDisable() {
        //arenaManager.serialise();
        for(Game game : gameManager.getGameList()){
            game.getBlockManager().restoreMap();
        }
    }

    public boolean isColor(String string){
        for(ChatColor c : ChatColor.values()){
            if(c.name().equalsIgnoreCase(string.toUpperCase())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("bwa")){
            if(sender instanceof Player){

                Player player = (Player) sender;
                if(args.length > 0){
                    if(args[0].equalsIgnoreCase("help")){
                        sendMessage(player, false, help);
                    }
                    else if(args[0].equalsIgnoreCase("debug")) {
                        if(arenaManager.getArenaList().size() > 0) {
                            sendMessage(player, generateDebug());
                        }else{
                            sendMessage(player, "There are no arenas to debug!");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("create")){
                        if (args.length > 3) {
                            if(arenaManager.getArena(args[1]) != null) {
                                sendMessage(player, "Arena named &6" + args[1] + "&e already exists.");
                            }else{
                                arenaManager.addArena(new Arena(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3])));
                                sendMessage(player, "Created arena named &6" + args[1] + "&e and set min players to &6"+args[2]+"&e and max players to &6" +args[3]+"&e.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa create <name> <minplayers> <maxplayers>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("addteam")){
                        if (args.length > 3) {
                            if(arenaManager.getArena(args[1]) != null) {
                                 Arena a = arenaManager.getArena(args[1]);
                                for(Team t : a.getTeams()){
                                    if(t.getName().equalsIgnoreCase(args[2])){
                                        sendMessage(player, "There is already a team named &6" + args[2] + "&e.");
                                        return true;
                                    }
                                }

                                if(isColor(args[3])){
                                    ChatColor color = ChatColor.valueOf(args[3].toUpperCase());
                                    Team t = new Team(args[2], color);
                                    a.addTeam(t);
                                    sendMessage(player, "Created team named " + t.getColor()+args[2] + "&e.");
                                }else{
                                    sendMessage(player, "Invalid color: &6" + args[3] + "&e.");
                                }

                            }else{
                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist!");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa addteam <name> <team name> <color>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("save")){
                        if (args.length > 1) {
                            if(arenaManager.getArena(args[1]) != null) {
                                arenaManager.serialise(arenaManager.getArena(args[1]));
                                sendMessage(player, "Saved arena &6" + args[1] + "&e.");
                            }else{

                                sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa save <name>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setlobby")){
                        if (args.length > 1) {
                            if(arenaManager.getArena(args[1]) != null) {
                                Arena a = arenaManager.getArena(args[1]);
                                a.setLobbyLocation(player.getLocation().toBlockLocation());
                                sendMessage(player, "Set the lobby of arena &6" + args[1] + "&e to &6x: "+a.getLobbyLocation().getBlockX()+" y: "+a.getLobbyLocation().getBlockY()+" z: "+a.getLobbyLocation().getBlockZ()+"&e.");
                            }else{

                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa setlobby <name>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setmainlobby")){
                        if (args.length > 1) {
                            if(arenaManager.getArena(args[1]) != null) {
                                Arena a = arenaManager.getArena(args[1]);
                                a.setMainLobbyLocation(player.getLocation().toBlockLocation());
                                sendMessage(player, "Set the mainlobby of arena &6" + args[1] + "&e to &6x: "+a.getMainLobbyLocation().getBlockX()+" y: "+a.getMainLobbyLocation().getBlockY()+" z: "+a.getMainLobbyLocation().getBlockZ()+"&e.");
                            }else{

                                sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa setmainlobby <name>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("addgen")){
                        if (args.length > 2) {
                            if(arenaManager.getArena(args[1]) != null) {

                                Arena a = arenaManager.getArena(args[1]);
                                if(args[2].equalsIgnoreCase("diamond") || args[2].equalsIgnoreCase("emerald")){
                                    Location playerLoc = player.getLocation();
                                    if(args[2].equalsIgnoreCase("diamond")){
                                          a.addDiamondGen(playerLoc);
                                        sendMessage(player, "Added a &bDIAMOND &egenerator at &6x: "+playerLoc.getBlockX()+" y: "+playerLoc.getBlockY()+" z: "+playerLoc.getBlockZ()+"&e to &6"+args[1]+"&e.");
                                    }else{
                                        a.addEmeraldGen(playerLoc);
                                        sendMessage(player, "Added a &aEMERALD &egenerator at &6x: "+playerLoc.getBlockX()+" y: "+playerLoc.getBlockY()+" z: "+playerLoc.getBlockZ()+"&e to &6"+args[1]+"&e.");
                                    }
                                }else{
                                    sendMessage(player, "&cIncorrect Usage. Do /bwa addgen <name> <diamond/emerald>");
                                }
                            }else{

                                sendMessage(player, "Arena named " + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa addgen <name> <diamond/emerald>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setteamgen")){
                        if (args.length > 1) {
                            if(arenaManager.getArena(args[1]) != null) {
                                Arena a = arenaManager.getArena(args[1]);
                                Team team = null;
                                for(Team t : a.getTeams()){
                                    if(t.getName().equals(args[2])){
                                        team = t;
                                    }
                                }
                                if(team == null){
                                    sendMessage(player, "Team named &6" + args[2] + "&e doesn't exist.");
                                    return true;
                                }
                                Location playerLoc = player.getLocation();
                                team.setGenerator(playerLoc);
                                sendMessage(player, "Set team "+team.getColor()+team.getName()+"&e's generator to &6x: "+playerLoc.getBlockX()+" y: "+playerLoc.getBlockY()+" z: "+playerLoc.getBlockZ()+"&e.");
                            }else{

                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa setteamgen <name> <team>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setspawn")){
                        if (args.length > 1) {
                            if(arenaManager.getArena(args[1]) != null) {
                                Arena a = arenaManager.getArena(args[1]);
                                Team team = null;
                                for(Team t : a.getTeams()){
                                    if(t.getName().equals(args[2])){
                                        team = t;
                                    }
                                }
                                if(team == null){
                                    sendMessage(player, "Team named &6" + args[2] + "&e doesn't exist.");
                                    return true;
                                }
                                Location playerLoc = player.getLocation();
                                team.setSpawn(playerLoc);
                                sendMessage(player, "Set team "+team.getColor()+team.getName()+"&e's spawn to &6x: "+playerLoc.getBlockX()+" y: "+playerLoc.getBlockY()+" z: "+playerLoc.getBlockZ()+"&e.");
                            }else{

                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa setspawn <name> <team>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("setbed")){
                        if (args.length > 2) {
                            if(arenaManager.getArena(args[1]) != null) {
                                Arena a = arenaManager.getArena(args[1]);
                                Team team = null;
                                for(Team t : a.getTeams()){
                                    if(t.getName().equals(args[2])){
                                        team = t;
                                    }
                                }
                                if(team == null){
                                    sendMessage(player, "Team named &6" + args[2] + "&e doesn't exist.");
                                    return true;
                                }
                                if(!player.getTargetBlock(5).getType().toString().contains("_BED")){
                                    sendMessage(player,"You must target a bed block!");
                                    return true;
                                }
                                Location playerLoc = player.getTargetBlock(5).getLocation();
                                team.setBed(playerLoc);
                                sendMessage(player,player.getTargetBlock(5).getType()+" set bed");
                                sendMessage(player, "Set team "+team.getColor()+team.getName()+"&e's bed to &6x: "+playerLoc.getBlockX()+" y: "+playerLoc.getBlockY()+" z: "+playerLoc.getBlockZ()+"&e.");
                            }else{

                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exist.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bwa setbed <name> <team>");
                        }
                    }
                    else if(args[0].equalsIgnoreCase("additemshop")){
                        Villager villager = (Villager) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                        villager.setAI(false);
                        villager.setCustomName("§i");
                        villager.setCustomNameVisible(false);
                        ArmorStand as = (ArmorStand) player.getWorld().spawn(player.getLocation().subtract(0,0.2,0), ArmorStand.class);
                        as.setVisible(false);
                        as.setCustomName(ChatColor.GOLD+""+ChatColor.BOLD+"RIGHT CLICK");
                        as.setCustomNameVisible(true);
                        as.setGravity(false);
                        as.setCollidable(false);

                        ArmorStand as1 = (ArmorStand) player.getWorld().spawn(player.getLocation().add(0,0.1,0), ArmorStand.class);
                        as1.setVisible(false);
                        as1.setCustomName(ChatColor.AQUA+"Item Shop");
                        as1.setCustomNameVisible(true);
                        as1.setGravity(false);
                        as1.setCollidable(false);
                    }
                    else if(args[0].equalsIgnoreCase("addteamshop")){
                        Villager villager = (Villager) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
                        villager.setAI(false);
                        villager.setCustomName("§t");
                        villager.setCustomNameVisible(false);
                        ArmorStand as = (ArmorStand) player.getWorld().spawn(player.getLocation().subtract(0,0.2,0), ArmorStand.class);
                        as.setVisible(false);
                        as.setCustomName(ChatColor.GOLD+""+ChatColor.BOLD+"RIGHT CLICK");
                        as.setCustomNameVisible(true);
                        as.setGravity(false);
                        as.setCollidable(false);

                        ArmorStand as1 = (ArmorStand) player.getWorld().spawn(player.getLocation().add(0,0.1,0), ArmorStand.class);
                        as1.setVisible(false);
                        as1.setCustomName(ChatColor.AQUA+"Team Upgrades");
                        as1.setCustomNameVisible(true);
                        as1.setGravity(false);
                        as1.setCollidable(false);
                    }
                    else {
                        sendMessage(player, "&cThat is not a command. Try /bwa help.");
                    }
                }else{
                    sendMessage(player, false, help);
                }
            }else{
                sender.sendMessage("This command can only be executed by a player!");
            }
        }
        if(label.equalsIgnoreCase("bw")) {
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(args.length >0){
                    if(args[0].equalsIgnoreCase("join")){
                        if (args.length > 1) {
                            if(gameManager.getGame(args[1]) != null) {
                                if(gameManager.getGame(args[1]).getPlayers().contains(player)){
                                    sendMessage(player,"&cYou are already in this game!");
                                    return true;
                                }
                                sendMessage(player, "Joined arena named &6" + args[1] + "&e.");
                                gameManager.getGame(args[1]).addPlayer(player);

                            }else{
                                sendMessage(player, "Arena named &6" + args[1] + "&e doesn't exists.");
                            }
                        }else{
                            sendMessage(player, "&cIncorrect Usage. Do /bw join <name> ");
                        }
                    }else if(args[0].equalsIgnoreCase("leave")){
                        if(gameManager.getGame(player) != null){
                            sendMessage(player,"Left arena "+gameManager.getGame(player).getName()+"!");
                            gameManager.getGame(player).removePlayer(player);

                        }else{
                            sendMessage(player,"&cYou are not in a game!");
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void sendMessage(Player player, String msg){
        String message = "&8[&6BW&8] &8> &e"+msg;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(Player player, boolean usePrefix, String msg){
        if(usePrefix) {
            String message = "&8[&6BW&8] &8> &e" + msg;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public String generateDebug(){
        String debug = "Debug:\n";
        for (Arena a : arenaManager.getArenaList()){
            debug +="   "+a.getName()+"\n" +
                    "     minplayers: "+a.getMinPlayers()+"\n" +
                    "     maxplayers: "+a.getMaxPlayers()+"\n";
            if(a.getLobbyLocation() !=null){
                debug+= "     lobby: x:"+a.getLobbyLocation().getBlockX()+" y:"+a.getLobbyLocation().getBlockY()+" z:"+a.getLobbyLocation().getBlockZ()+"\n";
            }else{
                debug+="     lobby: null\n";
            }
            if(a.getMainLobbyLocation() !=null){
                debug+= "     mainlobby: x:"+a.getMainLobbyLocation().getBlockX()+" y:"+a.getMainLobbyLocation().getBlockY()+" z:"+a.getMainLobbyLocation().getBlockZ()+"\n";
            }else{
                debug+="     mainlobby: null\n";
            }
            if(a.getDiamondGens().size() == 0){
                debug+= "     diamond-gens: null\n";
            }else{
                debug+= "     diamond-gens:\n";
                for(Location location : a.getDiamondGens()){
                   debug += "       x:"+location.getBlockX()+" y:"+location.getBlockY()+" z:"+location.getBlockZ()+"\n";
                }
            }
            if(a.getEmeraldGens().size() == 0){
                debug+= "     emerald-gens: null\n";
            }else{
                debug+= "     emerald-gens:\n";
                for(Location location : a.getEmeraldGens()){
                    debug += "       x:"+location.getBlockX()+" y:"+location.getBlockY()+" z:"+location.getBlockZ()+"\n";
                }
            }
            if(a.getTeams().size() == 0){
                debug+= "     teams: null\n";
            }else{
                debug+= "     teams:\n";
                for(Team t : a.getTeams()){
                    debug+="       "+t.getName()+":\n" +
                            "         color: "+t.getColor() + t.getName()+"&e\n";
                    if(t.getSpawn() != null){
                       debug += "         spawn: x:"+t.getSpawn().getBlockX()+" y:"+t.getSpawn().getBlockY()+" z:"+t.getSpawn().getBlockZ()+"\n";
                    }else{
                        debug += "         spawn: null\n";
                    }

                    if(t.getGenerator() != null){
                        debug += "         gen: x:"+t.getGenerator().getBlockX()+" y:"+t.getGenerator().getBlockY()+" z:"+t.getGenerator().getBlockZ()+"\n";
                    }else{
                        debug += "         gen: null\n";
                    }

                    if(t.getBed() != null){
                        debug += "         bed: x:"+t.getBed().getBlockX()+" y:"+t.getBed().getBlockY()+" z:"+t.getBed().getBlockZ()+"\n";
                    }else{
                        debug += "         bed: null\n";
                    }
                }
            }

        }
        return debug;
    }

    public static Color translateChatColorToColor(ChatColor chatColor)
    {
        switch (chatColor) {
            case AQUA:
                return Color.AQUA;
            case BLACK:
                return Color.BLACK;
            case BLUE:
                return Color.BLUE;
            case DARK_AQUA:
                return Color.BLUE;
            case DARK_BLUE:
                return Color.BLUE;
            case DARK_GRAY:
                return Color.GRAY;
            case DARK_GREEN:
                return Color.GREEN;
            case DARK_PURPLE:
                return Color.PURPLE;
            case DARK_RED:
                return Color.RED;
            case GOLD:
                return Color.YELLOW;
            case GRAY:
                return Color.GRAY;
            case GREEN:
                return Color.GREEN;
            case LIGHT_PURPLE:
                return Color.PURPLE;
            case RED:
                return Color.RED;
            case WHITE:
                return Color.WHITE;
            case YELLOW:
                return Color.YELLOW;
            default:
                break;
        }

        return null;
    }
}
