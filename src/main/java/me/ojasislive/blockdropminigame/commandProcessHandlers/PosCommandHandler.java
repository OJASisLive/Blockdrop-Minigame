package me.ojasislive.blockdropminigame.commandProcessHandlers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PosCommandHandler {

    private static final String POS1_MESSAGE = "§8[§a✔§8] §7First location set to %d,%d,%d";
    private static final String POS2_MESSAGE = "§8[§a✔§8] §7Second location set to %d,%d,%d";

    public boolean handle(CommandSender sender, String subCommand) {
        Player player = (Player) sender;

        switch (subCommand) {
            case "pos1":
                // Handle pos1 logic
                player.sendMessage(String.format(POS1_MESSAGE, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
                return true;
            case "pos2":
                // Handle pos2 logic
                player.sendMessage(String.format(POS2_MESSAGE, player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
                return true;
            default:
                return false;
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2");
        }
        return Collections.emptyList();
    }
}
