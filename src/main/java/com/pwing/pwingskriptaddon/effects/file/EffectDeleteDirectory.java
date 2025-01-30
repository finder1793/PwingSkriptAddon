package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.nio.file.Files;

public class EffectDeleteDirectory extends BaseFileEffect {
    private Expression<String> directoryPath;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        directoryPath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        File dir = new File(directoryPath.getSingle(e));
        
        if (!validateFile(dir, true)) {
            return;
        }

        try {
            Files.walk(dir.toPath())
                .sorted((a, b) -> b.compareTo(a))  // Reverse order to delete contents first
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (Exception ex) {
                        logError("deleting directory content", ex);
                    }
                });
        } catch (Exception ex) {
            logError("deleting directory", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "delete directory " + directoryPath.toString(e, debug);
    }
}
