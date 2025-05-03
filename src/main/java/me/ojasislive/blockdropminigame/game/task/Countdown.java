package me.ojasislive.blockdropminigame.game.task;

import me.ojasislive.blockdropminigame.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Countdown {

    private final Plugin plugin;
    private int time;
    private final Runnable onStart;
    private BukkitRunnable task;

    public Countdown(Plugin plugin, int time, Runnable onStart) {
        this.plugin = plugin;
        this.time = time;
        this.onStart = onStart;
    }

    public void start(Arena arena) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (time == 0) {
                    for (UUID uuid : arena.getPlayers()){
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            player.sendMessage(ChatColor.GREEN + "The game is starting now!");
                        }
                    }
                    onStart.run();
                    cancel();
                    return;
                }

                if (time % 10 == 0 || time <= 5) {
                    for (UUID uuid : arena.getPlayers()){
                        Player player = Bukkit.getPlayer(uuid);
                        if (player != null) {
                            player.sendMessage(ChatColor.YELLOW + "The game starts in " + time + " seconds!");
                            player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_FAIL,1f,1f);
                        }
                    }
                }

                time--;
            }
        };
        task.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second
    }

    public void cancel() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
    }

    public void reset(int newTime) {
        cancel();
        this.time = newTime;
    }
}
