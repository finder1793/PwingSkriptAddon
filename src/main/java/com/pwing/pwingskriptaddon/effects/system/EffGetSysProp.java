package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffGetSysProp extends Effect {

    private Expression<String> propertyName;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        propertyName = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get system property " + propertyName.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String propName = propertyName.getSingle(e);
        if (propName != null) {
            String value = System.getProperty(propName);
            System.out.println("System property '" + propName + "': " + value);
        }
    }
}
