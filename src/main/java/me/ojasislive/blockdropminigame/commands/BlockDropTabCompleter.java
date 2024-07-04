package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.commandProcessHandlers.ArenaCommandHandler;
import me.ojasislive.blockdropminigame.commandProcessHandlers.PosCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class BlockDropTabCompleter implements TabCompleter {

    private final PosCommandHandler posCommandHandler = new PosCommandHandler();
    private final ArenaCommandHandler arenaCommandHandler = new ArenaCommandHandler();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return posCommandHandler.onTabComplete(sender, args);
        }

        if (args.length > 1 && args[0].equalsIgnoreCase("arena")) {
            return arenaCommandHandler.onTabComplete(sender, args);
        }

        return null;
    }
}
