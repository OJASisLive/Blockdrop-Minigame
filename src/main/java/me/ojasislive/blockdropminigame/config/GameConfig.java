package me.ojasislive.blockdropminigame.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GameConfig {

    private static File configFile;
    private static FileConfiguration fileConfiguration;
    private static boolean saveInventoriesToFile;

    public static void init(File dataFolder) {
        configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                Bukkit.getLogger().warning("[Blockdrop-Minigame] Config file does not exist, creating a new one.");
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        // Set default values
        fileConfiguration.addDefault("savePlayerInventoriesToFile", false);

        // Copy defaults to the file and save
        fileConfiguration.options().copyDefaults(true);
        saveConfig();

        // Load the configuration values
        loadConfigValues();
    }

    public static void saveConfig() {
        try {
            fileConfiguration.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigValues() {
        saveInventoriesToFile = fileConfiguration.getBoolean("savePlayerInventoriesToFile");
    }

    public static boolean shouldSaveInventoriesToFile() {
        return saveInventoriesToFile;
    }

    public static FileConfiguration getConfig() {
        return fileConfiguration;
    }
}
