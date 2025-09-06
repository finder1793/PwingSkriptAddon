package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;
import java.util.Base64;

public class EffBase64 extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "encode " + text.toString(e, debug) + " to base64";
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            String encoded = Base64.getEncoder().encodeToString(input.getBytes());
            // Store result in Skript variable
            Variables.setVariable("base64result", encoded, e, false);
        }
    }
}
