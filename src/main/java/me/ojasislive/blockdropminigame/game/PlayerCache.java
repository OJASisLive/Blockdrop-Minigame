package me.ojasislive.blockdropminigame.game;

import java.util.HashMap;
import java.util.UUID;

public class PlayerCache {
    private static final HashMap<UUID,String> joinedPlayers=new HashMap<>();

    public static HashMap<UUID, String> getJoinedPlayers() {
        return joinedPlayers;
    }

    public static boolean addJoinedPlyers(UUID playerUUID, String arenaName){
        if(joinedPlayers.containsKey(playerUUID)){return false;}
        joinedPlayers.put(playerUUID, arenaName);
        return true;
    }

    public static boolean removeJoinedPlayers(UUID playerUUID){
        if(!joinedPlayers.containsKey(playerUUID)){return false;}
        joinedPlayers.remove(playerUUID);
        return true;
    }

}
