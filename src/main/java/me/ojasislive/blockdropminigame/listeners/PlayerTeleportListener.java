package me.ojasislive.blockdropminigame.listeners;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.JoinLeaveHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerTP(PlayerTeleportEvent event){

        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();
        if(PlayerCache.getJoinedPlayers().containsKey(playerUUID)){
            Arena arena = ArenaUtils.getArenaByName(PlayerCache.getJoinedPlayers().get(playerUUID));
            if (arena != null) {
                if ((arena.getState() == ArenaState.STARTING | arena.getState() == ArenaState.RUNNING)) {
                    event.setCancelled(true);
                    player.sendMessage("Your teleportation has been cancelled");
                    Bukkit.getLogger().info(ChatColor.RED + "Player teleport cancelled for player " + player.getName());
                }
                else{
                    JoinLeaveHandler.leaveArena(arena.getArenaName(),player.getName());
                }
            }
            else {
                PlayerCache.removeJoinedPlayers(playerUUID);
            }
        }
    }

}
