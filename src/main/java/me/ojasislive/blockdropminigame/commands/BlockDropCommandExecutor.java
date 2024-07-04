package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.commandProcessHandlers.ArenaCommandHandler;
import me.ojasislive.blockdropminigame.commandProcessHandlers.PosCommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockDropCommandExecutor implements CommandExecutor {

    private final PosCommandHandler posCommandHandler = new PosCommandHandler();
    private final ArenaCommandHandler arenaCommandHandler = new ArenaCommandHandler();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (!(sender.hasPermission("blockdropminigame.command"))) {
            sender.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /blockdrop <pos1|pos2> OR /blockdrop arena <save|regen|delete> <name>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("pos1") || subCommand.equals("pos2")) {
            return posCommandHandler.handle(sender, subCommand);
        } else if (subCommand.equals("arena")) {
            return arenaCommandHandler.handle(sender, args);
        } else {
            sender.sendMessage("Usage: /blockdrop <pos1|pos2> OR /blockdrop arena <save|regen|delete> <name>");
            return true;
        }
    }
}
