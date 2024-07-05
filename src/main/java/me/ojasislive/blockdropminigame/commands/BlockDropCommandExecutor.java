package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.commandFunctionHandlers.PosCommandHandler;
import me.ojasislive.blockdropminigame.commandFunctionHandlers.SaveCommandHandler;
import me.ojasislive.blockdropminigame.commandFunctionHandlers.Tuple;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BlockDropCommandExecutor implements CommandExecutor {

    // TODO Move into PlayerCache
    private Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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
                    "Usage: /blockdrop <pos1|pos2|arena> <save|regen|delete> <name>");

            return true;
        }

        Blockdropminigame plugin = Blockdropminigame.getInstance();
        Player player = (Player) sender;

        if(args.length == 1) {
            selections = PosCommandHandler.handle(player,args,selections);
        }

        if(args.length > 1 && args[0].equals("arena")) {
            String param = args[1].toLowerCase();


            switch (param) {
                case "save": {
                    Tuple<Location, Location> selection =
                            selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));
                    if (SaveCommandHandler.handle(player,args,selection) == 0){
                        return true;
                    }
                    break;
                }

                case "regen": {
                    if (args.length <= 2) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                "Usage: /blockdrop arena regen <name>");

                        return true;
                    }

                    File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

                    if (!file.exists()) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                "Schematic not found!");

                        return true;
                    }

                    if (Arena.getArenaByName(args[1]) == null) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                "Caching Error Occurred!" +
                                " - Arena was not loaded correctly." +
                                " This might have happened because of manually changing arenas.yml");

                        return true;
                    }

                    Location arenaMinLocation = Arena.getArenaByName(args[1]).getMinLocation();
                    if (arenaMinLocation == null) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                "Caching Error Occurred!" +
                                " - No schematic associated with arena or the schematic was deleted.");

                        return true;
                    }

                    WEHook.paste(arenaMinLocation, file);
                    sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                            +ChatColor.GRAY+
                            " Schematic pasted at "
                            + (int) arenaMinLocation.getX() + ","
                            + (int) arenaMinLocation.getY() + ","
                            + (int) arenaMinLocation.getZ());

                    break;
                }

                case "delete": {
                    if (args.length <= 2) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                "Usage: /blockdrop arena delete <name>");

                        return true;
                    }

                    Boolean deleted = Arena.removeArenaByName(args[2]);
                    if (deleted.equals(false)) {
                        sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "❌"+ ChatColor.YELLOW+ "]"
                                +ChatColor.GRAY+
                                " Arena not found!");
                        return true;
                    }

                    sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                            +ChatColor.GRAY+
                            " Arena & Schematic Deleted Successfully ");


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