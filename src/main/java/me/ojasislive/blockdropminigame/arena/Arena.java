package me.ojasislive.blockdropminigame.arena;

import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.GameStateHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Arena {


    public static Arena createArena(String arenaName, World arenaWorld, Location minLocation) {
        Arena arena = new Arena();
        arena.setArenaWorld(arenaWorld);
        arena.setArenaName(arenaName);
        arena.setMinLocation(minLocation);
        arena.setActive(false);
        arena.setState(ArenaState.WAITING);
        ArenaUtils.addArena(arena);
        ArenaUtils.saveArenas();
        return arena;
    }

    private String arenaName;
    private ArenaState state = ArenaState.WAITING;
    private boolean active = false;
    private World arenaWorld;
    private List<Location> spawnLocations = new ArrayList<>();
    private final List<String> players = new ArrayList<>();
    private Location minLocation;
    private int maxPlayersLimit;
    private String schematicFilePath; // Add this field to store the schematic file path

    public int getMaxPlayersLimit() {
        return maxPlayersLimit;
    }

    public void setMaxPlayersLimit(int maxPlayersLimit) {
        this.maxPlayersLimit = maxPlayersLimit;
    }

    public void setMinLocation(Location minLocation) {
        this.minLocation = minLocation;
    }

    public Location getMinLocation() {
        return Objects.requireNonNull(this.minLocation,"The min location is null!");
    }

    public World getArenaWorld() {
        return this.arenaWorld;
    }

    public void setArenaWorld(World arenaWorld) {
        this.arenaWorld = arenaWorld;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaName() {
        return this.arenaName;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }

    public void setActive(boolean active) {
        if(this.getState().equals(ArenaState.WAITING)){
            this.active = active && this.getSpawnLocations().size() >= this.maxPlayersLimit;
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public int addSpawnLocation(Location location) {
        if (spawnLocations.size() < 10) {
            this.spawnLocations.add(location);
            ArenaUtils.saveArenas();
            return 1;
        }
        return 0;
    }

    public void setSpawnLocations(List<Location> spawnLocations) {
        this.spawnLocations = spawnLocations;
    }

    public List<Location> getSpawnLocations() {
        return this.spawnLocations;
    }

    public int removeSpawnLocation(int index) {
        if (this.spawnLocations.size() > index) {
            this.spawnLocations.remove(index);
            ArenaUtils.saveArenas();
            return 1;
        }
        return 0;
    }


    public List<String> getPlayers() {
        return this.players;
    }

    private final List<String> playerNames = new ArrayList<>();

    public List<String> getPlayerNames() {
        for (String uuidString : this.getPlayers()){
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuidString));
            this.playerNames.add(player.getName());
        }
        return this.playerNames;
    }

    public void addPlayer(String playerUUID) {
        if(PlayerCache.addJoinedPlyers(playerUUID, this.arenaName)){
            if(this.players.size()<this.spawnLocations.size()) {
                this.players.add(playerUUID);
                GameStateHandler gameStateHandler = new GameStateHandler();
                gameStateHandler.gameStarter(this);
            }else{
                PlayerCache.removeJoinedPlayers(playerUUID);
            }
        }

    }

    public void removePlayer(String playerUUID) {
        if(!this.players.contains(playerUUID)){return;}
        if(PlayerCache.removeJoinedPlayers(playerUUID)) {
            this.players.remove(playerUUID);
            GameStateHandler gameStateHandler = new GameStateHandler();
            gameStateHandler.gameStarter(this);
        }
    }

    // Add getter and setter for schematic file path
    public String getSchematicFilePath() {
        return this.schematicFilePath;
    }

    public void setSchematicFilePath(String schematicFilePath) {
        this.schematicFilePath = schematicFilePath;
    }
}
