package com.pwing.pwingskriptaddon.effects.datetime;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EffParseDate extends Effect {

    private Expression<String> dateString;
    private Expression<String> format;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        dateString = (Expression<String>) exprs[0];
        format = (Expression<String>) exprs[1];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "parse date " + dateString.toString(e, debug) + " with format " + format.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String date = dateString.getSingle(e);
        String fmt = format.getSingle(e);
        if (date != null && fmt != null) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
                LocalDateTime parsed = LocalDateTime.parse(date, formatter);
                System.out.println("Parsed date: " + parsed);
            } catch (Exception ex) {
                System.out.println("Failed to parse date: " + ex.getMessage());
            }
        }
    }
}
