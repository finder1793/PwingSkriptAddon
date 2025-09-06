package com.pwing.pwingskriptaddon.effects.file;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffIsSymbolic extends Effect {

    private Expression<String> path;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        path = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return path.toString(e, debug) + " is a symbolic link";
    }

    @Override
    protected void execute(Event e) {
        String pathStr = path.getSingle(e);
        if (pathStr != null) {
            try {
                java.io.File file = new java.io.File(pathStr);
                boolean isSymbolic = java.nio.file.Files.isSymbolicLink(file.toPath());
                System.out.println("Is symbolic link: " + isSymbolic);
            } catch (Exception ex) {
                System.out.println("Failed to check symbolic link: " + ex.getMessage());
            }
        }
    }
}
