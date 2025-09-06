package com.pwing.pwingskriptaddon.effects.json;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffGetJsonValue extends Effect {

    private Expression<String> jsonPath;
    private Expression<String> jsonString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        jsonPath = (Expression<String>) exprs[0];
        jsonString = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get json value " + jsonPath.toString(e, debug) + " from " + jsonString.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String path = jsonPath.getSingle(e);
        String json = jsonString.getSingle(e);
        if (path != null && json != null) {
            try {
                // Simple JSON path extraction - in real implementation use JSON library
                System.out.println("Extracting JSON value at path: " + path);
            } catch (Exception ex) {
                System.out.println("Failed to extract JSON value: " + ex.getMessage());
            }
        }
    }
}
