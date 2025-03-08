package com.pwing.pwingskriptaddon.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
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
            Clipboard clipboard = ClipboardFormats.findByFile(schematicFile).getReader(fis).read();
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
                .world(FaweAPI.getWorld(loc.getWorld().getName()))
                .build();
            ClipboardHolder holder = new ClipboardHolder(clipboard);
            ForwardExtentCopy copy = new ForwardExtentCopy(holder.getClipboard(), holder.getClipboard().getRegion(), holder.getClipboard().getOrigin(), editSession, BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()));
            Operations.complete(copy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "paste schematic " + schematicPath.toString(e, debug) + " at " + location.toString(e, debug);
    }
}
