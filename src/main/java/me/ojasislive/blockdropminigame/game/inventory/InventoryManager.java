package me.ojasislive.blockdropminigame.game.inventory;

import me.ojasislive.blockdropminigame.Blockdropminigame;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class InventoryManager {

    Plugin plugin = Blockdropminigame.getInstance();

    private final Map<UUID, ItemStack[]> savedInventories = new HashMap<>();
    private final Map<UUID, ItemStack[]> savedArmorContents = new HashMap<>();

    public void saveInventory(Player player) {
        savedInventories.put(player.getUniqueId(), player.getInventory().getContents());
        savedArmorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
    }

    public void restoreInventory(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (savedInventories.containsKey(playerUUID) && savedArmorContents.containsKey(playerUUID)) {
            player.getInventory().setContents(savedInventories.get(playerUUID));
            player.getInventory().setArmorContents(savedArmorContents.get(playerUUID));
            savedInventories.remove(playerUUID);
            savedArmorContents.remove(playerUUID);
        } else {
            player.sendMessage(ChatColor.RED + "Error: Could not restore your inventory.");
        }
    }

    public void setupGameInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        // Example: Give the player a sword and some food
        player.getInventory().addItem(new ItemStack(Material.DIAMOND_SWORD));
        player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
    }

    public void saveInventoryToFile(Player player) {
        try {
            File file = new File(plugin.getDataFolder(),"inventories/" + player.getUniqueId() + ".dat");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            BukkitObjectOutputStream oos = new BukkitObjectOutputStream(fos);
            oos.writeObject(player.getInventory().getContents());
            oos.writeObject(player.getInventory().getArmorContents());
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreInventoryFromFile(Player player) {
        try {
            File file = new File(plugin.getDataFolder(),"inventories/" + player.getUniqueId() + ".dat");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BukkitObjectInputStream ois = new BukkitObjectInputStream(fis);
                player.getInventory().setContents((ItemStack[]) ois.readObject());
                player.getInventory().setArmorContents((ItemStack[]) ois.readObject());
                ois.close();
                file.delete();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
