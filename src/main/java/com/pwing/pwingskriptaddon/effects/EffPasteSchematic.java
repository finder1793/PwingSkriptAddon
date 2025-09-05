package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.event.Event;

import java.io.File;
import java.io.FileInputStream;

public class EffPasteSchematic extends Effect {

    private Expression<String> schematicPath;
    private Expression<Location> location;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        schematicPath = (Expression<String>) exprs[0];
        location = (Expression<Location>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        String path = schematicPath.getSingle(e);
        Location loc = location.getSingle(e);

        if (path == null || loc == null) return;

        File schematicFile = new File(path);
        if (!schematicFile.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(schematicFile)) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) {
                return;
            }
            Clipboard clipboard = format.getReader(fis).read();
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(FaweAPI.getWorld(loc.getWorld().getName()))
                .build();
            try (ClipboardHolder holder = new ClipboardHolder(clipboard)) {
                Operation operation = holder.createPaste(editSession)
                    .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                    .build();
                Operations.complete(operation);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "paste schematic " + schematicPath.toString(e, debug) + " at " + location.toString(e, debug);
    }
}
