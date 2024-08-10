package me.ojasislive.blockdropminigame.game;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class JoinLeaveHandler {

    public static String joinArena(String arenaName, String playerName) {
        Arena arena = ArenaUtils.getArenaByName(arenaName);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (arena == null) {
            return ChatColor.RED + "No such arena: " + arenaName + " or arena does not exist";
        }
        if (!offlinePlayer.isOnline()) {
            return ChatColor.RED + "Player: " + playerName + " is offline";
        }
        Player player = (Player) offlinePlayer;
        if (!arena.isActive()) {
            return ChatColor.RED + "Arena: " + arenaName + " is not active";
        }
        if (arena.getState() != ArenaState.WAITING) {
            return ChatColor.RED + "Arena: " + arenaName + " is in a game right now";
        }
        if (arena.getPlayers().contains(player.getUniqueId().toString())) {
            return ChatColor.RED + "Player " + playerName + " is already playing!";
        }
        arena.addPlayer(player);

        return ChatColor.GREEN + "Added player: " + playerName + " to the arena: " + arenaName + " successfully.";
    }

    public static String leaveArena(String arenaName, String playerName) {
        Arena arena = ArenaUtils.getArenaByName(arenaName);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (arena == null) {
            return ChatColor.RED + "No such arena: " + arenaName + " or arena does not exist";
        }
        if (!offlinePlayer.isOnline()) {
            return ChatColor.RED + "Player: " + playerName + " is offline";
        }
        Player player = (Player) offlinePlayer;
        if (!arena.isActive()) {
            return ChatColor.RED + "Arena: " + arenaName + " is not active";
        }
        if (arena.getState() != ArenaState.WAITING && arena.getState() != ArenaState.RESULTS) {
            return ChatColor.RED + "Arena: " + arenaName + " is in a game right now";
        }
        if (!arena.getPlayers().contains(player.getUniqueId().toString())) {
            return ChatColor.RED + "Player " + playerName + " is not already playing!";
        }
        arena.removePlayer(player);

        return ChatColor.GREEN + "Removed player: " + playerName + " from the arena: " + arenaName + " successfully.";
    }
}
