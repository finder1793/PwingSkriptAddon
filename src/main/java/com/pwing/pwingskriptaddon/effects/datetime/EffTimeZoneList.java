package com.pwing.pwingskriptaddon.effects.datetime;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

public class EffTimeZoneList extends Effect {

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "list all time zones";
    }

    @Override
    protected void execute(Event e) {
        String[] timeZones = java.util.TimeZone.getAvailableIDs();
        // Store result in Skript variables
        Variables.setVariable("timezones", timeZones, e, false);
        Variables.setVariable("timezonecount", timeZones.length, e, false);
    }
}
