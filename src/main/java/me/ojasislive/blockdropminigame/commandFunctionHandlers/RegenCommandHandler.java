package me.ojasislive.blockdropminigame.commandFunctionHandlers;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Objects;

public class RegenCommandHandler {
    public int handle(Player sender, String[] args, Plugin plugin){
        if (args.length <= 2) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Usage: /blockdrop arena regen <name>");

            return 0;
        }

        File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

        if (!file.exists()) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Schematic not found!");

            return 0;
        }

        if (Arena.getArenaByName(args[1]) == null) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Caching Error Occurred!" +
                    " - Arena was not loaded correctly." +
                    " This might have happened because of manually changing arenas.yml");

            return 0;
        }

        Location arenaMinLocation = Objects.requireNonNull(Arena.getArenaByName(args[1]),"Arena loading error!").getMinLocation();
        if (arenaMinLocation == null) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Caching Error Occurred!" +
                    " - No schematic associated with arena or the schematic was deleted.");

            return 0;
        }

        WEHook.paste(arenaMinLocation, file);
        sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                +ChatColor.GRAY+
                " Schematic pasted at "
                + (int) arenaMinLocation.getX() + ","
                + (int) arenaMinLocation.getY() + ","
                + (int) arenaMinLocation.getZ());

        return 1;
    }
}
