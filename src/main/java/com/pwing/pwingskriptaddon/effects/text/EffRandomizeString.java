package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EffRandomizeString extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "randomize " + text.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null && !input.isEmpty()) {
            List<String> characters = Arrays.asList(input.split(""));
            Collections.shuffle(characters);
            String randomized = String.join("", characters);
            // Store result in a variable or use it as needed
            System.out.println("Randomized: " + randomized);
        }
    }
}
