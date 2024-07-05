package me.ojasislive.blockdropminigame.commandFunctionHandlers;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class SaveCommandHandler {
    public int handle(Player sender, String[] args, Tuple<Location, Location> selection, Plugin plugin) {
        if (!arePositionsSelected(sender, selection)) {
            return 0;
        }

        if (args.length <= 2) {
            sendUsageMessage(sender);
            return 0;
        }

        String arenaName = args[2];
        if (ArenaUtils.getArenaByName(arenaName) != null) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "❌" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY +
                    "The arena " + arenaName + " already exists! Delete it first.");
            return 0;
        }

        if (!createSchematicDirectory(sender, arenaName, plugin)) {
            return 0;
        }

        saveSchematic(sender, selection, arenaName);
        return 1;
    }

    private boolean arePositionsSelected(Player sender, Tuple<Location, Location> selection) {
        if (selection.getFirst() == null || selection.getSecond() == null) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "❌" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY +
                    " Please select both positions first using /blockdrop pos1 and /blockdrop pos2");
            return false;
        }
        return true;
    }

    private void sendUsageMessage(Player sender) {
        sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                + ChatColor.GRAY +
                "Usage: /blockdrop arena save <name>");
    }

    private boolean createSchematicDirectory(Player sender, String arenaName, Plugin plugin) {
        File file = new File(plugin.getDataFolder(), "schematic/" + arenaName + ".schem");

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            sender.sendMessage(ChatColor.YELLOW + "[" + ChatColor.RED + "🛑" + ChatColor.YELLOW + "]"
                    + ChatColor.GRAY +
                    "Error - Unable to make directory.");
            return false;
        }
        return true;
    }

    private void saveSchematic(Player sender, Tuple<Location, Location> selection, String arenaName) {
        Plugin plugin = Blockdropminigame.getInstance();
        File file = new File(plugin.getDataFolder(), "schematic/" + arenaName + ".schem");

        WEHook.saveArenaSchematic(selection.getFirst(), selection.getSecond(), file, sender.getWorld());
        sender.sendMessage(ChatColor.AQUA + "[" + ChatColor.GREEN + "✔" + ChatColor.AQUA + "]"
                + ChatColor.GRAY +
                " Schematic saved!");
    }
}
