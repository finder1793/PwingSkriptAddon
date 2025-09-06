package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import ch.njol.skript.variables.Variables;

public class EffNumToHexa extends Effect {

    private Expression<Number> number;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        number = (Expression<Number>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert " + number.toString(e, debug) + " to hexadecimal";
    }

    @Override
    protected void execute(Event e) {
        Number num = number.getSingle(e);
        if (num != null) {
            String hex = Integer.toHexString(num.intValue()).toUpperCase();
            // Store result in Skript variable
            Variables.setVariable("hexresult", hex, e, false);
        }
    }
}
