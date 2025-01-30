package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.nio.file.Files;

public class EffectCopyFile extends BaseFileEffect {
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
        File srcFile = new File(source.getSingle(e));
        File destFile = new File(destination.getSingle(e));

        if (!validateFile(srcFile, true)) {
            return;
        }

        try {
            Files.copy(srcFile.toPath(), destFile.toPath());
        } catch (Exception ex) {
            logError("copying file", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "copy file " + source.toString(e, debug) + " to " + destination.toString(e, debug);
    }
}
