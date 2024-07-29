package me.ojasislive.blockdropminigame.game.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class BlockMechanics {

    private static final BlockMechanics instance = new BlockMechanics();

    private BlockMechanics() {
        // private constructor to prevent instantiation
    }

    public static BlockMechanics getInstance() {
        return instance;
    }

    private static final List<Block> blocks = new ArrayList<>();

    public void blockChange(Player player, Location toLocation, Plugin plugin){
        Block b = toLocation.getBlock().getRelative(BlockFace.DOWN);
        if (blocks.contains(b)) return;
        if(b.getType()== Material.AIR) return;
        player.playSound(toLocation, Sound.BLOCK_STONE_PLACE,1f,1f);
        blocks.add(b);
        b.setType(Material.YELLOW_CONCRETE);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            b.setType(Material.ORANGE_CONCRETE);
            Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(Material.RED_CONCRETE), 20L);
            Bukkit.getScheduler().runTaskLater(plugin, () -> b.setType(Material.AIR), 30L);
        }, 20L);
    }

}
