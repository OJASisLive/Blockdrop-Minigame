package me.ojasislive.blockdropminigame.game;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class GameStateHandler {

    Plugin plugin = Blockdropminigame.getInstance();
    private void startGameRunnable(Arena arena) {
        arena.setState(ArenaState.STARTING);
        for (int i = 0; i < arena.getPlayers().size(); i++) {
            if (i < arena.getSpawnLocations().size()) {
                UUID uuid = UUID.fromString(arena.getPlayers().get(i));
                Player player = Bukkit.getPlayer(uuid);
                Location location = arena.getSpawnLocations().get(i);

                if (player != null) {
                    // Set metadata to indicate this is a game-related teleport
                    player.setMetadata("gameTeleport", new FixedMetadataValue(plugin, true));
                    player.teleport(location);
                    player.sendMessage(ChatColor.GOLD + "Teleported you to the game. Good luck!");
                } else {
                    break;
                }
            }
        }
        arena.setState(ArenaState.RUNNING);
        // Clear metadata after a short delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < arena.getPlayers().size(); i++) {
                UUID uuid = UUID.fromString(arena.getPlayers().get(i));
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.removeMetadata("gameTeleport", plugin);
                }
            }
        }, 20L);

    }

    public void gameStarter(Arena arena){
        if(arena.getPlayers().size()>=arena.getMaxPlayersLimit()){
            ArenaUtils.getInstance().startCountdown(arena.getArenaName(), 10, () -> startGameRunnable(arena));
        }else{
            ArenaUtils.getInstance().cancelCountdown(arena.getArenaName());
        }
    }

}
