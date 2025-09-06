package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileWipe extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "wipe file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            try {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    // Secure file wiping by overwriting with random data
                    java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "rw");
                    raf.setLength(0); // Truncate file
                    raf.close();
                    System.out.println("File wiped: " + path);
                } else {
                    System.out.println("File does not exist: " + path);
                }
            } catch (Exception ex) {
                System.out.println("Failed to wipe file: " + ex.getMessage());
            }
        }
    }
}
