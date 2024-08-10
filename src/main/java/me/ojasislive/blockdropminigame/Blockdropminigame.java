package me.ojasislive.blockdropminigame;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.commands.BlockDropCommandExecutor;
import me.ojasislive.blockdropminigame.commands.BlockDropTabCompleter;
import me.ojasislive.blockdropminigame.commands.chatUtility.TeleportCommand;
import me.ojasislive.blockdropminigame.listeners.PlayerMovementListener;
import me.ojasislive.blockdropminigame.listeners.PlayerQuitListener;
import me.ojasislive.blockdropminigame.listeners.PlayerTeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

public final class Blockdropminigame extends JavaPlugin {

    private static Blockdropminigame instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getLogger().info(ChatColor.BLUE + "----------------------------------------------------------");
        Bukkit.getLogger().info(ChatColor.GOLD + "BlockDrop Minigame");
        Bukkit.getLogger().info(ChatColor.AQUA + "Made with " + ChatColor.RED + "♥" + ChatColor.AQUA + " by OJASisLive aka Om J Shah");
        Bukkit.getLogger().info(ChatColor.AQUA + "Please report any bug/crash to our Github Issue Tracker at");
        Bukkit.getLogger().info(ChatColor.AQUA + "https://github.com/OJASisLive/Blockdrop-Minigame/issues");
        Bukkit.getLogger().info(ChatColor.BLUE + "----------------------------------------------------------");

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);

        ArenaUtils.init(getDataFolder());

        // Register command executor and tab completer
        Objects.requireNonNull(getCommand("bteleport"),"Unable to register command '/bteleport'.").setExecutor(new TeleportCommand());
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register command '/blockdrop'.").setExecutor(new BlockDropCommandExecutor());
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register tabcompleter for command '/blockdrop'").setTabCompleter(new BlockDropTabCompleter());


        Bukkit.getLogger().info(ChatColor.GOLD + "[Blockdrop Minigame] Plugin Started!!");
    }

    @Override
    public void onDisable() {
        for (Arena arena: ArenaUtils.getArenas()){
            if (arena.isActive()){
                for (String playerUUIDString: arena.getPlayers()){
                    Player player = Bukkit.getPlayer(UUID.fromString(playerUUIDString));
                    if (player != null) {
                        arena.removePlayer(player);
                    }
                }
            }
        }
        ArenaUtils.saveArenas();
        Bukkit.getLogger().info(ChatColor.RED + "[Blockdrop Minigame] Plugin Stopped!!");
    }

    public static Blockdropminigame getInstance() {
        return instance;
    }
}
