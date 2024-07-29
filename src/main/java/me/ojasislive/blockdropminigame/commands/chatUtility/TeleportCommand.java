package me.ojasislive.blockdropminigame.commands.chatUtility;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeleportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bteleport") && args.length == 7) {
            Player player = Bukkit.getPlayer(UUID.fromString(args[0]));
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found!");
                return false;
            }

            try {
                World world = Bukkit.getWorld(args[1]);
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);
                float yaw = Float.parseFloat(args[5]);
                float pitch = Float.parseFloat(args[6]);

                Location location = new Location(world,x,y,z,yaw,pitch);
                player.teleport(location);
                player.sendMessage(ChatColor.GREEN + "Teleported!");
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid coordinates!");
                return false;
            }

            return true;
        }
        return false;
    }

}
