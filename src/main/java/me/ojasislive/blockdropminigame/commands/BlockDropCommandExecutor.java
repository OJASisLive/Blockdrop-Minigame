package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.commandFunctionHandlers.*;
import me.ojasislive.blockdropminigame.game.JoinLeaveHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BlockDropCommandExecutor implements CommandExecutor {

    // TODO Move into PlayerCache
    private Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender.hasPermission("blockdropminigame.admin"))){
            sender.sendMessage("You don't have the permission to use this command!");

            return true;
        }

        if (args.length < 3 && sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Usage: /blockdrop <join|leave> <arena name> <player name>");

            return true;
        }

        if (args.length == 3 && ("join".equalsIgnoreCase(args[0]))){
            String arenaName = args[1];
            String playerName = args[2];
            String msg = JoinLeaveHandler.joinArena(arenaName,playerName);
            if (sender instanceof ConsoleCommandSender){
                Bukkit.getLogger().info(msg);
            }
            if (sender instanceof Player){
                Player player = (Player) sender;
                player.sendMessage(msg);
            }
            return true;
        }










        //Below are the commands which need players to execute them
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");

            return true;
        }

        if (!(sender.hasPermission("blockdropminigame.admin"))){
            sender.sendMessage("You don't have the permission to use this command!");

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                    +ChatColor.GRAY+
                    "Usage: /blockdrop <pos1|pos2|arena> <save|regen|delete|settings> <name>"+
                    "Usage: /blockdrop <join|leave> <arena name> <player name>");

            return true;
        }


        Blockdropminigame plugin = Blockdropminigame.getInstance();
        Player player = (Player) sender;

        if(args.length == 1) {
            PosCommandHandler posCommandHandler = new PosCommandHandler();
            selections = posCommandHandler.handle(player,args,selections);
        }

        if(args.length > 1 && args[0].equals("arena")) {
            String param = args[1].toLowerCase();


            switch (param) {
                case "save": {
                    Tuple<Location, Location> selection =
                            selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));
                    SaveCommandHandler saveCommandHandler = new SaveCommandHandler();
                    if (saveCommandHandler.handle(player,args,selection,plugin) == 0){
                        return true;
                    }
                    break;
                }

                case "regen": {
                    RegenCommandHandler regenCommandHandler = new RegenCommandHandler();
                    if (regenCommandHandler.handle(player,args,plugin) == 0){
                        return true;
                    }
                    break;
                }

                case "delete": {
                    DeleteCommandHandler deleteCommandHandler = new DeleteCommandHandler();
                    if (deleteCommandHandler.handle(player,args) == 0){
                        return true;
                    }
                    break;
                }

                case "settings": {
                    SettingsCommandHandler settingsCommandHandler = new SettingsCommandHandler();
                    if (settingsCommandHandler.handle(player, args) == 0) {
                        return true;
                    }
                    break;
                }

                default:
                    sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                            +ChatColor.GRAY+
                            "Usage: /blockdrop <pos1|pos2|arena> <save|regen|delete> <name>");

                    break;
            }
        }


        return true;
    }

}