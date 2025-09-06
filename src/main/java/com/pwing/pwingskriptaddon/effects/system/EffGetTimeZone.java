package com.pwing.pwingskriptaddon.effects.system;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffGetTimeZone extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "get current time zone";
    }

    @Override
    protected void execute(Event e) {
        String timeZone = java.util.TimeZone.getDefault().getID();
        System.out.println("Current Time Zone: " + timeZone);
    }
}
