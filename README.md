# BedwarsPlus - ALPHA [![Download at SpigotMC.org](https://img.shields.io/badge/download-SpigotMC.org-orange.svg)](https://www.spigotmc.org/resources/92767/) 
Lightweight recreation of Hypixels Bedwars plugin for paper!  
Supported versions [1.16.*]
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
<b>Warning: Each bedwars arena must be in its own world.</b>  
You can use plugins like <a href="https://www.spigotmc.org/resources/multiverse-core.390/">Multiverse Core</a> to load or create worlds.  
  
/bwa create [arena name] - Creates arena  
/bwa setlobby [arena name] - Sets the waiting lobby  
/bwa setmainlobby [arena name] - Sets the location that the players get teleported to after the game  
/bwa addgen [arena name] [diamond/emerald] - Add a diamond or emerald generator  
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
######################################################################################################################
#  /$$$$$$$                  /$$                                             /$$$$$$$  /$$
#  | $$__  $$                | $$                                            | $$__  $$| $$
#  | $$  \ $$  /$$$$$$   /$$$$$$$ /$$  /$$  /$$  /$$$$$$   /$$$$$$   /$$$$$$$| $$  \ $$| $$ /$$   /$$  /$$$$$$$
#  | $$$$$$$  /$$__  $$ /$$__  $$| $$ | $$ | $$ |____  $$ /$$__  $$ /$$_____/| $$$$$$$/| $$| $$  | $$ /$$_____/
#  | $$__  $$| $$$$$$$$| $$  | $$| $$ | $$ | $$  /$$$$$$$| $$  \__/|  $$$$$$ | $$____/ | $$| $$  | $$|  $$$$$$
#  | $$  \ $$| $$_____/| $$  | $$| $$ | $$ | $$ /$$__  $$| $$       \____  $$| $$      | $$| $$  | $$ \____  $$
#  | $$$$$$$/|  $$$$$$$|  $$$$$$$|  $$$$$/$$$$/|  $$$$$$$| $$       /$$$$$$$/| $$      | $$|  $$$$$$/ /$$$$$$$/
#  |_______/  \_______/ \_______/ \_____/\___/  \_______/|__/      |_______/ |__/      |__/ \______/ |_______/
######################################################################################################################

################# CHAT #################
#The prefix for most in game mesages
prefix: '&8[&6BW&8] &8> '
#Main color
primary-color: '&e'
#Secondary color
secondary-color: '&6'
#The command success message color
success-color: '&a'
#The color of error messages
error-color: '&c'

################# BossBar #################
#Bossbar cycle time (in ticks (20 per second))
bossbardelay: 20
#Bossbar messages
bossbar:
 - '&e&lPlaying &f&lBEDWARS &e&lon &a&lYOURSERVER.NET'
  - '&e&lPlaying &f&lBEDWARS &e&lon &b&lYOURSERVER.NET'
  - '&e&lPlaying &f&lBEDWARS &e&lon &6&lYOURSERVER.NET'
################# Scoreboard #################
#Server name displayed in yellow at the bottom of the scoreboard
servername: yourserver.net
#Main scoreboard color
scoreboard-primary: "&f"
#Secondary scoreboard color
scoreboard-secondary: "&a"
# Team size to mode text
# Example: 1 player team is 'Solos'
# Example: 2 player team is 'Duos'
#
# Anything not in the list below will display
# as %teamsize%v%teamsize%v%teamsize% for
# the amount of teams

gamemodes:
 - '1:Solos'
  - '2:Duos'
  - '3:Trios'

```
