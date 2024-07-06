package me.ojasislive.blockdropminigame.game;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class JoinLeaveHandler {
    public static String joinArena(String arenaName,String playerName){
        Arena arena = ArenaUtils.getArenaByName(arenaName);
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        if (arena == null){return ChatColor.RED+"No such arena: "+arenaName+" or arena does not exist";} //0 means no such arena or arena does not exist
        if (!offlinePlayer.isOnline()){return ChatColor.RED+"Player: "+playerName+" is offline";}//1 means player is offline
        if (!arena.isActive()){return ChatColor.RED+"Arena: "+arenaName+" is not active";}//2 means arena is not active
        if (arena.getState()!=ArenaState.WAITING){return ChatColor.RED+"Arena: "+arenaName+" is in a game right now";}//3 means arena is in a game right now
        if (arena.getPlayers().contains(offlinePlayer.getUniqueId().toString())){return ChatColor.RED+"Player "+playerName+" is already playing!";}
        arena.addPlayer(offlinePlayer.getUniqueId().toString());

        return ChatColor.GREEN +"Added player: "+playerName+" to the arena: "+arenaName+" susscesfully.";
        //TODO:Add a method to check if the player is added in an arena to avoid adding same player in multiple arenas
        //TODO:Add leaveArena method
        //TODO:Add a listener if player leaves remove the player from arena's player list
    }
}
