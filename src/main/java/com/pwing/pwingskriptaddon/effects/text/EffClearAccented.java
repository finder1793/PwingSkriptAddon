package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import java.text.Normalizer;

public class EffClearAccented extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "clear accented characters from " + text.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
            String result = normalized.replaceAll("\\p{M}", "");
            // Store result in a variable or use it as needed
            System.out.println("Cleared accented: " + result);
        }
    }
}
