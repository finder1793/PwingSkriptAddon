package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffFileExists extends Effect {

    private Expression<String> filePath;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        filePath = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "file " + filePath.toString(e, debug) + " exists";
    }

    @Override
    protected void execute(Event e) {
        String path = filePath.getSingle(e);
        if (path != null) {
            java.io.File file = new java.io.File(path);
            boolean exists = file.exists();
            System.out.println("File exists: " + exists);
        }
    }
}
