package me.ojasislive.blockdropminigame.commands;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;


import java.io.File;
import java.util.*;

public final class BlockDropCommandExecutor implements CommandExecutor, TabCompleter {

    // TODO Move into PlayerCache
    private final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");

            return true;
        }

        if (!(sender.hasPermission("blockdropminigame.arena"))){
            sender.sendMessage("You don't have the permission to use this command!");

            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /arenaregion <pos1|pos2|save <name>|paste <name>>");

            return true;
        }

        Blockdropminigame plugin = Blockdropminigame.getInstance();
        Player player = (Player) sender;
        String param = args[0].toLowerCase();

        Tuple<Location, Location> selection = selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        switch (param) {
            case "pos1":
                Location firstLocation = player.getTargetBlock(null, 5).getLocation();
                selection.setFirst(firstLocation);

                sender.sendMessage("§8[§a✔§8] §7First location set to "+(int)firstLocation.getX()+","+(int)firstLocation.getY()+","+(int)firstLocation.getZ());
                selections.put(player.getUniqueId(), selection);

                break;
            case "pos2":
                Location secondLocation = player.getTargetBlock(null, 5).getLocation();
                selection.setSecond(secondLocation);

                sender.sendMessage("§8[§a✔§8] §7Second location set to "+(int)secondLocation.getX()+","+(int)secondLocation.getY()+","+(int)secondLocation.getZ());
                selections.put(player.getUniqueId(), selection);

                break;
            case "save": {
                if (selection.getFirst() == null || selection.getSecond() == null) {
                    sender.sendMessage("§8[§c❌§8] §7Please select both positions first using /arenaregion pos1 and /arenaregion pos2");

                    return true;
                }

                if (args.length != 2) {
                    sender.sendMessage("§8[§6🛑§8] §7Usage: /arenaregion save <name>");

                    return true;
                }

                if (Arena.getArenaByName(args[1]) != null) {
                    sender.sendMessage("§8[§c❌§8] §7The arena " + args[1] + " §7already exists! Delete it first.");
                    return true;
                }

                File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");
                //Arena.createArena(args[1], player.getWorld(), selection.getFirst()); //Implemented in saveArenaSchematic()


                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                WEHook.saveArenaSchematic(selection.getFirst(), selection.getSecond(), file, player.getWorld());
                sender.sendMessage("§8[§a✔§8] §7Schematic saved!");

                break;
            }
            case "regen": {
                if (args.length != 2) {
                    sender.sendMessage("§8[§6🛑§8] §7Usage: /arenaregion regen <name>");

                    return true;
                }

                File file = new File(plugin.getDataFolder(), "schematic/" + args[1] + ".schem");

                if (!file.exists()) {
                    sender.sendMessage("§8[§c❌§8] §7Schematic not found!");
                    return true;
                }

                Location arenaMinLocation = Arena.getArenaByName(args[1]).getMinLocation();
                if (arenaMinLocation == null){
                    sender.sendMessage("§8[§c❌§8] §7Caching Error Occurred!");
                    return true;
                }

                WEHook.paste(arenaMinLocation, file);
                sender.sendMessage("§8[§a✔§8] §7Schematic pasted at " + arenaMinLocation);

                break;
            }
            case "delete": {
                if (args.length != 2) {
                    sender.sendMessage("§8[§6🛑§8] §7Usage: /arenaregion delete <name>");

                    return true;
                }

                Boolean deleted = Arena.removeArenaByName(args[1]);
                if(deleted.equals(false)){
                    sender.sendMessage("§8[§c❌§8] §7Arena not found!");
                    return true;
                }

                sender.sendMessage("§8[§a✔§8] §7Arena Schematic Deleted Successfully");

                break;
            }
            default:
                sender.sendMessage("§8[§6🛑§8] §7Usage: /arenaregion <pos1|pos2|save <name>|paste <name>>");
                break;
        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Debug statement to print the args array length and its contents
        //System.out.println("args length: " + args.length);
        //for (int i = 0; i < args.length; i++) {
        //    System.out.println("args[" + i + "]: " + args[i]);
        //}

        if (args.length == 1) {
            return Arrays.asList("pos1", "pos2", "save", "regen","delete");
        }

        if (args.length == 2 && ("regen".equalsIgnoreCase(args[0]) | "delete".equalsIgnoreCase(args[0]))) {
            // Debug statement to print the retrieved arena names
            //System.out.println("Arena names: " + arenaNamesAsList);
            return Arena.getArenaNamesAsList();
        }

        return Collections.emptyList();
    }

    private static class Tuple<A, B> {
        private A first;
        private B second;

        public Tuple(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public void setFirst(A first) {
            this.first = first;
        }

        public B getSecond() {
            return second;
        }

        public void setSecond(B second) {
            this.second = second;
        }
    }
}