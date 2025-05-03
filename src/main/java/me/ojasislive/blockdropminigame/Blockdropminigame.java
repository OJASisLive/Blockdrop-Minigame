package me.ojasislive.blockdropminigame;

import me.ojasislive.blockdropminigame.arena.Arena;
import me.ojasislive.blockdropminigame.arena.ArenaUtils;
import me.ojasislive.blockdropminigame.commands.BlockDropCommandExecutor;
import me.ojasislive.blockdropminigame.commands.BlockDropTabCompleter;
import me.ojasislive.blockdropminigame.commands.chatUtility.AnsiColors;
import me.ojasislive.blockdropminigame.commands.chatUtility.TeleportCommand;
import me.ojasislive.blockdropminigame.listeners.PlayerMovementListener;
import me.ojasislive.blockdropminigame.listeners.PlayerQuitListener;
import me.ojasislive.blockdropminigame.listeners.PlayerTeleportListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Blockdropminigame extends JavaPlugin {

    private static Blockdropminigame instance;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getLogger().info(AnsiColors.BLUE + "----------------------------------------------------------"+AnsiColors.RESET);
        Bukkit.getLogger().info(AnsiColors.GOLD + "BlockDrop Minigame"+AnsiColors.RESET);
        Bukkit.getLogger().info(AnsiColors.AQUA + "Made with " + AnsiColors.RED + "♥" + AnsiColors.AQUA + " by OJASisLive aka Om J Shah"+AnsiColors.RESET);
        Bukkit.getLogger().info(AnsiColors.AQUA + "Please report any bug/crash to our Github Issue Tracker at"+AnsiColors.RESET);
        Bukkit.getLogger().info(AnsiColors.AQUA + "https://github.com/OJASisLive/Blockdrop-Minigame/issues"+AnsiColors.RESET);
        Bukkit.getLogger().info(AnsiColors.BLUE + "----------------------------------------------------------"+AnsiColors.RESET);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(),this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);

        ArenaUtils.init(getDataFolder());

        // Register command executor and tab completer
        Objects.requireNonNull(getCommand("bteleport"),"Unable to register command '/bteleport'.").setExecutor(new TeleportCommand());
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register command '/blockdrop'.").setExecutor(new BlockDropCommandExecutor());
        Objects.requireNonNull(getCommand("blockdrop"),"Unable to register tabcompleter for command '/blockdrop'").setTabCompleter(new BlockDropTabCompleter());


        Bukkit.getLogger().info(AnsiColors.GOLD + "[Blockdrop Minigame] Plugin Started!!"+AnsiColors.RESET);
    }

    @Override
    public void onDisable() {
        for (Arena arena: ArenaUtils.getArenas()){
            if (arena.isActive()){
                List<UUID> playerUUIDs = new ArrayList<>(arena.getPlayers());
                for (UUID playerUUID: playerUUIDs){
                    Player player = Bukkit.getPlayer(playerUUID);
                    if (player != null) {
                        arena.removePlayer(player);
                        player.teleport(arena.getLobbyLocation());
                    }
                }
            }
        }
        ArenaUtils.saveArenas();
        Bukkit.getLogger().info(AnsiColors.RED + "[Blockdrop Minigame] Plugin Stopped!!"+AnsiColors.RESET);
    }

    public static Blockdropminigame getInstance() {
        return instance;
    }
}
