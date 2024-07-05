package me.ojasislive.blockdropminigame;

import me.ojasislive.blockdropminigame.arena.Arena;
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

        Arena.init(getDataFolder());

        // Register command executor and tab completer
        getCommand("blockdrop").setExecutor(new BlockDropCommandExecutor());
        getCommand("blockdrop").setTabCompleter(new BlockDropTabCompleter());

        Bukkit.getLogger().info(ChatColor.GREEN + "[Blockdrop Minigame] Plugin Started!!");
    }

    @Override
    public void onDisable() {
        Arena.saveArenas();
        Bukkit.getLogger().info(ChatColor.RED + "[Blockdrop Minigame] Plugin Stopped!!");
    }

    public static Blockdropminigame getInstance() {
        return instance;
    }
}
