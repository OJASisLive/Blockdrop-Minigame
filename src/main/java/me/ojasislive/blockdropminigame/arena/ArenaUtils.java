package me.ojasislive.blockdropminigame.arena;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import me.ojasislive.blockdropminigame.game.ArenaState;
import me.ojasislive.blockdropminigame.game.task.Countdown;
import me.ojasislive.blockdropminigame.hooks.WEHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ArenaUtils {

    private static final List<Arena> arenas = new ArrayList<>();

    public static List<Arena> getArenas() {
        return arenas;
    }

    public static void addArena(Arena arena) {
        arenas.add(arena);
    }

    public static Arena getArenaByName(String arenaName) {
        for (Arena arena : arenas) {
            if (arena.getArenaName().equalsIgnoreCase(arenaName)) {
                return arena;
            }
        }
        return null; // or throw an exception if you prefer
    }

    public static List<String> getArenaNamesAsList() {
        List<String> names = new ArrayList<>();
        for (Arena arena : arenas) {
            names.add(arena.getArenaName());
        }
        return names;
    }

    public static boolean isInRegion(Location location, Arena arena){
        return isInRegion(location,arena,0,0,0,0,0,0);
    }

    public static boolean isInRegion(Location location, Arena arena, int xMinOffset, int yMinOffset, int zMinOffset,
                                     int xMaxOffset, int yMaxOffset, int zMaxOffset){
        Location minLocation = arena.getMinLocation();
        Location maxLocation = arena.getMaxLocation();
        return minLocation.getX()+xMinOffset <= location.getX() & location.getX() <= maxLocation.getX()-xMaxOffset &
                minLocation.getY()+yMinOffset <= location.getY() & location.getY() <= maxLocation.getY()-yMaxOffset &
                minLocation.getZ()+zMinOffset <= location.getZ() & location.getZ() <= maxLocation.getZ()-zMaxOffset;
        //TODO:This method's logic needs to be fixed
    }

    private static File arenasFile;
    private static FileConfiguration arenasConfig;

    public static void init(File dataFolder) {
        arenasFile = new File(dataFolder, "arenas.yml");
        if (!arenasFile.exists()) {
            try {
                Bukkit.getLogger().warning("[Blockdrop-Minigame] Arena file does not exist");
                //noinspection ResultOfMethodCallIgnored
                arenasFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        arenasConfig = YamlConfiguration.loadConfiguration(arenasFile);
        loadArenas();

    }

    public static void saveArenas() {
        for (Arena arena : getArenas()) {
            if (arena.getArenaWorld() == null) {
                Bukkit.getLogger().warning("[Blockdrop-Minigame] Arena world is null for arena: " + arena.getArenaName());
                continue; // Skip saving this arena if world is null
            }

            String path = "arenas." + arena.getArenaName();
            arenasConfig.set(path + ".world", arena.getArenaWorld().getName());
            arenasConfig.set(path + ".minLocation", serializeLocation(arena.getMinLocation()));
            arenasConfig.set(path + ".maxLocation", serializeLocation(arena.getMaxLocation()));
            if (arena.getLobbyLocation()==null){
                arenasConfig.set(path + ".lobbyLocation", serializeLocation(arena.getMinLocation()));
                Bukkit.getLogger().warning("[Blockdrop-Minigame] Set the lobby location temporarily the same as minlocation for arena " + arena.getArenaName());
                Bukkit.getLogger().warning(ChatColor.RED+"[Blockdrop-Minigame] Active is set to false for " + arena.getArenaName());
                Bukkit.getLogger().warning("[Blockdrop-Minigame] Please set a better lobby location for " + arena.getArenaName());
            }else {
                arenasConfig.set(path + ".lobbyLocation", serializeLocation(arena.getLobbyLocation()));
            }
            arenasConfig.set(path + ".spawnLocations", serializeLocations(arena.getSpawnLocations()));
            arenasConfig.set(path + ".schematicFilePath", arena.getSchematicFilePath()); // Save the schematic file path
            arenasConfig.set(path + ".maxplayers",arena.getMaxPlayersLimit());
            arenasConfig.set(path + ".active",arena.isActive());
            Bukkit.getLogger().info(ChatColor.AQUA+"[Blockdrop-Minigame] Saved Arena "+arena.getArenaName());
        }
        try {
            arenasConfig.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loadArenas() {
        if (arenasConfig.contains("arenas") && arenasConfig.getConfigurationSection("arenas")!=null) {

            for (String arenaName : (Objects.requireNonNull(arenasConfig.getConfigurationSection("arenas"))).getKeys(false)) {
                String path = "arenas." + arenaName;
                World world = Bukkit.getWorld(Objects.requireNonNull(arenasConfig.getString(path + ".world"),
                        "[Blockdrop-Minigame] World not found for arena: " + arenaName));
                if (world == null) {
                    Bukkit.getLogger().warning("[Blockdrop-Minigame] World not found for arena: " + arenaName);
                    continue; // Skip loading this arena if world is not found
                }

                int maxplayers = arenasConfig.getInt(path + ".maxplayers");
                boolean active = Boolean.parseBoolean(arenasConfig.getString(path + ".active"));

                List<Location> spawnLocations = deserializeLocations(arenasConfig.getStringList(path + ".spawnLocations"));
                String schematicFilePath = arenasConfig.getString(path + ".schematicFilePath"); // Load the schematic file path

                String minLocationString = arenasConfig.getString(path + ".minLocation");
                String maxLocationString = arenasConfig.getString(path + ".maxLocation");
                String lobbyLocationString = arenasConfig.getString(path + ".lobbyLocation");

                Location minLocation = null;
                Location maxLocation = null;
                Location lobbyLocation = null;

                if (minLocationString == null | maxLocationString == null | lobbyLocationString == null) {
                    Bukkit.getLogger().warning("[Blockdrop-Minigame] Minlocation/Maxlocation/LobbyLocation is not set for arena " + arenaName);
                    List<Location> minMaxLocationsFromSchematic = WEHook.getMinMaxLocationsFromSchematic(schematicFilePath,world);
                    if (minMaxLocationsFromSchematic.size() != 2) {
                        Bukkit.getLogger().severe("[Blockdrop-Minigame] Schematic not found for arena " + arenaName);
                        Bukkit.getLogger().severe("[Blockdrop-Minigame] This arena won't be loaded");
                        continue;
                    } else {
                        if (maxLocationString == null) {
                            Bukkit.getLogger().warning("[Blockdrop-Minigame] Trying to auto-configure Maxlocation for arena " + arenaName);
                            maxLocation = minMaxLocationsFromSchematic.get(1);
                            if (minLocationString != null) {
                                minLocation = deserializeLocation(minLocationString);
                            }
                            if (lobbyLocationString != null) {
                                lobbyLocation = deserializeLocation(lobbyLocationString);
                            }
                        }
                        if (minLocationString == null) {
                            Bukkit.getLogger().warning("[Blockdrop-Minigame] Trying to auto-configure Minlocation for arena " + arenaName);
                            minLocation = minMaxLocationsFromSchematic.get(0);
                            if (maxLocationString != null) {
                                maxLocation = deserializeLocation(maxLocationString);
                            }
                            if (lobbyLocationString != null) {
                                lobbyLocation = deserializeLocation(lobbyLocationString);
                            }
                        }
                        if (lobbyLocationString == null) {
                            Bukkit.getLogger().warning("[Blockdrop-Minigame] Set the lobby location temporarily the same as minlocation for arena " + arenaName);
                            Bukkit.getLogger().warning(ChatColor.RED+"[Blockdrop-Minigame] Active is set to false for " + arenaName);
                            Bukkit.getLogger().warning("[Blockdrop-Minigame] Please set a better lobby location for " + arenaName);
                            lobbyLocation = minLocation;
                            active = false;
                            if (maxLocationString != null) {
                                maxLocation = deserializeLocation(maxLocationString);
                            }
                            if (minLocationString != null) {
                                minLocation = deserializeLocation(minLocationString);
                            }
                        }
                    }
                }else{
                    minLocation = deserializeLocation(minLocationString);
                    maxLocation = deserializeLocation(maxLocationString);
                    lobbyLocation = deserializeLocation(lobbyLocationString);
                }
                Arena arena = Arena.createArena(arenaName, world, minLocation, maxLocation);
                arena.setMaxPlayersLimit(maxplayers);
                arena.setLobbyLocation(lobbyLocation);
                arena.setState(ArenaState.WAITING);
                arena.setSpawnLocations(spawnLocations);
                arena.setActive(active);//fixed logical error
                arena.setSchematicFilePath(schematicFilePath); // Set the schematic file path
                if (schematicFilePath != null) {
                    WEHook.paste(arena.getMinLocation(),new File(schematicFilePath));//regenerate arena on loading
                    Bukkit.getLogger().info(ChatColor.AQUA+"[Blockdrop-Minigame] Regenerated arena "+arenaName);
                }
            }
        }
    }

    private static String serializeLocation(Location location) {
        return Objects.requireNonNull(location.getWorld(), "[Blockdrop-Minigame] No world for location " + location).getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
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

    private static Blockdropminigame plugin = Blockdropminigame.getInstance();

    public static boolean removeArenaByName(String arenaName) {
        Iterator<Arena> iterator = getArenas().iterator();
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
                        return true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //CountDown logic
    private final Map<String, Countdown> countdowns = new HashMap<>();


    public void startCountdown(String arenaName, int time, Runnable onStart) {
        if (!countdowns.containsKey(arenaName)) {
            Countdown countdown = new Countdown(plugin, time, onStart);
            countdowns.put(arenaName, countdown);
            countdown.start(ArenaUtils.getArenaByName(arenaName));
        }
    }

    /*public void resetCountdown(String arenaName, int newTime) {
        if (countdowns.containsKey(arenaName)) {
            countdowns.get(arenaName).reset(newTime);
        } else {
            startCountdown(arenaName, newTime, () -> {}); // Provide a default onStart action
        }
    }*/

    public void cancelCountdown(String arenaName) {
        if (countdowns.containsKey(arenaName)) {
            countdowns.get(arenaName).cancel();
            countdowns.remove(arenaName);
        }
    }
    private static ArenaUtils instance;
    private ArenaUtils(Blockdropminigame plugin) {
        ArenaUtils.plugin = plugin;
    }

    public static ArenaUtils getInstance() {
        if (instance == null) {
            instance = new ArenaUtils(Blockdropminigame.getInstance());
        }
        return instance;
    }

}
