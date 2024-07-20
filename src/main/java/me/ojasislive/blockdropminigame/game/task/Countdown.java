package me.ojasislive.blockdropminigame.game.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    public void start() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (time == 0) {
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The game is starting now!");
                    onStart.run();
                    cancel();
                    return;
                }

                if (time % 10 == 0 || time <= 5) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "The game starts in " + time + " seconds!");
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
