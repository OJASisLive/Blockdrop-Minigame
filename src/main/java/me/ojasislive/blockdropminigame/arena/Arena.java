package me.ojasislive.blockdropminigame.arena;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Arena {

    private static List<Arena> arenas = new ArrayList<>();
    private static File arenasFile;
    private static FileConfiguration arenasConfig;

    public static void init(File dataFolder) {
        arenasFile = new File(dataFolder, "arenas.yml");
        if (!arenasFile.exists()) {
            try {
                arenasFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        loadArenas();
    }

    public static void saveArenas() {
        for (Arena arena : arenas) {
            if (arena.getArenaWorld() == null) {
                Bukkit.getLogger().warning("Arena world is null for arena: " + arena.getArenaName());
                continue; // Skip saving this arena if world is null
            }

            String path = "arenas." + arena.getArenaName();
            arenasConfig.set(path + ".world", arena.getArenaWorld().getName());
            arenasConfig.set(path + ".minLocation", serializeLocation(arena.getMinLocation()));
            arenasConfig.set(path + ".spawnLocations", serializeLocations(arena.getSpawnLocations()));
            arenasConfig.set(path + ".schematicFilePath", arena.getSchematicFilePath()); // Save the schematic file path

        }
        try {
            arenasConfig.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadArenas() {
        if (arenasConfig.contains("arenas")) {
            for (String arenaName : arenasConfig.getConfigurationSection("arenas").getKeys(false)) {
                String path = "arenas."+arenaName;
                World world = Bukkit.getWorld(arenasConfig.getString(path+".world"));
                if (world == null) {
                    Bukkit.getLogger().warning("World not found for arena: " + arenaName);
                    continue; // Skip loading this arena if world is not found
                }
                Location minLocation = deserializeLocation(arenasConfig.getString(path + ".minLocation"));
                List<Location> spawnLocations = deserializeLocations(arenasConfig.getStringList(path + ".spawnLocations"));
                String schematicFilePath = arenasConfig.getString(path + ".schematicFilePath"); // Load the schematic file path
                Arena arena = createArena(arenaName, world, minLocation);
                arena.spawnLocations = spawnLocations;
                arena.setSchematicFilePath(schematicFilePath); // Set the schematic file path
            }
        }
    }

    private static String serializeLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

    private static Location deserializeLocation(String str) {
        String[] parts = str.split(",");
        World world = Bukkit.getWorld(parts[0]);
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        float yaw = Float.parseFloat(parts[4]);
        float pitch = Float.parseFloat(parts[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    private static List<String> serializeLocations(List<Location> locations) {
        List<String> serializedLocations = new ArrayList<>();
        for (Location loc : locations) {
            serializedLocations.add(serializeLocation(loc));
        }
        return serializedLocations;
    }

    private static List<Location> deserializeLocations(List<String> strList) {
        List<Location> locations = new ArrayList<>();
        for (String str : strList) {
            locations.add(deserializeLocation(str));
        }
        return locations;
    }

    public static List<Arena> getArenas() {
        return arenas;
    }

    public static void addArena(Arena arena) {
        arenas.add(arena);
    }

    public static Arena createArena(String arenaName, World arenaWorld, Location minLocation) {
        Arena arena = new Arena();
        arena.setArenaWorld(arenaWorld);
        arena.setArenaName(arenaName);
        arena.setMinLocation(minLocation);
        arena.setPlayable(false);
        arena.setActive(false);
        addArena(arena);
        saveArenas();
        return arena;
    }

    public static Arena getArenaByName(String arenaName) {
        for (Arena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(arenaName)) {
                return arena;
            }
        }
        return null; // or throw an exception if you prefer
    }

    private static final Blockdropminigame plugin = Blockdropminigame.getInstance();

    public static boolean removeArenaByName(String arenaName) {
        Iterator<Arena> iterator = arenas.iterator();
        while (iterator.hasNext()) {
            Arena arena = iterator.next();
            if (arena.getArenaName().equals(arenaName)) {
                File file = new File(plugin.getDataFolder(), "schematic/" + arenaName + ".schem");
                boolean isDeleted = file.delete();
                List<String> arenasInConfig =arenasConfig.getStringList("arenas");
                arenasInConfig.remove(arenaName);
                arenasConfig.set("arenas",arenasInConfig);
                try {
                    arenasConfig.save(arenasFile);
                    if (isDeleted | !file.exists()) {
                        iterator.remove();
                        saveArenas();
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false; // or throw an exception if you prefer
    }

    public static List<String> getArenaNamesAsList() {
        List<String> names = new ArrayList<>();
        for (Arena arena : arenas) {
            names.add(arena.getArenaName());
        }
        return names;
    }

    private String arenaName;
    private boolean active = false;
    private boolean playable;
    private World arenaWorld;
    private List<Location> spawnLocations = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Location minLocation;
    private String schematicFilePath; // Add this field to store the schematic file path

    public void setMinLocation(Location minLocation) {
        this.minLocation = minLocation;
    }

    public Location getMinLocation() {
        return minLocation;
    }

    public World getArenaWorld() {
        return arenaWorld;
    }

    public void setArenaWorld(World arenaWorld) {
        this.arenaWorld = arenaWorld;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaName() {
        return arenaName;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean playable) {
        this.playable = playable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public int addSpawnLocation(Location location) {
        if (spawnLocations.size() < 10) {
            spawnLocations.add(location);
            saveArenas();
            return 1;
        }
        return 0;
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public int removeSpawnLocation(int index) {
        if (spawnLocations.size() > index) {
            spawnLocations.remove(index);
            saveArenas();
            return 1;
        }
        return 0;
    }

    public void clearSpawnLocations() {
        spawnLocations.clear();
        saveArenas();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    // Add getter and setter for schematic file path
    public String getSchematicFilePath() {
        return schematicFilePath;
    }

    public void setSchematicFilePath(String schematicFilePath) {
        this.schematicFilePath = schematicFilePath;
    }
}
