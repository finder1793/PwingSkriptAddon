package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileDirSize extends Effect {

    private Expression<String> directoryPath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        directoryPath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get size of directory " + directoryPath.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = directoryPath.getSingle(e);
        if (path != null) {
            try {
                java.nio.file.Path dirPath = java.nio.file.Paths.get(path);
                long size = java.nio.file.Files.walk(dirPath)
                    .filter(java.nio.file.Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return java.nio.file.Files.size(p);
                        } catch (Exception ex) {
                            return 0L;
                        }
                    })
                    .sum();
                System.out.println("Directory size: " + (size / 1024 / 1024) + " MB");
            } catch (Exception ex) {
                System.out.println("Failed to get directory size: " + ex.getMessage());
            }
        }
    }
}
