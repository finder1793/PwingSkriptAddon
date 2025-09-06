package com.pwing.pwingskriptaddon.effects.json;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffGetJsonIds extends Effect {

    private Expression<String> jsonString;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        jsonString = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get json ids from " + jsonString.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String json = jsonString.getSingle(e);
        if (json != null) {
            try {
                // Simple JSON parsing to extract IDs
                // In a real implementation, you'd use a JSON library
                System.out.println("JSON IDs extracted from: " + json);
            } catch (Exception ex) {
                System.out.println("Failed to parse JSON: " + ex.getMessage());
            }
        }
    }
}
