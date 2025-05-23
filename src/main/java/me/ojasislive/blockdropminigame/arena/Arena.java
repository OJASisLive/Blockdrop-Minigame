package me.ojasislive.blockdropminigame.arena;

import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.GameStateHandler;
import me.ojasislive.blockdropminigame.game.PlayerCache;
import me.ojasislive.blockdropminigame.game.inventory.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Arena {


    public static Arena createArena(String arenaName, World arenaWorld, Location minLocation, Location maxLocation) {
        Arena arena = new Arena();
        arena.setArenaWorld(arenaWorld);
        arena.setArenaName(arenaName);
        arena.setMinLocation(minLocation);
        arena.setMaxLocation(maxLocation);
        arena.setActive(false);
        arena.setState(ArenaState.WAITING);
        ArenaUtils.addArena(arena);
        return arena;
    }

    private String arenaName;
    private ArenaState state = ArenaState.WAITING;
    private boolean active = false;
    private World arenaWorld;
    private List<Location> spawnLocations = new ArrayList<>();
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> eliminatedPlayers = new ArrayList<>();
    private Location minLocation;
    private Location maxLocation;
    private Location lobbyLocation;
    private int minPlayersLimit;
    private int maxPlayersLimit;
    private String schematicFilePath; // Add this field to store the schematic file path

    public int getMinPlayersLimit() {
        return minPlayersLimit;
    }

    public void setMinPlayersLimit(int minPlayersLimit) {
        this.minPlayersLimit = minPlayersLimit;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }

    public List<UUID> getEliminatedPlayers() {
        return eliminatedPlayers;
    }

    public void addEliminatedPlayers(UUID playerUUID){
        this.eliminatedPlayers.add(playerUUID);
    }


    public void setMaxLocation(Location maxLocation) {
        this.maxLocation = maxLocation;
    }

    public Location getMaxLocation() {
        return maxLocation;
    }

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
        return Objects.requireNonNull(this.minLocation,"[Blockdrop-Minigame] The min location is null!");
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
            this.active = active && this.spawnLocations.size() >= this.maxPlayersLimit;
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public int addSpawnLocation(Location location) {
        if (this.spawnLocations.size() < this.maxPlayersLimit) {
            if (ArenaUtils.isInRegion(location,this)) {
                this.spawnLocations.add(location);
                return 1; //successful
            }else {return 2;} //Location out of bonds
        }
        return 0;//max locations reached
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
            return 1;
        }
        return 0;
    }


    public List<UUID> getPlayers() {
        return this.players;
    }

    private final List<String> playerNames = new ArrayList<>();

    public List<String> getPlayerNames() {
        for (UUID uuid : this.getPlayers()){
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            this.playerNames.add(player.getName());
        }
        return this.playerNames;
    }

    private static final InventoryManager inventoryManager = new InventoryManager();
    public void addPlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if(PlayerCache.addJoinedPlyers(playerUUID, this.arenaName)){
            if(this.players.size()<this.spawnLocations.size()) {
                this.players.add(playerUUID);
                inventoryManager.saveInventory(player);
                inventoryManager.setupGameInventory(player);
                GameStateHandler gameStateHandler = GameStateHandler.getInstance();
                gameStateHandler.gameStarter(this);
            }else{
                PlayerCache.removeJoinedPlayers(playerUUID);
            }
        }

    }

    public void removePlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if(PlayerCache.removeJoinedPlayers(playerUUID)) {
            this.players.remove(playerUUID);
            GameStateHandler gameStateHandler = GameStateHandler.getInstance();
            gameStateHandler.gameStarter(this);
            inventoryManager.restoreInventory(player);
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
