package me.ojasislive.blockdropminigame.commandProcessHandlers;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArenaCommandHandler {

    private static final String SAVE_SUCCESS_MESSAGE = "§8[§a✔§8] §7Schematic saved!";
    private static final String REGEN_SUCCESS_MESSAGE = "§8[§a✔§8] §7Schematic pasted at %d,%d,%d";
    private static final String DELETE_SUCCESS_MESSAGE = "§8[§a✔§8] §7Arena Schematic Deleted Successfully";

    public boolean handle(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("Usage: /blockdrop arena <save|regen|delete> <name>");
            return true;
        }

        Player player = (Player) sender;
        String subCommand = args[1].toLowerCase();
        String name = args[2];

        switch (subCommand) {
            case "save":
                // Handle save logic
                File file = new File(player.getWorld().getName(), name + ".schem");
                WEHook.saveArenaSchematic(player.getLocation(), player.getLocation(), file, player.getWorld());
                sender.sendMessage(SAVE_SUCCESS_MESSAGE);
                return true;
            case "regen":
                // Handle regen logic
                Arena arena = Arena.getArenaByName(name);
                if (arena != null) {
                    WEHook.paste(arena.getMinLocation(), new File(player.getWorld().getName(), name + ".schem"));
                    sender.sendMessage(String.format(REGEN_SUCCESS_MESSAGE, arena.getMinLocation().getBlockX(), arena.getMinLocation().getBlockY(), arena.getMinLocation().getBlockZ()));
                } else {
                    sender.sendMessage("Arena not found!");
                }
                return true;
            case "delete":
                // Handle delete logic
                boolean deleted = Arena.removeArenaByName(name);
                if (deleted) {
                    sender.sendMessage(DELETE_SUCCESS_MESSAGE);
                } else {
                    sender.sendMessage("Arena not found!");
                }
                return true;
            default:
                return false;
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return Arrays.asList("save", "regen", "delete");
        }
        return Collections.emptyList();
    }
}
