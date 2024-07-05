package me.ojasislive.blockdropminigame.commandFunctionHandlers;

import me.ojasislive.blockdropminigame.arena.Arena;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DeleteCommandHandler {
    public int handle(Player sender, String[] args){
        if (args.length <= 2) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Usage: /blockdrop arena delete <name>");

            return 0;
        }

        Boolean deleted = Arena.removeArenaByName(args[2]);
        if (deleted.equals(false)) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    " Arena not found!");
            return 0;
        }

        sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                +ChatColor.GRAY+
                " Arena & Schematic Deleted Successfully ");


        return 1;
    }
}
