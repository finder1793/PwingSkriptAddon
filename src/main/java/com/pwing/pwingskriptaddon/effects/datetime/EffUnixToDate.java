package com.pwing.pwingskriptaddon.effects.datetime;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffUnixToDate extends Effect {

    private Expression<Number> unixTimestamp;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        unixTimestamp = (Expression<Number>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert unix timestamp " + unixTimestamp.toString(e, debug) + " to date";
    }

    @Override
    protected void execute(Event e) {
        Number timestamp = unixTimestamp.getSingle(e);
        if (timestamp != null) {
            try {
                long unixTime = timestamp.longValue();
                java.util.Date date = new java.util.Date(unixTime * 1000L);
                System.out.println("Date: " + date.toString());
            } catch (Exception ex) {
                System.out.println("Failed to convert timestamp: " + ex.getMessage());
            }
        }
    }
}
