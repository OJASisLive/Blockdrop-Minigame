package me.ojasislive.blockdropminigame;

import me.ojasislive.blockdropminigame.commands.BlockDropCommandExecutor;
import me.ojasislive.blockdropminigame.commands.BlockDropTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Blockdropminigame extends JavaPlugin {

    private static Blockdropminigame instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register command executor and tab completer
        getCommand("blockdrop").setExecutor(new BlockDropCommandExecutor());
        getCommand("blockdrop").setTabCompleter(new BlockDropTabCompleter());

        Bukkit.getLogger().info(ChatColor.GREEN + "Plugin Started!!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Plugin Stopped!!");
    }

    public static Blockdropminigame getInstance() {
        return instance;
    }
}
