package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import java.io.File;
import java.nio.file.Files;

public class EffectDeleteFile extends BaseFileEffect {
    private Expression<String> filePath;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected void execute(Event e) {
        File file = new File(filePath.getSingle(e));
        
        if (!validateFile(file, true)) {
            return;
        }

        try {
            Files.delete(file.toPath());
        } catch (Exception ex) {
            logError("deleting file", ex);
        }
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "delete file " + filePath.toString(e, debug);
    }
}
