package com.pwing.pwingskriptaddon.effects.server;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffRunApp extends Effect {

    private Expression<String> application;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        application = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "run application " + application.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String app = application.getSingle(e);
        if (app != null) {
            try {
                Runtime.getRuntime().exec(app);
                System.out.println("Application launched: " + app);
            } catch (Exception ex) {
                System.out.println("Failed to run application: " + ex.getMessage());
            }
        }
    }
}
