package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.nio.file.Files;

public class EffectCopyDirectory extends BaseFileEffect {
    private Expression<String> source;
    private Expression<String> destination;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        source = (Expression<String>) exprs[0];
        destination = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    protected void execute(Event e) {
        File srcDir = new File(source.getSingle(e));
        File destDir = new File(destination.getSingle(e));

        if (!validateFile(srcDir, true)) {
            return;
        }

        try {
            Files.walk(srcDir.toPath())
                .forEach(sourcePath -> {
                    try {
                        Files.copy(sourcePath, destDir.toPath().resolve(srcDir.toPath().relativize(sourcePath)));
                    } catch (Exception ex) {
                        logError("copying directory content", ex);
                    }
                });
        } catch (Exception ex) {
            logError("copying directory", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "copy directory " + source.toString(e, debug) + " to " + destination.toString(e, debug);
    }
}
