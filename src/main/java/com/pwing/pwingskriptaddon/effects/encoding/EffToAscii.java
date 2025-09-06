package com.pwing.pwingskriptaddon.effects.encoding;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class EffToAscii extends Effect {

    private Expression<String> number;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        number = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean debug) {
        return "convert " + number.toString(e, debug) + " to ascii";
    }

    @Override
    protected void execute(Event e) {
        String numStr = number.getSingle(e);
        if (numStr != null) {
            try {
                int code = Integer.parseInt(numStr);
                char ascii = (char) code;
                System.out.println("ASCII: " + ascii);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid number for ASCII conversion");
            }
        }
    }
}
