package me.ojasislive.blockdropminigame.listeners;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.GameStateHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(PlayerCache.getJoinedPlayers().containsKey(player.getUniqueId().toString())){
            String arenaName = PlayerCache.getJoinedPlayers().get(player.getUniqueId().toString());
            Arena arena = ArenaUtils.getArenaByName(arenaName);
            if (arena != null) {
                arena.removePlayer(player);
                arena.getEliminatedPlayers().remove(player.getUniqueId().toString());
                if(arena.getPlayers().size()==1 && arena.getState().equals(ArenaState.RUNNING)){
                    String winnerUUIDstring=arena.getPlayers().get(0);
                    Player winner = Bukkit.getPlayer(UUID.fromString(winnerUUIDstring));
                    if (winner != null) {
                        winner.playSound(arena.getLobbyLocation(), Sound.ENTITY_PLAYER_LEVELUP,1f,1f);
                    }
                    PlayerCache.removeJoinedPlayers(winnerUUIDstring);
                    arena.addEliminatedPlayers(winnerUUIDstring);
                    arena.getPlayers().remove(winnerUUIDstring);
                    GameStateHandler.getInstance().gameEnder(arena);
                }
            }else {
                Bukkit.getLogger().severe("Arena "+arenaName+" does not exist!");
                Bukkit.getLogger().severe("Player "+player.getName()+" can not be removed from non existing Arena!");

            }
            Bukkit.getLogger().info(ChatColor.GREEN +player.getName()+" removed from Arena "+arenaName);
        }
    }
}
