package me.ojasislive.blockdropminigame.listeners;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.GameStateHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import me.ojasislive.blockdropminigame.game.mechanics.BlockMechanics;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
            String arenaName = PlayerCache.getJoinedPlayers().get(player.getUniqueId().toString());
            Arena arena = ArenaUtils.getArenaByName(arenaName);
            if (arena != null){
                if(arena.getState().equals(ArenaState.STARTING) & !event.getFrom().getBlock().equals(event.getTo().getBlock())) event.setCancelled(true);
                if(arena.getState() != ArenaState.RUNNING) return;
                if (!ArenaUtils.isInRegion(toLocation,arena)) {
                    if(arena.getMinLocation().getY()+1>=toLocation.getY()){
                        PlayerCache.removeJoinedPlayers(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.RED+"You're out!");
                        arena.addEliminatedPlayers(player.getUniqueId().toString());
                        arena.getPlayers().remove(player.getUniqueId().toString());
                        if(arena.getPlayers().size()==0){
                            GameStateHandler.getInstance().gameEnder(arena);
                        }

                    }
                }else {

                    BlockMechanics.getInstance().blockChange(player,toLocation,plugin);

                }

            }


        }

    }
}
