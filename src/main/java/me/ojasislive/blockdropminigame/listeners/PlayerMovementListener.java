package me.ojasislive.blockdropminigame.listeners;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class PlayerMovementListener implements Listener {
    Plugin plugin = Blockdropminigame.getInstance();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location toLocation = event.getTo();
        if(!(toLocation == null)) {

            if (!PlayerCache.getJoinedPlayers().containsKey(player.getUniqueId().toString())) return;
            if (event.getFrom().getBlock().equals(event.getTo().getBlock())) return;
            String arenaName = PlayerCache.getJoinedPlayers().get(player.getUniqueId().toString());
            Arena arena = ArenaUtils.getArenaByName(arenaName);
            if (arena != null){
                if(arena.getState().equals(ArenaState.STARTING)) event.setCancelled(true);
                if(arena.getState() != ArenaState.RUNNING) return;
                if (!ArenaUtils.isInRegion(toLocation,arena,2,2,2,2,2,2)) {
                    if(arena.getMinLocation().getY()+1>=toLocation.getY()){
                        PlayerCache.removeJoinedPlayers(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.RED+"You're out!");
                        //TODO:Check no of players in arena and stop if it is 1 to display results

                    }
                }else {

                    Block b = toLocation.getBlock().getRelative(BlockFace.DOWN);

                    if(b.getType()==Material.AIR) return;
                    b.setType(Material.YELLOW_CONCRETE);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        b.setType(Material.ORANGE_CONCRETE);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(Material.RED_CONCRETE), 20L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(Material.AIR), 30L);
                    }, 20L);
                }

            }


        }

    }
}
