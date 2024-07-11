package me.ojasislive.blockdropminigame;

import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.commands.BlockDropCommandExecutor;
import me.ojasislive.blockdropminigame.commands.BlockDropTabCompleter;
import me.ojasislive.blockdropminigame.listeners.PlayerQuitListener;
import me.ojasislive.blockdropminigame.listeners.PlayerTeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Blockdropminigame extends JavaPlugin {

    private static Blockdropminigame instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);

        ArenaUtils.init(getDataFolder());

        // Register command executor and tab completer
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register command '/blockdrop'.").setExecutor(new BlockDropCommandExecutor());
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register tabcompleter for command '/blockdrop'").setTabCompleter(new BlockDropTabCompleter());

        Bukkit.getLogger().info(ChatColor.GREEN + "[Blockdrop Minigame] Plugin Started!!");
    }

    @Override
    public void onDisable() {
        ArenaUtils.saveArenas();
        Bukkit.getLogger().info(ChatColor.RED + "[Blockdrop Minigame] Plugin Stopped!!");
    }

    public static Blockdropminigame getInstance() {
        return instance;
    }
}
