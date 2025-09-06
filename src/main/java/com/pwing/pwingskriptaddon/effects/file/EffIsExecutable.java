package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffIsExecutable extends Effect {

    private Expression<String> path;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        path = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is executable";
    }

    @Override
    protected void execute(Event e) {
        String pathStr = path.getSingle(e);
        if (pathStr != null) {
            java.io.File file = new java.io.File(pathStr);
            boolean isExecutable = file.canExecute();
            System.out.println("Is executable: " + isExecutable);
        }
    }
}
