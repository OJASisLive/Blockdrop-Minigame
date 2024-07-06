package me.ojasislive.blockdropminigame.hooks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.Closer;
import me.ojasislive.blockdropminigame.arena.Arena;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class WEHook {

    public static void saveArenaSchematic(Location primary, Location secondary, File schematicFile, org.bukkit.World bukkitWorld) {
        try (Closer closer = Closer.create()) {
            Region region = new CuboidRegion(BukkitAdapter.asBlockVector(primary), BukkitAdapter.asBlockVector(secondary));
            EditSession editSession = createEditSession(primary.getWorld());

            BlockVector3 minPoint = region.getMinimumPoint();

            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
            ForwardExtentCopy copy = new ForwardExtentCopy(editSession, region, clipboard, minPoint);

            Operations.complete(copy);

            FileOutputStream outputStream = closer.register(new FileOutputStream(schematicFile));
            ClipboardWriter writer = closer.register(BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(outputStream));

            writer.write(clipboard);

            String name = schematicFile.getName();
            // Remove the .schem extension if it exists
            if (name.endsWith(".schem")) {
                name = name.substring(0, name.lastIndexOf(".schem"));
            }

            Location location = new Location(bukkitWorld, minPoint.getX(), minPoint.getY(), minPoint.getZ());
            Arena arena = Arena.createArena(name, bukkitWorld, location);
            arena.setSchematicFilePath(schematicFile.getAbsolutePath()); // Set the schematic file path

        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    public static void paste(Location to, File schematicFile) {
        try (EditSession session = createEditSession(to.getWorld())) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format != null) {
                ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));

                Clipboard schematic = reader.read();

                Operation operation = new ClipboardHolder(schematic)
                        .createPaste(session)
                        .to(BukkitAdapter.asBlockVector(to))
                        .build();

                Operations.complete(operation);
            }
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private static EditSession createEditSession(World world) {
        return WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(world));
    }
}
