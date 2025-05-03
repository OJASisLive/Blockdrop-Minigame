package me.ojasislive.blockdropminigame.listeners;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.GameStateHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import me.ojasislive.blockdropminigame.game.mechanics.BlockMechanics;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerMovementListener implements Listener {

    private static Set<Block> getBlocksPlayerIsStandingOn(Player player) {
        Set<Block> blocks = new HashSet<>();

        // Get the player's current location
        Location playerLocation = player.getLocation();
        double playerX = playerLocation.getX();
        double playerY = playerLocation.getY();
        double playerZ = playerLocation.getZ();

        // Player's bounding box width (assume player width is 0.6 blocks)
        double playerWidth = 0.6;

        // Calculate the corner points of the player's bounding box at foot level
        Vector minCorner = new Vector(playerX - playerWidth / 2, playerY - 0.1, playerZ - playerWidth / 2);
        Vector maxCorner = new Vector(playerX + playerWidth / 2, playerY - 0.1, playerZ + playerWidth / 2);

        // Loop through each block in the bounding box
        for (double x = Math.floor(minCorner.getX()); x <= Math.floor(maxCorner.getX()); x++) {
            for (double z = Math.floor(minCorner.getZ()); z <= Math.floor(maxCorner.getZ()); z++) {
                Block block = player.getWorld().getBlockAt(new Location(player.getWorld(), x, Math.floor(playerY - 0.1), z));
                if (block.getType() != Material.AIR) {
                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    Plugin plugin = Blockdropminigame.getInstance();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location toLocation = event.getTo();
        if (!(toLocation == null)) {

            if (!PlayerCache.getJoinedPlayers().containsKey(player.getUniqueId())) return;
            String arenaName = PlayerCache.getJoinedPlayers().get(player.getUniqueId());
            Arena arena = ArenaUtils.getArenaByName(arenaName);
            if (arena != null) {
                if (arena.getState().equals(ArenaState.STARTING) & !event.getFrom().getBlock().equals(event.getTo().getBlock()))
                    event.setCancelled(true);
                if (arena.getState() != ArenaState.RUNNING) return;
                if (!ArenaUtils.isInRegion(toLocation, arena)) {
                    if (arena.getMinLocation().getY() + 1 >= toLocation.getY()) {
                        PlayerCache.removeJoinedPlayers(player.getUniqueId());
                        player.sendMessage(ChatColor.RED + "You're out!");
                        arena.addEliminatedPlayers(player.getUniqueId());
                        arena.getPlayers().remove(player.getUniqueId());
                        if (arena.getPlayers().size() == 1) {
                            UUID winnerUUID=arena.getPlayers().get(0);
                            Player winner = Bukkit.getPlayer(winnerUUID);
                            if (winner != null) {
                                winner.playSound(arena.getLobbyLocation(), Sound.ENTITY_PLAYER_LEVELUP,1f,1f);
                            }
                            PlayerCache.removeJoinedPlayers(winnerUUID);
                            arena.addEliminatedPlayers(winnerUUID);
                            arena.getPlayers().remove(winnerUUID);
                            GameStateHandler.getInstance().gameEnder(arena);
                        }

                    }
                } else {
                    //BlockMechanics.getInstance().blockChange(player, toLocation, plugin);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        Set<Block> blocksBelowPlayer = getBlocksPlayerIsStandingOn(player);
                        for (Block block : blocksBelowPlayer) {
                            BlockMechanics.getInstance().blockChange(player, block, plugin);
                        }} ,1L);

                }
            }
        }
    }
}
