package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.arena.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockDropTabCompleter implements TabCompleter {
    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2", "arena");
        }

        if (args.length == 2 && "arena".equalsIgnoreCase(args[0])) {
            return Arrays.asList("save","regen","delete");
        }

        if (args.length == 3 && Arrays.asList("save","regen","delete").contains(args[1])) {
            return Arena.getArenaNamesAsList();
        }
        return Collections.emptyList();
    }
}
