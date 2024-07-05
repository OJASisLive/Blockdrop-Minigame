package me.ojasislive.blockdropminigame.commandFunctionHandlers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PosCommandHandler {

    public static Map<UUID, Tuple<Location, Location>> handle(Player sender, String[] args, Map<UUID, Tuple<Location, Location>> selections){
        String param = args[0].toLowerCase();

        Tuple<Location, Location> selection =
                selections.getOrDefault(sender.getUniqueId(), new Tuple<>(null, null));

        switch (param) {
            case "pos1":
                Location firstLocation = sender.getTargetBlock(null, 5).getLocation();
                selection.setFirst(firstLocation);
                selections.put(sender.getUniqueId(), selection);

                sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                        +ChatColor.GRAY+
                        " First location set to "
                        + (int) firstLocation.getX() + ","
                        + (int) firstLocation.getY() + ","
                        + (int) firstLocation.getZ());
                return selections;

            case "pos2":
                Location secondLocation = sender.getTargetBlock(null, 5).getLocation();
                selection.setSecond(secondLocation);
                selections.put(sender.getUniqueId(), selection);

                sender.sendMessage(ChatColor.AQUA +"["+ ChatColor.GREEN+ "✔"+ ChatColor.AQUA + "]"
                        +ChatColor.GRAY+
                        " Second location set to "
                        + (int) secondLocation.getX() + ","
                        + (int) secondLocation.getY() + ","
                        + (int) secondLocation.getZ());
                return selections;


            default:
                sender.sendMessage(ChatColor.YELLOW+"["+ ChatColor.RED+ "🛑"+ ChatColor.YELLOW+ "]"
                        +ChatColor.GRAY+
                        "Usage: /blockdrop <pos1|pos2|arena> <save|regen|delete> <name>");
                return selections;

        }
    }

}
