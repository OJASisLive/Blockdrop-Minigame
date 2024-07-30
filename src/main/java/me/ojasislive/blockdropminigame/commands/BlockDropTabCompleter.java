package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlockDropTabCompleter implements TabCompleter {
    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2", "arena","join","leave");
        }

        if (args.length == 2 && "arena".equalsIgnoreCase(args[0])) {
            return Arrays.asList("save", "regen", "delete", "settings");
        }

        if (args.length == 2 && Arrays.asList("join","leave").contains(args[0].toLowerCase())) {
            return ArenaUtils.getArenaNamesAsList();
        }

        if (args.length == 3 && Arrays.asList("save", "regen", "delete", "settings").contains(args[1])) {
            return ArenaUtils.getArenaNamesAsList();
        }

        if (args.length == 4 && "settings".equalsIgnoreCase(args[1])) {
            return Arrays.asList("get", "set", "add", "remove");
        }

        if (args.length == 5 && "settings".equalsIgnoreCase(args[1])) {
            if (Arrays.asList("get","g").contains(args[3].toLowerCase())) {
                return Arrays.asList("spawnlocations", "active", "state","maxplayers","minplayers","players","lobbylocation");
            }if (Arrays.asList("set","s").contains(args[3].toLowerCase())) {
                return Arrays.asList("active", "state","maxplayers","minplayers","lobbylocation");
            }
            if (Arrays.asList("add", "remove", "a", "r").contains(args[3].toLowerCase())) {
                return Collections.singletonList("spawnlocations");
            }
        }

        if (args.length == 5 && "settings".equalsIgnoreCase(args[1]) && "get".equalsIgnoreCase(args[3]) &&
        "players".equalsIgnoreCase(args[4])) {
            if (ArenaUtils.getArenaNamesAsList().contains(args[2])){
                return Objects.requireNonNull(ArenaUtils.getArenaByName(args[2]),"Arena not instanced yet").getPlayerNames();
                //return ArenaUtils.getArenaByName(args[2]).getPlayers();
            }
        }


        // Further completions for specific values
        if (args.length == 6 && "settings".equalsIgnoreCase(args[1]) && "set".equalsIgnoreCase(args[3])) {
            if ("state".equalsIgnoreCase(args[4])) {
                return Arrays.asList("WAITING", "STARTING", "RUNNING", "RESULTS");
            }
            if (Arrays.asList("active","act").contains(args[4].toLowerCase())) {
                return Arrays.asList("true", "false");
            }
        }

        return Collections.emptyList();
    }
}
