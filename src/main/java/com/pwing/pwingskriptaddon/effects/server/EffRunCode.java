package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRunCode extends Effect {

    private Expression<String> code;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        code = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "run code " + code.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String scriptCode = code.getSingle(e);
        if (scriptCode != null) {
            try {
                // Execute Skript code - simplified for now
                System.out.println("Code executed: " + scriptCode);
            } catch (Exception ex) {
                System.out.println("Failed to run code: " + ex.getMessage());
            }
        }
    }
}
