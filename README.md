# Bedwars 
Paper bedwars minigame plugin!  
Supported version [1.16.4]
## Features
- Normal bedwars gameplay!
- Ingame item shop
- Team upgrades shop
- Arena break/place protection
- Arena resseting
- Countdown
- Colored armor
- Animated bossbar with custom message
- Custom messages and prefix
- Tab completion on most commands
- Placeholder API suport[COMMING SOON]
# Setup
/bwa create [arena name] - Creates arena  
/bwa setlobby [arena name] - Sets the waiting lobby  
/bwa setmainlobby [arena name] - Sets the location that the players get teleported to after the game  
/bwa addgen [arena name] [diamond/emerald] - Add a diammond or emerald generator  
/bwa debug - Displays all of the arenas  
/bwa save [arena name] - Save the arena (do this after you have added the teams)  
<h3>Teams</h3>  
/bwa addteam [arena name] [team name] [color] - Creates a new team using one of <a href="https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/ChatColor.html">these</a> colors. <br>    
/bwa setspawn [arena name] [team name] - Sets a team spawn<br>
/bwa setteamgen [arena name] [team name] - Sets a teams generator location    <br>
/bwa setbed [arena name] [team name] - Sets a teams bed location  
<h3>Villagers</h3> 

/bwa additemshop - Add a item shop at your current location  
/bwa addteamshop - Add a team upgrades shop at your current location  

# Config
servername - The ip adress displayed at the bottom of the scoreboard   
prefix - The prefix before all bedwars messages  
bossbardelay - The amount of ticks inbetween bossbar cycle(20 ticks per second)    
bossbar - A list of the bossbar messages to be displayed in the waiting lobby  

<h3>Default</h3>     

```yaml
servername: mc.server.net
prefix: '&8[&6BW&8] &8> &e'
bossbardelay: 60
bossbar:
- '&e&lPlaying &f&lBEDWARS &e&lon &a&lYOURSERVER.NET'
- '&e&lPlaying &f&lBEDWARS &e&lon &b&lYOURSERVER.NET'
- '&e&lPlaying &f&lBEDWARS &e&lon &6&lYOURSERVER.NET'


```
