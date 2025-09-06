package com.pwing.pwingskriptaddon.effects.text;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

public class EffMirrorText extends Effect {

    private Expression<String> text;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        text = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "mirror " + text.toString(e, debug);
    }

    @Override
    protected void execute(Event e) {
        String input = text.getSingle(e);
        if (input != null) {
            String mirrored = new StringBuilder(input).reverse().toString();
            // Store result in Skript variable
            Variables.setVariable("mirroredtext", mirrored, e, false);
        }
    }
}
