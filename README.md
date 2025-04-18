# Blockdrop Minigame

Blockdrop Minigame is a Minecraft plugin that enables players to participate in exciting block drop minigames within predefined arena regions. This plugin is designed to manage arena creation, save and load schematics, handle player commands, and start games with countdown timers.

## About: 
This minigame is the remake of the [Fall Guys](https://www.fallguys.com/) / [Stumble Guys](https://www.stumbleguys.com/) / 
Blockdrop minigame from [TheHiveMC](https://playhive.com/)

<details>
<summary>Spoiler Fallguys</summary>

![Blockdrop Minigame in Fall Guys](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/Fall_guys.png)

</details>

<details>
<summary>Spoiler Stumble Guys</summary>

![Blockdrop Minigame in Stumble Guys](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/Stumble_guys_honeydrop.png)

</details>

<details>

<summary>Spoiler HIVE</summary>

![Blockdrop Minigame in HIVE mc](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/Blockdrop_hive.png)

</details> 

## How does this game work?
When the game starts, the blocks under the players feet start disappearing, the players' goal is to stay inside the arena and try to be the last player standing to win the game.

## Features

- **Arena Management**: Create, save, and manage multiple arenas.
- **Schematic Handling**: Save and load WorldEdit schematics for arena regeneration.
- **Player Commands**: Join and leave arenas, start games, and manage arena settings.
- **Countdown Timer**: Start games with configurable countdowns.
- **State Handling**: Manage different states of the game (starting, running, ending).

## Permissions

- `blockdropminigame.admin`: Allows access to all admin commands. Default: OP

# Setting up the Plugin
## Installation Instructions:

### Download the Snapshot Plugin:
- Download the latest snapshot version of Blockdrop Minigame.

### Install Dependencies:
- Ensure you have WorldEdit version **7.2.16+** installed on your server as it's a dependency for this plugin.

### Place the Plugin:
- Place the downloaded Blockdrop Minigame snapshot `.jar` file into your server's `plugins` directory.

### Restart the Server:
- Restart your Minecraft server to load the plugin.

# In-Game Setup
Follow 8 simple steps to set up the game. I have not edited the tutorial video and the language used is Hindi, watch it only if you do not understsand the following 8 steps
https://drive.google.com/file/d/1LU8NnmtywG8nJf0MALQL7Hl2Aah-5J9s/preview

## Step 1: Create an Arena and a Lobby
To start using the plugin, you need to set up an arena where the game will take place and a lobby where players can wait before the game starts. You can use the same lobby for multiple arenas.

### For example, something like this ![Arena](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/arena.png)

## Step 2: Configure Arena Boundaries
Once the arena is created, set two temporary blocks to define the boundaries of the playing area:
1. Place one temporary block one **Y level above** the highest point of the arena.
2. Place another temporary block one **Y level below** the lowest point of the arena.
    - Make sure the blocks are in the opposite direction (body diagonal) 
    - ![Body_Diagonal](https://homework.study.com/cimages/multimages/16/capture5765884675377654389.jpg)
    - Credits: [homework.study.com](homework.study.com)
      

### For clarity, refer to the images 

### Upper Bound (one level above highest) ![lowerbound](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/Upperbound.png)
### Lower Bound (one level below lowest) ![lowerbound](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/lowerbound.png)

## Step 3: Set Positions with Commands
After placing the temporary blocks:
1. Look at one of the blocks (crosshair placed on the block) and use the command `/blockdrop pos1`. (The plugin supports tab-completion.)
2. Move to the other temporary block, look at it, and use the command `/blockdrop pos2`.
3. Once both positions are set, you can remove the temporary blocks.

## Step 4: Save and Name the Arena
Now, you need to save and name your arena for further modifications:
1. Use the command `/blockdrop arena save <your_arena_name_here>`.
2. Ensure that the name you provide is unique. The plugin will display already available arena names in the tab-completion options to help avoid duplication.
3. This action will save a schematic of the arena in the plugin's folder for internal use.
   - **Important:** Do not edit or delete the schematic file. Doing so will prevent the arena from regenerating after a game ends.

## Step 5: Set Arena Properties
To make an arena functional, its `active` property must be set to `true`. To achieve this, you need to configure the minimum and maximum players for the arena:

1. **Set Maximum Players:**
   - Use the command `/blockdrop arena settings <arena_name> set maxplayers <number>`.
   - The plugin provides tab-completion to display available arena names.
2. **Set Minimum Players:**
   - Use the command `/blockdrop arena settings <arena_name> set minplayers <number>`.

**Note:** Always set the `maxplayers` property first and then set the `minplayers` property. The plugin enforces that `minplayers` must be less than `maxplayers` and will not allow the reverse order.

## Step 6: Set Spawn Points for Players

After setting the `maxplayers` and `minplayers` properties, you need to define the spawn points where players will appear when the game starts. The number of spawn points should be **equal to** the number of `maxplayers`, neither more nor less.

1. **Set Spawn Points:**
   - Stand at the location where you want a player to spawn.
   - Use the command `/blockdrop arena settings <arena_name> add spawnlocations`.
   - The player will spawn at the exact location and orientation (yaw and pitch) where you are standing when you execute the command.
   - For Example:
   - ![Add Spawn Locations](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/add_spawnlocations.png)

2. **Repeat for All Spawn Locations:**
   - Continue to stand at each desired spawn point and use the `/blockdrop arena settings <arena_name> add spawnlocations` command for each location.
   - Ensure that the total number of spawn locations equals the `maxplayers` value set earlier.
   - **Tip**: Use the up arrow key in the chatbox to quickly repeat the previous command without typing it again.

3. **View Spawn Locations:**
   - To check the spawn locations you have set, use the command:
     - `/blockdrop arena settings <arena_name> get spawnlocations`
   - A list of spawn locations with their coordinates will be printed in the chat, starting from index 0.
   - To quickly teleport to one of the spawn locations, click on the `[TP]` beside the desired coordinates. The plugin supports clickable chat, so clicking the `[TP]` will teleport you directly to the spawn location.
   - It looks like this:
   - ![TP Example](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/TP_example.png)

4. **Remove a Spawn Location:**
   - To remove a spawn location, reference it by its index number using the following command:
     - `/blockdrop arena settings <arena_name> remove spawnlocations <index_number>`
   - This will remove the spawn location at the specified index.
   - Example:
   - ![Remove Spawn Location](https://github.com/OJASisLive/Blockdrop-Minigame/blob/master/pictures/remove_spawnlocations.png)

## Step 7: Set Lobby Location (For Post-Game Spawn)

After a player loses the game, you need to set a lobby location where they will respawn.

1. **Set Lobby Location:**
   - Go to your designated lobby area and stand in the location where you want the players to spawn.
   - Use the command `/blockdrop arena settings <arena_name> set lobbylocation`.
   - This will set the lobby location where players will spawn after being eliminated.

## Step 8: Activate the Arena

To activate the arena and make it ready for use, run the following command:

- `/blockdrop arena settings <arena_name> set active true`

This will set the arena as active, allowing players to join and start the game.

With this, your arena is set up and ready to be activated.

# Joining the Game

Once the arena is set up and activated, players can join or leave the game. However, the commands to join or leave the game can only be executed by players with admin privileges or by the console.

## Admin or Console Command

To join or leave an arena as an admin or console, you can use the following commands:

1. **Join an Arena:**
   - Use the command:
     - `/bd join <arenaName> <playerName>`
   - Replace `<arenaName>` with the name of the arena the player should join, and `<playerName>` with the name of the player joining.

2. **Leave an Arena:**
   - To remove a player from an arena, use the command:
     - `/bd leave <arenaName> <playerName>`
   - This command will remove the specified player from the arena.

## Using NPCs for Joining and Leaving

To allow players to join or leave the arena through interaction, you can set up NPCs in the lobby. When players click on the NPCs, the appropriate commands will be executed automatically. This requires the use of the **Znpcs** or **Citizens** plugin, along with **Placeholder API** to execute the commands.

#### Setting Up NPCs with Znpcs

1. **Join NPC:**
   - Use the following command to assign the join action to an NPC:
     - `/znpcs action <id> add CONSOLE bd join <arena_name> %player%`
     - Replace `<id>` with the NPC ID, and `<arena_name>` with the name of the arena.

2. **Leave NPC:**
   - Use the following command to assign the leave action to an NPC:
     - `/znpcs action <id> add CONSOLE bd leave <arena_name> %player%`
     - Replace `<id>` with the NPC ID, and `<arena_name>` with the name of the arena.

**Note:** These commands will allow players to join or leave the arena by simply clicking on the corresponding NPC in the lobby.

### Suggested Plugins

- **Znpcs** or **Citizens NPC**: These plugins allow you to create and manage NPCs for game interactions.
- **Placeholder API**: This plugin is required to execute player-specific commands like joining or leaving the arena when using Znpcs.

### Additional Tips:
- You can keep two NPCs in the lobby: one for joining the game and another for leaving the game. Set up each NPC with the corresponding command action.

# Regeneration of Arena

The plugin automatically regenerates the arena after a game ends. However, if an admin accidentally breaks blocks in the arena (since the plugin prevents players from breaking blocks, but not admins), you can manually regenerate the arena by using the following command:

- **Regenerate Arena:**
  - Use the command:
    - `/blockdrop arena regen <arena_name>`
  - Replace `<arena_name>` with the name of the arena you want to regenerate.

This command will restore the arena to its original state by loading the saved schematic for the arena.

### Additional Notes:
- Ensure that the arena is properly saved and that the schematic file is intact. Regenerating the arena will replace any changes made manually by admins.

# Enjoy the Minigame!

We hope you enjoy playing the Blockdrop Minigame! If you encounter any issues or need assistance, feel free to submit your issues through our GitHub issue tracker:

- [Submit an Issue on GitHub](https://github.com/OJASisLive/Blockdrop-Minigame/issues/new/choose)

We appreciate your feedback and will work to resolve any problems as quickly as possible.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This plugin is licensed under the MIT License. See [LICENSE](LICENSE) for more details.

## Acknowledgements

- [WorldEdit](https://enginehub.org/worldedit/) - for schematic handling.
- [SpigotMC](https://www.spigotmc.org/) - for providing the platform for plugin development.

## Made with ♥ by [Om J Shah](https://github.com/OJASisLive)
