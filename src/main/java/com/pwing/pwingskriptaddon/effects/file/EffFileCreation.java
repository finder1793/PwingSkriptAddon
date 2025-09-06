package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileCreation extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get creation time of file " + filePath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            try {
                java.nio.file.Path filePath = java.nio.file.Paths.get(path);
                java.nio.file.attribute.BasicFileAttributes attrs = java.nio.file.Files.readAttributes(filePath, java.nio.file.attribute.BasicFileAttributes.class);
                java.nio.file.attribute.FileTime creationTime = attrs.creationTime();
                System.out.println("File creation time: " + creationTime.toString());
            } catch (Exception ex) {
                System.out.println("Failed to get file creation time: " + ex.getMessage());
            }
        }
    }
}
