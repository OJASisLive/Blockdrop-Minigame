# Blockdrop Minigame

Blockdrop Minigame is a Minecraft plugin that enables players to participate in
exciting block drop minigames within predefined arena regions.
This plugin is designed to manage arena creation, save and load schematics,
handle player commands, and start games with countdown timers.

## Features

- **Arena Management**: Create, save, and manage multiple arenas.
- **Schematic Handling**: Save and load WorldEdit schematics for arena regeneration.
- **Player Commands**: Join and leave arenas, start games, and manage arena settings.
- **Countdown Timer**: Start games with configurable countdowns.
- **State Handling**: Manage different states of the game (starting, running, ending).

## Commands

### `/blockdrop pos1,pos2`
Set positions for creating an arena.

### `/blockdrop arena <save|regen|delete> <name>`
Manage arenas with save, regen, and delete subcommands.

### `/bd join <arenaName> <playerName>`
Join a player to an arena.

### `/bd leave <arenaName> <playerName>`
Remove a player from an arena.

## Permissions

- `blockdropminigame.admin`: Allows access to all admin commands. Default: OP

## Usage

### Setting Positions
1. Select the two positions of the arena using `/blockdrop pos1` and `/blockdrop pos2`.
2. Save the arena using `/blockdrop arena save <name>`.

### Starting a Game
1. Join players to the arena using `/bd join <arenaName> <playerName>`.
2. The game will start automatically if the number of players in the arena reaches the required limit.

### Resetting or Deleting an Arena
1. Regenerate the arena using `/blockdrop arena regen <name>`.
2. Delete the arena using `/blockdrop arena delete <name>`.

## Configuration

The plugin configuration is managed in `arenas.yml`. Here's an example:

```yaml
arenas:
  myArena:
    world: world
    minLocation: world,100,64,200,0,0
    spawnLocations:
      - world,102,64,202,0,0
      - world,104,64,204,0,0
    maxplayers: 0
    active: false
    schematicFilePath: myArena.schem
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This plugin is licensed under the MIT License. See [LICENSE](LICENSE) for more details.

## Issues

If you encounter any issues, please report them on the GitHub [issue tracker](https://github.com/OJASisLive/Blockdrop-Minigame/issues).

## Acknowledgements

- [WorldEdit](https://enginehub.org/worldedit/) - for schematic handling.
- [SpigotMC](https://www.spigotmc.org/) - for providing the platform for plugin development.

## Made with ♥ by [Om J Shah](https://github.com/OJASisLive)

---

Thank you for using Blockdrop Minigame! Enjoy your game!
