package me.ojasislive.blockdropminigame.game;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.mechanics.BlockMechanics;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.UUID;

public class GameStateHandler {

    private static final GameStateHandler instance = new GameStateHandler();

    private GameStateHandler() {
        // private constructor to prevent instantiation
    }

    public static GameStateHandler getInstance() {
        return instance;
    }

    Plugin plugin = Blockdropminigame.getInstance();
    private void startGameRunnable(Arena arena) {
        arena.setState(ArenaState.STARTING);
        for (int i = 0; i < arena.getPlayers().size(); i++) {
            if (i < arena.getSpawnLocations().size()) {
                UUID uuid = arena.getPlayers().get(i);
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
        for (UUID playerUUID : arena.getPlayers()){
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                BlockMechanics.getInstance().blockChange(player,player.getLocation(),plugin);
            }
        }
        ArenaUtils.getInstance().cancelCountdown(arena.getArenaName());
        // Clear metadata after a short delay
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (int i = 0; i < arena.getPlayers().size(); i++) {
                UUID uuid = arena.getPlayers().get(i);
                Player player = Bukkit.getPlayer(uuid);
                if (player != null) {
                    player.removeMetadata("gameTeleport", plugin);
                }
            }
        }, 20L);

    }

    public void gameStarter(Arena arena){
        if(arena.getPlayers().size()>=arena.getMinPlayersLimit()){
            ArenaUtils.getInstance().startCountdown(arena.getArenaName(), 10, () -> startGameRunnable(arena));
        }else{
            ArenaUtils.getInstance().cancelCountdown(arena.getArenaName());
        }
    }

    public void gameEnder(Arena arena) {
        arena.setState(ArenaState.RESULTS);
        int i = arena.getEliminatedPlayers().size() - 1;
        int rank = 1;

        while (i >= 0) {
            UUID uuid = arena.getEliminatedPlayers().get(i);
            Player player = Bukkit.getPlayer(uuid);

            if (player == null) {
                rank++;
                i--;
                continue;
            }

            switch (rank) {
                case 1:
                    player.sendMessage(ChatColor.GOLD + "Congratulations! You are the first winner!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);
                    break;
                case 2:
                    player.sendMessage(ChatColor.GOLD + "Congratulations! You are the second winner!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);
                    break;
                case 3:
                    player.sendMessage(ChatColor.GOLD + "Congratulations! You are the third winner!");
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);
                    break;
                default:
                    player.sendMessage(ChatColor.GREEN + "Well done! You ranked #" + rank + "!");
                    break;
            }

            player.teleport(arena.getLobbyLocation());
            arena.getEliminatedPlayers().remove(uuid);
            JoinLeaveHandler.leaveArena(arena.getArenaName(),player.getName());
            rank++;
            i--;
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {String schematicFilePath = arena.getSchematicFilePath();
            if (schematicFilePath != null) {
                WEHook.paste(arena.getMinLocation(),new File(schematicFilePath));//regenerate arena
                Bukkit.getLogger().info("[Blockdrop-Minigame] Regenerated arena "+arena.getArenaName());
                arena.setState(ArenaState.WAITING);
            }else{
                arena.setActive(false);
                Bukkit.getLogger().severe("[Blockdrop-Minigame] Missing Schematic file for arena "+arena.getArenaName());
            }
        },200L);

    }

}
