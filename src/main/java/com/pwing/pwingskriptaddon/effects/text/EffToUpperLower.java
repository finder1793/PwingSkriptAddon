package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffToUpperLower extends Effect {

    private Expression<String> text;
    private boolean toUpper;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        toUpper = matchedPattern == 0; // Pattern 0 = to uppercase, Pattern 1 = to lowercase
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert " + text.toString(e, debug) + (toUpper ? " to uppercase" : " to lowercase");
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            String result = toUpper ? input.toUpperCase() : input.toLowerCase();
            // Store result in a variable or use it as needed
            System.out.println("Case converted: " + result);
        }
    }
}
