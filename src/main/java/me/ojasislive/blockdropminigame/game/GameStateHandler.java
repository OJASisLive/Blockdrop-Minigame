package me.ojasislive.blockdropminigame.game;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameStateHandler {

    private void startGameRunnable(Arena arena){
        arena.setState(ArenaState.STARTING);
        for (int i = 0; i < 10; i++) {
            if (arena.getPlayers().size()>=i && arena.getSpawnLocations().size()>=i){
                UUID uuid = UUID.fromString(arena.getPlayers().get(i));
                Player player = Bukkit.getPlayer(uuid);
                Location location = arena.getSpawnLocations().get(i);

                if (player != null) {
                    player.teleport(location);
                    player.sendMessage(ChatColor.GOLD+"Teleported you to the game. Good luck!");
                }
                else {
                    arena.setState(ArenaState.RUNNING);
                    break;
                }
            }
        }
    }

    public void gameStarter(Arena arena){
        if(arena.getPlayers().size()>=arena.getMaxPlayersLimit()){
            ArenaUtils.getInstance().startCountdown(arena.getArenaName(), 10, () -> startGameRunnable(arena));
        }else{
            ArenaUtils.getInstance().cancelCountdown(arena.getArenaName());
        }
    }

}
