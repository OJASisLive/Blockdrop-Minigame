package me.ojasislive.blockdropminigame.commandFunctionHandlers;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SettingsCommandHandler {
    public int handle(Player sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY +
                    "Usage: /blockdrop arena settings <arenaname> <get|set|add|remove> <spawnlocations|active|state|players>");
            return 0;
        }

        String arenaName = args[2];
        String action = args[3].toLowerCase();
        String setting = args[4].toLowerCase();
        Arena arena = ArenaUtils.getArenaByName(arenaName);

        if (arena == null) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "❌" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + " Arena " + arenaName + " not found!");
            return 0;
        }

        switch (action) {
            case "get":
                handleGet(sender, arena, setting);
                break;
            case "set":
                handleSet(sender, arena, setting, args);
                break;
            case "add":
                handleAdd(sender, arena, setting, args);
                break;
            case "remove":
                handleRemove(sender, arena, setting, args);
                break;
            default:
                sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                        + ChatColor.GRAY + "Usage: /blockdrop arena settings <arenaname> <get|set|add|remove> <spawnlocations|playable|active|state|players>");
                return 0;
        }

        return 1;
    }

    private void handleGet(Player sender, Arena arena, String setting) {
        switch (setting) {
            case "spawnlocations":
                sender.sendMessage(ChatColor.GREEN + "Spawn Locations: " + arena.getSpawnLocations().toString());
                break;
            case "active":
                sender.sendMessage(ChatColor.GREEN + "Active: " + arena.isActive());
                break;
            case "state":
                sender.sendMessage(ChatColor.GREEN + "State: " + arena.getState().name());
                break;
            case "players":
                sender.sendMessage(ChatColor.GREEN + "Players: " + arena.getPlayers().toString());
                break;
            case "maxplayers":
                sender.sendMessage(ChatColor.GREEN + "MaxPlayers: " + arena.getMaxPlayersLimit());
                break;
            default:
                sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                        + ChatColor.GRAY + "Invalid setting: " + setting);
                break;
        }
    }

    private void handleSet(Player sender, Arena arena, String setting, String[] args) {
        if (args.length < 6) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + "Usage: /blockdrop arena settings <arenaname> set <active|state> <value>");
            return;
        }

        String value = args[5].toLowerCase();
        switch (setting) {
            case "active":
                if(arena.getState().equals(ArenaState.WAITING)) {
                    boolean active = Boolean.parseBoolean(value);
                    arena.setActive(active);
                    sender.sendMessage(ChatColor.GREEN + "Active set to: " + active);
                }else {
                    sender.sendMessage(ChatColor.RED+"Cannot deactivate the arena because a game is going on in the arena");
                }
                break;
            case "maxplayers":
                if(arena.isActive()){
                    sender.sendMessage(ChatColor.RED + "(Error!) Active is set to: true. You need to deactivate the arena");
                    break;
                }
                int maxplayers = Integer.parseInt(value);
                arena.setMaxPlayersLimit(maxplayers);
                sender.sendMessage(ChatColor.GREEN+"Maxplayers set to: "+maxplayers);
                sender.sendMessage(ChatColor.RED + "(Reminder) Active is set to: false. You need to reactivate the arena");
                break;
            case "state":
                try {
                    ArenaState state = ArenaState.valueOf(value.toUpperCase());
                    arena.setState(state);
                    sender.sendMessage(ChatColor.GREEN + "State set to: " + state.name());
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                            + ChatColor.GRAY + "Invalid state value: " + value);
                }
                break;
            default:
                sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                        + ChatColor.GRAY + "Invalid setting: " + setting);
                break;
        }
    }

    private void handleAdd(Player sender, Arena arena, String setting, String[] args) {
        if (args.length < 5) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + "Usage: /blockdrop arena settings <arenaname> add <spawnlocations>");
            return;
        }

        if ("spawnlocations".equals(setting)) {
            Location location = sender.getLocation();
            int added = arena.addSpawnLocation(location);
            if (added == 0) {
                sender.sendMessage(ChatColor.RED + "Maximum"+arena.getMaxPlayersLimit()+" spawn locations only!");
                return;
            }
            sender.sendMessage(ChatColor.GREEN + "Added spawn location: " + location);
        } else {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + "Invalid setting: " + setting);
        }
    }

    private void handleRemove(Player sender, Arena arena, String setting, String[] args) {
        if (args.length < 6) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + "Usage: /blockdrop arena settings <arenaname> remove <spawnlocations> <value>");
            return;
        }

        if ("spawnlocations".equals(setting)) {// Assuming we want to remove by index for simplicity
            try {
                int index = Integer.parseInt(args[5]);
                int removed = arena.removeSpawnLocation(index);
                if (removed == 1) {
                    sender.sendMessage(ChatColor.GREEN + "Removed spawn location at index: " + index);
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                        + ChatColor.GRAY + "Invalid index: " + args[5]);
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY + "Invalid setting: " + setting);
        }
    }
}
