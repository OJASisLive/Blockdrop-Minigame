package me.ojasislive.blockdropminigame.game;

import java.util.HashMap;

public class PlayerCache {
    private static final HashMap<String,String> joinedPlayers=new HashMap<>();

    public static HashMap<String, String> getJoinedPlayers() {
        return joinedPlayers;
    }

    public static boolean addJoinedPlyers(String playerUUID, String arenaName){
        if(joinedPlayers.containsKey(playerUUID)){return false;}
        joinedPlayers.put(playerUUID, arenaName);
        return true;
    }

    public static boolean removeJoinedPlayers(String playerUUID){
        if(!joinedPlayers.containsKey(playerUUID)){return false;}
        joinedPlayers.remove(playerUUID);
        return true;
    }

}
